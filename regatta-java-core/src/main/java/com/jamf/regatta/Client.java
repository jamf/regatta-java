package com.jamf.regatta;

public interface Client extends AutoCloseable {

	KV getKVClient();

	static ClientBuilder builder() {
		return new ClientBuilder();
	}
}
