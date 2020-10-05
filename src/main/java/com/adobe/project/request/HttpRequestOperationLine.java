package com.adobe.project.request;

import com.adobe.project.headers.HttpRequestHeader;
import lombok.Builder;
import lombok.Getter;

/**
 * @author <a href="mailto:amansinh@gmail.com">Aman Sinha</a>
 *
 * {@link HttpRequestOperationLine} contains method, path and version of the request.
 */

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
