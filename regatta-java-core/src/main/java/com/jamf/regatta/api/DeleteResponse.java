package com.jamf.regatta.api;

public record DeleteResponse(Header header) implements Response {

    @Override
    public Header getHeader() {
        return header;
    }
}
