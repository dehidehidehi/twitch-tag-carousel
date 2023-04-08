package com.dehidehidehi.twitchtagcarousel.annotation.impl;
import com.dehidehidehi.twitchtagcarousel.annotation.PropertyLoader;
import com.dehidehidehi.twitchtagcarousel.annotation.qualifier.ApplicationProperty;
import jakarta.annotation.Nullable;
import jakarta.enterprise.inject.spi.InjectionPoint;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

abstract class BasePropertyLoader implements PropertyLoader {

	protected void loadPropertiesFile(Properties properties, InputStream inputStream) {
		final Consumer<InputStream> tryReadProperties = propertiesStream -> {
			try {
				properties.load(propertiesStream);
			} catch (final IOException e) {
				throw new IllegalStateException("Configuration could not be loaded!");
			}
		};
		tryReadProperties.accept(inputStream);
	}

	/**
	 * Convenience method for getting the absolute path of where the .jar file will be deployed.
	 */
	protected File getDirPathOfThisJar() {
		final String jarLocation;
		try {
			jarLocation = new File(ApplicationPropertyProducer.class
												  .getProtectionDomain()
												  .getCodeSource()
												  .getLocation()
												  .toURI()
												  .getPath()).getParent();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		return new File(jarLocation);
	}
	
	/**
	 * Returns the property value, or if not found, the name of the path to that property.
	 */
	protected String getKey(final InjectionPoint ip) {
		final boolean isIpPropertyPresent = ip.getAnnotated().isAnnotationPresent(ApplicationProperty.class);
		final BooleanSupplier isIpPropertyValueEmpty = () -> ip.getAnnotated().getAnnotation(ApplicationProperty.class).value().isEmpty();
		return (isIpPropertyPresent && !isIpPropertyValueEmpty.getAsBoolean()) ? ip
				.getAnnotated()
				.getAnnotation(ApplicationProperty.class)
				.value() : ip.getMember().getName();
	}

	@Nullable public String produceString(final InjectionPoint ip) {
		return findProperty(ip);
	}

	@Nullable public Integer produceInt(final InjectionPoint ip) {
		final String maybeProperty = findProperty(ip);
		return maybeProperty != null ? Integer.parseInt(maybeProperty) : null;
	}

	@Nullable public Boolean produceBoolean(final InjectionPoint ip) {
		final String maybeProperty = findProperty(ip);
		return maybeProperty != null ? Boolean.parseBoolean(maybeProperty) : null;
	}
}
