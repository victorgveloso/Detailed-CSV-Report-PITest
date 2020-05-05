package org.pitest.mutationtest.report.detailed.csv;

import org.pitest.mutationtest.report.detailed.csv.utils.FileWriter;

public interface CoverageReporter {
    /**
     * Export coverage data to proper output file.
     *
     * @param fileWriter Utility object for handling IO
     */
    void createCoverageReport(FileWriter fileWriter);

    /**
     * Export lines of code of project's classes to proper output file
     *
     * @param fileWriter Utility object for handling IO
     */
    void createLOCReport(FileWriter fileWriter);

    /**
     * Register classes from a given package and java file to classTotalLines.
     *
     * @param filename    Path to class containing file
     * @param packageName Package of classes
     */
    void appendClassTotalLines(String filename, String packageName);
}
