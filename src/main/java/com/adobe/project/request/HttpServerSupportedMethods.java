package com.adobe.project.request;

import lombok.Getter;

@Getter
public enum HttpServerSupportedMethods {
    GET("GET"),
    HEAD("HEAD");

    String value;

    HttpServerSupportedMethods(final String value) {
        this.value = value;
    }
}
