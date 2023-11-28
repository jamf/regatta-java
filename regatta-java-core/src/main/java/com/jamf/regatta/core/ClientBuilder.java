package com.jamf.regatta.core;

import com.google.common.base.Strings;
import com.jamf.regatta.core.impl.ClientImpl;
import io.grpc.ClientInterceptor;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NegotiationType;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.netty.shaded.io.netty.channel.ChannelOption;
import io.grpc.netty.shaded.io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import javax.net.ssl.SSLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientBuilder {

    private String target;
    private boolean insecureSkipTLSVerify;
    private Duration connectTimeout;
    private NegotiationType negotiationType;
    private List<ClientInterceptor> interceptors;

    ClientBuilder() {
    }

    /**
     * configure regatta server endpoint.
     *
     * @param target regatta server target
     * @return this builder to train
     * @throws NullPointerException if target is null or one of endpoint is null
     */
    public ClientBuilder target(String target) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(target), "target can't be null or empty");

        this.target = target;

        return this;
    }

    /**
     * Configure if TLS cert verification should be skipped
     *
     * @return this builder
     */
    public ClientBuilder insecureSkipTLSVerify(boolean insecureSkipTLSVerify) {
        this.insecureSkipTLSVerify = insecureSkipTLSVerify;
        return this;
    }

    /**
     * Configure connection timeout.
     *
     * @param connectTimeout Sets the connection timeout.
     * @return this builder
     */
    public ClientBuilder connectTimeout(Duration connectTimeout) {
        if (connectTimeout != null) {
            long millis = connectTimeout.toMillis();
            if ((int) millis != millis) {
                throw new IllegalArgumentException("connectTimeout outside of its bounds, max value: " +
                        Integer.MAX_VALUE);
            }
        }
        this.connectTimeout = connectTimeout;
        return this;
    }

    /**
     * Configure negotiationType
     *
     * @param negotiationType negotiationType (TLS / plaintext)
     * @return this builder
     */
    public ClientBuilder negotiationType(NegotiationType negotiationType) {
        this.negotiationType = negotiationType;

        return this;
    }

    /**
     * Add an interceptor(s).
     *
     * @param interceptor  an interceptors to add
     * @param interceptors additional interceptors
     * @return this builder
     */
    public ClientBuilder interceptor(ClientInterceptor interceptor, ClientInterceptor... interceptors) {
        if (this.interceptors == null) {
            this.interceptors = new ArrayList<>();
        }

        this.interceptors.add(interceptor);
        this.interceptors.addAll(Arrays.asList(interceptors));

        return this;
    }

    /**
     * build a new Client.
     *
     * @return Client instance.
     */
    public Client build() throws SSLException {
        Preconditions.checkState(target != null, "please configure etcd server endpoints before build.");

        NettyChannelBuilder channelBuilder = NettyChannelBuilder.forTarget(target);

        if (negotiationType != null) {
            channelBuilder.negotiationType(negotiationType);
        }
        if (insecureSkipTLSVerify) {
            channelBuilder.sslContext(GrpcSslContexts.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build());
        }

        if (connectTimeout != null) {
            channelBuilder.withOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, (int) connectTimeout.toMillis());
        }

        if (interceptors != null) {
            channelBuilder.intercept(interceptors);
        }

        return new ClientImpl(channelBuilder.build());
    }
}
