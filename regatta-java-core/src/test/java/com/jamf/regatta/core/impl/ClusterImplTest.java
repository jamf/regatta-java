/*
 * Copyright JAMF Software, LLC
 */

package com.jamf.regatta.core.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.function.Consumer;

import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.migrationsupport.rules.EnableRuleMigrationSupport;

import com.google.protobuf.Message;
import com.jamf.regatta.proto.ClusterGrpc;
import com.jamf.regatta.proto.Member;
import com.jamf.regatta.proto.MemberListRequest;
import com.jamf.regatta.proto.MemberListResponse;

import io.grpc.stub.StreamObserver;
import io.grpc.testing.GrpcServerRule;

@EnableRuleMigrationSupport
class ClusterImplTest {

	@Rule
	public GrpcServerRule serverRule = new GrpcServerRule();

	private ClusterImpl client;

	@BeforeEach
	void setUp() {
		client = new ClusterImpl(serverRule.getChannel());
	}

	@Test
	void memberList() {
		var expectedMemberList = new com.jamf.regatta.core.api.MemberListResponse(
				"cluster-1",
				List.of(new com.jamf.regatta.core.api.Member("member-id-1", "member-1", List.of("peer-1-url"), List.of("client-1-url")))
		);
		var stub = ClusterTestStub.withResponse(
				MemberListResponse.newBuilder()
						.setCluster("cluster-1")
						.addMembers(Member.newBuilder()
								.setId("member-id-1")
								.setName("member-1")
								.addPeerURLs("peer-1-url")
								.addClientURLs("client-1-url")
								.build())
						.build()
		);
		serverRule.getServiceRegistry().addService(stub);

		var result = client.memberList();

		assertThat(result).isNotNull()
				.isEqualTo(expectedMemberList);
	}

	private static class ClusterTestStub<R extends Message> extends ClusterGrpc.ClusterImplBase {

		private Consumer<StreamObserver<R>> responseConsumer;

		public static <R extends Message> ClusterTestStub<R> withResponse(R response) {
			var stub = new ClusterTestStub<R>();
			stub.responseConsumer = observer -> observer.onNext(response);
			return stub;
		}

		@Override
		public void memberList(MemberListRequest request, StreamObserver<MemberListResponse> responseObserver) {
			responseConsumer.accept((StreamObserver<R>) responseObserver);
			responseObserver.onCompleted();
		}
	}
}
