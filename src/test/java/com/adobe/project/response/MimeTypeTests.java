package com.adobe.project.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class MimeTypeTests {

    @DisplayName("Check the values of the supported mime types")
    @Test
    public void mimeTypeSupportedTest() {
        assertThat(MimeType.HTML.getMimeTypeFormat()).isEqualTo("text/html");
        assertThat(MimeType.PLAIN.getMimeTypeFormat()).isEqualTo("text/plain");
    }

    @DisplayName("Test for unsupported mime type exception")
    @Test
    public void mimeTypeUnSupportedTest() {
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> MimeType.valueOf("TEST"));
    }
}
