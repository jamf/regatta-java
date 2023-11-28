package com.jamf.regatta;

public interface Client extends AutoCloseable {

    static ClientBuilder builder() {
        return new ClientBuilder();
    }

    KV getKVClient();
}
