package com.adobe.project.response;

import com.adobe.project.headers.HttpResponseHeader;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class HttpResponse
{
    private HttpResponseHeader httpResponseHeader;
    private ResponseCodes responseCode;
    private final String version = "HTTP/1.1";
    private String contentBody;

    @Override
    public String toString(){
        final String SPACE_SEPARATOR = " ";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(version)
            .append(SPACE_SEPARATOR)
            .append(responseCode.toString())
            .append(System.lineSeparator())
            .append(httpResponseHeader.toString())
            .append(System.lineSeparator());
        return stringBuilder.toString();
    }
}
