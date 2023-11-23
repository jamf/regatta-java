package com.jamf.regatta.data.configuration;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;

import org.springframework.data.keyvalue.annotation.KeySpace;
import org.springframework.data.keyvalue.repository.config.KeyValueRepositoryConfigurationExtension;

public class RegattaRepositoryConfigurationExtension extends KeyValueRepositoryConfigurationExtension {

    @Override
    public String getModuleName() {
        return "Regatta";
    }

    @Override
    protected String getDefaultKeyValueTemplateRef() {
        return "regattaTemplateRef";
    }

    @Override
    protected Collection<Class<? extends Annotation>> getIdentifyingAnnotations() {
        return Collections.singleton(KeySpace.class);
    }

}
