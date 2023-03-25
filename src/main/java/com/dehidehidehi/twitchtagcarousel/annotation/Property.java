package com.dehidehidehi.twitchtagcarousel.annotation;

import jakarta.enterprise.util.Nonbinding;
import jakarta.inject.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Represents a property key to be injected.
 */
@Qualifier
@Retention(RUNTIME)
@Target({METHOD, FIELD, PARAMETER})
public @interface Property {
    
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
