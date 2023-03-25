package com.dehidehidehi.twitchtagcarousel;

import com.dehidehidehi.twitchtagcarousel.domain.TwitchTagEnum;
import com.dehidehidehi.twitchtagcarousel.service.TagRotatorService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import jakarta.inject.Inject;

import java.util.Set;

@ApplicationScoped
public class TagRotatorApplication {

    private final TagRotatorService tagRotatorService;

    @Inject
    public TagRotatorApplication(final TagRotatorService tagRotatorService) {
        this.tagRotatorService = tagRotatorService;
    }

    void start() {
        final Set<TwitchTagEnum> tags = tagRotatorService.selectTags();
        tagRotatorService.updateTags(tags);
    }

    public static void main(String[] args) {
        try (SeContainer container = SeContainerInitializer.newInstance().initialize()) {
            final TagRotatorApplication tagRotatorApplication = container.select(TagRotatorApplication.class).get();
            tagRotatorApplication.start();
        }
    }

}
