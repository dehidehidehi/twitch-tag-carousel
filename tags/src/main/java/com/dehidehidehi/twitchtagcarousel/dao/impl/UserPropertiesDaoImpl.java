package com.dehidehidehi.twitchtagcarousel.dao.impl;

import com.dehidehidehi.twitchtagcarousel.dao.UserPropertiesDao;
import com.dehidehidehi.twitchtagcarousel.domain.TwitchTag;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Typed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Typed(UserPropertiesDao.class)
@ApplicationScoped
class UserPropertiesDaoImpl implements UserPropertiesDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserPropertiesDaoImpl.class);

	static final String USER_PROPERTIES_FILE = "/user.properties";
	static final String PROPERTY_MANDATORY_TAGS = "tags.mandatory";
	static final String PROPERTY_ROTATING_TAGS = "tags.rotating";

	private Properties properties;
	private InputStream propertiesInputStream;

	@PostConstruct
	void init() {
		properties = new Properties();
		try {
			propertiesInputStream = getClass().getResourceAsStream(USER_PROPERTIES_FILE);
			properties.load(this.propertiesInputStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void saveMandatoryTags(final List<TwitchTag> tags) {
		final String concatenated = tags
				.stream()
				.map(TwitchTag::toString)
				.collect(Collectors.joining(","));
		properties.setProperty(PROPERTY_MANDATORY_TAGS, concatenated);
	}

	@Override
	public List<TwitchTag> getMandatoryTags() {
		final String concatenatedTags = properties.getProperty(PROPERTY_MANDATORY_TAGS);
		return Arrays.stream(concatenatedTags.split(","))
						 .map(TwitchTag::new)
						 .distinct()
						 .toList();
	}

	@Override
	public void saveRotatingTags(final List<TwitchTag> tags) {
		final String concatenated = tags
				.stream()
				.map(TwitchTag::toString)
				.collect(Collectors.joining(","));
		properties.setProperty(PROPERTY_ROTATING_TAGS, concatenated);
	}

	@Override
	public List<TwitchTag> getRotatingTags() {
		final String concatenatedTags = properties.getProperty(PROPERTY_ROTATING_TAGS);
		return Arrays.stream(concatenatedTags.split(","))
						 .map(TwitchTag::new)
						 .distinct()
						 .toList();
	}
}
