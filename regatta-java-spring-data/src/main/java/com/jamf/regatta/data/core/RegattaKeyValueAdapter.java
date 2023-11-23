package com.jamf.regatta.data.core;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.keyvalue.core.AbstractKeyValueAdapter;
import org.springframework.data.util.CloseableIterator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamf.regatta.ByteSequence;
import com.jamf.regatta.Client;
import com.jamf.regatta.api.GetResponse;

public class RegattaKeyValueAdapter extends AbstractKeyValueAdapter implements InitializingBean, ApplicationContextAware {

	private Client regattaClient;

	private static final ObjectMapper objectMapper = new ObjectMapper();

	public RegattaKeyValueAdapter(Client regattaClient) {
		this.regattaClient = regattaClient;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

	}

	@Override
	public Object put(Object id, Object item, String keyspace) {
		return regattaClient.getKVClient()
				.put(ByteSequence.fromUtf8String(keyspace), ByteSequence.from((byte[]) id), ByteSequence.from((byte[]) item));
	}

	@Override
	public boolean contains(Object id, String keyspace) {
		return false;
	}

	@Override
	public Object get(Object id, String keyspace) {
		return this.get(id, keyspace, Object.class);
	}

	@Override
	public <T> T get(Object id, String keyspace, Class<T> type) {
		GetResponse response = regattaClient.getKVClient()
				.get(ByteSequence.fromUtf8String(keyspace), ByteSequence.fromUtf8String((String) id));

		if (response.kvs().isEmpty()) {
			return null;
		}

		var data = response.kvs().get(0).value().getBytes();
		try {
			return objectMapper.readValue(data, type);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Object delete(Object id, String keyspace) {
		return regattaClient.getKVClient().delete(ByteSequence.fromUtf8String(keyspace), ByteSequence.from((byte[]) id));
	}

	@Override
	public Iterable<?> getAllOf(String keyspace) {
		return null;
	}

	@Override
	public CloseableIterator<Map.Entry<Object, Object>> entries(String keyspace) {
		return null;
	}

	@Override
	public void deleteAllOf(String keyspace) {

	}

	@Override
	public void clear() {

	}

	@Override
	public long count(String keyspace) {
		return 0;
	}

	@Override
	public void destroy() throws Exception {

	}
}
