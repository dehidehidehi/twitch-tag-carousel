package com.dehidehidehi.twitchtagcarousel;

import com.dehidehidehi.twitchtagcarousel.annotation.Property;
import com.dehidehidehi.twitchtagcarousel.service.TagRotatorService;
import com.dehidehidehi.twitchtagcarousel.service.TwitchTagService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Orchestrates application.
 */
@ApplicationScoped
public class CommandCenter {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommandCenter.class);
	private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

	@Property("twitch-app.start-delay-seconds")
	@Inject
	private int startDelaySeconds;
	private final TwitchTagService twitchTagService;

	private final TagRotatorService tagRotatorService;
	private final CarouselUi carouselUi;
	@Property("twitch-app.tag-rotation-frequency-seconds")
	@Inject
	private int tagRotationFrequencySeconds;

	@Inject
	public CommandCenter(final TagRotatorService tagRotatorService,
								final TwitchTagService twitchTagService,
								final CarouselUi carouselUi) {
		this.tagRotatorService = tagRotatorService;
		this.twitchTagService = twitchTagService;
		this.carouselUi = carouselUi;
	}

	public void startUi() {
		carouselUi.start(twitchTagService);
	}

	/**
	 * Executes tag updater at a specified frequency.
	 */
	public void startUpdatingTags() {
		LOGGER.debug("Scheduling execution of app every {} {}.", tagRotationFrequencySeconds, TIME_UNIT);
		LOGGER.info("First execution of Twitch Tag rotator will start in {} {}", startDelaySeconds, TIME_UNIT);
		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
		executorService.scheduleAtFixedRate(tagRotatorService::updateTags,
														startDelaySeconds,
														tagRotationFrequencySeconds,
														TIME_UNIT);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			LOGGER.debug("Shutting down executor service.");
			executorService.shutdown();
		}));

	}

	String getBanner() {
		return """
				 _____              ____                                    _\s
				|_   _|_ _  __ _   / ___|__ _ _ __ _ __ ___  _   _ ___  ___| |
				  | |/ _` |/ _` | | |   / _` | '__| '__/ _ \\| | | / __|/ _ \\ |
				  | | (_| | (_| | | |__| (_| | |  | | | (_) | |_| \\__ \\  __/ |
				  |_|\\__,_|\\__, |  \\____\\__,_|_|  |_|  \\___/ \\__,_|___/\\___|_|
				           |___/""";
	}
}
