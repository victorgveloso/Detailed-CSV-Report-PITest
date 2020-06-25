package org.pitest.mutationtest.report.detailed.csv.describers;

import org.pitest.mutationtest.report.detailed.csv.ReportFormatter;
import org.pitest.mutationtest.report.detailed.csv.describers.tests.TestDescriber;

import java.util.Collection;

public interface DescribersFactory {
    MutationDescriber getMutationDescriber(ReportFormatter reportFormatter);

    Collection<TestDescriber> getTestDescribers(ReportFormatter reportFormatter, MutationDescriber mutationDescriber);
}
