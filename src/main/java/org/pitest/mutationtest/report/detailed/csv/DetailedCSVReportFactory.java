package org.pitest.mutationtest.report.detailed.csv;

import org.pitest.mutationtest.ListenerArguments;
import org.pitest.mutationtest.MutationResultListener;
import org.pitest.mutationtest.MutationResultListenerFactory;
import org.pitest.mutationtest.report.detailed.csv.utils.FileWriter;

import java.io.Writer;

public interface DetailedCSVReportFactory extends MutationResultListenerFactory {
    /**
     * Alternative DetailedCSVReportListener Factory Method
     *
     * @param args             Wrapper of common used PITest objects and values
     * @param coverageFilename Coverage report filename
     * @param locFilename      Lines of code report filename
     * @param mutationFilename Mutation report filename
     * @return Custom result listener
     */
    MutationResultListener getListener(ListenerArguments args, String coverageFilename, String locFilename, String mutationFilename);

    /**
     * Alternative DetailedCSVReportListener Factory Method
     *
     * @param fileWriter       Utility object for writing to file abstraction
     * @param mutationReporter Mutation test report generator
     * @return Custom result listener
     */
    MutationResultListener getListener(FileWriter fileWriter, MutationReporter mutationReporter);

    /**
     * Delegates to FileWriter.getInstance()
     *
     * @return the instance of file writer singleton
     */
    FileWriter getFileWriter();

    /**
     * Delegates to ReportFormatter.getInstance()
     *
     * @return the instance of report formatter singleton
     */
    ReportFormatter getReportFormatter();

    /**
     * MutationReporter Factory Method
     *
     * @param mutationOutput Mutation report file writer
     * @return a mutation reporter
     */
    MutationReporter getMutationReporter(Writer mutationOutput);

    /**
     * MutationReporter Factory Method
     *
     * @param reportFormatter Utility for CSV report formatting
     * @param mutationOutput  Mutation report file writer
     * @return a mutation reporter
     */
    MutationReporter getMutationReporter(ReportFormatter reportFormatter, Writer mutationOutput);

}
