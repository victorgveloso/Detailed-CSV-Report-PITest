package org.pitest.mutationtest.report.detailed.csv;

import org.pitest.classinfo.ClassName;
import org.pitest.coverage.BlockCoverage;
import org.pitest.coverage.BlockLocation;
import org.pitest.coverage.CoverageData;
import org.pitest.coverage.CoverageDatabase;
import org.pitest.mutationtest.ClassMutationResults;
import org.pitest.mutationtest.DetectionStatus;
import org.pitest.mutationtest.MutationResult;
import org.pitest.mutationtest.MutationResultListener;
import org.pitest.mutationtest.engine.Location;
import org.pitest.util.ResultOutputStrategy;

import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implementation of Listener responsible for generating the following files:
 * <ul>
 * <li><strong>mutation.csv:</strong> Relates mutation with tests (For each tests which mutations survived or not)
 * <li><strong>coverage.csv:</strong> Describes block coverage for each test case
 * <li><strong>classes-loc.csv:</strong> Stores the size (number of instructions) of each source code classes
 * </ul>
 */
public class DetailedCSVReportListener implements MutationResultListener {
    private static final String NEW_LINE = System.getProperty("line.separator");
    // Output Files:
    private Writer mutationOutput;
    private Writer coverageOutput;
    private Writer locOutput;
    // Object for coverage data recovering TODO: Should use CoverageDatabase interface instead!
    private CoverageData coverage;
    // LOC of each class
    private Map<String, Integer> classTotalLines = new HashMap<>();

    /**
     * Main constructor (used by Abstract Factory).
     *
     * @param outputStrategy Output files creator
     * @param coverage       Object for coverage data recovering
     */
    public DetailedCSVReportListener(final ResultOutputStrategy outputStrategy, final CoverageDatabase coverage) {
        this(outputStrategy.createWriterForFile("mutations.csv"), outputStrategy.createWriterForFile("coverage.csv"), outputStrategy.createWriterForFile("classes-loc.csv"), coverage);
    }

    /**
     * Constructor with easily mockable parameters (used for Testing purpose).
     *
     * @param mutationOutput Relates mutation with tests (For each tests which mutations survived or not)
     * @param coverageOutput Describes block coverage for each test case
     * @param locOutput      Stores the size (number of instructions) of each source code classes
     * @param coverage       Object for coverage data recovering
     */
    public DetailedCSVReportListener(final Writer mutationOutput, final Writer coverageOutput, final Writer locOutput, final CoverageDatabase coverage) {
        this.mutationOutput = mutationOutput;
        this.coverageOutput = coverageOutput;
        this.locOutput = locOutput;
        try {
            this.coverage = (CoverageData) coverage;
        } catch (ClassCastException exception) {
            System.err.println("DetailedCSV currently only supports default CoverageDatabase (CoverageData)");
            throw exception;
        }
    }

    /**
     * Execute before all handleMutationResult calls.
     * <p>
     * Used for: create coverage report
     */
    @Override
    public void runStart() {
        createCoverageReport();
    }

    /**
     * Execute after all handleMutationResult calls.
     * <p>
     * Used for: Close output files and exporting classes sizes to locOutput.
     */
    @Override
    public void runEnd() {
        classTotalLines.forEach((clazz, loc) -> writeToFile(locOutput, makeCsv(clazz, loc)));

        closeOutputFiles(mutationOutput, coverageOutput, locOutput);
    }

    /**
     * Execute for each finished mutation.
     *
     * @param metaData Finished mutation information
     */
    @Override
    public void handleMutationResult(final ClassMutationResults metaData) {
        // Satisfy line coverage analysis pre-conditions
        appendClassTotalLines(metaData.getFileName(), metaData.getPackageName());
        // Report mutation results
        metaData.getMutations().forEach(mutation -> writeToFile(mutationOutput, generateMutationReport(mutation)));
    }

