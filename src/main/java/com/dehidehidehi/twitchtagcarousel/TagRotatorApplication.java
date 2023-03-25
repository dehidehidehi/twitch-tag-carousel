package com.dehidehidehi.twitchtagcarousel;

import com.dehidehidehi.twitchtagcarousel.service.StartupService;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TagRotatorApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(TagRotatorApplication.class);

    /**
     * Instantiates CDI container then delegates to {@link StartupService#start()}.
     */
    public static void main(String[] args) throws InterruptedException {
        try (SeContainer container = SeContainerInitializer.newInstance().initialize()) {
            LOGGER.debug("CDI container started successfully.");
            final StartupService startupService = container.select(StartupService.class).get();
            startupService.start();
            Thread.sleep(Long.MAX_VALUE);
        }
    }
}
