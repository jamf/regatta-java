package com.jamf.regatta.core.api;

public record PutResponse(Header header, KeyValue prev) implements Response {
}
