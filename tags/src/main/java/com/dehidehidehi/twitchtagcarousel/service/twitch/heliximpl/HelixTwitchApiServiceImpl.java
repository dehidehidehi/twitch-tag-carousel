package com.dehidehidehi.twitchtagcarousel.service.twitch.heliximpl;
import com.dehidehidehi.twitchtagcarousel.dao.PrivateUserPropertiesDao;
import com.dehidehidehi.twitchtagcarousel.dao.UserPropertiesDao;
import com.dehidehidehi.twitchtagcarousel.domain.TwitchTag;
import com.dehidehidehi.twitchtagcarousel.domain.TwitchTagBatch;
import com.dehidehidehi.twitchtagcarousel.error.AuthTokenQueryException;
import com.dehidehidehi.twitchtagcarousel.error.TwitchChannelIdException;
import com.dehidehidehi.twitchtagcarousel.error.MissingAuthTokenException;
import com.dehidehidehi.twitchtagcarousel.error.TwitchTagUpdateException;
import com.dehidehidehi.twitchtagcarousel.service.TwitchAuthService;
import com.dehidehidehi.twitchtagcarousel.service.twitch.TwitchApiService;
import com.dehidehidehi.twitchtagcarousel.service.twitch.basicimpl.BasicTwitchApiServiceImpl;
import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.helix.TwitchHelixBuilder;
import com.github.twitch4j.helix.domain.ChannelInformation;
import com.github.twitch4j.helix.domain.UserList;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Typed;
import jakarta.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutionException;


@Typed(TwitchApiService.class)
@ApplicationScoped
public class HelixTwitchApiServiceImpl extends BasicTwitchApiServiceImpl implements TwitchApiService {

	private static final Logger LOGGER = LoggerFactory.getLogger(HelixTwitchApiServiceImpl.class);
	public static final String PROPERTY_KEY_CHANNEL_NAME = "twitch-app.channel-name";
	private final UserPropertiesDao userPropertiesDao;
	
	private final TwitchAuthService twitchAuthService;
	private final PrivateUserPropertiesDao privateUserPropertiesDao;
	private final TwitchHelix twitchHelix;
	
	@Inject
	HelixTwitchApiServiceImpl(final TwitchAuthService twitchAuthService,
									  final UserPropertiesDao userPropertiesDao,
									  final PrivateUserPropertiesDao privateUserPropertiesDao) {
		this.twitchAuthService = twitchAuthService;
		this.privateUserPropertiesDao = privateUserPropertiesDao;
		this.userPropertiesDao = userPropertiesDao;
		twitchHelix = TwitchHelixBuilder.builder().build();
	}

	private void validateUserAccessToken(final String userAccessToken) {
		final String hiddenToken = StringUtils.abbreviateMiddle(userAccessToken, "******", 12);
		LOGGER.info("Validating userAccessToken: {}", hiddenToken);
		final boolean isValid = isUserAccessTokenValid(userAccessToken);
		if (!isValid) {
			LOGGER.error("Your user access token is invalid : {}", hiddenToken);
			throw new AuthTokenQueryException("Your user access token is invalid : %s".formatted(hiddenToken));
		}
	}

	@Override
	public boolean isUserAccessTokenValid(final String userAccessToken) throws AuthTokenQueryException {
		// Twitch4J implementation is otherwise available in the Auth module, TwitchIdentityProvider#getAdditionalCredentialInformation
		return super.isUserAccessTokenValid(userAccessToken);
	}

	/**
	 * Requests the channel's id using the channel's name as a parameter.
	 */
	@Override
	public String getBroadcasterIdOf(final String channelName) throws TwitchChannelIdException, MissingAuthTokenException {
		LOGGER.debug("{} entered getChannelIdFrom method.", HelixTwitchApiServiceImpl.class.getSimpleName());
		final UserList userList;
		try {
			final String userAccessToken = privateUserPropertiesDao.getUserAccessToken();
			userList = twitchHelix
					.getUsers(userAccessToken, null, List.of(channelName))
					.execute();
		} catch (HystrixRuntimeException e) {
			throw new TwitchChannelIdException(e);
		}
		if (userList.getUsers().isEmpty()) {
			throw new TwitchChannelIdException("No channel information found for channelName=%s".formatted(channelName));
		}
		final String channelId = userList.getUsers().get(0).getId();
		LOGGER.debug("Found channelId from {}: {}", channelName, channelId);
		return channelId;
	}

	/**
	 * <a href="https://dev.twitch.tv/docs/api/reference/#replace-stream-tags">Channel Information Twitch API
	 * documentation</a><br>
	 * Requires a user access token that includes the channel:manage:broadcast scope.<br>
	 * channel%3Amanage%3Abroadcast<br>
	 * https://id.twitch.tv/oauth2/authorize?client_id=6k3qz1pdf1wko4xec9cjbfh3fbla24&redirect_uri=http://localhost&response_type=token&scope=channel%3Amanage%3Abroadcast
	 */
	@Override
	public void updateTags(final TwitchTagBatch tags) throws TwitchTagUpdateException, MissingAuthTokenException {
		LOGGER.debug("{} entered update tags method with params {}.", HelixTwitchApiServiceImpl.class.getSimpleName(), tags);
		final List<String> tagsAsList = tags.get().stream().map(TwitchTag::toString).toList();
		final ChannelInformation channelInformation = new ChannelInformation().withTags(tagsAsList);
		try {
			final String userAccessToken = privateUserPropertiesDao.getUserAccessToken();
			final String channelName = userPropertiesDao.readUserProperty(PROPERTY_KEY_CHANNEL_NAME);
			final String broadcasterId = getBroadcasterIdOf(channelName);
			twitchHelix
					.updateChannelInformation(userAccessToken, broadcasterId, channelInformation)
					.execute();
		} catch (HystrixRuntimeException e) {
			throw new TwitchTagUpdateException(e);
		}
	}

	@Override
	public void queryUserAccessToken() {
		try {
			twitchAuthService.asyncQueryAccessTokenWithImplicitGrantFlow(privateUserPropertiesDao::setUserAccessToken).get();
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

}
