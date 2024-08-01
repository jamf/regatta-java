/*
 * Copyright JAMF Software, LLC
 */

package com.jamf.regatta.core;

import com.jamf.regatta.core.api.ByteSequence;
import com.jamf.regatta.core.api.DeleteResponse;
import com.jamf.regatta.core.api.GetResponse;
import com.jamf.regatta.core.api.PutResponse;
import com.jamf.regatta.core.options.DeleteOption;
import com.jamf.regatta.core.options.GetOption;
import com.jamf.regatta.core.options.PutOption;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Stream;

public interface KV extends CloseableClient {

    /**
     * put a key-value pair into regatta.
     *
     * @param table table in ByteSequence
     * @param key   key in ByteSequence
     * @param value value in ByteSequence
     * @return PutResponse
     */
    PutResponse put(ByteSequence table, ByteSequence key, ByteSequence value);

    /**
     * put a key-value pair into regatta.
     *
     * @param table table in ByteSequence
     * @param key   key in ByteSequence
     * @param value value in ByteSequence
     * @param option options for the put operation
     * @return PutResponse
     */
    PutResponse put(ByteSequence table, ByteSequence key, ByteSequence value, PutOption option);

    /**
     * retrieve value for the given key.
     *
     * @param table table in ByteSequence
     * @param key   key in ByteSequence
     * @return GetResponse
     */
    GetResponse get(ByteSequence table, ByteSequence key);

    /**
     * retrieve value for the given key.
     *
     * @param table table in ByteSequence
     * @param key   key in ByteSequence
     * @param option options for the get operation
     * @return GetResponse
     */
    GetResponse get(ByteSequence table, ByteSequence key, GetOption option);

    /**
     * Asynchronously retrieve value for the given key
     *
     * @param table table in ByteSequence
     * @param key   key in ByteSequence
     * @param executor executor that will be used complete the returned completable future
     * @return GetResponse
     */
    CompletableFuture<GetResponse> getAsync(ByteSequence table, ByteSequence key, Executor executor);

    /**
     * Asynchronously retrieve value for the given key
     *
     * @param table table in ByteSequence
     * @param key   key in ByteSequence
     * @param option options for the get operation
     * @param executor executor that will be used complete the returned completable future
     * @return GetResponse
     */
    CompletableFuture<GetResponse> getAsync(ByteSequence table, ByteSequence key, GetOption option, Executor executor);

    /**
     * retrieve values for the given keys.
     *
     * @param table table in ByteSequence
     * @param key   key in ByteSequence
     * @return Stream of responses
     */
    Stream<GetResponse> iterate(ByteSequence table, ByteSequence key, GetOption option);

    /**
     * delete value with given key.
     *
     * @param table table in ByteSequence
     * @param key   key in ByteSequence
     * @return DeleteResponse
     */
    DeleteResponse delete(ByteSequence table, ByteSequence key);

    /**
     * delete value with given key.
     *
     * @param table table in ByteSequence
     * @param key   key in ByteSequence
     * @param option options for the delete operation
     * @return DeleteResponse
     */
    DeleteResponse delete(ByteSequence table, ByteSequence key, DeleteOption option);
}
