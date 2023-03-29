package com.dehidehidehi.twitchtagcarousel.service.twitchclient;
import com.dehidehidehi.twitchtagcarousel.error.TwitchUserAccessTokenException;
import com.dehidehidehi.twitchtagcarousel.error.WebServerStartException;
import com.dehidehidehi.twitchtagcarousel.resource.TwitchCallbackResource;
import io.helidon.microprofile.server.Server;
import jakarta.inject.Inject;
import jakarta.ws.rs.SeBootstrap;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.spi.ConfigBuilder;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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

	@Inject
	private TwitchCallbackResource twitchCallbackResource;

	private int port = 8080;
	private String protocol = "HTTPS";
	private String host = "localhost";

	@Override
	public void startAuthServlet() throws WebServerStartException {
//		server.start();
		final Server server = Server.builder()
											.addApplication(twitchCallbackResource)
											.host(host)
											.port(port)
											.build();
		server.start();

//		Routing.builder()
//				.register(jaxRsApplication)
//				.build();
//		final WebServer webServer = WebServer
//				.builder(routing)
//				.port(port)
//				.host(host)
//				.tls(WebServerTls.builder().enabled(false).build())
//				.build();
		try {
			Thread.currentThread().join(); // prevents server from just shutting down.
		} catch (InterruptedException e) {
			throw new WebServerStartException(e);
		}
	}

	private SeBootstrap.Configuration getServerConfiguration() {
		return SeBootstrap.Configuration
				.builder()
				.host(host)
				.port(port)
				.protocol(protocol)
				.sslClientAuthentication(SeBootstrap.Configuration.SSLClientAuthentication.OPTIONAL)
				.build();
	}

	@Override
	public void closeAuthServlet() {
		throw new UnsupportedOperationException("BasicTwitchClient.closeAuthServlet not implemented.");
	}

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
