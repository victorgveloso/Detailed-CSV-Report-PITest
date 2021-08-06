package org.pitest.mutationtest.report.detailed.csv;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.pitest.mutationtest.ClassMutationResults;
import org.pitest.mutationtest.report.MutationTestResultMother;
import org.pitest.mutationtest.report.detailed.csv.utils.FileWriter;

public class DetailedCSVReportListenerTest {
    @Mock
    FileWriter fw;

    @Mock
    MutationReporter mr;

    private DetailedCSVReportListener testee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        testee = new DetailedCSVReportListener(fw, mr);
    }

    @Test
    void testEnd() {
        testee.runEnd();
        Mockito.verify(fw).closeAllUsedFiles();
    }

    @Test
    void testMutationResult() {
        ClassMutationResults r = MutationTestResultMother.createClassResults(MutationTestResultMother.createResult());
        testee.handleMutationResult(r);
        Mockito.verify(mr).reportMutationResults(r, fw);
    }
}
