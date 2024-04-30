/*
 * Copyright JAMF Software, LLC
 */

package com.jamf.regatta.core;

import com.jamf.regatta.core.api.CreateTableResponse;
import com.jamf.regatta.core.api.DeleteTableResponse;
import com.jamf.regatta.core.api.ListTablesResponse;
import com.jamf.regatta.core.options.TableOption;

public interface Tables extends CloseableClient {

	CreateTableResponse createTable(String name);

	CreateTableResponse createTable(String name, TableOption option);

	DeleteTableResponse deleteTable(String name);

	DeleteTableResponse deleteTable(String name, TableOption option);

	ListTablesResponse listTables();

	ListTablesResponse listTables(TableOption option);
}
