package com.adobe.project.request;

import lombok.Getter;

/**
 * @author <a href="mailto:amansinh@gmail.com">Aman Sinha</a>
 *
 * {@link HttpServerSupportedMethods} contains the methods which are currently supported by the server.
 */

@Getter
public enum HttpServerSupportedMethods {
    GET("GET"),
    HEAD("HEAD");

    String value;

    HttpServerSupportedMethods(final String value) {
        this.value = value;
    }
}
