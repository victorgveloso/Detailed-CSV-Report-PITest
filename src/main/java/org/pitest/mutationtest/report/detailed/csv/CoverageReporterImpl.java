package org.pitest.mutationtest.report.detailed.csv;

import org.pitest.coverage.BlockCoverage;
import org.pitest.coverage.BlockLocation;
import org.pitest.coverage.CoverageData;
import org.pitest.mutationtest.report.detailed.csv.utils.FileWriter;

import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Creates reports about test coverage and classes' number of instructions
 */
public class CoverageReporterImpl implements CoverageReporter {
    private static final String NEW_LINE = System.getProperty("line.separator");
    // Output files
    private Writer coverageOutput;
    private Writer locOutput;
    // Utility classes
    private CoverageData coverage;
    private ReportFormatter reportFormatter;
    // LOC of each class
    private Map<String, Integer> classTotalLines = new HashMap<>();

    /**
     * Main constructor
     *
     * @param coverage        Controller for coverage data recovering
     * @param coverageOutput  Report output file for coverage data
     * @param locOutput       Report output file for classes' LOC data
     * @param reportFormatter Utility object for CSV formatting
     */
    public CoverageReporterImpl(CoverageData coverage, Writer coverageOutput, Writer locOutput, ReportFormatter reportFormatter) {
        this.coverage = coverage;
        this.coverageOutput = coverageOutput;
        this.locOutput = locOutput;
        this.reportFormatter = reportFormatter;
    }

    @Override
    public void createCoverageReport(FileWriter fileWriter) {
        new HashSet<>(coverage.createCoverage()).forEach(block -> fileWriter.writeToFile(coverageOutput, generateReportCoveredBlock(block) + NEW_LINE));
    }

    @Override
    public void createLOCReport(FileWriter fileWriter) {
        classTotalLines.forEach((clazz, loc) -> fileWriter.writeToFile(locOutput, reportFormatter.makeCsv(clazz, loc)));
    }

    @Override
    public void appendClassTotalLines(String filename, String packageName) {
        coverage.getClassesForFile(filename, packageName).forEach(classInfo -> classTotalLines.putIfAbsent(classInfo.getName().asJavaName(), classInfo.getNumberOfCodeLines()));
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

        String blockInsRange = reportFormatter.makeCsv(reportFormatter.getFormattedLocation(coverage), b.getBlock(), size);

        return coverage.getTests().stream().map(test -> reportFormatter.makeCsv(reportFormatter.clearSyntax(test), blockInsRange)).collect(Collectors.joining(NEW_LINE));
    }
}
