package com.dehidehidehi.twitchtagcarousel.resource;

import jakarta.annotation.Nullable;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * A simple servlet which listens for oAuth callbacks from Twitch.
 * Should NOT be a CDI bean because we don't want this instanciated at CDI discovery.
 */
@Produces(MediaType.APPLICATION_JSON)
@Path("/")
public class TwitchCallbackResource extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwitchCallbackResource.class);

    private final Set<Class<?>> classes;

    //	@Inject
    //	TwitchClient twitchClient;

    public TwitchCallbackResource() {
        classes = Set.of(TwitchCallbackResource.class);
    }

    @GET
    @Path("auth-token")
    public Response handleAuthTokenCallBack(@Nullable @QueryParam("access_token") String accessToken,
                                            @Nullable @QueryParam("scope") String scope) {
        LOGGER.debug("Entrée dans la méthode TwitchCallbackResource.handleAuthTokenCallBack");
        LOGGER.debug("accessToken={}", accessToken);
        LOGGER.debug("scope={}", scope);
        //		Optional.ofNullable(accessToken).ifPresentOrElse(
        //				safeAccessToken -> twitchClient.setAccessToken(safeAccessToken),
        //				() -> {throw new IllegalStateException("Received empty access_token!");}
        //		);
        return Response.ok().build();
    }

    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }
}
