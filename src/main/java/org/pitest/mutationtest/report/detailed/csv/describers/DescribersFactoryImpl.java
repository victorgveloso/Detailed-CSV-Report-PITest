package org.pitest.mutationtest.report.detailed.csv.describers;

import org.pitest.mutationtest.report.detailed.csv.ReportFormatter;
import org.pitest.mutationtest.report.detailed.csv.describers.tests.AlternativeTestsDescriber;
import org.pitest.mutationtest.report.detailed.csv.describers.tests.KilledTestsDescriber;
import org.pitest.mutationtest.report.detailed.csv.describers.tests.SurvivedTestsDescriber;
import org.pitest.mutationtest.report.detailed.csv.describers.tests.TestDescriber;

import java.util.Arrays;
import java.util.Collection;

public class DescribersFactoryImpl implements DescribersFactory {
    @Override
    public MutationDescriber getMutationDescriber(ReportFormatter reportFormatter) {
        return new MutationDescriberImpl(reportFormatter);
    }

    @Override
    public Collection<TestDescriber> getTestDescribers(ReportFormatter reportFormatter, MutationDescriber mutationDescriber) {
        KilledTestsDescriber killed = new KilledTestsDescriber(reportFormatter, mutationDescriber);
        SurvivedTestsDescriber survived = new SurvivedTestsDescriber(reportFormatter, mutationDescriber);
        AlternativeTestsDescriber alternative = new AlternativeTestsDescriber(reportFormatter, mutationDescriber);

        return Arrays.asList(killed, survived, alternative);
    }
}
