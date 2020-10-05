package com.adobe.project.request;

import com.adobe.project.headers.HttpRequestHeader;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class HttpRequest
{
    final HttpRequestOperationLine httpRequestOperationLine;
    final HttpRequestHeader httpRequestHeader;
}
