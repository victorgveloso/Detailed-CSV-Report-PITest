/*
 * Copyright 2011 Henry Coles
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package org.pitest.mutationtest.report;

import org.pitest.coverage.TestInfo;
import org.pitest.mutationtest.ClassMutationResults;
import org.pitest.mutationtest.MutationResult;
import org.pitest.mutationtest.engine.MutationDetails;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.pitest.mutationtest.LocationMother.aMutationId;

public class MutationTestResultMother {

  public static MutationDetails createDetails() {
    return createDetails("file", "clazz", "method");
  }

  public static MutationDetails createDetails(final String file, final String clazz, final String method) {
    return new MutationDetails(aMutationId(clazz, method).build(), file, "desc", 42, 0);
  }

  public static MutationResult createResult() {
    return new MutationResult(createDetails(), null);
  }

  public static MutationResult createResult(final String file, final String clazz, final String method) {
    return new MutationResult(createDetails(file, clazz, method), null);
  }

  public static ClassMutationResults createClassResults(
      final MutationResult... mrs) {
    return new ClassMutationResults(Arrays.asList(mrs));
  }

  public static MutationDetails getDetails(List<String> tests) {
    MutationDetails details = createDetails();
    details.addTestsInOrder(tests.stream().map(MutationTestResultMother::getTestInfo).collect(Collectors.toList()));
    return details;
  }

  private static TestInfo getTestInfo(String s) {
    return new TestInfo(null, s, 0, Optional.empty(), 0);
  }
}
