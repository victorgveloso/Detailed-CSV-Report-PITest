package org.pitest.mutationtest.report.detailed.csv;

import org.pitest.mutationtest.ClassMutationResults;
import org.pitest.mutationtest.MutationResult;
import org.pitest.mutationtest.report.detailed.csv.describers.DescribersFactory;
import org.pitest.mutationtest.report.detailed.csv.describers.MutationDescriber;
import org.pitest.mutationtest.report.detailed.csv.describers.tests.TestDescriber;
import org.pitest.mutationtest.report.detailed.csv.utils.FileWriter;

import java.io.Writer;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Creates reports about mutation testing
 */
public class MutationReporterImpl implements MutationReporter {
    private DescribersFactory describersFactory;
    private Collection<TestDescriber> testDescribers;
    // Output file
    private Writer mutationOutput;

    /**
     * Main constructor
     * @param reportFormatter Utility object for CSV formatting
     * @param describersFactory Factory of test describers
     * @param mutationOutput  Report output file
     */
    public MutationReporterImpl(ReportFormatter reportFormatter, DescribersFactory describersFactory, Writer mutationOutput) {
        this.describersFactory = describersFactory;
        this.mutationOutput = mutationOutput;

        testDescribers = createTestDescribers(reportFormatter);
    }

    @Override
    public void reportMutationResults(ClassMutationResults metaData, FileWriter fileWriter) {
        metaData.getMutations().forEach(mutation -> fileWriter.writeToFile(mutationOutput, generateMutationReport(mutation)));
    }

    /**
     * Factory method of test describers
     *
     * @param reportFormatter Utility object for CSV formatting
     * @return Default test describers
     */
    private Collection<TestDescriber> createTestDescribers(ReportFormatter reportFormatter) {
        MutationDescriber mutationDescriber = describersFactory.getMutationDescriber(reportFormatter);
        return describersFactory.getTestDescribers(reportFormatter, mutationDescriber);
    }

    /**
     * Generate report for a given mutation result.
     *
     * @param mutation Result of a mutation
     * @return CSV rows describing mutation result
     */
    private String generateMutationReport(MutationResult mutation) {

        Stream<String> concatenatedDescription = concat(testDescribers.stream().map(describer -> describer.describeTests(mutation).stream()).collect(Collectors.toSet()));
        return concatenatedDescription.collect(Collectors.joining(""));
    }

    /**
     * Concatenate a collection of two or more string streams
     *
     * @param steams Collection of string streams
     * @return concatenated stream
     */
    private Stream<String> concat(Collection<Stream<String>> steams) {
        Stream<String> output = Stream.empty();
        for (Stream<String> st : steams) {
            output = Stream.concat(output, st);
        }
        return output;
    }

}
