package com.jamf.regatta.data.repository;

import org.springframework.data.keyvalue.core.KeyValueOperations;
import org.springframework.data.keyvalue.repository.support.KeyValueRepositoryFactoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.data.repository.query.parser.AbstractQueryCreator;

public class RegattaRepositoryFactoryBean<T extends Repository<S, ID>, S, ID> extends KeyValueRepositoryFactoryBean<T, S, ID> {

	public RegattaRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
		super(repositoryInterface);
	}

	@Override
	protected RegattaRepositoryFactory createRepositoryFactory(KeyValueOperations operations,
			Class<? extends AbstractQueryCreator<?, ?>> queryCreator, Class<? extends RepositoryQuery> repositoryQueryType) {
		return new RegattaRepositoryFactory(operations, queryCreator, repositoryQueryType);
	}
}

