package org.pitest.mutationtest.report.detailed.csv;

import org.pitest.coverage.CoverageData;
import org.pitest.coverage.CoverageDatabase;
import org.pitest.mutationtest.ClassMutationResults;
import org.pitest.mutationtest.MutationResultListener;
import org.pitest.mutationtest.report.detailed.csv.utils.FileWriter;
import org.pitest.mutationtest.report.detailed.csv.utils.FileWriterImpl;
import org.pitest.util.ResultOutputStrategy;

import java.io.Writer;

/**
 * Implementation of Listener responsible for generating the following files:
 * <ul>
 * <li><strong>mutation.csv:</strong> Relates mutation with tests (For each tests which mutations survived or not)
 * <li><strong>coverage.csv:</strong> Describes block coverage for each test case
 * <li><strong>classes-loc.csv:</strong> Stores the size (number of instructions) of each source code classes
 * </ul>
 */
public class DetailedCSVReportListener implements MutationResultListener {

    private CoverageReporter coverageReporter;
    private MutationReporter mutationReporter;

    private FileWriter fileWriter;

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
        try {
            this.fileWriter = new FileWriterImpl();
            ReportFormatter reportFormatter = new ReportFormatterImpl();
            this.mutationReporter = new MutationReporterImpl(reportFormatter, mutationOutput);
            this.coverageReporter = new CoverageReporterImpl((CoverageData) coverage, coverageOutput, locOutput, reportFormatter);
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
        coverageReporter.createCoverageReport(fileWriter);
    }

    /**
     * Execute after all handleMutationResult calls.
     * <p>
     * Used for: Close output files and exporting classes sizes to locOutput.
     */
    @Override
    public void runEnd() {
        coverageReporter.createLOCReport(fileWriter);

        fileWriter.closeAllUsedFiles();
    }

    /**
     * Execute for each finished mutation.
     *
     * @param metaData Finished mutation information
     */
    @Override
    public void handleMutationResult(final ClassMutationResults metaData) {
        // Satisfy line coverage analysis pre-conditions
        coverageReporter.appendClassTotalLines(metaData.getFileName(), metaData.getPackageName());
        // Report mutation results
        mutationReporter.reportMutationResults(metaData, fileWriter);
    }

}
