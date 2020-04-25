package org.pitest.mutationtest.report.detailed.csv;

import org.pitest.mutationtest.ListenerArguments;
import org.pitest.mutationtest.MutationResultListener;
import org.pitest.mutationtest.MutationResultListenerFactory;

import java.util.Properties;

public class DetailedCSVReportFactory implements MutationResultListenerFactory {
    @Override
    public MutationResultListener getListener(Properties props, ListenerArguments args) {
        return new DetailedCSVReportListener(args.getOutputStrategy());
    }

    @Override
    public String name() {
        return "DetailedCSV";
    }

    @Override
    public String description() {
        return "Detailed csv report plugin (report all killing tests for each mutant";
    }
}
