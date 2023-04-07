package com.dehidehidehi.twitchtagcarousel.dao.impl;

import com.dehidehidehi.twitchtagcarousel.dao.PrivateUserPropertiesDao;
import com.dehidehidehi.twitchtagcarousel.error.TwitchMissingAuthTokenException;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Typed;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Typed(PrivateUserPropertiesDao.class)
@ApplicationScoped
class PrivateUserPropertiesDaoImpl implements PrivateUserPropertiesDao, AutoCloseable {

	private static final Logger LOGGER = LoggerFactory.getLogger(PrivateUserPropertiesDaoImpl.class);
	static final String PRIVATE_USER_PROPERTIES_FILE = "/private-user.properties";
	static final String PROPERTY_TWITCH_ACCESS_TOKEN = "twitch.access-token";
	private Properties properties;
	private InputStream propertiesInputStream;

	@PostConstruct
	void init() {
		properties = new Properties();
		try {
			propertiesInputStream = getClass().getResourceAsStream(PRIVATE_USER_PROPERTIES_FILE);
			properties.load(this.propertiesInputStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	// TODO simple caching mecanism
	@Override
	public String getUserAccessToken() throws TwitchMissingAuthTokenException {
		final String accessToken = properties.getProperty(PROPERTY_TWITCH_ACCESS_TOKEN);
		if (StringUtils.isEmpty(accessToken)) {
			throw new TwitchMissingAuthTokenException("Missing user access token! Did you query it before calling this method?");
		}
		return accessToken;
	}

	@Override
	public void setUserAccessToken(final String userAccessToken) {
		final String hiddenToken = StringUtils.abbreviateMiddle(userAccessToken, "******", 12);
		properties.setProperty(PROPERTY_TWITCH_ACCESS_TOKEN, userAccessToken);
		LOGGER.info("Successfully received access token :)");
		LOGGER.debug("Received access token {}", hiddenToken);
	}

	@Override
	public void close() throws Exception {
		if (propertiesInputStream != null) {
			propertiesInputStream.close();
		}
	}
}
