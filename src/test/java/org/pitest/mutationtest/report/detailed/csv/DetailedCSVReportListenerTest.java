package org.pitest.mutationtest.report.detailed.csv;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pitest.mutationtest.DetectionStatus;
import org.pitest.mutationtest.MutationResult;
import org.pitest.mutationtest.MutationStatusTestPair;
import org.pitest.mutationtest.report.MutationTestResultMother;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import static org.mockito.Mockito.verify;

public class DetailedCSVReportListenerTest {

    private static final String NEW_LINE = System.getProperty("line.separator");
    private static final List<String> killingTests = List.of("foo", "bar", "baz");

    private DetailedCSVReportListener testee;

    @Mock
    private Writer out;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.testee = new DetailedCSVReportListener(this.out);
    }

    @Test
    public void shouldOutputAllKillingTestsWhenFound() throws IOException {
        final MutationResult mr = new MutationResult(
                MutationTestResultMother.createDetails(),
                new MutationStatusTestPair(
                        3,
                        DetectionStatus.KILLED,
                        killingTests,
                        List.of()
                )
        );
        this.testee.handleMutationResult(MutationTestResultMother
                .createClassResults(mr));
        final String expected = "file,clazz,mutator,method,42,KILLED,"
                + String.join(",", killingTests)
                + NEW_LINE;
        verify(this.out).write(expected);
    }

    @Test
    public void shouldOutputNoneWhenNoKillingTestFound() throws IOException {
        final MutationResult mr = new MutationResult(
                MutationTestResultMother.createDetails(), MutationStatusTestPair.notAnalysed(1,
                DetectionStatus.SURVIVED));
        this.testee.handleMutationResult(MutationTestResultMother
                .createClassResults(mr));
        final String expected = "file,clazz,mutator,method,42,SURVIVED,none"
                + NEW_LINE;

        verify(this.out).write(expected);
    }

}
