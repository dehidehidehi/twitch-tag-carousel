package com.dehidehidehi.twitchtagcarousel;

import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainApplication.class);

    /**
     * Instantiates CDI container then calls methods from the {@link CommandCenter} class.
     */
    public static void main(String[] args) throws InterruptedException {
        try (SeContainer container = SeContainerInitializer.newInstance().initialize()) {
            LOGGER.debug("CDI container started successfully.");
            final CommandCenter commandCenter = container.select(CommandCenter.class).get();
            commandCenter.startUi();
            LOGGER.info(commandCenter.getBanner());
            commandCenter.startUpdatingTags();
            Thread.sleep(Long.MAX_VALUE);
        }
    }
}
