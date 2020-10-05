package com.adobe.project.request;

import com.adobe.project.headers.HttpRequestHeader;
import lombok.Builder;
import lombok.Getter;

/**
 * @author <a href="mailto:amansinh@gmail.com">Aman Sinha</a>
 *
 * {@link HttpRequest} is a composition of the method, path and version, mentioned in the {@link HttpRequestOperationLine}
 * which is the followed by the header lines in the request, mentioned in the {@link HttpRequestHeader}.
 */

@Builder
@Getter
public class HttpRequest
{
    final HttpRequestOperationLine httpRequestOperationLine;
    final HttpRequestHeader httpRequestHeader;
}
