package com.jamf.regatta.impl;

import com.google.protobuf.ByteString;
import com.jamf.regatta.ByteSequence;
import com.jamf.regatta.KV;
import com.jamf.regatta.api.DeleteResponse;
import com.jamf.regatta.api.GetResponse;
import com.jamf.regatta.api.KeyValue;
import com.jamf.regatta.api.PutResponse;
import com.jamf.regatta.api.Response;
import com.jamf.regatta.proto.*;

import io.grpc.Channel;

public class KVImpl implements KV {

	private final KVGrpc.KVBlockingStub stub;

	KVImpl(Channel managedChannel) {
		stub = KVGrpc.newBlockingStub(managedChannel);
	}

	@Override
	public PutResponse put(ByteSequence table, ByteSequence key, ByteSequence value) {
		var request = PutRequest.newBuilder()
				.setTable(ByteString.copyFrom(table.getBytes()))
				.setKey(ByteString.copyFrom(key.getBytes()))
				.setValue(ByteString.copyFrom(value.getBytes()))
				.build();

		var put = stub.put(request);
		return new PutResponse(new Response.HeaderImpl(put.getHeader()));
	}

	@Override
	public GetResponse get(ByteSequence table, ByteSequence key) {
		var request = RangeRequest.newBuilder()
				.setTable(ByteString.copyFrom(table.getBytes()))
				.setKey(ByteString.copyFrom(key.getBytes()))
				.build();

		var get = stub.range(request);
		var kvs = get.getKvsList().stream().map(keyValue -> new KeyValue(ByteSequence.from(keyValue.getKey()), ByteSequence.from(keyValue.getValue()))).toList();
		return new GetResponse(new Response.HeaderImpl(get.getHeader()), kvs);
	}

	@Override
	public DeleteResponse delete(ByteSequence table, ByteSequence key) {
		var request = DeleteRangeRequest.newBuilder()
				.setTable(ByteString.copyFrom(table.getBytes()))
				.setKey(ByteString.copyFrom(key.getBytes()))
				.build();

		var delete = stub.deleteRange(request);
		return new DeleteResponse(new Response.HeaderImpl(delete.getHeader()));
	}
}
