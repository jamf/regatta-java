package com.jamf.regatta.data.configuration;

import com.jamf.regatta.data.query.RegattaQueryCreator;
import com.jamf.regatta.data.repository.RegattaRepositoryFactoryBean;

import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Import;
import org.springframework.data.keyvalue.repository.config.QueryCreatorType;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(RegattaRepositoriesRegistrar.class)
@QueryCreatorType(RegattaQueryCreator.class)
public @interface EnableRegattaRepositories {

	String[] value() default {};
	String[] basePackages() default {};
	Class<?>[] basePackageClasses() default {};
	Filter[] includeFilters() default {};
	Filter[] excludeFilters() default {};
	Class<?> repositoryFactoryBeanClass() default RegattaRepositoryFactoryBean.class;
	String namedQueriesLocation() default "";
	String repositoryImplementationPostfix() default "Impl";
	String keyValueTemplateRef() default "regattaTemplateRef";
}
