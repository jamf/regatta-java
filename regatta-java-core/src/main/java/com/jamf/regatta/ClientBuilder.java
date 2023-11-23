package com.jamf.regatta;

import com.google.common.base.Strings;
import com.jamf.regatta.impl.ClientImpl;

import io.grpc.ManagedChannelBuilder;

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
	public Client build() {
		Preconditions.checkState(target != null, "please configure etcd server endpoints before build.");

		var channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();
		return new ClientImpl(channel);
	}
}
