package org.pitest.mutationtest.report.detailed.csv;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.pitest.mutationtest.MutationResult;
import org.pitest.mutationtest.report.MutationTestResultMother;

class ReportFormatterTest {
    ReportFormatter fmt;


    @BeforeEach
    void setUp() {
        fmt = new ReportFormatterImpl();
    }

    @Test
    void testClearSyntaxOfMutationInfo() {
        Assertions.assertEquals("any.amount.of.Dots.oi", fmt.clearSyntax("any.amount.of.Dots.[followed.by.brackets]/[then.more.brackets].[more?].[method:oi()]"));
    }

    @Test
    void testClearSyntaxOfMethodDescription() {
        Assertions.assertEquals("okhttp3.internal.cache.DiskLruCacheTest.constructorDoesNotAllowZeroCacheSize", fmt.clearSyntax("okhttp3.internal.cache.DiskLruCacheTest.constructorDoesNotAllowZeroCacheSize[FileSystem.SYSTEM](okhttp3.internal.cache.DiskLruCacheTest)"));
    }

    @Test
    void testClearSyntaxOfMethodWithWhiteSpaces() {
        Assertions.assertEquals("okhttp3.internal.cache.DiskLruCacheTest.removewhilewritingcreateszombiethatisremovedwhenwritefinishes", fmt.clearSyntax("okhttp3.internal.cache.DiskLruCacheTest.remove while writing creates zombie that is removed when write finishes[InMemoryFileSystem](okhttp3.internal.cache.DiskLruCacheTest)"));
    }

    @Test
    void testClearSyntaxOfParametrizedTest() {
        Assertions.assertEquals("okhttp3.internal.cache.DiskLruCacheTest.editSnapshotAfterChangeCommitted", fmt.clearSyntax("okhttp3.internal.cache.DiskLruCacheTest.editSnapshotAfterChangeCommitted[FileSystem.SYSTEM](okhttp3.internal.cache.DiskLruCacheTest)"));
    }

    @Test
    void testMakeCsvFromStrings() {
        Assertions.assertEquals("first,second,third,fourth", fmt.makeCsv("first", "second", "third", "fourth"));
    }

    @Test
    void testMakeCsvFromIntegers() {
        Assertions.assertEquals("1,2,3,4,5,6", fmt.makeCsv(1, 2, 3, 4, 5, 6));
    }

    @Nested
    class RequiresMutationResult {
        MutationResult mutation;
        private String clazz;
        private String method;

        @BeforeEach
        void setUp() {
            clazz = "Example.Package.ExampleClass";
            method = "ExampleMethod";
            mutation = MutationTestResultMother.createResult("", clazz, method);
        }

        @Test
        void testFormattedLocation() {
            Assertions.assertEquals(String.format("%s.%s", clazz, method), fmt.getFormattedLocation(mutation));
        }
    }
}
