package org.pitest.mutationtest.report.detailed.csv.describers;

import org.pitest.mutationtest.MutationResult;
import org.pitest.mutationtest.report.detailed.csv.ReportFormatter;

public class MutationDescriberImpl implements MutationDescriber {
    private ReportFormatter reportFormatter;

    public MutationDescriberImpl(ReportFormatter reportFormatter) {
        this.reportFormatter = reportFormatter;
    }

    /**
     * Describe mutation in CSV complaining format
     *
     * @param mutation mutation to describe
     * @return CSV description of mutation ("class::method,mutated-line,mutator")
     */
    @Override
    public String getMutationInfo(MutationResult mutation) {
        return reportFormatter.makeCsv(reportFormatter.getFormattedLocation(mutation), mutation.getDetails().getLineNumber(), mutation.getDetails().getMutator());
    }
}
