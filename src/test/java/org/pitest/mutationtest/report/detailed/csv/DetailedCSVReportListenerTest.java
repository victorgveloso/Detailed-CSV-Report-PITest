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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

public class DetailedCSVReportListenerTest {
    private static final List<String> killingTests = Arrays.asList("foo", "bar", "baz");

    private DetailedCSVReportListener testee;

    @Mock
    private Writer mutationOutput;

    @Mock
    private Writer coverageOutput;

    @Mock
    private Writer locOutput;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DetailedCSVReportFactory detailedCSVReportFactory = new DetailedCSVReportFactoryImpl();
        testee = new DetailedCSVReportListener(detailedCSVReportFactory.getFileWriter(), detailedCSVReportFactory.getMutationReporter(mutationOutput));
    }

    @Test
    public void shouldOutputAllKillingTestsWhenFound() throws IOException {
        final MutationResult mr = new MutationResult(
                MutationTestResultMother.createDetails(),
                new MutationStatusTestPair(
                        3,
                        DetectionStatus.KILLED,
                        killingTests,
                        Collections.emptyList()
                )
        );
        this.testee.handleMutationResult(MutationTestResultMother
                .createClassResults(mr));
        for (String test : killingTests) {
            final String expected = test + "KILLED,clazz::method,42,mutator" + Constants.NEW_LINE;
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
