package org.pitest.mutationtest.report.detailed.csv.describers.tests;

import org.pitest.mutationtest.DetectionStatus;
import org.pitest.mutationtest.report.detailed.csv.ReportFormatter;
import org.pitest.mutationtest.report.detailed.csv.describers.MutationDescriber;
import org.pitest.mutationtest.report.detailed.csv.utils.Constants;

import java.util.Set;
import java.util.stream.Collectors;

abstract class AbstractTestDescriber implements TestDescriber {
    // Utility class
    private ReportFormatter reportFormatter;
    private MutationDescriber mutationDescriber;

    AbstractTestDescriber(ReportFormatter reportFormatter, MutationDescriber mutationDescriber) {
        this.reportFormatter = reportFormatter;
        this.mutationDescriber = mutationDescriber;
    }

    Set<String> describeTests(String mutationInfo, Set<String> tests, DetectionStatus status) {
        return tests.stream().map(test -> reportFormatter.makeCsv(reportFormatter.clearSyntax(test), status, mutationInfo) + Constants.NEW_LINE).collect(Collectors.toSet());
    }

    ReportFormatter getReportFormatter() {
        return reportFormatter;
    }

    MutationDescriber getMutationDescriber() {
        return mutationDescriber;
    }
}
