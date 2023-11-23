package com.jamf.regatta.api;

public record PutResponse(Header header) implements Response {

    @Override
    public Response.Header getHeader() {
        return header;
    }

}
