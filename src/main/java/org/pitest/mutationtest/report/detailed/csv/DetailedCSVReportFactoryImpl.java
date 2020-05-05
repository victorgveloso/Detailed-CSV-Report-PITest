package org.pitest.mutationtest.report.detailed.csv;

import org.pitest.coverage.CoverageData;
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
        return getListener(args, "coverage.csv", "classes-loc.csv", "mutations.csv");
    }

    @Override
    public MutationResultListener getListener(ListenerArguments args, String coverageFilename, String locFilename, String mutationFilename) {
        Writer coverageOutput = args.getOutputStrategy().createWriterForFile(coverageFilename);
        Writer locOutput = args.getOutputStrategy().createWriterForFile(locFilename);
        Writer mutationOutput = args.getOutputStrategy().createWriterForFile(mutationFilename);

        CoverageReporter coverageReporter = getCoverageReporter((CoverageData) args.getCoverage(), coverageOutput, locOutput);
        MutationReporter mutationReporter = getMutationReporter(mutationOutput);
        return getListener(getFileWriter(), coverageReporter, mutationReporter);

    }

    @Override
    public MutationResultListener getListener(FileWriter fileWriter, CoverageReporter coverageReporter, MutationReporter mutationReporter) {
        return new DetailedCSVReportListener(fileWriter, coverageReporter, mutationReporter);
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
    public CoverageReporter getCoverageReporter(CoverageData coverageData, Writer coverageOutput, Writer locOutput) {
        return getCoverageReporter(coverageData, coverageOutput, locOutput, getReportFormatter());
    }

    @Override
    public CoverageReporter getCoverageReporter(CoverageData coverageData, Writer coverageOutput, Writer locOutput, ReportFormatter reportFormatter) {
        return new CoverageReporterImpl(coverageData, coverageOutput, locOutput, reportFormatter);
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
