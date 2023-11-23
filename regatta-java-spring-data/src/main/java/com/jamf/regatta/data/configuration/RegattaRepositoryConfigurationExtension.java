package com.jamf.regatta.data.configuration;

import org.springframework.data.keyvalue.repository.config.KeyValueRepositoryConfigurationExtension;

public class RegattaRepositoryConfigurationExtension extends KeyValueRepositoryConfigurationExtension {

    @Override
    public String getModuleName() {
        return "Regatta";
    }

    @Override
    protected String getDefaultKeyValueTemplateRef() {
        return "regattaKeyValueTemplate";
    }

}
