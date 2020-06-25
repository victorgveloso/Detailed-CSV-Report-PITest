package org.pitest.mutationtest.report.detailed.csv.describers.tests;

import org.pitest.mutationtest.DetectionStatus;
import org.pitest.mutationtest.MutationResult;
import org.pitest.mutationtest.report.detailed.csv.ReportFormatter;
import org.pitest.mutationtest.report.detailed.csv.describers.MutationDescriber;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AlternativeTestsDescriber extends AbstractTestDescriber {
    public AlternativeTestsDescriber(ReportFormatter reportFormatter, MutationDescriber mutationDescriber) {
        super(reportFormatter, mutationDescriber);
    }

    @Override
    public Set<String> describeTests(MutationResult mutation) {
        String mutationInfo = getMutationDescriber().getMutationInfo(mutation);

        Set<String> succeedingTests = getSucceedingTestsFormatted(mutation);

        Set<String> coveringTests = getAllCoveringTestsFormatted(mutation);
        if (coveringTests.size() == 0) {
            System.out.println("No test case covers this mutation!");
            HashSet<String> none = new HashSet<>(Collections.singletonList("none"));
            return describeTests(mutationInfo, none, DetectionStatus.NO_COVERAGE);
        }
        Set<String> timedOutTests = coveringTests.stream().filter(Predicate.not(succeedingTests::contains)).collect(Collectors.toSet());

        if (timedOutTests.size() == 0) {
            System.out.println("No time out was detected for this mutation!");
            return Collections.emptySet();
        }
        return describeTests(mutationInfo, timedOutTests, DetectionStatus.TIMED_OUT);
    }

    private Set<String> getSucceedingTestsFormatted(MutationResult mutation) {
        Set<String> survivedTests = new HashSet<>(mutation.getSucceedingTests());
        Set<String> killedTests = new HashSet<>(mutation.getKillingTests());
        return Stream.concat(survivedTests.stream(), killedTests.stream()).map(getReportFormatter()::clearSyntax).collect(Collectors.toSet());
    }

    private Set<String> getAllCoveringTestsFormatted(MutationResult mutation) {
        return mutation.getDetails().getTestsInOrder().stream().map(tests -> getReportFormatter().clearSyntax(tests.getName())).collect(Collectors.toSet());
    }
}
