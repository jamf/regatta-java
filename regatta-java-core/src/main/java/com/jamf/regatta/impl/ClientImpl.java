package com.jamf.regatta.impl;

import com.jamf.regatta.Client;
import com.jamf.regatta.KV;
import io.grpc.Channel;

public final class ClientImpl implements Client {

    private final KV kvClient;

    public ClientImpl(Channel channel) {
        this.kvClient = new KVImpl(channel);

    }

    @Override
    public KV getKVClient() {
        return kvClient;
    }

    @Override
    public void close() throws Exception {
        kvClient.close();
    }
}
