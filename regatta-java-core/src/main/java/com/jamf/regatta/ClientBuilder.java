package com.jamf.regatta;

import javax.net.ssl.SSLException;

import com.google.common.base.Strings;
import com.jamf.regatta.impl.ClientImpl;

import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NettyChannelBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

public class ClientBuilder {

	private String target;

	ClientBuilder() {
	}

	/**
	 * Gets the regatta target.
	 *
	 * @return the regatta target.
	 */
	public String target() {
		return target;
	}

	/**
	 * configure etcd server endpoints.
	 *
	 * @param target regatta server target
	 * @return this builder to train
	 * @throws NullPointerException if target is null or one of endpoint is null
	 */
	public ClientBuilder target(String target) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(target), "target can't be null or empty");

		this.target = target;

		return this;
	}

	/**
	 * build a new Client.
	 *
	 * @return Client instance.
	 */
	public Client build() throws SSLException {
		Preconditions.checkState(target != null, "please configure etcd server endpoints before build.");

		var channel = NettyChannelBuilder.forTarget(target)
				.sslContext(GrpcSslContexts.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build())
				.build();
		return new ClientImpl(channel);
	}
}
