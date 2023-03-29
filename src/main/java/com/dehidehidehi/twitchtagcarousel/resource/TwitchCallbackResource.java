package com.dehidehidehi.twitchtagcarousel.resource;

import com.dehidehidehi.twitchtagcarousel.service.twitchclient.TwitchClient;
import jakarta.annotation.Nullable;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * A simple servlet which listens for oAuth callbacks from Twitch.
 */
@ApplicationScoped
@ApplicationPath("twitch-tag-carousel")
public class TwitchCallbackResource extends Application {

	private static final Logger LOGGER = LoggerFactory.getLogger(TwitchCallbackResource.class);

	@Inject
	TwitchClient twitchClient;

	@GET
	@Path("auth-token")
	Response handleAuthTokenCallBack(@Nullable @QueryParam("access_token") String accessToken,
												@Nullable @QueryParam("scope") String scope) {
		LOGGER.debug("Entrée dans la méthode TwitchCallbackResource.handleAuthTokenCallBack");
		LOGGER.debug("accessToken={}", accessToken);
		LOGGER.debug("scope={}", scope);
		Optional.ofNullable(accessToken).ifPresentOrElse(
				safeAccessToken -> twitchClient.setAccessToken(safeAccessToken),
				() -> {throw new IllegalStateException("Received empty access_token!");}
		);
		return Response.ok().build();
	}
}
