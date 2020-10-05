package com.adobe.project.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HttpRequestOperationLine {
    final String method;
    final String path;
    final String version;

    @Override
    public String toString() {
        return method + " " + path + " " + version;
    }
}
