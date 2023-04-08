package com.dehidehidehi.twitchtagcarousel.annotation.qualifier;

import jakarta.enterprise.util.Nonbinding;
import jakarta.inject.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Represents an application specific property to be loaded from a properties file.
 */
@Qualifier
@Retention(RUNTIME)
@Target({METHOD, FIELD, PARAMETER})
public @interface ApplicationProperty {
    
    /**
     * Key to search for in the property files.
     * @return a string.
     */
    @Nonbinding String value() default "";

    /**
     * Is the key a mandatory key.
     * @return true as default but false if set
     */
    @Nonbinding boolean required() default true;
}
