package org.pitest.mutationtest.report.detailed.csv;

import org.pitest.mutationtest.ClassMutationResults;
import org.pitest.mutationtest.DetectionStatus;
import org.pitest.mutationtest.MutationResult;
import org.pitest.mutationtest.report.detailed.csv.utils.FileWriter;

import java.io.Writer;
import java.util.*;
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
        Set<String> survivedTests = new HashSet<>(mutation.getSucceedingTests());
        Set<String> killedTests = new HashSet<>(mutation.getKillingTests());
        Set<String> normalTests = Stream.concat(survivedTests.stream(), killedTests.stream()).collect(Collectors.toSet());

        Set<String> alternativeTests = describeAlternativeTests(countTimedOutTests(mutation), getAlternativeTestsName(mutation, normalTests), mutationInfo);

        return concat(new HashSet<>(Arrays.asList(describeSurvivedTests(survivedTests, mutationInfo).stream(), describeKilledTests(killedTests, mutationInfo).stream(), alternativeTests.stream()))).collect(Collectors.joining(""));
    }

    private Stream<String> concat(Collection<Stream<String>> steams) {
        Stream<String> output = Stream.empty();
        for (Stream<String> st : steams) {
            output = Stream.concat(output, st);
        }
        return output;
    }

    private int countTimedOutTests(MutationResult mutation) {
        switch (mutation.getStatus()) {
            case KILLED:
            case SURVIVED:
                int succeeding_test = mutation.getKillingTests().size() + mutation.getSucceedingTests().size();
                return mutation.getNumberOfTestsRun() - succeeding_test;
            case TIMED_OUT:
                return mutation.getNumberOfTestsRun();
            case NO_COVERAGE:
                return 0;
            default:
                throw new IllegalArgumentException(String.format("This mutation status, (%s), wasn't expected. Expected KILLED, SURVIVED, TIMED_OUT, or NO_COVERAGE.", mutation.getStatus()));
        }
    }

    private Set<String> describeAlternativeTests(int timedOutTests, Set<String> tests, String mutationInfo) {
        if (tests.size() != timedOutTests) {
            System.out.println("" + tests.size() + " != " + timedOutTests);
        }
        if (tests.size() == timedOutTests && tests.size() <= 0) {
            HashSet<String> none = new HashSet<>(Collections.singletonList("none"));
            return describeTests(mutationInfo, none, DetectionStatus.NO_COVERAGE);
        }
        return describeTests(mutationInfo, tests, DetectionStatus.TIMED_OUT);
    }

    //TODO: Extract utility class (There's too much private methods in this class)
    private Set<String> getAlternativeTestsName(MutationResult mutation, Set<String> filteringTests) {
        Set<String> formattedFilteringTests = filteringTests.stream().map(reportFormatter::clearSyntax).collect(Collectors.toSet());
        Set<String> allTestsName = mutation.getDetails().getTestsInOrder().stream().map(tests -> reportFormatter.clearSyntax(tests.getName())).collect(Collectors.toSet());

        allTestsName.removeAll(formattedFilteringTests);
        return allTestsName;
    }

    private Set<String> describeKilledTests(Set<String> tests, String mutationInfo) {
        return describeTests(mutationInfo, tests, DetectionStatus.KILLED);
    }

    private Set<String> describeSurvivedTests(Set<String> tests, String mutationInfo) {
        return describeTests(mutationInfo, tests, DetectionStatus.SURVIVED);
    }

    /**
     * Describe mutation in CSV complaining format
     *
     * @param mutation mutation to describe
     * @return CSV description of mutation ("class::method,mutated-line,mutator")
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
     * @return CSV rows describing mutation results with the same elimination status ("class::testcase-method,KILLED?,class::method,mutated-line,mutator")
     */
    private Set<String> describeTests(String mutationInfo, Set<String> tests, DetectionStatus status) {
        return tests.stream().map(test -> reportFormatter.makeCsv(reportFormatter.clearSyntax(test), status, mutationInfo) + NEW_LINE).collect(Collectors.toSet());
    }
}
