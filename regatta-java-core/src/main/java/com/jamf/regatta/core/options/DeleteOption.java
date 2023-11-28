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

public final class DeleteOption {
    public static final DeleteOption DEFAULT = builder().build();

    private final ByteSequence endKey;
    private final boolean prevKV;
    private final boolean prefix;

    private DeleteOption(ByteSequence endKey, boolean prevKV, boolean prefix) {
        this.endKey = endKey;
        this.prevKV = prevKV;
        this.prefix = prefix;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Optional<ByteSequence> getEndKey() {
        return Optional.ofNullable(endKey);
    }

    /**
     * Whether to get the previous key/value pairs before deleting them.
     *
     * @return true if get the previous key/value pairs before deleting them, otherwise false.
     */
    public boolean isPrevKV() {
        return prevKV;
    }

    public boolean isPrefix() {
        return prefix;
    }

    public static final class Builder {
        private ByteSequence endKey;
        private boolean prevKV = false;
        private boolean prefix = false;

        private Builder() {
        }

        /**
         * Set the end key of the delete request. If it is set, the delete request will delete the keys
         * from <i>key</i> to <i>endKey</i> (exclusive).
         *
         * <p>
         * If end key is '\0', the range is all keys {@literal >=}
         * key.
         *
         * <p>
         * If the end key is one bit larger than the given key, then it deletes all keys with
         * the prefix (the given key).
         *
         * <p>
         * If both key and end key are '\0', it deletes all keys.
         *
         * @param endKey end key
         * @return builder
         */
        public Builder withRange(ByteSequence endKey) {
            this.endKey = endKey;
            return this;
        }

        /**
         * Enables 'Delete' requests to delete all the keys by prefix.
         *
         * <p>
         *
         * @param prefix flag to delete all the keys by prefix
         * @return builder
         */
        public DeleteOption.Builder isPrefix(boolean prefix) {
            this.prefix = prefix;
            return this;
        }

        /**
         * Get the previous key/value pairs before deleting them.
         *
         * @param prevKV flag to get previous key/value pairs before deleting them.
         * @return builder
         */
        public Builder withPrevKV(boolean prevKV) {
            this.prevKV = prevKV;
            return this;
        }

        public DeleteOption build() {
            return new DeleteOption(endKey, prevKV, prefix);
        }

    }
}
