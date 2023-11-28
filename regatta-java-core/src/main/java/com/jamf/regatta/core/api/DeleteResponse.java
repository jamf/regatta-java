package com.jamf.regatta.core.api;

import java.util.List;

public record DeleteResponse(Header header, List<KeyValue> prevKv, long deleted) implements Response {
}
