/*
 * Copyright JAMF Software, LLC
 */

package com.jamf.regatta.core.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.migrationsupport.rules.EnableRuleMigrationSupport;

import com.google.protobuf.ListValue;
import com.google.protobuf.Message;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import com.jamf.regatta.core.options.TableOption;
import com.jamf.regatta.proto.CreateTableRequest;
import com.jamf.regatta.proto.CreateTableResponse;
import com.jamf.regatta.proto.DeleteTableRequest;
import com.jamf.regatta.proto.DeleteTableResponse;
import com.jamf.regatta.proto.ListTablesRequest;
import com.jamf.regatta.proto.ListTablesResponse;
import com.jamf.regatta.proto.TableInfo;
import com.jamf.regatta.proto.TablesGrpc;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import io.grpc.testing.GrpcServerRule;

@EnableRuleMigrationSupport
class TablesImplTest {

	private static final String TABLE_ID = "table-id-1";
	private static final String TABLE_NAME = "table1";

	@Rule
	public GrpcServerRule serverRule = new GrpcServerRule();

	private TablesImpl client;

	@BeforeEach
	void setUp() {
		client = new TablesImpl(serverRule.getChannel());
	}

	@Test
	void createTable() {
		var stub = TablesTestStub.withResponse(
				CreateTableResponse.newBuilder()
						.setId(TABLE_ID)
						.build()
		);
		serverRule.getServiceRegistry().addService(stub);

		var result = client.createTable(TABLE_NAME);

		assertThat(result.id()).isEqualTo(TABLE_ID);
	}

	@Test
	void createTable_timout() {
		var stub = TablesTestStub.withDelayedResponse(
				CreateTableResponse.newBuilder()
						.setId(TABLE_ID)
						.build(),
				Duration.ofMillis(100)
		);
		serverRule.getServiceRegistry().addService(stub);

		var thrown = catchThrowable(
				() -> client.createTable(TABLE_NAME, TableOption.builder().withTimeout(10, TimeUnit.MILLISECONDS).build()));

		assertThat(thrown).isInstanceOf(StatusRuntimeException.class);
		assertThat(((StatusRuntimeException) thrown).getStatus().getCode()).isEqualTo(Status.DEADLINE_EXCEEDED.getCode());
	}

	@Test
	void deleteTable() {
		var stub = TablesTestStub.withResponse(
				DeleteTableResponse.newBuilder().build()
		);
		serverRule.getServiceRegistry().addService(stub);

		var result = client.deleteTable(TABLE_NAME);

		assertThat(result).isNotNull();
	}

	@Test
	void deleteTable_timout() {
		var stub = TablesTestStub.withDelayedResponse(
				DeleteTableResponse.newBuilder().build(),
				Duration.ofMillis(100)
		);
		serverRule.getServiceRegistry().addService(stub);

		var thrown = catchThrowable(
				() -> client.deleteTable(TABLE_NAME, TableOption.builder().withTimeout(10, TimeUnit.MILLISECONDS).build()));

		assertThat(thrown).isInstanceOf(StatusRuntimeException.class);
		assertThat(((StatusRuntimeException) thrown).getStatus().getCode()).isEqualTo(Status.DEADLINE_EXCEEDED.getCode());
	}

	@Test
	void listTables() {
		var expectedTableInfo = new com.jamf.regatta.core.api.TableInfo(
				TABLE_ID,
				TABLE_NAME,
				Map.of("someConfig", 1.0, "someListConfig", List.of("listValue"))
		);
		var stub = TablesTestStub.withResponse(
				ListTablesResponse.newBuilder()
						.addTables(TableInfo.newBuilder()
								.setId(TABLE_ID)
								.setName(TABLE_NAME)
								.setConfig(Struct.newBuilder()
										.putFields("someConfig", Value.newBuilder().setNumberValue(1).build())
										.putFields("someListConfig", Value.newBuilder().setListValue(ListValue.newBuilder()
														.addValues(Value.newBuilder().setStringValue("listValue").build())
														.build())
												.build())
										.build())
								.build())
						.build()
		);
		serverRule.getServiceRegistry().addService(stub);

		var result = client.listTables();

		assertThat(result).isNotNull();
		assertThat(result.tables()).hasSize(1)
				.contains(expectedTableInfo);
	}

	@Test
	void listTables_timout() {
		var stub = TablesTestStub.withDelayedResponse(
				ListTablesResponse.newBuilder()
						.addTables(TableInfo.newBuilder()
								.setId(TABLE_ID)
								.setName(TABLE_NAME)
								.build())
						.build(),
				Duration.ofMillis(100)
		);
		serverRule.getServiceRegistry().addService(stub);

		var thrown = catchThrowable(
				() -> client.listTables(TableOption.builder().withTimeout(10, TimeUnit.MILLISECONDS).build()));

		assertThat(thrown).isInstanceOf(StatusRuntimeException.class);
		assertThat(((StatusRuntimeException) thrown).getStatus().getCode()).isEqualTo(Status.DEADLINE_EXCEEDED.getCode());
	}

	private static class TablesTestStub<R extends Message> extends TablesGrpc.TablesImplBase {

		private Consumer<StreamObserver<R>> responseConsumer;

		public static <R extends Message> TablesTestStub<R> withResponse(R response) {
			var stub = new TablesTestStub<R>();
			stub.responseConsumer = observer -> observer.onNext(response);
			return stub;
		}

		public static <R extends Message> TablesTestStub<R> withDelayedResponse(R response, Duration delay) {
			var stub = new TablesTestStub<R>();
			stub.responseConsumer = observer -> {
				try {
					Thread.sleep(delay.toMillis());
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				observer.onNext(response);
			};
			return stub;
		}

		@Override
		public void create(CreateTableRequest request, StreamObserver<CreateTableResponse> responseObserver) {
			responseConsumer.accept((StreamObserver<R>) responseObserver);
			responseObserver.onCompleted();
		}

		@Override
		public void delete(DeleteTableRequest request, StreamObserver<DeleteTableResponse> responseObserver) {
			responseConsumer.accept((StreamObserver<R>) responseObserver);
			responseObserver.onCompleted();
		}

		@Override
		public void list(ListTablesRequest request, StreamObserver<ListTablesResponse> responseObserver) {
			responseConsumer.accept((StreamObserver<R>) responseObserver);
			responseObserver.onCompleted();
		}
	}
}
