package com.jamf.regatta.data.convert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.jamf.regatta.core.api.ByteSequence;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;

public class RegattaConverter implements InitializingBean, ApplicationContextAware {

    private final DefaultConversionService converter = new DefaultConversionService();
    private ApplicationContext applicationContext;

    public RegattaConverter() {
        ByteSequenceConverters.converters().forEach(converter::addConverter);
    }

    public ByteSequence write(Object source) {
        return converter.convert(source, ByteSequence.class);
    }

    public <T> T read(ByteSequence source, Class<T> clazz) {
        return converter.convert(source, clazz);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        converter.addConverter(new JsonConverterFactory(this.applicationContext.getBean(ObjectMapper.class)));
        converter.addConverter(new XmlConverterFactory(this.applicationContext.getBean(XmlMapper.class)));
        this.applicationContext.getBeansOfType(Converter.class).values().forEach(converter::addConverter);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
