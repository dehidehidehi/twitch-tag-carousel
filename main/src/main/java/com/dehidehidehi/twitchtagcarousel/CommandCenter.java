package com.dehidehidehi.twitchtagcarousel;

import com.dehidehidehi.twitchtagcarousel.annotation.Property;
import com.dehidehidehi.twitchtagcarousel.service.TagCarouselService;
import com.dehidehidehi.twitchtagcarousel.service.TagRotatorService;
import com.dehidehidehi.twitchtagcarousel.ui.BannerUi;
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
class CommandCenter {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommandCenter.class);
	private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

	@Property("twitch-app.start-delay-seconds")
	@Inject
	private int startDelaySeconds;

	private final TagRotatorService tagRotatorService;
	private final CarouselUi carouselUi;
	@Property("twitch-app.tag-rotation-frequency-seconds")
	@Inject
	private int tagRotationFrequencySeconds;

	@Inject
	CommandCenter(final TagRotatorService tagRotatorService,
					  final CarouselUi carouselUi) {
		this.tagRotatorService = tagRotatorService;
		this.carouselUi = carouselUi;
	}

	void startUi() {
		carouselUi.start(tagRotatorService.getTagCarouselService());
	}

	/**
	 * Executes tag updater at a specified frequency.
	 */
	void startUpdatingTags() {
		LOGGER.info(BannerUi.getBanner());
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


}
