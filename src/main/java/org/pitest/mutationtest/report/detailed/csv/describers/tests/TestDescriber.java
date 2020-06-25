package org.pitest.mutationtest.report.detailed.csv.describers.tests;

import org.pitest.mutationtest.MutationResult;

import java.util.Set;

public interface TestDescriber {
    Set<String> describeTests(MutationResult mutation);
}
