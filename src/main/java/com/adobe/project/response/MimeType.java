package com.adobe.project.response;

import lombok.Getter;

@Getter
public enum MimeType {
    HTML("text/html"),
    PLAIN("text/plain");

    private final String mimeTypeFormat;

    MimeType(final String mimeTypeFormat) {
        this.mimeTypeFormat = mimeTypeFormat;
    }
}
