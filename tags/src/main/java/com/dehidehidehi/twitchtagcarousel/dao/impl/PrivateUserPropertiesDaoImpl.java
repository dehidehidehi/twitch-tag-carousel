package com.dehidehidehi.twitchtagcarousel.dao.impl;

import com.dehidehidehi.twitchtagcarousel.dao.PrivateUserPropertiesDao;
import com.dehidehidehi.twitchtagcarousel.error.TwitchMissingAuthTokenException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Typed;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Typed(PrivateUserPropertiesDao.class)
@ApplicationScoped
class PrivateUserPropertiesDaoImpl implements PrivateUserPropertiesDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(PrivateUserPropertiesDaoImpl.class);
	
	private String userAccessToken;

	// TODO simple caching mecanism
	@Override
	public String getUserAccessToken() throws TwitchMissingAuthTokenException {
		if (userAccessToken == null) {
			throw new TwitchMissingAuthTokenException("Missing user access token! Did you query it before calling this method?");
		}
		return userAccessToken;
	}

	@Override
	public void setUserAccessToken(final String userAccessToken) {
		final String hiddenToken = StringUtils.abbreviateMiddle(userAccessToken, "******", 12);
		LOGGER.info("Successfully received access token :)");
		LOGGER.debug("Received access token {}", hiddenToken);
		// todo save it in user config files?
	}
}
