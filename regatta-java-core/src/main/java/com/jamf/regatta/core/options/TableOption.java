/*
 * Copyright JAMF Software, LLC
 */

package com.jamf.regatta.core.options;

import java.util.concurrent.TimeUnit;

/**
 * The options for table operation.
 */
public final class TableOption {
    public static final TableOption DEFAULT = builder().build();

    private final long timeout;
    private final TimeUnit timeoutUnit;

    private TableOption(long timeout, TimeUnit timeoutUnit) {
        this.timeout = timeout;
        this.timeoutUnit = timeoutUnit;
    }

    public static Builder builder() {
        return new Builder();
    }

    public long getTimeout() {
        return timeout;
    }

    public TimeUnit getTimeoutUnit() {
        return timeoutUnit;
    }

    /**
     * Builder to construct a table option.
     */
    public static final class Builder {

        private long timeout = 30;
        private TimeUnit timeoutUnit = TimeUnit.SECONDS;

        private Builder() {
        }

        /**
         * Sets the operation timeout.
         *
         * @param amount timeout value.
         * @param unit   unit for value provided.
         * @return builder
         */
        public Builder withTimeout(long amount, TimeUnit unit) {
            this.timeout = amount;
            this.timeoutUnit = unit;
            return this;
        }

        /**
         * build the table option.
         *
         * @return the table option
         */
        public TableOption build() {
            return new TableOption(this.timeout, this.timeoutUnit);
        }

    }
}
