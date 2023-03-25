package com.dehidehidehi.twitchtagcarousel;

import com.dehidehidehi.twitchtagcarousel.domain.TwitchTagBatch;
import com.dehidehidehi.twitchtagcarousel.service.TagRotatorService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class TagRotatorApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(TagRotatorApplication.class);

    private static final int INITIAL_DELAY = 10;
    private static final int PERIOD = 30;
    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

    private final TagRotatorService tagRotatorService;

    @Inject
    public TagRotatorApplication(final TagRotatorService tagRotatorService) {
        this.tagRotatorService = tagRotatorService;
    }

    /**
     * Instantiates CDI then delegates to {@link #start()}.
     */
    public static void main(String[] args) {
        try (SeContainer container = SeContainerInitializer.newInstance().initialize()) {
            LOGGER.debug("CDI container started successfully.");
            final TagRotatorApplication tagRotatorApplication = container.select(TagRotatorApplication.class).get();
            tagRotatorApplication.start();
        }
    }

    /**
     * Executes tag rotator at a specific frequency.
     */
    void start() {
        final Runnable runnable = () -> {
            final TwitchTagBatch tags = tagRotatorService.selectTags();
            tagRotatorService.updateTags(tags);
        };
        final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        LOGGER.debug("Scheduling execution of app every {} {}.", PERIOD, TIME_UNIT);
        LOGGER.info("First execution of Twitch Tag rotator will start in {} {}", INITIAL_DELAY, TIME_UNIT);
        scheduledExecutorService.scheduleAtFixedRate(runnable, INITIAL_DELAY, PERIOD, TIME_UNIT);
        while (true) {
            // no-op
        }
    }
}
