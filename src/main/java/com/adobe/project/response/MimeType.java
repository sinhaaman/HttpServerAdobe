package com.adobe.project.response;

import lombok.Getter;

/**
 * @author <a href="mailto:amansinh@gmail.com">Aman Sinha</a>
 *
 * This class contains the supported {@link MimeType of the server.
 */

@Getter
public enum MimeType {
    HTML("text/html"),
    PLAIN("text/plain");

    private final String mimeTypeFormat;

    MimeType(final String mimeTypeFormat) {
        this.mimeTypeFormat = mimeTypeFormat;
    }
}
