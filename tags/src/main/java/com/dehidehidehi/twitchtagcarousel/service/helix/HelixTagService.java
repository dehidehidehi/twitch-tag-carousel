package com.dehidehidehi.twitchtagcarousel.service.helix;
import com.dehidehidehi.twitchtagcarousel.annotation.Property;
import com.dehidehidehi.twitchtagcarousel.domain.TwitchTagBatch;
import com.dehidehidehi.twitchtagcarousel.error.TwitchAuthTokenQueryException;
import com.dehidehidehi.twitchtagcarousel.error.TwitchChannelIdException;
import com.dehidehidehi.twitchtagcarousel.error.TwitchMissingAuthTokenException;
import com.dehidehidehi.twitchtagcarousel.error.TwitchTagUpdateException;
import com.dehidehidehi.twitchtagcarousel.service.TwitchAuthService;
import com.dehidehidehi.twitchtagcarousel.service.TwitchTagService;
import com.dehidehidehi.twitchtagcarousel.service.basic.BasicTwitchTagService;
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


@Typed(TwitchTagService.class)
@ApplicationScoped
public class HelixTagService extends BasicTwitchTagService implements TwitchTagService {

	private static final Logger LOGGER = LoggerFactory.getLogger(HelixTagService.class);
	
	@Inject
	@Property("twitch-app.user-access-token")
	private String userAccessToken;
	
	@Inject
	@Property("twitch-app.channel-name")
	private String channelName;
	
	private String broadcasterId;
	
	private final TwitchHelix twitchHelix;
	private final TwitchAuthService twitchAuthService;
	
	@Inject
	HelixTagService(final TwitchAuthService twitchAuthService) {
		this.twitchAuthService = twitchAuthService;
		twitchHelix = TwitchHelixBuilder.builder().build();
	}

	private void validateUserAccessToken() {
		final String hiddenToken = StringUtils.abbreviateMiddle(userAccessToken, "******", 12);
		LOGGER.info("Validating userAccessToken: {}", hiddenToken);
		final boolean isValid = isUserAccessTokenValid(userAccessToken);
		if (!isValid) {
			LOGGER.error("Your user access token is invalid : {}", hiddenToken);
			throw new TwitchAuthTokenQueryException("Your user access token is invalid : %s".formatted(hiddenToken));
		}
	}

	private void requestChannelId() {
		LOGGER.info("Requesting channelId from channelName={}", channelName);
		broadcasterId = getChannelIdFrom(channelName);
	}

	@Override
	public boolean isUserAccessTokenValid(final String userAccessToken) throws TwitchAuthTokenQueryException {
		// Twitch4J implementation is otherwise available in the Auth module, TwitchIdentityProvider#getAdditionalCredentialInformation
		return super.isUserAccessTokenValid(userAccessToken);
	}

	/**
	 * Requests the channel's id using the channel's name as a parameter.
	 */
	@Override
	public String getChannelIdFrom(final String channelName) throws TwitchChannelIdException {
		LOGGER.debug("{} entered getChannelIdFrom method.", HelixTagService.class.getSimpleName());
		final UserList userList;
		try {
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
	public void updateTags(final TwitchTagBatch tags) throws TwitchTagUpdateException {
		LOGGER.debug("{} entered update tags method with params {}.", HelixTagService.class.getSimpleName(), tags);
		final List<String> tagsAsList = List.copyOf(tags.get());
		final ChannelInformation channelInformation = new ChannelInformation().withTags(tagsAsList);
		try {
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
			twitchAuthService.asyncQueryAccessTokenWithImplicitGrantFlow(this::setUserAccessToken).get();
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getUserAccessToken() throws TwitchMissingAuthTokenException {
		if (userAccessToken == null) {
			throw new TwitchMissingAuthTokenException("Missing user access token! Did you query it before calling this method?");
		}
		return userAccessToken;
	}

	@Override
	public void setUserAccessToken(final String userAccessToken) {
		this.userAccessToken = userAccessToken;
		final String hiddenToken = StringUtils.abbreviateMiddle(userAccessToken, "******", 12);
		LOGGER.info("Successfully received access token :)");
		LOGGER.debug("Received access token {}", hiddenToken);
		// todo save it in user config files?
	}
}
