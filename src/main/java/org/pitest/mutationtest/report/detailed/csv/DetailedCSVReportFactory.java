package org.pitest.mutationtest.report.detailed.csv;

import org.pitest.mutationtest.ListenerArguments;
import org.pitest.mutationtest.MutationResultListener;
import org.pitest.mutationtest.MutationResultListenerFactory;

import java.util.Properties;

/**
 * MutationResultListener Abstract Factory implementation for DetailedCSVReportListener class
 */
public class DetailedCSVReportFactory implements MutationResultListenerFactory {
    /**
     * DetailedCSVReportListener Factory Method
     *
     * @param props Configuration properties
     * @param args  Wrapper of common used PITest objects and values
     * @return Custom result listener
     */
    @Override
    public MutationResultListener getListener(Properties props, ListenerArguments args) {
        return new DetailedCSVReportListener(args.getOutputStrategy(), args.getCoverage());
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
