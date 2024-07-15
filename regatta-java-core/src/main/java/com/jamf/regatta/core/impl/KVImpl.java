/*
 * Copyright JAMF Software, LLC
 */

package com.jamf.regatta.core.impl;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.google.protobuf.ByteString;
import com.jamf.regatta.core.KV;
import com.jamf.regatta.core.api.ByteSequence;
import com.jamf.regatta.core.api.DeleteResponse;
import com.jamf.regatta.core.api.GetResponse;
import com.jamf.regatta.core.api.KeyValue;
import com.jamf.regatta.core.api.PutResponse;
import com.jamf.regatta.core.api.Response;
import com.jamf.regatta.core.encoding.SnappyCodec;
import com.jamf.regatta.core.options.DeleteOption;
import com.jamf.regatta.core.options.GetOption;
import com.jamf.regatta.core.options.PutOption;
import com.jamf.regatta.proto.DeleteRangeRequest;
import com.jamf.regatta.proto.KVGrpc;
import com.jamf.regatta.proto.KVGrpc.KVBlockingStub;
import com.jamf.regatta.proto.KVGrpc.KVFutureStub;
import com.jamf.regatta.proto.PutRequest;
import com.jamf.regatta.proto.RangeRequest;
import com.jamf.regatta.proto.RangeResponse;

import io.grpc.Channel;

public class KVImpl implements KV {

    private final KVBlockingStub stub;
    private final KVFutureStub futureStub;

    KVImpl(Channel channel) {
        stub = KVGrpc.newBlockingStub(channel)
                .withCompression(SnappyCodec.NAME);
        futureStub = KVGrpc.newFutureStub(channel)
                .withCompression(SnappyCodec.NAME);
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
        var put = stub.withDeadlineAfter(option.getTimeout(), option.getTimeoutUnit())
                .put(request);
        return new PutResponse(new Response.HeaderImpl(put.getHeader()), keyValue(put.getPrevKv()));
    }

    @Override
    public GetResponse get(ByteSequence table, ByteSequence key) {
        return get(table, key, GetOption.DEFAULT);
    }

    @Override
    public GetResponse get(ByteSequence table, ByteSequence key, GetOption option) {
        var request = rangeRequest(table, key, option);
        var response = stub.withDeadlineAfter(option.getTimeout(), option.getTimeoutUnit())
                .range(request);
        return getResponse(response);
    }

    @Override
    public CompletableFuture<GetResponse> getAsync(ByteSequence table, ByteSequence key, Executor executor) {
        return getAsync(table, key, GetOption.DEFAULT, executor);
    }

    @Override
    public CompletableFuture<GetResponse> getAsync(ByteSequence table, ByteSequence key, GetOption option,
            Executor executor) {
        var request = rangeRequest(table, key, option);
        var responseFuture = futureStub.withDeadlineAfter(option.getTimeout(), option.getTimeoutUnit())
                .range(request);
        return CompletableFutures.from(responseFuture, executor)
                .thenApply(KVImpl::getResponse);
    }

    @Override
    public Stream<GetResponse> iterate(ByteSequence table, ByteSequence key, GetOption option) {
        var request = rangeRequest(table, key, option);
        var iterator = stub.withDeadlineAfter(option.getTimeout(), option.getTimeoutUnit())
                .iterateRange(request);
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator,
                        Spliterator.ORDERED | Spliterator.DISTINCT | Spliterator.IMMUTABLE), false)
                .map(KVImpl::getResponse);
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
                .setRangeEnd(option.getEndKey()
                        .map(byteSequence -> ByteString.copyFrom(byteSequence.getBytes()))
                        .orElse(ByteString.EMPTY))
                .setPrevKv(option.isPrevKV())
                .build();

        var delete = stub.withDeadlineAfter(option.getTimeout(), option.getTimeoutUnit())
                .deleteRange(request);
        var kvs = delete.getPrevKvsList().stream()
                .map(KVImpl::keyValue)
                .toList();
        return new DeleteResponse(new Response.HeaderImpl(delete.getHeader()), kvs, delete.getDeleted());
    }

    private static RangeRequest rangeRequest(ByteSequence table, ByteSequence key, GetOption option) {
        return RangeRequest.newBuilder()
                .setTable(ByteString.copyFrom(table.getBytes()))
                .setKey(ByteString.copyFrom(key.getBytes()))
                .setRangeEnd(option.getEndKey()
                        .map(byteSequence -> ByteString.copyFrom(byteSequence.getBytes()))
                        .orElse(ByteString.EMPTY))
                .setLimit(option.getLimit())
                .setCountOnly(option.isCountOnly())
                .setKeysOnly(option.isKeysOnly())
                .setLinearizable(!option.isSerializable())
                .build();
    }

    private static GetResponse getResponse(RangeResponse response) {
        var kvs = response.getKvsList().stream()
                .map(KVImpl::keyValue)
                .toList();
        return new GetResponse(new Response.HeaderImpl(response.getHeader()), kvs, response.getCount());
    }

    private static KeyValue keyValue(com.jamf.regatta.proto.KeyValue kv) {
        return new KeyValue(ByteSequence.from(kv.getKey()), ByteSequence.from(kv.getValue()));
    }
}
