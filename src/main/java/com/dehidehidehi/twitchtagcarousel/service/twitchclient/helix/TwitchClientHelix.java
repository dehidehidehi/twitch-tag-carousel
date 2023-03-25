package com.dehidehidehi.twitchtagcarousel.service.twitchclient.helix;
import com.dehidehidehi.twitchtagcarousel.annotation.Property;
import com.dehidehidehi.twitchtagcarousel.service.twitchclient.TwitchClient;
import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.helix.TwitchHelixBuilder;
import com.github.twitch4j.helix.domain.ChannelInformation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Typed;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Set;

@HelixClient
@Typed(TwitchClient.class)
@ApplicationScoped
class TwitchClientHelix implements TwitchClient {

	private final TwitchHelix twitchHelix;
	
	@Inject
	@Property("twitch-app.auth-token")
	private String authToken;
	
	@Inject
	@Property("twitch-app.broadcaster-id")
	private String broadcasterId;

	TwitchClientHelix() {
		twitchHelix = TwitchHelixBuilder
				.builder()
				.withClientId(null)
				.withClientSecret(null)
				.build();
	}

	/**
	 * <a href="https://dev.twitch.tv/docs/api/reference/#replace-stream-tags">Channel Information Twitch API
	 * documentation</a><br>
	 * Requires a user access token that includes the channel:manage:broadcast scope.<br>
	 * channel%3Amanage%3Abroadcast<br>
	 * https://id.twitch.tv/oauth2/authorize?client_id=6k3qz1pdf1wko4xec9cjbfh3fbla24&redirect_uri=http://localhost&response_type=token&scope=channel%3Amanage%3Abroadcast
	 */
	@Override
	public void updateTags(final Set<String> tags) {
		final ChannelInformation channelInformation = new ChannelInformation().withTags(List.copyOf(tags));
		twitchHelix
				.updateChannelInformation(authToken, broadcasterId, channelInformation)
				.execute();
	}
}
