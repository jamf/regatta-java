package com.jamf.regatta.api;

import java.util.List;

public record GetResponse(Header header, List<KeyValue> kvs) implements Response {

    @Override
    public Header getHeader() {
        return header;
    }

}
