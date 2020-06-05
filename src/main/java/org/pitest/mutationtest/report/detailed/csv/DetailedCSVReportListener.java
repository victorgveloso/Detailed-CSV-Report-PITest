package org.pitest.mutationtest.report.detailed.csv;

import org.pitest.mutationtest.ClassMutationResults;
import org.pitest.mutationtest.MutationResultListener;
import org.pitest.mutationtest.report.detailed.csv.utils.FileWriter;

/**
 * Implementation of Listener responsible for generating the following files:
 * <ul>
 * <li><strong>mutation.csv:</strong> Relates mutation with tests (For each tests which mutations survived or not)
 * <li><strong>coverage.csv:</strong> Describes block coverage for each test case
 * <li><strong>classes-loc.csv:</strong> Stores the size (number of instructions) of each source code classes
 * </ul>
 */
public class DetailedCSVReportListener implements MutationResultListener {

    private MutationReporter mutationReporter;

    private FileWriter fileWriter;

    /**
     * Main constructor (used by Abstract Factory).
     *  @param fileWriter       Utility object for writing to file abstraction
     * @param mutationReporter Mutation test report generator
     */
    public DetailedCSVReportListener(FileWriter fileWriter, MutationReporter mutationReporter) {
        this.mutationReporter = mutationReporter;
        this.fileWriter = fileWriter;
    }

    /**
     * Execute before all handleMutationResult calls.
     * <p>
     * Used for: create coverage report
     */
    @Override
    public void runStart() {
    }

    /**
     * Execute after all handleMutationResult calls.
     * <p>
     * Used for: Close output files and exporting classes sizes to locOutput.
     */
    @Override
    public void runEnd() {
        fileWriter.closeAllUsedFiles();
    }

    /**
     * Execute for each finished mutation.
     *
     * @param metaData Finished mutation information
     */
    @Override
    public void handleMutationResult(final ClassMutationResults metaData) {
        // Report mutation results
        mutationReporter.reportMutationResults(metaData, fileWriter);
    }

}
