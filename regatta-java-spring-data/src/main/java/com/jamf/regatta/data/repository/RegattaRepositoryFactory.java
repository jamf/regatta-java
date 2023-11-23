package com.jamf.regatta.data.repository;

import org.springframework.data.keyvalue.core.KeyValueOperations;
import org.springframework.data.keyvalue.repository.query.KeyValuePartTreeQuery;
import org.springframework.data.keyvalue.repository.support.KeyValueRepositoryFactory;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.data.repository.query.parser.AbstractQueryCreator;

import com.jamf.regatta.data.query.RegattaQueryCreator;

public class RegattaRepositoryFactory extends KeyValueRepositoryFactory {

	private final KeyValueOperations operations;

	public RegattaRepositoryFactory(KeyValueOperations keyValueOperations) {
		this(keyValueOperations, RegattaQueryCreator.class);
	}

	public RegattaRepositoryFactory(KeyValueOperations keyValueOperations,
			Class<? extends AbstractQueryCreator<?, ?>> queryCreator) {
		this(keyValueOperations, queryCreator, KeyValuePartTreeQuery.class);
	}

	public RegattaRepositoryFactory(KeyValueOperations keyValueOperations,
			Class<? extends AbstractQueryCreator<?, ?>> queryCreator, Class<? extends RepositoryQuery> repositoryQueryType) {
		super(keyValueOperations, queryCreator, repositoryQueryType);

		this.operations = keyValueOperations;
	}
}
