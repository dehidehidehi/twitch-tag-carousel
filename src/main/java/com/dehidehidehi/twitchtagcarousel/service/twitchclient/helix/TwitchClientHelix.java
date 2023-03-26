package com.dehidehidehi.twitchtagcarousel.service.twitchclient.helix;
import com.dehidehidehi.twitchtagcarousel.annotation.Property;
import com.dehidehidehi.twitchtagcarousel.domain.TwitchTagBatch;
import com.dehidehidehi.twitchtagcarousel.error.TwitchTagUpdateException;
import com.dehidehidehi.twitchtagcarousel.service.twitchclient.TwitchClient;
import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.helix.TwitchHelixBuilder;
import com.github.twitch4j.helix.domain.ChannelInformation;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Typed;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@HelixClient
@Typed(TwitchClient.class)
@ApplicationScoped
class TwitchClientHelix implements TwitchClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(TwitchClientHelix.class);
	
	private final TwitchHelix twitchHelix;
	
	@Inject
	@Property("twitch-app.auth-token")
	private String authToken;
	
	@Inject
	@Property("twitch-app.broadcaster-id")
	private String broadcasterId;

	TwitchClientHelix() {
		twitchHelix = TwitchHelixBuilder.builder().build();
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
		LOGGER.debug("{} entered update tags method.", TwitchClientHelix.class.getSimpleName());
		final List<String> tagsAsList = List.copyOf(tags.get());
		final ChannelInformation channelInformation = new ChannelInformation().withTags(tagsAsList);
		try {
			twitchHelix
					.updateChannelInformation(authToken, broadcasterId, channelInformation)
					.execute();
		} catch (HystrixRuntimeException e) {
			throw new TwitchTagUpdateException(e);
		}
	}
}
