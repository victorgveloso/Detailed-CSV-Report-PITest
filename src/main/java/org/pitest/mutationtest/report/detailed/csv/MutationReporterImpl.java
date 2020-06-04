package org.pitest.mutationtest.report.detailed.csv;

import org.pitest.mutationtest.ClassMutationResults;
import org.pitest.mutationtest.DetectionStatus;
import org.pitest.mutationtest.MutationResult;
import org.pitest.mutationtest.report.detailed.csv.utils.FileWriter;

import java.io.Writer;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Creates reports about mutation testing
 */
public class MutationReporterImpl implements MutationReporter {
    private static final String NEW_LINE = System.getProperty("line.separator");
    // Utility class
    private ReportFormatter reportFormatter;
    // Output file
    private Writer mutationOutput;

    /**
     * Main constructor
     *
     * @param reportFormatter Utility object for CSV formatting
     * @param mutationOutput  Report output file
     */
    public MutationReporterImpl(ReportFormatter reportFormatter, Writer mutationOutput) {
        this.reportFormatter = reportFormatter;
        this.mutationOutput = mutationOutput;
    }

    @Override
    public void reportMutationResults(ClassMutationResults metaData, FileWriter fileWriter) {
        metaData.getMutations().forEach(mutation -> fileWriter.writeToFile(mutationOutput, generateMutationReport(mutation)));
    }

    /**
     * Generate report for a given mutation result.
     *
     * @param mutation Result of a mutation
     * @return CSV rows describing mutation result
     */
    private String generateMutationReport(MutationResult mutation) {
        String mutationInfo = getMutationInfo(mutation);
        return Stream.concat(describeTests(mutationInfo, mutation.getSucceedingTests(), DetectionStatus.SURVIVED), describeTests(mutationInfo, mutation.getKillingTests(), DetectionStatus.KILLED)).collect(Collectors.joining(NEW_LINE)) + NEW_LINE;
    }

    /**
     * Describe mutation in CSV complaining format
     *
     * @param mutation mutation to describe
     * @return CSV description of mutation ("class::testcase-method,KILLED?,class::method,mutated-line,mutator")
     */
    private String getMutationInfo(MutationResult mutation) {
        return reportFormatter.makeCsv(reportFormatter.getFormattedLocation(mutation), mutation.getDetails().getLineNumber(), mutation.getDetails().getMutator());
    }

    /**
     * Generate rows describing mutation elimination status for each test case
     *
     * @param mutationInfo Information about reported mutation
     * @param tests        All tests with same elimination status for mutation
     * @param status       Elimination status of tests
     * @return CSV rows describing mutation results with the same elimination status
     */
    private Stream<String> describeTests(String mutationInfo, List<String> tests, DetectionStatus status) {
        return tests.stream().map(test -> reportFormatter.makeCsv(reportFormatter.clearSyntax(test), status, mutationInfo));
    }
}
