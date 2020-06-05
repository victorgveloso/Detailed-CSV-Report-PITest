package org.pitest.mutationtest.report.detailed.csv;

import org.pitest.mutationtest.MutationResult;

public interface ReportFormatter {
    /**
     * Remove dirty output arisen from test names.
     *
     * @param s text containing test names
     * @return cleared text
     */
    String clearSyntax(String s);

    /**
     * Join parameters into a string with separated with comma.
     *
     * @param os any object whose string representation doesn't contain the delimiter character (comma)
     * @return valid CSV row
     */
    String makeCsv(Object... os);

    /**
     * Method name and class formatting (specialized for MutationResult, delegates to generic method).
     *
     * @param mutation Finished mutation data
     * @return Formatted path
     */
    String getFormattedLocation(MutationResult mutation);
}
