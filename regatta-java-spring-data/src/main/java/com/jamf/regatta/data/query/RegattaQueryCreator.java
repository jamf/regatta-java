package com.jamf.regatta.data.query;

import org.springframework.data.domain.Sort;
import org.springframework.data.keyvalue.core.query.KeyValueQuery;
import org.springframework.data.repository.query.ParameterAccessor;
import org.springframework.data.repository.query.parser.AbstractQueryCreator;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.data.repository.query.parser.PartTree;
import org.springframework.util.CollectionUtils;

import java.util.Iterator;

public class RegattaQueryCreator extends AbstractQueryCreator<KeyValueQuery<RegattaOperationChain>, RegattaOperationChain> {

    public RegattaQueryCreator(PartTree tree) {
        super(tree);
    }

    public RegattaQueryCreator(PartTree tree, ParameterAccessor parameters) {
        super(tree, parameters);
    }

    @Override
    protected RegattaOperationChain create(Part part, Iterator<Object> iterator) {
        return from(part, iterator, new RegattaOperationChain());
    }

    private RegattaOperationChain from(Part part, Iterator<Object> iterator, RegattaOperationChain sink) {

        switch (part.getType()) {
            case SIMPLE_PROPERTY -> sink.sismember(part.getProperty().toDotPath(), iterator.next());
            case TRUE -> sink.sismember(part.getProperty().toDotPath(), true);
            case FALSE -> sink.sismember(part.getProperty().toDotPath(), false);
            default -> {
                String message = String.format("%s is not supported for Regatta query derivation", part.getType());
                throw new IllegalArgumentException(message);
            }
        }

        return sink;
    }

    @Override
    protected RegattaOperationChain and(Part part, RegattaOperationChain base, Iterator<Object> iterator) {
        return from(part, iterator, base);
    }

    @Override
    protected RegattaOperationChain or(RegattaOperationChain base, RegattaOperationChain criteria) {
        base.orSismember(criteria.getSismember());
        return base;
    }

    @Override
    protected KeyValueQuery<RegattaOperationChain> complete(RegattaOperationChain criteria, Sort sort) {
        KeyValueQuery<RegattaOperationChain> query = new KeyValueQuery<>(criteria);

        if (query.getCriteria() != null && !CollectionUtils.isEmpty(query.getCriteria().getSismember())
                && !CollectionUtils.isEmpty(query.getCriteria().getOrSismember()))
            if (query.getCriteria().getSismember().size() == 1 && query.getCriteria().getOrSismember().size() == 1) {

                query.getCriteria().getOrSismember().add(query.getCriteria().getSismember().iterator().next());
                query.getCriteria().getSismember().clear();
            }

        if (sort.isSorted()) {
            query.setSort(sort);
        }

        return query;
    }

}
