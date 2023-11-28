/*
 * Copyright 2016-2021 The jetcd authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jamf.regatta.core.options;

import com.jamf.regatta.core.api.ByteSequence;

import java.util.Optional;

/**
 * The option for get operation.
 */
public final class GetOption {
    public static final GetOption DEFAULT = builder().build();

    private final ByteSequence endKey;
    private final long limit;
    private final long revision;
    private final SortOrder sortOrder;
    private final SortTarget sortTarget;
    private final boolean serializable;
    private final boolean keysOnly;
    private final boolean countOnly;
    private final boolean prefix;

    private GetOption(
            ByteSequence endKey,
            long limit,
            long revision,
            SortOrder sortOrder,
            SortTarget sortTarget,
            boolean serializable,
            boolean keysOnly,
            boolean countOnly,
            boolean prefix) {

        this.endKey = endKey;
        this.limit = limit;
        this.revision = revision;
        this.sortOrder = sortOrder;
        this.sortTarget = sortTarget;
        this.serializable = serializable;
        this.keysOnly = keysOnly;
        this.countOnly = countOnly;
        this.prefix = prefix;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Get the maximum number of keys to return for a get request.
     *
     * @return the maximum number of keys to return.
     */
    public long getLimit() {
        return this.limit;
    }

    public Optional<ByteSequence> getEndKey() {
        return Optional.ofNullable(this.endKey);
    }

    public long getRevision() {
        return revision;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public SortTarget getSortField() {
        return sortTarget;
    }

    public boolean isSerializable() {
        return serializable;
    }

    public boolean isKeysOnly() {
        return keysOnly;
    }

    public boolean isCountOnly() {
        return countOnly;
    }

    public boolean isPrefix() {
        return prefix;
    }

    public enum SortOrder {
        NONE, ASCEND, DESCEND,
    }

    public enum SortTarget {
        KEY, VERSION, CREATE, MOD, VALUE,
    }

    public static final class Builder {

        private long limit = 0L;
        private long revision = 0L;
        private SortOrder sortOrder = SortOrder.NONE;
        private SortTarget sortTarget = SortTarget.KEY;
        private boolean serializable = false;
        private boolean keysOnly = false;
        private boolean countOnly = false;
        private ByteSequence endKey;
        private boolean prefix = false;

        private Builder() {
        }

        /**
         * Limit the number of keys to return for a get request. By default is 0 - no limitation.
         *
         * @param limit the maximum number of keys to return for a get request.
         * @return builder
         */
        public Builder withLimit(long limit) {
            this.limit = limit;
            return this;
        }

        /**
         * Provide the revision to use for the get request.
         *
         * <p>
         * If the revision is less or equal to zero, the get is over the newest key-value store.
         *
         * <p>
         * If the revision has been compacted, ErrCompacted is returned as a response.
         *
         * @param revision the revision to get.
         * @return builder
         */
        public Builder withRevision(long revision) {
            this.revision = revision;
            return this;
        }

        /**
         * Sort the return key value pairs in the provided <i>order</i>.
         *
         * @param order order to sort the returned key value pairs.
         * @return builder
         */
        public Builder withSortOrder(SortOrder order) {
            this.sortOrder = order;
            return this;
        }

        /**
         * Sort the return key value pairs in the provided <i>field</i>.
         *
         * @param field field to sort the key value pairs by the provided
         * @return builder
         */
        public Builder withSortField(SortTarget field) {
            this.sortTarget = field;
            return this;
        }

        /**
         * Set the get request to be a serializable get request.
         *
         * <p>
         * Get requests are linearizable by
         * default. For better performance, a serializable get request is served locally without needing
         * to reach consensus with other nodes in the cluster.
         *
         * @param serializable is the get request a serializable get request.
         * @return builder
         */
        public Builder withSerializable(boolean serializable) {
            this.serializable = serializable;
            return this;
        }

        /**
         * Set the get request to only return keys.
         *
         * @param keysOnly flag to only return keys
         * @return builder
         */
        public Builder withKeysOnly(boolean keysOnly) {
            this.keysOnly = keysOnly;
            return this;
        }

        /**
         * Set the get request to only return count of the keys.
         *
         * @param countOnly flag to only return count of the keys
         * @return builder
         */
        public Builder withCountOnly(boolean countOnly) {
            this.countOnly = countOnly;
            return this;
        }

        /**
         * Set the end key of the get request. If it is set, the get request will return the keys from
         * <i>key</i> to <i>endKey</i> (exclusive).
         *
         * <p>
         * If end key is '\0', the range is all keys {@literal >=} key.
         *
         * <p>
         * If the end key is one bit larger than the given key, then it gets all keys with the
         * prefix (the given key).
         *
         * <p>
         * If both key and end key are '\0', it returns all keys.
         *
         * @param endKey end key
         * @return builder
         */
        public Builder withRange(ByteSequence endKey) {
            this.endKey = endKey;
            return this;
        }

        /**
         * Enables 'Get' requests to obtain all the keys by prefix.
         *
         * <p>
         *
         * @param prefix flag to obtain all the keys by prefix
         * @return builder
         */
        public Builder isPrefix(boolean prefix) {
            this.prefix = prefix;
            return this;
        }

        /**
         * Build the GetOption.
         *
         * @return the GetOption
         */
        public GetOption build() {
            return new GetOption(
                    endKey,
                    limit,
                    revision,
                    sortOrder,
                    sortTarget,
                    serializable,
                    keysOnly,
                    countOnly,
                    prefix);
        }

    }
}
