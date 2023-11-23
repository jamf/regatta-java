package com.jamf.regatta.impl;

import com.google.protobuf.ByteString;
import com.jamf.regatta.ByteSequence;
import com.jamf.regatta.KV;
import com.jamf.regatta.proto.KVGrpc;
import com.jamf.regatta.proto.PutRequest;
import com.jamf.regatta.proto.PutResponse;
import com.jamf.regatta.proto.RangeRequest;
import com.jamf.regatta.proto.RangeResponse;

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

		return stub.put(request);
	}

	@Override
	public RangeResponse get(ByteSequence table, ByteSequence key) {
		var request = RangeRequest.newBuilder()
				.setTable(ByteString.copyFrom(table.getBytes()))
				.setKey(ByteString.copyFrom(key.getBytes()))
				.build();

		return stub.range(request);
	}
}
