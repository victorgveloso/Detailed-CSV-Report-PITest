package org.pitest.mutationtest.report.detailed.csv.describers.tests;

import org.pitest.mutationtest.DetectionStatus;
import org.pitest.mutationtest.MutationResult;
import org.pitest.mutationtest.report.detailed.csv.ReportFormatter;
import org.pitest.mutationtest.report.detailed.csv.describers.MutationDescriber;

import java.util.HashSet;
import java.util.Set;

public class KilledTestsDescriber extends AbstractTestDescriber {
    public KilledTestsDescriber(ReportFormatter reportFormatter, MutationDescriber mutationDescriber) {
        super(reportFormatter, mutationDescriber);
    }

    @Override
    public Set<String> describeTests(MutationResult mutation) {
        String mutationInfo = getMutationDescriber().getMutationInfo(mutation);

        return describeTests(mutationInfo, new HashSet<>(mutation.getKillingTests()), DetectionStatus.KILLED);
    }
}
