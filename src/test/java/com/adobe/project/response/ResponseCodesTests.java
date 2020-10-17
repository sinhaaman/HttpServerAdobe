package com.adobe.project.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class ResponseCodesTests {

    @DisplayName("Test the status code and status messages of the response codes")
    @Test
    public void ResponseCodeStatusCodeStatusMessageTest() {
        // given


        // when


        // then
        assertThat(ResponseCodes.ACCEPTED.getStatusCode()).isEqualTo(202);
        assertThat(ResponseCodes.OK.getStatusCode()).isEqualTo(200);
        assertThat(ResponseCodes.NOT_FOUND.getStatusCode()).isEqualTo(404);
        assertThat(ResponseCodes.INTERNAL_ERROR.getStatusCode()).isEqualTo(500);
        assertThat(ResponseCodes.NOT_IMPLEMENTED.getStatusCode()).isEqualTo(501);

        assertThat(ResponseCodes.ACCEPTED.getStatusMessage()).isEqualTo("Accepted");
        assertThat(ResponseCodes.OK.getStatusMessage()).isEqualTo("OK");
        assertThat(ResponseCodes.NOT_FOUND.getStatusMessage()).isEqualTo("Not Found");
        assertThat(ResponseCodes.INTERNAL_ERROR.getStatusMessage()).isEqualTo("Internal Error");
        assertThat(ResponseCodes.NOT_IMPLEMENTED.getStatusMessage()).isEqualTo("Not Implemented");
    }

    @DisplayName("Test toString method of the response codes")
    @Test
    public void ResponseCodeToStringTest() {
        // given


        // when


        // then
        assertThat(ResponseCodes.ACCEPTED.toString()).isEqualTo("202 Accepted");
    }

    @DisplayName("Test toString method of the response codes")
    @Test
    public void UnimplementedResponseCodeTest() {
        // given


        // when


        // then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> ResponseCodes.valueOf("TEST"));
    }
}
