package com.dehidehidehi.twitchtagcarousel.annotation.impl;

import com.dehidehidehi.twitchtagcarousel.annotation.qualifier.ApplicationProperty;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;

import java.io.InputStream;
import java.util.Properties;

/**
 * Producer for property value injection using the {@link ApplicationProperty} annotation.
 */
@ApplicationScoped
public class ApplicationPropertyProducer extends BasePropertyLoader {

	private static final String APPLICATION_PROPERTIES = "/application.properties";
	private Properties applicationProperties;

	/**
	 * Loads the {@link ApplicationPropertyProducer#APPLICATION_PROPERTIES} contents into the {@link Properties} field.
	 */
	@PostConstruct
	public void init() {
		this.applicationProperties = new Properties();
		final InputStream applicationPropertiesInputStream = ApplicationPropertyProducer.class.getResourceAsStream(APPLICATION_PROPERTIES);
		loadPropertiesFile(applicationProperties, applicationPropertiesInputStream);
	}

	@ApplicationProperty
	@Produces
	public String produceString(final InjectionPoint ip) {
		return findProperty(ip);
	}

	@ApplicationProperty
	@Produces
	@Nullable public Integer produceInt(final InjectionPoint ip) {
		return Integer.parseInt(findProperty(ip));
	}

	@ApplicationProperty
	@Produces
	@Nullable public Boolean produceBoolean(final InjectionPoint ip) {
		return Boolean.parseBoolean(findProperty(ip));
	}

	@Nullable public String findProperty(final InjectionPoint ip) {
		return (String) applicationProperties.get(getKey(ip));
	}

}
