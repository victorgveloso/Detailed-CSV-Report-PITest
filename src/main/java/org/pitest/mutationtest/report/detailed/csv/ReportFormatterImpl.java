package org.pitest.mutationtest.report.detailed.csv;

import org.pitest.classinfo.ClassName;
import org.pitest.mutationtest.MutationResult;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * CSV Report Formatting utility class
 */
public class ReportFormatterImpl implements ReportFormatter {
    private static ReportFormatter instance;

    public static ReportFormatter getInstance() {
        if (instance == null) instance = new ReportFormatterImpl();
        return instance;
    }

    @Override
    public String clearSyntax(String s) {
        String formatted = extractMethodSignature(s);
        formatted = removeUndesiredTokens(formatted);
        if (isWellFormatted(formatted)) {
            return formatted;
        } else {
            return s;
        }
    }

    private String removeUndesiredTokens(String formatted) {
        return formatted.replaceAll(" |/|\\[.+?]|\\(.+?\\)", "").replaceAll("\\.*([.:])", "$1");
    }

    private String extractMethodSignature(String s) {
        return s.replaceAll("\\[method:(.+?)\\(\\)]", ".$1");
    }

    private boolean isWellFormatted(String formatted) {
        return formatted.matches("[.a-zA-Z0-9]+(?:::[.a-zA-Z0-9]+)?");
    }

    @Override
    public String makeCsv(final Object... os) {
        return Arrays.stream(os).map(Object::toString).collect(Collectors.joining(","));
    }

    @Override
    public String getFormattedLocation(MutationResult mutation) {
        return getFormattedLocation(mutation.getDetails().getClassName(), mutation.getDetails().getMethod().name());
    }

    /**
     * Method name and class formatting (Single point of configuration)
     *
     * @param clazz  Method's class
     * @param method Method name
     * @return Formatted path
     */
    private String getFormattedLocation(ClassName clazz, String method) {
        return String.format("%s.%s", clazz.asJavaName(), method);
    }
}
