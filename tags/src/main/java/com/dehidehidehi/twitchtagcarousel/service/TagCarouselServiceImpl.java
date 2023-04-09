package com.dehidehidehi.twitchtagcarousel.service;
import com.dehidehidehi.twitchtagcarousel.dao.PrivateUserPropertiesDao;
import com.dehidehidehi.twitchtagcarousel.dao.UserPropertiesDao;
import com.dehidehidehi.twitchtagcarousel.domain.TwitchTag;
import com.dehidehidehi.twitchtagcarousel.domain.TwitchTagBatch;
import com.dehidehidehi.twitchtagcarousel.error.*;
import com.dehidehidehi.twitchtagcarousel.service.twitch.TwitchApiService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Typed;
import jakarta.inject.Inject;

import java.util.List;

/**
 * Giant service facade for our application. It's passed around in the UI.
 */
@Typed(TagCarouselService.class)
@ApplicationScoped
class TagCarouselServiceImpl implements TagCarouselService {

	private final UserPropertiesDao userPropertiesDao;
	private final PrivateUserPropertiesDao privateUserPropertiesDao;
	private final TwitchApiService twitchApiService;

	@Inject
	TagCarouselServiceImpl(final UserPropertiesDao userPropertiesDao,
								  final PrivateUserPropertiesDao privateUserPropertiesDao,
								  final TwitchApiService twitchApiService) {
		this.userPropertiesDao = userPropertiesDao;
		this.privateUserPropertiesDao = privateUserPropertiesDao;
		this.twitchApiService = twitchApiService;
	}

	@Override
	public String getUserAccessToken() throws MissingAuthTokenException {
		return privateUserPropertiesDao.getUserAccessToken();
	}

	@Override
	public void setUserAccessToken(final String userAccessToken) {
		privateUserPropertiesDao.setUserAccessToken(userAccessToken);
	}

	@Override
	public String readUserProperty(final String propertyKey) {
		throw new UnsupportedOperationException("Method should not be used in %s".formatted(getClass().getSimpleName()));
	}

	@Override
	public void saveMandatoryTags(final List<TwitchTag> tags) throws TwitchTagValidationException {
		userPropertiesDao.saveMandatoryTags(tags);
	}

	@Override
	public List<TwitchTag> getMandatoryTags() {
		return userPropertiesDao.getMandatoryTags();
	}

	@Override
	public int countMandatoryTags() {
		return userPropertiesDao.countMandatoryTags();
	}

	@Override
	public void saveRotatingTags(final List<TwitchTag> tags) throws TwitchTagValidationException {
		userPropertiesDao.saveRotatingTags(tags);
	}

	@Override
	public List<TwitchTag> getRotatingTags() {
		return userPropertiesDao.getRotatingTags();
	}

	@Override
	public int countRotatingTags() {
		return userPropertiesDao.countRotatingTags();
	}

	@Override
	public boolean isUserAccessTokenValid(final String userAccessToken) throws AuthTokenQueryException {
		return twitchApiService.isUserAccessTokenValid(userAccessToken);
	}

	@Override
	public String getBroadcasterIdOf(final String channelName) throws TwitchChannelIdException, MissingAuthTokenException {
		return twitchApiService.getBroadcasterIdOf(channelName);
	}

	@Override
	public void updateTags(final TwitchTagBatch tags)
	throws TwitchTagUpdateException, MissingAuthTokenException, MissingUserProvidedTagsException {
		twitchApiService.updateTags(tags);
	}

	@Override
	public void queryUserAccessToken() throws AuthTokenQueryException {
		twitchApiService.queryUserAccessToken();
	}

	@Override
	public void close() throws Exception {
		throw new UnsupportedOperationException("Method should not be used in %s".formatted(getClass().getSimpleName()));
	}
}
