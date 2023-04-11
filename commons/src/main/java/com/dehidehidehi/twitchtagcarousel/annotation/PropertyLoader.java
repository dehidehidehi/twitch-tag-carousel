package com.dehidehidehi.twitchtagcarousel.annotation;
import jakarta.annotation.Nullable;
import jakarta.enterprise.inject.spi.InjectionPoint;

public interface PropertyLoader {

    String findProperty(final InjectionPoint ip);

    /**
     * If no property found will return null.
     */
    @Nullable String produceString(final InjectionPoint ip);

    /**
     * If no property found will return null.
     */
    @Nullable Integer produceInt(final InjectionPoint ip);

    /**
     * If no property found will return null.
     */
    @Nullable Boolean produceBoolean(final InjectionPoint ip);
}
