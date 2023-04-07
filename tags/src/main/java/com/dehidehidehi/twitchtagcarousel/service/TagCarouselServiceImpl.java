package com.dehidehidehi.twitchtagcarousel.service;
import com.dehidehidehi.twitchtagcarousel.dao.PrivateUserPropertiesDao;
import com.dehidehidehi.twitchtagcarousel.dao.UserPropertiesDao;
import com.dehidehidehi.twitchtagcarousel.domain.TwitchTag;
import com.dehidehidehi.twitchtagcarousel.domain.TwitchTagBatch;
import com.dehidehidehi.twitchtagcarousel.error.TwitchAuthTokenQueryException;
import com.dehidehidehi.twitchtagcarousel.error.TwitchChannelIdException;
import com.dehidehidehi.twitchtagcarousel.error.TwitchMissingAuthTokenException;
import com.dehidehidehi.twitchtagcarousel.error.TwitchTagUpdateException;
import com.dehidehidehi.twitchtagcarousel.service.twitch.TwitchApiService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Typed;
import jakarta.inject.Inject;

import java.util.List;

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
	public String getUserAccessToken() throws TwitchMissingAuthTokenException {
		return privateUserPropertiesDao.getUserAccessToken();
	}

	@Override
	public void setUserAccessToken(final String userAccessToken) {
		privateUserPropertiesDao.setUserAccessToken(userAccessToken);
	}

	@Override
	public void saveAllTags(final List<TwitchTag> tags) {
		userPropertiesDao.saveAllTags(tags);
	}

	@Override
	public List<TwitchTag> getAllTags() {
		return userPropertiesDao.getAllTags();
	}

	@Override
	public boolean isUserAccessTokenValid(final String userAccessToken) throws TwitchAuthTokenQueryException {
		return twitchApiService.isUserAccessTokenValid(userAccessToken);
	}

	@Override
	public String getChannelIdFrom(final String channelName) throws TwitchChannelIdException, TwitchMissingAuthTokenException {
		return twitchApiService.getChannelIdFrom(channelName);
	}

	@Override
	public void updateTags(final TwitchTagBatch tags) throws TwitchTagUpdateException, TwitchMissingAuthTokenException {
		twitchApiService.updateTags(tags);
	}

	@Override
	public void queryUserAccessToken() throws TwitchAuthTokenQueryException {
		twitchApiService.queryUserAccessToken();
	}
}
