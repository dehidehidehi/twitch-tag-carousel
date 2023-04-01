package com.dehidehidehi.twitchtagcarousel.service.impl;
import com.dehidehidehi.twitchtagcarousel.annotation.Property;
import com.dehidehidehi.twitchtagcarousel.error.TwitchAuthTokenQueryException;
import com.dehidehidehi.twitchtagcarousel.resource.TwitchAuthResource;
import com.dehidehidehi.twitchtagcarousel.service.TwitchAuthService;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Typed;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.ext.RuntimeDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.concurrent.*;
import java.util.function.Consumer;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * Implements the Twitch implicit grant flow using a JAX-RS webserver with no coupling to the underlying implementations.
 */
@Typed(TwitchAuthService.class)
@Dependent
public class TwitchAuthJakartaWebServerService implements TwitchAuthService, AutoCloseable {

	private static final Logger LOGGER = LoggerFactory.getLogger(TwitchAuthJakartaWebServerService.class);

	private final String host = "http://localhost";
	private final int port = 8080;
	private final String rootApplicationPath = "twitch-tag-carousel";

	private final int serverStopDelaySeconds = 0;

	private HttpServer server;
	private CountDownLatch browserSignalCountDownLatch;
	private String accessToken;
	
	@Inject
	@Property("twitch-app.auth-uri.implicit-grant-flow")
	private String implicitGrantFlowUriString;

	/**
	 * Registers the access token and sends signal to async processes.
	 */
	public void receiveAccessToken(final String accessToken) {
		this.accessToken = accessToken;
		browserSignalCountDownLatch.countDown();
	}

	/**
	 * Starts a webserver, waits for the token, registers it, executes the callback, then shuts down.
	 *
	 * @param onAccessTokenReceivedCallback to be executed when the access token has been received.
	 */
	@Override
	public CompletableFuture<Void> asyncQueryAccessTokenWithImplicitGrantFlow(final Consumer<String> onAccessTokenReceivedCallback) {
		final Executor executorService = Executors.newCachedThreadPool();
		return supplyAsync(this::startWebServer, executorService)
				.thenRun(this::openBrowserToAuthPage)
				.thenCompose(i -> handleBrowserCallbackSignal(onAccessTokenReceivedCallback)) // compose waits for completion
				.thenRun(this::close);
	}

	private CompletableFuture<Void> handleBrowserCallbackSignal(Consumer<String> onAccessTokenReceivedCallBack)
	throws TwitchAuthTokenQueryException {
		final ExecutorService executorService = Executors.newCachedThreadPool();
		return CompletableFuture
				.supplyAsync(this::waitForBrowserSignal, executorService)
				.thenAccept(toIgnore -> onAccessTokenReceivedCallBack.accept(accessToken));
	}

	/**
	 * When the browser receives the access token, the web resource should decrement the countdown latch to zero.
	 */
	private String waitForBrowserSignal() {
		browserSignalCountDownLatch = new CountDownLatch(1);
		try {
			browserSignalCountDownLatch.await();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		return "OK";
	}

	private HttpServer startWebServer() {
		final URI targetUri = UriBuilder.fromUri(("%s/" + rootApplicationPath + "/").formatted(host)).port(port).build();
		try {
			server = HttpServer.create(new InetSocketAddress(targetUri.getPort()), 0);
		} catch (IOException e) {
			throw new TwitchAuthTokenQueryException("Failed to start auth token server", e);
		}
		Runtime.getRuntime().addShutdownHook(new Thread(() -> server.stop(0)));
		registerTwitchCallbackJaxRsApplication(targetUri);
		server.start();
		return server;
	}

	private void registerTwitchCallbackJaxRsApplication(final URI targetUri) {
		final Application jaxRsApplication = new TwitchAuthResource(this);
		HttpHandler handler = RuntimeDelegate.getInstance().createEndpoint(jaxRsApplication, HttpHandler.class);
		server.createContext(targetUri.getPath(), handler);
	}

	/**
	 * Open a web page to a specified URL.
	 */
	private void openBrowserToAuthPage() {
		try {
			Desktop.getDesktop().browse(URI.create(implicitGrantFlowUriString));
		} catch (Exception ex) {
			LOGGER.warn("Failed to open web page: " + ex.getMessage());
		}
	}

	/**
	 * Close the webserver.
	 */
	@Override
	public void close() {
		LOGGER.debug("Closing server.");
		server.stop(serverStopDelaySeconds);
	}

	public CountDownLatch getBrowserSignalCountDownLatch() {
		return browserSignalCountDownLatch;
	}
}
