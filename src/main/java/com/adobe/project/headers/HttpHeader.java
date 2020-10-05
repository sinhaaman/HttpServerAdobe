package com.adobe.project.headers;

import java.util.Map;
import java.util.TreeMap;

import lombok.Getter;

/**
 * @author <a href="mailto:amansinh@gmail.com">Aman Sinha</a>
 *
 * An abstract class which contains the common header of the Http headers for both request and response.
 */
@Getter
public abstract class HttpHeader {

    public static final String DATE = "Date";
    public static final String CONNECTION = "Connection";

    protected volatile Map<String, String> headersMap = new TreeMap<>();

    protected HttpHeader() {
    }

    protected HttpHeader(final Map<String, String> map) {
        this.headersMap.putAll(map);
    }

    public abstract void setHeaders(final String header, final String value);

    @Override
    public String toString() {
        final String SPACE_SEPARATOR = " ";
        final String KEY_VALUE_SEPARATOR = ":";
        StringBuilder stringBuilder = new StringBuilder();
        headersMap.entrySet().stream()
            .forEach(entry -> stringBuilder
                .append(entry.getKey())
                .append(KEY_VALUE_SEPARATOR)
                .append(SPACE_SEPARATOR)
                .append(entry.getValue())
                .append(System.lineSeparator()));

        return stringBuilder.toString();
    }
}
