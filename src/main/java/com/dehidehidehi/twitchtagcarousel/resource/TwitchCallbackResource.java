package com.dehidehidehi.twitchtagcarousel.resource;

import jakarta.annotation.Nullable;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
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
    public Response handleAuthTokenCallBack(
//            @Context UriInfo uriInfo,
//            @Context HttpHeaders httpHeaders
//            @Context HttpR httpHeaders
            ) {
        LOGGER.debug("Entrée dans la méthode TwitchCallbackResource.handleAuthTokenCallBack");
//        LOGGER.debug("accessToken={}", accessToken);
//        LOGGER.debug("scope={}", scope);
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
