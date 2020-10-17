package com.adobe.project.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class HttpServerSupportedMethodsTests {

    @DisplayName("Check the values of the supported methods")
    @Test
    public void httpServerSupportedMethodTest() {
        assertThat(HttpServerSupportedMethods.GET.getValue()).isEqualTo("GET");
        assertThat(HttpServerSupportedMethods.HEAD.getValue()).isEqualTo("HEAD");
    }

    @DisplayName("Test for unsupported method exception")
    @Test
    public void httpServerUnSupportedMethodTest() {
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> HttpServerSupportedMethods.valueOf("TEST"));
    }

}
