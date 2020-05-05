package org.pitest.mutationtest.report.detailed.csv.utils;

import java.io.Writer;

public interface FileWriter {
    /**
     * Write content to a file and handle IOExceptions
     *
     * @param output  Output file
     * @param content Content to write
     */
    void writeToFile(Writer output, String content);

    /**
     * Close files and handles IOExceptions.
     */
    void closeAllUsedFiles();
}
