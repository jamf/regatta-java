package com.jamf.regatta.data.convert;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface RegattaValueMapping {
    Type value() default Type.JSON;

    enum Type {
        XML, JSON
    }
}
