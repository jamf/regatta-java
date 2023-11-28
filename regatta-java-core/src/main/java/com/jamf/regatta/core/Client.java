package com.jamf.regatta.core;

public interface Client extends AutoCloseable {

    static ClientBuilder builder() {
        return new ClientBuilder();
    }

    KV getKVClient();
}
