/*
 * Copyright JAMF Software, LLC
 */

package com.jamf.regatta.core.impl;

import com.jamf.regatta.core.*;
import io.grpc.ManagedChannel;

public final class ClientImpl implements Client {

    private final KV kvClient;
    private final Cluster clusterClient;
    private final Tables tables;
    private final ManagedChannel channel;

    public ClientImpl(ManagedChannel channel, RetryConfig retryConfig) {
        this.channel = channel;
        this.kvClient = new KVImpl(channel, retryConfig);
        this.clusterClient = new ClusterImpl(channel, retryConfig);
        this.tables = new TablesImpl(channel, retryConfig);
    }

    @Override
    public KV getKVClient() {
        return kvClient;
    }

    @Override
    public Cluster getClusterClient() {
        return clusterClient;
    }

    @Override
    public Tables getTablesClient() {
        return tables;
    }

    @Override
    public void close() throws Exception {
        channel.shutdown();
    }
}
