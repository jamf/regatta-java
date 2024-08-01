/*
 * Copyright JAMF Software, LLC
 */

package com.jamf.regatta.core.impl;

import com.jamf.regatta.core.Client;
import com.jamf.regatta.core.Cluster;
import com.jamf.regatta.core.KV;
import com.jamf.regatta.core.Tables;

import io.grpc.Channel;

public final class ClientImpl implements Client {

    private final KV kvClient;
    private final Cluster clusterClient;
    private final Tables tables;
    private final Runnable onClose;

    public ClientImpl(Channel channel, Runnable onClose) {
        this.onClose = onClose;
        this.kvClient = new KVImpl(channel);
        this.clusterClient = new ClusterImpl(channel);
		this.tables = new TablesImpl(channel);
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
        onClose.run();
    }
}
