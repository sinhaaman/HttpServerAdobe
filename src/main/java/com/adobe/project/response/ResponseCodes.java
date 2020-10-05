package com.adobe.project.response;

import lombok.Getter;

/**
 * @author <a href="mailto:amansinh@gmail.com">Aman Sinha</a>
 *
 * This class contains the status codes of the responses along with messages with them.
 */
@Getter
public enum ResponseCodes {

    ACCEPTED(202, "Accepted"),
    OK(200, "OK"),
    NOT_FOUND(404, "Not Found"),
    INTERNAL_ERROR(500, "Internal Error"),
    NOT_IMPLEMENTED(501, "Not Implemented");

    int statusCode;
    String statusMessage;

    ResponseCodes(int statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    @Override
    public String toString() {
        final String SPACE_SEPARATOR = " ";
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder.append(statusCode)
            .append(SPACE_SEPARATOR)
            .append(statusMessage)
            .toString();
    }
}
