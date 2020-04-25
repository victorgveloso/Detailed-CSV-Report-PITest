package org.pitest.mutationtest.report.detailed.csv;

import org.pitest.mutationtest.ClassMutationResults;
import org.pitest.mutationtest.MutationResult;
import org.pitest.mutationtest.MutationResultListener;
import org.pitest.util.ResultOutputStrategy;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DetailedCSVReportListener implements MutationResultListener {
    private Writer out;

    public DetailedCSVReportListener(final ResultOutputStrategy outputStrategy) {
        this(outputStrategy.createWriterForFile("mutations.csv"));
    }

    public DetailedCSVReportListener(final Writer out) {
        this.out = out;
    }

    @Override
    public void runStart() {

    }

    private String describeKillingTests(final List<String> killingTest) {
        if (killingTest.isEmpty()) {
            return "none";
        } else {
            return String.join(",", killingTest);
        }
    }

    private String makeCsv(final Object... os) {
        return Arrays.stream(os)
                .map(Object::toString)
                .collect(Collectors.joining(","));
    }

    @Override
    public void handleMutationResult(final ClassMutationResults metaData) {
        for (final MutationResult mutation : metaData.getMutations()) {
            try {
                this.out.write(makeCsv(mutation.getDetails().getFilename(), mutation
                                .getDetails().getClassName().asJavaName(), mutation.getDetails()
                                .getMutator(), mutation.getDetails().getMethod(), mutation
                                .getDetails().getLineNumber(), mutation.getStatus(),
                        describeKillingTests(mutation.getKillingTests()))
                        + System.getProperty("line.separator"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void runEnd() {
        try {
            this.out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
