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

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.verify;

public class DetailedCSVReportListenerIntegrationTest {
    private static final List<String> killingTests = Arrays.asList("foo", "bar", "baz");

    private DetailedCSVReportListener testee;

    @Mock
    private Writer mutationOutput;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DetailedCSVReportFactory detailedCSVReportFactory = new DetailedCSVReportFactoryImpl();
        testee = new DetailedCSVReportListener(detailedCSVReportFactory.getFileWriter(), detailedCSVReportFactory.getMutationReporter(mutationOutput));
    }

    @Test
    public void shouldOutputAllKillingTestsWhenFound() throws IOException {
        final MutationResult mr = new MutationResult(MutationTestResultMother.getDetails(killingTests), new MutationStatusTestPair(3, DetectionStatus.KILLED, killingTests, Collections.emptyList()));
        this.testee.handleMutationResult(MutationTestResultMother.createClassResults(mr));
        String expectedOutput = killingTests.stream().map(s -> s + ",KILLED,clazz.method,42,mutator" + Constants.NEW_LINE).collect(Collectors.joining());
        verify(this.mutationOutput).write(expectedOutput);
    }

    @Test
    public void shouldOutputNoneWhenNoCoveringTestFound() throws IOException {
        final MutationResult mr = new MutationResult(MutationTestResultMother.getDetails(Collections.emptyList()), MutationStatusTestPair.notAnalysed(1, DetectionStatus.NO_COVERAGE));
        this.testee.handleMutationResult(MutationTestResultMother.createClassResults(mr));
        verify(this.mutationOutput).write("none,NO_COVERAGE,clazz.method,42,mutator" + Constants.NEW_LINE);
    }

    @Test
    public void shouldOutputSurvivedWhenNoKillingTestFound() throws IOException {
        final MutationResult mr = new MutationResult(MutationTestResultMother.getDetails(Collections.singletonList("test")), new MutationStatusTestPair(3, DetectionStatus.SURVIVED, Collections.emptyList(), Collections.singletonList("test")));
        this.testee.handleMutationResult(MutationTestResultMother.createClassResults(mr));
        verify(this.mutationOutput).write("test,SURVIVED,clazz.method,42,mutator" + Constants.NEW_LINE);
    }


}
