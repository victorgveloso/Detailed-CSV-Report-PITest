package org.pitest.mutationtest.report.detailed.csv;

import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;
import org.pitest.coverage.TestInfo;
import org.pitest.mutationtest.ClassMutationResults;
import org.pitest.mutationtest.DetectionStatus;
import org.pitest.mutationtest.MutationResult;
import org.pitest.mutationtest.report.detailed.csv.utils.FileWriter;

import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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
        List<String> survivedTests = describeSurvivedTests(mutation, mutationInfo);
        List<String> killedTests = describeKilledTests(mutation, mutationInfo);
        Stream<String> succeedingTests = Stream.concat(survivedTests.stream(), killedTests.stream());
        List<String> timedOutTests = describeTimedOutTests(countTimedOutTests(mutation), getTimedOutTestsName(mutation, succeedingTests), mutationInfo);
        return concat(Arrays.asList(survivedTests.stream(), killedTests.stream(), timedOutTests.stream())).collect(Collectors.joining(NEW_LINE)) + NEW_LINE;
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
                throw new ValueException(String.format("This mutation status, (%s), wasn't expected. Expected KILLED, SURVIVED, TIMED_OUT, or NO_COVERAGE.", mutation.getStatus()));
        }
    }

    private List<String> describeTimedOutTests(int timedOutTests, List<String> tests, String mutationInfo) {
        if (timedOutTests <= 0) {
            return Collections.emptyList(); //TODO: Return NO_COVERAGE with compatible formatting
        }
        return describeTests(mutationInfo, tests, DetectionStatus.TIMED_OUT);
    }

    //TODO: Extract utility class (There's too much private methods in this class)
    private List<String> getTimedOutTestsName(MutationResult mutation, Stream<String> filteringTests) {
        Stream<String> allTestsName = mutation.getDetails().getTestsInOrder().stream().map(TestInfo::getName);
        return allTestsName.filter(name -> filteringTests.noneMatch(name::equals)).collect(Collectors.toList());
    }

    private List<String> describeKilledTests(MutationResult mutation, String mutationInfo) {
        return describeTests(mutationInfo, mutation.getKillingTests(), DetectionStatus.KILLED);
    }

    private List<String> describeSurvivedTests(MutationResult mutation, String mutationInfo) {
        return describeTests(mutationInfo, mutation.getSucceedingTests(), DetectionStatus.SURVIVED);
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
    private List<String> describeTests(String mutationInfo, List<String> tests, DetectionStatus status) {
        return tests.stream().map(test -> reportFormatter.makeCsv(reportFormatter.clearSyntax(test), status, mutationInfo)).collect(Collectors.toList());
    }
}
