package com.dehidehidehi.twitchtagcarousel.resource;

import com.dehidehidehi.twitchtagcarousel.service.twitchclient.TwitchClient;
import jakarta.annotation.Nullable;
import jakarta.enterprise.inject.Vetoed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A simple servlet which listens for oAuth callbacks from Twitch.
 * Should NOT be a CDI bean because we don't want this instantiated at CDI discovery.
 */
@Vetoed
@Produces(MediaType.TEXT_HTML)
@Path("/")
public class TwitchCallbackResource extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwitchCallbackResource.class);

    private final Set<Class<?>> classes;

    //	@Inject
    	TwitchClient twitchClient;

    public TwitchCallbackResource() {
        classes = Set.of(TwitchCallbackResource.class);
    }

    /**
     * Saves the access token locally then displays a `success` html page.
     */
    @Path("token")
    @GET
    public Response handleAuthToken(@QueryParam("access_token") @Nullable final String accessToken) throws URISyntaxException {
        LOGGER.info("Entrée dans la méthode TwitchCallbackResource.handleAuthToken");
        LOGGER.debug("accessToken={}", accessToken);
        Optional.ofNullable(accessToken).ifPresentOrElse(
//                safeAccessToken -> twitchClient.setAccessToken(safeAccessToken),
                safeAccessToken -> {},  // NO OP
                () -> {throw new IllegalStateException("Received empty access_token!");}
        );
        final URI page = Objects.requireNonNull(getClass().getResource("/WEB-INF/html/token_received.html")).toURI();
        final File file = new File(page);
        return Response.ok(file).build();
    }

    /**
     * Given Twitch send the auth token AFTER the fragment part of the URL (the # symbol),
     * a server cannot access the token value.<br>
     * However, redirecting the request to an HTML page with javascript, which CAN get that value, solves the issue.
     */
    @Path("token-redirect")
    @GET
    public Response handleAuthTokenCallBack() throws URISyntaxException {
        LOGGER.info("Serving static html page which captures URL fragments then redirects the user.");
        final URI page = Objects.requireNonNull(getClass().getResource("/WEB-INF/html/token_redirect.html")).toURI();
        final File file = new File(page);
        return Response.ok(file).build();
    }

    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }
}
