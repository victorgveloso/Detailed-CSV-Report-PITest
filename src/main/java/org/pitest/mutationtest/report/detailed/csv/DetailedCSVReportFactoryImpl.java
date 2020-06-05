package org.pitest.mutationtest.report.detailed.csv;

import org.pitest.mutationtest.ListenerArguments;
import org.pitest.mutationtest.MutationResultListener;
import org.pitest.mutationtest.report.detailed.csv.utils.FileWriter;
import org.pitest.mutationtest.report.detailed.csv.utils.FileWriterImpl;

import java.io.Writer;
import java.util.Properties;

/**
 * MutationResultListener Abstract Factory implementation for DetailedCSVReportListener class
 */
public class DetailedCSVReportFactoryImpl implements DetailedCSVReportFactory {
    /**
     * Main DetailedCSVReportListener Factory Method
     *
     * @param props Configuration properties
     * @param args  Wrapper of common used PITest objects and values
     * @return Custom result listener
     */
    @Override
    public MutationResultListener getListener(Properties props, ListenerArguments args) {
        return getListener(args, "coverage.csv", "classes-loc.csv", "detailed-mutations.csv");
    }

    @Override
    public MutationResultListener getListener(ListenerArguments args, String coverageFilename, String locFilename, String mutationFilename) {
        Writer mutationOutput = args.getOutputStrategy().createWriterForFile(mutationFilename);

        MutationReporter mutationReporter = getMutationReporter(mutationOutput);
        return getListener(getFileWriter(), mutationReporter);

    }

    @Override
    public MutationResultListener getListener(FileWriter fileWriter, MutationReporter mutationReporter) {
        return new DetailedCSVReportListener(fileWriter, mutationReporter);
    }

    @Override
    public FileWriter getFileWriter() {
        return FileWriterImpl.getInstance();
    }

    @Override
    public MutationReporter getMutationReporter(Writer mutationOutput) {
        return getMutationReporter(getReportFormatter(), mutationOutput);
    }

    @Override
    public MutationReporter getMutationReporter(ReportFormatter reportFormatter, Writer mutationOutput) {
        return new MutationReporterImpl(reportFormatter, mutationOutput);
    }

    @Override
    public ReportFormatter getReportFormatter() {
        return ReportFormatterImpl.getInstance();
    }

    /**
     * Get the name used on pitest's output format configuration .
     *
     * @return Output format name
     */
    @Override
    public String name() {
        return "DetailedCSV";
    }

    /**
     * Get detailed description printed during the pitest execution.
     *
     * @return Detailed description.
     */
    @Override
    public String description() {
        return "Detailed csv report plugin (reports both method coverage and mutation  all killing tests for each mutant";
    }
}
