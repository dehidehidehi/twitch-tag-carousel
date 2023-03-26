package com.dehidehidehi.twitchtagcarousel.service.twitchclient.basic;
import com.dehidehidehi.twitchtagcarousel.error.TwitchUserAccessTokenException;
import com.dehidehidehi.twitchtagcarousel.service.twitchclient.TwitchClient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public abstract class BasicHttpTwitchClient implements TwitchClient {

	private final HttpClient httpClient = HttpClient
			.newBuilder()
			.connectTimeout(Duration.ofSeconds(5L))
			.build();

	@Override
	public boolean isUserAccessTokenValid(final String userAccessToken) throws TwitchUserAccessTokenException {
		final HttpRequest request = HttpRequest
				.newBuilder()
				.GET()
				.uri(URI.create("https://id.twitch.tv/oauth2/validate"))
				.header("Authorization", "OAuth %s".formatted(userAccessToken))
				.build();
		final HttpResponse<String> response;
		try {
			response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			throw new TwitchUserAccessTokenException(e);
		}
		return response.statusCode() == 200;
	}
}
