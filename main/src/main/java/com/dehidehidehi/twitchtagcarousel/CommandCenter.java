package com.dehidehidehi.twitchtagcarousel;

import com.dehidehidehi.twitchtagcarousel.annotation.qualifier.ApplicationProperty;
import com.dehidehidehi.twitchtagcarousel.dao.UserPropertiesDao;
import com.dehidehidehi.twitchtagcarousel.error.MissingAuthTokenException;
import com.dehidehidehi.twitchtagcarousel.error.MissingUserProvidedTagsException;
import com.dehidehidehi.twitchtagcarousel.error.TwitchTagUpdateException;
import com.dehidehidehi.twitchtagcarousel.error.TwitchTagValidationException;
import com.dehidehidehi.twitchtagcarousel.service.carousel.TagRotatorServiceImpl;
import com.dehidehidehi.twitchtagcarousel.ui.BannerUi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.dehidehidehi.twitchtagcarousel.dao.UserPropertiesDao.PROPERTY_KEY_TAG_ROTATION_FREQUENCY_SECONDS;

/**
 * Orchestrates application.
 */
@ApplicationScoped
class CommandCenter {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommandCenter.class);
	private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

	@ApplicationProperty("twitch-app.start-delay-seconds")
	@Inject
	private int startDelaySeconds;

	private final TagRotatorServiceImpl tagRotatorServiceImpl;
	private final CarouselUi carouselUi;
	private final UserPropertiesDao userPropertiesDao;

	@Inject
	CommandCenter(final TagRotatorServiceImpl tagRotatorServiceImpl,
					  final CarouselUi carouselUi, final UserPropertiesDao userPropertiesDao) {
		this.tagRotatorServiceImpl = tagRotatorServiceImpl;
		this.carouselUi = carouselUi;
		this.userPropertiesDao = userPropertiesDao;
	}

	void startUiMode() {
		carouselUi.start(tagRotatorServiceImpl.getTagCarouselService());
	}

	/**
	 * Executes tag updater at a specified frequency.
	 */
	void startCliMode() {
		LOGGER.info(BannerUi.getBanner());
		final long tagRotationFrequencySeconds = Long.parseLong(userPropertiesDao.readUserProperty(PROPERTY_KEY_TAG_ROTATION_FREQUENCY_SECONDS));
		LOGGER.debug("Scheduling execution of app every {} {}.", tagRotationFrequencySeconds, TIME_UNIT);
		LOGGER.info("First execution of Twitch Tag rotator will start in {} {}", startDelaySeconds, TIME_UNIT);
		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
		Runnable startUpdatingTags = () -> {
			try {
				tagRotatorServiceImpl.updateTags();
			} catch (MissingUserProvidedTagsException | MissingAuthTokenException | TwitchTagUpdateException |
						TwitchTagValidationException e) {
				throw new RuntimeException(e);
			}
		};
		executorService.scheduleAtFixedRate(startUpdatingTags,
														startDelaySeconds,
														tagRotationFrequencySeconds,
														TIME_UNIT);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			LOGGER.debug("Shutting down executor service.");
			executorService.shutdown();
		}));
	}
}
