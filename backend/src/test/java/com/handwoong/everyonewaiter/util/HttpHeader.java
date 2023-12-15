package com.handwoong.everyonewaiter.util;

import java.util.Arrays;
import java.util.List;

public enum HttpHeader {
    VARY("Vary"),
    CONTENT_DISPOSITION("Content-Disposition"),
    X_CONTENT_TYPE_OPTIONS("X-Content-Type-Options"),
    X_XSS_PROTECTION("X-XSS-Protection"),
    CACHE_CONTROL("Cache-Control"),
    PRAGMA("Pragma"),
    EXPIRES("Expires"),
    X_FRAME_OPTIONS("X-Frame-Options"),
    TRANSFER_ENCODING("Transfer-Encoding"),
    KEEP_ALIVE("Keep-Alive"),
    CONNECTION("Connection"),
    HOST("Host"),
    DATE("Date");

    private final String name;

    HttpHeader(final String name) {
        this.name = name;
    }

    public static List<String> getUnusedHeaders() {
        return Arrays.stream(HttpHeader.values())
                .map(HttpHeader::getName)
                .toList();
    }

    public String getName() {
        return name;
    }
}
