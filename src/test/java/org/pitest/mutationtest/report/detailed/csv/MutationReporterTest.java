package org.pitest.mutationtest.report.detailed.csv;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pitest.mutationtest.DetectionStatus;
import org.pitest.mutationtest.MutationResult;
import org.pitest.mutationtest.MutationStatusTestPair;
import org.pitest.mutationtest.report.MutationTestResultMother;
import org.pitest.mutationtest.report.detailed.csv.utils.Constants;
import org.pitest.mutationtest.report.detailed.csv.utils.FileWriter;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.verify;

public class MutationReporterTest {
    private static final List<String> killingTests = Arrays.asList("foo", "bar", "baz");

    private MutationReporter testee;

    @Mock
    private Writer mutationOutput;

    private FileWriter fileWriter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DetailedCSVReportFactoryImpl detailedCSVReportFactory = new DetailedCSVReportFactoryImpl();
        fileWriter = detailedCSVReportFactory.getFileWriter();
        testee = new MutationReporterImpl(detailedCSVReportFactory.getReportFormatter(), detailedCSVReportFactory.getDescribersFactory(), mutationOutput);
    }

    @Test
    public void shouldOutputAllKillingTestsWhenFound() throws IOException {
        final MutationResult mr = new MutationResult(MutationTestResultMother.getDetails(killingTests), new MutationStatusTestPair(3, DetectionStatus.KILLED, killingTests, Collections.emptyList()));
        this.testee.reportMutationResults(MutationTestResultMother.createClassResults(mr), fileWriter);
        String expectedOutput = killingTests.stream().map(s -> s + ",KILLED,clazz.method,42,mutator" + Constants.NEW_LINE).collect(Collectors.joining());
        verify(this.mutationOutput).write(expectedOutput);
    }

    @Test
    public void shouldOutputNoneWhenNoCoveringTestFound() throws IOException {
        final MutationResult mr = new MutationResult(MutationTestResultMother.getDetails(Collections.emptyList()), MutationStatusTestPair.notAnalysed(1, DetectionStatus.NO_COVERAGE));
        this.testee.reportMutationResults(MutationTestResultMother.createClassResults(mr), fileWriter);
        verify(this.mutationOutput).write("none,NO_COVERAGE,clazz.method,42,mutator" + Constants.NEW_LINE);
    }

    @Test
    public void shouldOutputSurvivedWhenNoKillingTestFound() throws IOException {
        final MutationResult mr = new MutationResult(MutationTestResultMother.getDetails(Collections.singletonList("test")), new MutationStatusTestPair(3, DetectionStatus.SURVIVED, Collections.emptyList(), Collections.singletonList("test")));
        this.testee.reportMutationResults(MutationTestResultMother.createClassResults(mr), fileWriter);
        verify(this.mutationOutput).write("test,SURVIVED,clazz.method,42,mutator" + Constants.NEW_LINE);
    }


}
