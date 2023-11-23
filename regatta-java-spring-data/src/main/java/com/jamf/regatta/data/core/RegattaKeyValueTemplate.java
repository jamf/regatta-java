package com.jamf.regatta.data.core;

import org.springframework.data.keyvalue.core.IdentifierGenerator;
import org.springframework.data.keyvalue.core.KeyValueAdapter;
import org.springframework.data.keyvalue.core.KeyValueTemplate;
import org.springframework.data.keyvalue.core.mapping.KeyValuePersistentEntity;
import org.springframework.data.keyvalue.core.mapping.KeyValuePersistentProperty;
import org.springframework.data.mapping.context.MappingContext;

public class RegattaKeyValueTemplate extends KeyValueTemplate {

    public RegattaKeyValueTemplate(KeyValueAdapter adapter) {
        super(adapter);
    }

    public RegattaKeyValueTemplate(KeyValueAdapter adapter, MappingContext<? extends KeyValuePersistentEntity<?, ?>, ? extends KeyValuePersistentProperty<?>> mappingContext) {
        super(adapter, mappingContext);
    }

    public RegattaKeyValueTemplate(KeyValueAdapter adapter, MappingContext<? extends KeyValuePersistentEntity<?, ?>, ? extends KeyValuePersistentProperty<?>> mappingContext, IdentifierGenerator identifierGenerator) {
        super(adapter, mappingContext, identifierGenerator);
    }

}
