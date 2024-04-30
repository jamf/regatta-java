/*
 * Copyright JAMF Software, LLC
 */

package com.jamf.regatta.core.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.protobuf.ListValue;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import com.jamf.regatta.core.Tables;
import com.jamf.regatta.core.api.CreateTableResponse;
import com.jamf.regatta.core.api.DeleteTableResponse;
import com.jamf.regatta.core.api.ListTablesResponse;
import com.jamf.regatta.core.api.TableInfo;
import com.jamf.regatta.core.options.TableOption;
import com.jamf.regatta.proto.CreateTableRequest;
import com.jamf.regatta.proto.DeleteTableRequest;
import com.jamf.regatta.proto.ListTablesRequest;
import com.jamf.regatta.proto.TablesGrpc;

import io.grpc.Channel;

public class TablesImpl implements Tables {

	private final TablesGrpc.TablesBlockingStub stub;

	TablesImpl(Channel managedChannel) {
		stub = TablesGrpc.newBlockingStub(managedChannel);
	}

	@Override
	public CreateTableResponse createTable(String name) {
		return createTable(name, TableOption.DEFAULT);
	}

	@Override
	public CreateTableResponse createTable(String name, TableOption option) {
		var request = CreateTableRequest.newBuilder()
				.setName(name)
				.build();

		var ct = stub.withDeadlineAfter(option.getTimeout(), option.getTimeoutUnit())
				.create(request);

		return new CreateTableResponse(ct.getId());
	}

	@Override
	public DeleteTableResponse deleteTable(String name) {
		return deleteTable(name, TableOption.DEFAULT);
	}

	@Override
	public DeleteTableResponse deleteTable(String name, TableOption option) {
		var request = DeleteTableRequest.newBuilder()
				.setName(name)
				.build();

		stub.withDeadlineAfter(option.getTimeout(), option.getTimeoutUnit())
				.delete(request);

		return new DeleteTableResponse();
	}

	@Override
	public ListTablesResponse listTables() {
		return listTables(TableOption.DEFAULT);
	}

	@Override
	public ListTablesResponse listTables(TableOption option) {
		var lt = stub.withDeadlineAfter(option.getTimeout(), option.getTimeoutUnit())
				.list(ListTablesRequest.getDefaultInstance());

		return new ListTablesResponse(
				lt.getTablesList().stream()
						.map(table -> new TableInfo(
								table.getId(),
								table.getName(),
								table.getConfig().getFieldsMap().entrySet()
										.stream()
										.collect(Collectors.toMap(Map.Entry::getKey, e -> convertValue(e.getValue())))
						))
						.toList());
	}

	private static Object convertValue(Value protoValue) {
		return switch (protoValue.getKindCase()) {
			case NULL_VALUE -> null;
			case NUMBER_VALUE -> protoValue.getNumberValue();
			case STRING_VALUE -> protoValue.getStringValue();
			case BOOL_VALUE -> protoValue.getBoolValue();
			case STRUCT_VALUE -> convertStruct(protoValue.getStructValue());
			case LIST_VALUE -> convertList(protoValue.getListValue());
			default -> null;
		};
	}

	private static Map<String, Object> convertStruct(Struct protoStruct) {
		return protoStruct.getFieldsMap().entrySet()
				.stream()
				.collect(Collectors.toMap(Map.Entry::getKey, entry -> convertValue(entry.getValue())));
	}

	private static List<Object> convertList(ListValue protoList) {
		return protoList.getValuesList().stream()
				.map(TablesImpl::convertValue)
				.toList();
	}

}
