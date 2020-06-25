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
    // Utility class
    private ReportFormatter reportFormatter;
    private DescribersFactory describersFactory;
    // Output file
    private Writer mutationOutput;

    /**
     * Main constructor
     * @param reportFormatter Utility object for CSV formatting
     * @param describersFactory
     * @param mutationOutput  Report output file
     */
    public MutationReporterImpl(ReportFormatter reportFormatter, DescribersFactory describersFactory, Writer mutationOutput) {
        this.reportFormatter = reportFormatter;
        this.describersFactory = describersFactory;
        this.mutationOutput = mutationOutput;
    }

    @Override
    public void reportMutationResults(ClassMutationResults metaData, FileWriter fileWriter) {
        metaData.getMutations().forEach(mutation -> fileWriter.writeToFile(mutationOutput, generateMutationReport(mutation)));
    }

    private Collection<TestDescriber> testDescribersFactory(ReportFormatter reportFormatter) {

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
        Collection<TestDescriber> describers = testDescribersFactory(reportFormatter);

        Stream<String> concatenatedDescription = concat(describers.stream().map(describer -> describer.describeTests(mutation).stream()).collect(Collectors.toSet()));
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
