package com.dehidehidehi.twitchtagcarousel;

import com.dehidehidehi.twitchtagcarousel.service.StartService;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public class TagRotatorApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(TagRotatorApplication.class);

    /**
     * Instantiates CDI container then delegates to {@link StartService#startUpdateTagExecutorService()}.
     */
    public static void main(String[] args) throws InterruptedException {
        Supplier<SeContainer> cdiContainer = () -> SeContainerInitializer
                .newInstance()
                .initialize();
        try (SeContainer container = cdiContainer.get()) {
            LOGGER.debug("CDI container started successfully.");
            final StartService startService = container.select(StartService.class).get();
            startService.startUiLayer();
            Thread.sleep(Long.MAX_VALUE);
        }
    }
}
