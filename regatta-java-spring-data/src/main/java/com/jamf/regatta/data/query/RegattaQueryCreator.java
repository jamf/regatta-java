package com.jamf.regatta.data.query;

import org.springframework.data.domain.Sort;
import org.springframework.data.keyvalue.core.query.KeyValueQuery;
import org.springframework.data.repository.query.ParameterAccessor;
import org.springframework.data.repository.query.parser.AbstractQueryCreator;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.data.repository.query.parser.PartTree;

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
        return null;
    }

    @Override
    protected RegattaOperationChain and(Part part, RegattaOperationChain base, Iterator<Object> iterator) {
        return null;
    }

    @Override
    protected RegattaOperationChain or(RegattaOperationChain base, RegattaOperationChain criteria) {
        return null;
    }

    @Override
    protected KeyValueQuery<RegattaOperationChain> complete(RegattaOperationChain criteria, Sort sort) {
        return null;
    }

}
