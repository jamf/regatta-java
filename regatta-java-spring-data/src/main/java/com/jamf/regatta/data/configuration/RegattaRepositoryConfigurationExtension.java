/*
 * Copyright JAMF Software, LLC
 */

package com.jamf.regatta.data.configuration;

import com.jamf.regatta.data.convert.RegattaConverter;
import com.jamf.regatta.data.core.RegattaKeyValueAdapter;
import com.jamf.regatta.data.core.RegattaKeyValueTemplate;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.data.keyvalue.annotation.KeySpace;
import org.springframework.data.keyvalue.repository.config.KeyValueRepositoryConfigurationExtension;
import org.springframework.data.repository.config.RepositoryConfigurationSource;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;

public class RegattaRepositoryConfigurationExtension extends KeyValueRepositoryConfigurationExtension {

    private static final String REGATTA_ADAPTER_BEAN_NAME = "regattaKeyValueAdapter";
    private static final String REGATTA_CONVERTER_BEAN_NAME = "regattaConverter";
    private static final String REGATTA_CLIENT_BEAN_NAME = "regattaClient";

    private static AbstractBeanDefinition createRegattaConverterDefinition() {
        return BeanDefinitionBuilder.genericBeanDefinition(RegattaConverter.class)
                .getBeanDefinition();
    }

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

    @Override
    protected AbstractBeanDefinition getDefaultKeyValueTemplateBeanDefinition(RepositoryConfigurationSource configurationSource) {
        return BeanDefinitionBuilder.rootBeanDefinition(RegattaKeyValueTemplate.class)
                .addConstructorArgReference(REGATTA_ADAPTER_BEAN_NAME)
                .getBeanDefinition();
    }

    @Override
    public void registerBeansForRoot(BeanDefinitionRegistry registry, RepositoryConfigurationSource configuration) {
        registerIfNotAlreadyRegistered(RegattaRepositoryConfigurationExtension::createRegattaConverterDefinition, registry, REGATTA_CONVERTER_BEAN_NAME,
                configuration.getSource());
        registerIfNotAlreadyRegistered(() -> createRegattaKeyValueAdapter(configuration), registry, REGATTA_ADAPTER_BEAN_NAME,
                configuration.getSource());

        super.registerBeansForRoot(registry, configuration);
    }

    private AbstractBeanDefinition createRegattaKeyValueAdapter(RepositoryConfigurationSource configuration) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(RegattaKeyValueAdapter.class)
                .addConstructorArgReference(REGATTA_CLIENT_BEAN_NAME)
                .addConstructorArgReference(REGATTA_CONVERTER_BEAN_NAME);
        return builder.getBeanDefinition();
    }

}
