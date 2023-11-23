package com.jamf.regatta.api;

import com.jamf.regatta.proto.ResponseHeader;

public interface Response {

    /**
     * Returns the response header
     *
     * @return the header.
     */
    Header getHeader();

    interface Header {

        /**
         * Returns the cluster id
         *
         * @return the cluster id.
         */
        long getShardId();

        /**
         * Returns the member id
         *
         * @return the member id.
         */
        long getReplicaId();

        /**
         * Returns the revision id
         *
         * @return the revision.
         */
        long getRevision();

        /**
         * Returns the raft term
         *
         * @return theraft term.
         */
        long getRaftTerm();
    }

    class HeaderImpl implements Response.Header {

        private final ResponseHeader responseHeader;

        public HeaderImpl(ResponseHeader responseHeader) {
            this.responseHeader = responseHeader;
        }

        @Override
        public long getShardId() {
            return responseHeader.getShardId();
        }

        @Override
        public long getReplicaId() {
            return responseHeader.getReplicaId();
        }

        @Override
        public long getRevision() {
            return responseHeader.getRevision();
        }

        @Override
        public long getRaftTerm() {
            return responseHeader.getRaftTerm();
        }
    }
}