    /**
     * Close files and handles IOExceptions.
     *
     * @param files output files to close properly
     */
    private void closeOutputFiles(Writer... files) {
        try {
            for (Writer f : files) {
                f.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Export coverage data to proper output file.
     */
    private void createCoverageReport() {
        new HashSet<>(coverage.createCoverage()).forEach(block -> writeToFile(coverageOutput, generateReportCoveredBlock(block) + NEW_LINE));
    }

    /**
     * Register classes from a given package and java file to classTotalLines.
     *
     * @param filename    Path to class containing file
     * @param packageName Package of classes
     */
    private void appendClassTotalLines(String filename, String packageName) {
        coverage.getClassesForFile(filename, packageName).forEach(classInfo -> classTotalLines.putIfAbsent(classInfo.getName().asJavaName(), classInfo.getNumberOfCodeLines()));
    }

    /**
     * Remove dirty output arisen from test names.
     *
     * @param s text containing test names
     * @return cleared text
     */
    private String clearSyntax(String s) {
        return s.replaceFirst("\\.\\[engine:.+?]/\\[class:.+?]/\\[method:(.+?)\\(\\)]", "::$1");
    }

    /**
     * Join parameters into a string with separated with comma.
     *
     * @param os any object whose string representation doesn't contain the delimiter character (comma)
     * @return valid CSV row
     */
    private String makeCsv(final Object... os) {
        return Arrays.stream(os).map(Object::toString).collect(Collectors.joining(","));
    }

    /**
     * Generate report for a given mutation result.
     *
     * @param mutation Result of a mutation
     * @return CSV rows describing mutation result
     */
    private String generateMutationReport(MutationResult mutation) {
        String mutationInfo = getMutationInfo(mutation);
        return Stream.concat(describeTests(mutationInfo, mutation.getSucceedingTests(), DetectionStatus.SURVIVED), describeTests(mutationInfo, mutation.getKillingTests(), DetectionStatus.KILLED)).collect(Collectors.joining(NEW_LINE));
    }

    /**
     * Generate rows describing mutation elimination status for each test case
     *
     * @param mutationInfo Information about reported mutation
     * @param tests        All tests with same elimination status for mutation
     * @param status       Elimination status of tests
     * @return CSV rows describing mutation results with the same elimination status
     */
    private Stream<String> describeTests(String mutationInfo, List<String> tests, DetectionStatus status) {
        return tests.stream().map(test -> makeCsv(clearSyntax(test), status, mutationInfo));
    }

    /**
     * Describe mutation in CSV complaining format
     *
     * @param mutation mutation to describe
     * @return CSV description of mutation
     */
    private String getMutationInfo(MutationResult mutation) {
        return makeCsv(getFormattedLocation(mutation), mutation.getDetails().getLineNumber(), mutation.getDetails().getMutator());
    }

    /**
     * Method name and class formatting (specialized for MutationResult, delegates to generic method).
     *
     * @param mutation Finished mutation data
     * @return Formatted path
     */
    private String getFormattedLocation(MutationResult mutation) {
        return getFormattedLocation(mutation.getDetails().getClassName(), mutation.getDetails().getMethod().name());
    }

    /**
     * Method name and class formatting (specialized for BlockCoverage, delegates to generic method).
     *
     * @param block Covered block
     * @return Formatted path
     */
    private String getFormattedLocation(BlockCoverage block) {
        Location location = block.getBlock().getLocation();
        return getFormattedLocation(location.getClassName(), location.getMethodName().name());
    }

    /**
     * Method name and class formatting (Single point of configuration)
     *
     * @param clazz  Method's class
     * @param method Method name
     * @return Formatted path
     */
    private String getFormattedLocation(ClassName clazz, String method) {
        return String.format("%s::%s", clazz.asJavaName(), method);
    }

    /**
     * Write content to a file and handle IOExceptions
     *
     * @param output  Output file
     * @param content Content to write
     */
    private void writeToFile(Writer output, String content) {
        try {
            output.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generate CSV rows describing covered block id and size for each test case.
     *
     * @param coverage A block covered by test cases
     * @return CSV rows with coverage description
     */
    private String generateReportCoveredBlock(BlockCoverage coverage) {
        BlockLocation b = coverage.getBlock();

        int size = b.getLastInsnInBlock() - b.getFirstInsnInBlock() + 1;

        String blockInsRange = makeCsv(getFormattedLocation(coverage), b.getBlock(), size);

        return coverage.getTests().stream().map(test -> makeCsv(clearSyntax(test), blockInsRange)).collect(Collectors.joining(NEW_LINE));
    }
}
