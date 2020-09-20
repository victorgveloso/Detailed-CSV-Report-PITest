package org.pitest.mutationtest.report.detailed.csv;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
}
