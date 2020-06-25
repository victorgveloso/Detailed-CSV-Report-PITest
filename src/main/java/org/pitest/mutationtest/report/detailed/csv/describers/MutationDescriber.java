package org.pitest.mutationtest.report.detailed.csv.describers;

import org.pitest.mutationtest.MutationResult;

public interface MutationDescriber {
    String getMutationInfo(MutationResult mutation);
}
