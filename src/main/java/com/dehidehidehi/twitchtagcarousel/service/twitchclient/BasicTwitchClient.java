package com.dehidehidehi.twitchtagcarousel.service.twitchclient;
import com.dehidehidehi.twitchtagcarousel.error.TwitchUserAccessTokenException;
import com.dehidehidehi.twitchtagcarousel.error.WebServerStartException;
import com.dehidehidehi.twitchtagcarousel.resource.TwitchCallbackResource;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.ext.RuntimeDelegate;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Handles emitting and receiving http calls for simple purposes, using Jakarta.
 */
public abstract class BasicTwitchClient implements TwitchClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(BasicTwitchClient.class);

    private final HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5L)).build();

    private int port = 8080;
    private String host = "http://localhost";

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

    @SneakyThrows
    @Override
    public HttpServer startAuthServer() throws WebServerStartException {
        final URI targetUri = UriBuilder
                .fromUri("%s/twitch-tag-carousel/".formatted(host))
                .port(port)
                .build();
        final HttpServer server = HttpServer.create(new InetSocketAddress(targetUri.getPort()), 0);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> server.stop(0)));
        HttpHandler handler = RuntimeDelegate.getInstance().createEndpoint(new TwitchCallbackResource(), HttpHandler.class);
        server.createContext(targetUri.getPath(), handler);
        server.start();
        return server;
    }

    @Override
    public void stopAuthServer(HttpServer httpServer) {
        //        throw new UnsupportedOperationException("BasicTwitchClient.closeAuthServlet not implemented.");
    }
}
