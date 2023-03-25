package com.dehidehidehi.twitchtagcarousel.annotation;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.function.BooleanSupplier;

/**
 * Producer for property value injection using the {@link Property} annotation.
 */
@ApplicationScoped
public class PropertyProducer {

	public static final String APPLICATION_PROPERTIES = "/application.properties";
	private Properties properties;

	/**
	 * Loads the {@link PropertyProducer#APPLICATION_PROPERTIES} contents into the {@link Properties} field.
	 */
	@PostConstruct
	public void init() {
		this.properties = new Properties();
		final InputStream stream = PropertyProducer.class.getResourceAsStream(APPLICATION_PROPERTIES);
		if (stream == null) {
			throw new IllegalStateException("No %s found!".formatted(APPLICATION_PROPERTIES));
		}
		try {
			this.properties.load(stream);
		} catch (final IOException e) {
			throw new IllegalStateException("Configuration could not be loaded!");
		}
	}

	@Property
	@Produces
	public String produceString(final InjectionPoint ip) {
		return this.properties.getProperty(getKey(ip));
	}

	/**
	 * Returns the property value, or if not found, the name of the path to that property.
	 */
	private String getKey(final InjectionPoint ip) {
		final boolean isIpPropertyPresent = ip.getAnnotated().isAnnotationPresent(Property.class);
		final BooleanSupplier isIpPropertyValueEmpty = () -> ip.getAnnotated().getAnnotation(Property.class).value().isEmpty();
		return (isIpPropertyPresent && !isIpPropertyValueEmpty.getAsBoolean()) ? ip
				.getAnnotated()
				.getAnnotation(Property.class)
				.value() : ip.getMember().getName();
	}

	@Property
	@Produces
	public int produceInt(final InjectionPoint ip) {
		return Integer.parseInt(this.properties.getProperty(getKey(ip)));
	}

	@Property
	@Produces
	public boolean produceBoolean(final InjectionPoint ip) {
		return Boolean.parseBoolean(this.properties.getProperty(getKey(ip)));
	}
}
