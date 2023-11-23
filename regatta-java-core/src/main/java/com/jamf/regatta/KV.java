package com.jamf.regatta;

import com.jamf.regatta.proto.DeleteRangeResponse;
import com.jamf.regatta.proto.RangeResponse;
import com.jamf.regatta.proto.PutResponse;

public interface KV extends CloseableClient {

	/**
	 * put a key-value pair into regatta.
	 *
	 * @param table table in ByteSequence
	 * @param key   key in ByteSequence
	 * @param value value in ByteSequence
	 * @return PutResponse
	 */
	PutResponse put(ByteSequence table, ByteSequence key, ByteSequence value); //TODO use custom DTO

	/**
	 * retrieve value for the given key.
	 *
	 * @param table table in ByteSequence
	 * @param key   key in ByteSequence
	 * @return GetResponse
	 */
	RangeResponse get(ByteSequence table, ByteSequence key); //TODO use custom DTO

	/**
	 * delete value with given key.
	 *
	 * @param table table in ByteSequence
	 * @param key   key in ByteSequence
	 * @return GetResponse
	 */
	DeleteRangeResponse delete(ByteSequence table, ByteSequence key); //TODO use custom DTO
}
