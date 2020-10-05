package com.adobe.project.headers;

import lombok.Getter;

@Getter
public class HttpResponseHeader extends HttpHeader {

    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    public static final String CONTENT_ENCODING = "Content-Encoding";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String ETAG = "Etag";
    public static final String KEEP_ALIVE = "Keep-Alive";
    public static final String LAST_MODIFIED = "Last-Modified";
    public static final String SERVER = "Server";
    public static final String SET_COOKIE = "Set-Cookie";
    public static final String TRANSFER_ENCODING = "Transfer-Encoding";
    public static final String VARY = "Vary";
    public static final String X_BACKEND_SERVER = "X-Backend-Server";
    public static final String X_CACHE_INFO = "X-Cache-Info";
    public static final String X_KUMA_REVISION = "X-kuma-revision";
    public static final String X_FRAME_OPTIONS = "x-frame-options";

    public HttpResponseHeader() {
        super();
    }

    @Override
    public void setHeaders(String header, String value) {
        this.headersMap.put(header, value);
    }

}
