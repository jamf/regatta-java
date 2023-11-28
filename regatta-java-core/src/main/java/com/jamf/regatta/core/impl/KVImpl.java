package com.jamf.regatta.core.impl;

import com.google.protobuf.ByteString;
import com.jamf.regatta.core.api.ByteSequence;
import com.jamf.regatta.core.KV;
import com.jamf.regatta.core.api.*;
import com.jamf.regatta.core.options.DeleteOption;
import com.jamf.regatta.core.options.GetOption;
import com.jamf.regatta.core.options.PutOption;
import com.jamf.regatta.proto.DeleteRangeRequest;
import com.jamf.regatta.proto.KVGrpc;
import com.jamf.regatta.proto.PutRequest;
import com.jamf.regatta.proto.RangeRequest;
import io.grpc.Channel;

public class KVImpl implements KV {

    private final KVGrpc.KVBlockingStub stub;

    KVImpl(Channel managedChannel) {
        stub = KVGrpc.newBlockingStub(managedChannel);
    }

    @Override
    public PutResponse put(ByteSequence table, ByteSequence key, ByteSequence value) {
        return put(table, key, value, PutOption.DEFAULT);
    }

    @Override
    public PutResponse put(ByteSequence table, ByteSequence key, ByteSequence value, PutOption option) {
        var request = PutRequest.newBuilder()
                .setTable(ByteString.copyFrom(table.getBytes()))
                .setKey(ByteString.copyFrom(key.getBytes()))
                .setValue(ByteString.copyFrom(value.getBytes()))
                .build();

        var put = stub.put(request);
        return new PutResponse(new Response.HeaderImpl(put.getHeader()), new KeyValue(ByteSequence.from(put.getPrevKv().getKey()), ByteSequence.from(put.getPrevKv().getValue())));
    }

    @Override
    public GetResponse get(ByteSequence table, ByteSequence key) {
        return get(table, key, GetOption.DEFAULT);
    }

    @Override
    public GetResponse get(ByteSequence table, ByteSequence key, GetOption option) {
        var request = RangeRequest.newBuilder()
                .setTable(ByteString.copyFrom(table.getBytes()))
                .setKey(ByteString.copyFrom(key.getBytes()))
                .setRangeEnd(option.getEndKey().map(byteSequence -> ByteString.copyFrom(byteSequence.getBytes())).orElse(ByteString.EMPTY))
                .setLimit(option.getLimit())
                .setCountOnly(option.isCountOnly())
                .setKeysOnly(option.isKeysOnly())
                .setLinearizable(!option.isSerializable())
                .build();

        var get = stub.range(request);
        var kvs = get.getKvsList().stream().map(keyValue -> new KeyValue(ByteSequence.from(keyValue.getKey()), ByteSequence.from(keyValue.getValue()))).toList();
        return new GetResponse(new Response.HeaderImpl(get.getHeader()), kvs, get.getCount());
    }

    @Override
    public DeleteResponse delete(ByteSequence table, ByteSequence key) {
        return delete(table, key, DeleteOption.DEFAULT);
    }

    @Override
    public DeleteResponse delete(ByteSequence table, ByteSequence key, DeleteOption option) {
        var request = DeleteRangeRequest.newBuilder()
                .setTable(ByteString.copyFrom(table.getBytes()))
                .setKey(ByteString.copyFrom(key.getBytes()))
                .setRangeEnd(option.getEndKey().map(byteSequence -> ByteString.copyFrom(byteSequence.getBytes())).orElse(ByteString.EMPTY))
                .setPrevKv(option.isPrevKV())
                .build();

        var delete = stub.deleteRange(request);
        var kvs = delete.getPrevKvsList().stream().map(keyValue -> new KeyValue(ByteSequence.from(keyValue.getKey()), ByteSequence.from(keyValue.getValue()))).toList();
        return new DeleteResponse(new Response.HeaderImpl(delete.getHeader()), kvs, delete.getDeleted());
    }
}
