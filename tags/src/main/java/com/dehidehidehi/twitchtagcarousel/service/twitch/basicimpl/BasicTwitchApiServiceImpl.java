package com.dehidehidehi.twitchtagcarousel.service.twitch.basicimpl;
import com.dehidehidehi.twitchtagcarousel.error.TwitchAuthTokenQueryException;
import com.dehidehidehi.twitchtagcarousel.service.twitch.TwitchApiService;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Handles emitting and receiving http calls for simple purposes, using Jakarta.
 */
public abstract class BasicTwitchApiServiceImpl implements TwitchApiService {

    private final HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5L)).build();

    @Override
    public boolean isUserAccessTokenValid(final String userAccessToken) throws TwitchAuthTokenQueryException {
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
            throw new TwitchAuthTokenQueryException(e);
        }
        return response.statusCode() == 200;
    }
}
