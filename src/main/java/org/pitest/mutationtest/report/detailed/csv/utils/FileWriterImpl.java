package org.pitest.mutationtest.report.detailed.csv.utils;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

/**
 * Utility class for handling IOExceptions and closing used output files
 */
public class FileWriterImpl implements FileWriter {
    private static FileWriter instance;
    private Set<Writer> usedFiles = new HashSet<>();

    public static FileWriter getInstance() {
        if (instance == null) instance = new FileWriterImpl();
        return instance;
    }

    @Override
    public void writeToFile(Writer output, String content) {
        usedFiles.add(output);
        try {
            output.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeAllUsedFiles() {
        try {
            for (Writer f : usedFiles) {
                f.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
