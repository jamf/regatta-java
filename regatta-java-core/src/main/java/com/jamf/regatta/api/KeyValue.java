package com.jamf.regatta.api;

import com.jamf.regatta.ByteSequence;

public record KeyValue(ByteSequence key, ByteSequence value) {
}
