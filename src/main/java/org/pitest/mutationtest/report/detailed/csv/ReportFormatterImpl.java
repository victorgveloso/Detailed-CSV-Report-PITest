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
        return s.replaceFirst("\\.\\[engine:.+?]/\\[class:.+?]/\\[method:(.+?)\\(\\)]", "::$1");
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
        return String.format("%s::%s", clazz.asJavaName(), method);
    }
}
