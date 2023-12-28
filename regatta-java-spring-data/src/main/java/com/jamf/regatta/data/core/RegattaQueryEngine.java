package com.jamf.regatta.data.core;

import com.jamf.regatta.data.query.RegattaOperationChain;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.data.keyvalue.core.CriteriaAccessor;
import org.springframework.data.keyvalue.core.QueryEngine;
import org.springframework.data.keyvalue.core.SortAccessor;
import org.springframework.data.keyvalue.core.SpelSortAccessor;
import org.springframework.data.keyvalue.core.query.KeyValueQuery;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.CollectionUtils;

import java.util.*;

public class RegattaQueryEngine extends QueryEngine<RegattaKeyValueAdapter, RegattaOperationChain, Comparator<?>> {

    public RegattaQueryEngine() {
        this(new RedisCriteriaAccessor(), new SpelSortAccessor(new SpelExpressionParser()));
    }

    public RegattaQueryEngine(CriteriaAccessor<RegattaOperationChain> criteriaAccessor, SortAccessor<Comparator<?>> sortAccessor) {
        super(criteriaAccessor, sortAccessor);
    }

    public <T> Collection<T> execute(RegattaOperationChain criteria, Comparator<?> sort, long offset, int rows,
                                     String keyspace, Class<T> type) {
        List<T> result = doFind(criteria, offset, rows, keyspace, type);

        if (sort != null) {
            result.sort((Comparator<? super T>) sort);
        }

        return result;
    }

    @Override
    public Collection<?> execute(RegattaOperationChain criteria, Comparator<?> sort, long offset, int rows, String keyspace) {
        return execute(criteria, sort, offset, rows, keyspace, Object.class);
    }

    @Override
    public long count(RegattaOperationChain criteria, String keyspace) {
        if (criteria == null || criteria.isEmpty()) {
            return this.getRequiredAdapter().count(keyspace);
        }
        return 0;
    }

    private <T> List<T> doFind(RegattaOperationChain criteria, long offset, int rows, String keyspace, Class<T> type) {
        Iterator<T> iterator = getRequiredAdapter().getAllOf(keyspace, type).iterator();
        var result = new LinkedList<T>();
        if (criteria == null || (CollectionUtils.isEmpty(criteria.getOrSismember()) && CollectionUtils.isEmpty(criteria.getSismember()))) {
            iterator.forEachRemaining(result::add);
            return result;
        }
        while (iterator.hasNext()) {
            var item = iterator.next();
            var wrapper = PropertyAccessorFactory.forBeanPropertyAccess(item);
            if (criteria.getSismember().stream().allMatch(c -> Objects.equals(wrapper.getPropertyValue(c.getPath()), c.getFirstValue()))
                    && criteria.getOrSismember().stream().anyMatch(c -> Objects.equals(wrapper.getPropertyValue(c.getPath()), c.getFirstValue()))) {
                result.add(item);
            }
        }
        return result;
    }

    static class RedisCriteriaAccessor implements CriteriaAccessor<RegattaOperationChain> {

        @Override
        public RegattaOperationChain resolve(KeyValueQuery<?> query) {
            return (RegattaOperationChain) query.getCriteria();
        }
    }
}
