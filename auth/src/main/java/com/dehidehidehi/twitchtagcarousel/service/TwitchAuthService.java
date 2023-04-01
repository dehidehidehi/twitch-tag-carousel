package com.dehidehidehi.twitchtagcarousel.service;


import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Represents authentication with Twitch.tv.
 */
public interface TwitchAuthService {

    /**
     * Asynchronously returns an access token using the Twitch implicit grant flow.
     *
     * @param onAccessTokenReceivedCallback to be executed when the access token has been received.
     */
    CompletableFuture<Void> asyncQueryAccessTokenWithImplicitGrantFlow(Consumer<String> onAccessTokenReceivedCallback);

}
