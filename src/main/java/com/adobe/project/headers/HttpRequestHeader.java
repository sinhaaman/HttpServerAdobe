package com.adobe.project.headers;

import java.util.Map;

import lombok.Getter;

@Getter
public class HttpRequestHeader extends HttpHeader {

    public final String HOST = "Host";
    public final String USER_AGENT = "User-Agent";
    public final String ACCEPT = "Accept";
    public final String ACCEPT_LANGUAGE = "Accept-Language";
    public final String ACCEPT_ENCODING = "Accept-Encoding";
    public final String REFERER = "Referer";
    public final String UPGRADE_INSECURE_REQUESTS = "Upgrade-Insecure-Requests";
    public final String IF_MODIFIED_SINCE = "If-Modified-Since";
    public final String IF_NONE_MATCH = "If-None-Match";
    public final String CACHE_CONTROL = "Cache-Control";
    public final String CONTENT_LENGTH = "Content-Length";

    public HttpRequestHeader() {
        super();
    }

    public HttpRequestHeader(final Map<String, String> map) {
        super(map);
    }

    @Override
    public void setHeaders(String header, String value) {
        this.headersMap.put(header, value);
    }
}
