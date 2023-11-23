package com.jamf.regatta.data.configuration;

import com.jamf.regatta.data.query.RegattaQueryCreator;
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

}
