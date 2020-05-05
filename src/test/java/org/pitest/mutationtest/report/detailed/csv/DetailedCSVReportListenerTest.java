package org.pitest.mutationtest.report.detailed.csv;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pitest.coverage.CoverageDatabase;
import org.pitest.mutationtest.DetectionStatus;
import org.pitest.mutationtest.MutationResult;
import org.pitest.mutationtest.MutationStatusTestPair;
import org.pitest.mutationtest.report.MutationTestResultMother;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import static org.mockito.Mockito.*;

public class DetailedCSVReportListenerTest {

    private static final String NEW_LINE = System.getProperty("line.separator");
    private static final List<String> killingTests = List.of("foo", "bar", "baz");

    private DetailedCSVReportListener testee;

    @Mock
    private Writer mutationOutput;

    @Mock
    private Writer coverageOutput;

    @Mock
    private Writer locOutput;

    @Mock
    private CoverageDatabase coverage;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        testee = new DetailedCSVReportListener(mutationOutput, coverageOutput, locOutput, coverage);
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
        for (String test : killingTests) {
            final String expected = test + "KILLED,clazz::method,42,mutator" + NEW_LINE;
            verify(this.mutationOutput).write(expected);
        }
    }

    @Test
    public void shouldOutputNoneWhenNoKillingTestFound() {
        final MutationResult mr = new MutationResult(
                MutationTestResultMother.createDetails(), MutationStatusTestPair.notAnalysed(1,
                DetectionStatus.SURVIVED));
        this.testee.handleMutationResult(MutationTestResultMother
                .createClassResults(mr));

        verifyNoInteractions(this.mutationOutput);
    }

}
