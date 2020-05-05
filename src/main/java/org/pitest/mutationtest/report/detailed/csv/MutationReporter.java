package org.pitest.mutationtest.report.detailed.csv;

import org.pitest.mutationtest.ClassMutationResults;
import org.pitest.mutationtest.report.detailed.csv.utils.FileWriter;

public interface MutationReporter {
    /**
     * Export mutation data to proper output file.
     *
     * @param fileWriter Utility object for handling IO
     */
    void reportMutationResults(ClassMutationResults metaData, FileWriter fileWriter);
}
