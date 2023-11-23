package com.jamf.regatta.data.configuration;

import org.springframework.data.repository.config.RepositoryBeanDefinitionRegistrarSupport;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;

import java.lang.annotation.Annotation;

public class RegattaRepositoriesRegistrar extends RepositoryBeanDefinitionRegistrarSupport {

    @Override
    protected Class<? extends Annotation> getAnnotation() {
        return EnableRegattaRepositories.class;
    }

    @Override
    protected RepositoryConfigurationExtension getExtension() {
        return new RegattaRepositoryConfigurationExtension();
    }

}
