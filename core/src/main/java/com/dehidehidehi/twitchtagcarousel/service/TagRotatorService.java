package com.dehidehidehi.twitchtagcarousel.service;

import com.dehidehidehi.twitchtagcarousel.annotation.Property;
import com.dehidehidehi.twitchtagcarousel.domain.TwitchTagBatch;
import com.dehidehidehi.twitchtagcarousel.service.twitch.TwitchService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.dehidehidehi.twitchtagcarousel.domain.TwitchTagBatch.MAX_NB_TAGS_PER_CHANNEL;

@ApplicationScoped
public class TagRotatorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TagRotatorService.class);

    private final TwitchService twitchService;

    @Inject
    @Property("tag-carousel.mandatory-tags")
    private String mandatoryTagsString;

    @Inject
    @Property("twitch-app.user-access-token")
    private String userAccessToken;

    @Inject
    @Property("tag-carousel.tags")
    private String concatenatedTags;

    @Inject
    @Property("twitch-app.tag-rotation-frequency-seconds")
    private int tagRotationFrequencySeconds;

    private List<String> tagsToRotate;

    @Inject
    public TagRotatorService(final TwitchService twitchService) {
        this.twitchService = twitchService;
    }

    @PostConstruct
    private void parseThenShuffleTagsToRotate() {
        final String[] mandatoryTags = mandatoryTagsString.split(",");
        final String[] tags = concatenatedTags.split(",");
        final Stream<String> combinedTagsStream = Stream.concat(Arrays.stream(mandatoryTags), Arrays.stream(tags));
        tagsToRotate = combinedTagsStream
                             .map(String::trim)
                             .collect(Collectors.toUnmodifiableSet())
                             .stream()
                             .toList();
    }

    public void updateTags() {
        LOGGER.debug("Entered in updating tags method.");
        try {
            final Set<String> mandatoryTags = getMandatoryTags();
            final TwitchTagBatch tags = selectNewTags(mandatoryTags);
            twitchService.updateTags(tags);
            LOGGER.info("Updated stream tags with: {}", tags.get().stream().sorted().toList());
            LOGGER.info("Next update at {}", getNextExecutionTime());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            System.exit(1);
        }
    }

    /**
     * Selects a new batch of tags, rotates tag selection at each invocation.
     */
    TwitchTagBatch selectNewTags(Set<String> mandatoryTags) {
        final Set<String> toReturn = tagsToRotate
                .stream()
                .limit(MAX_NB_TAGS_PER_CHANNEL - mandatoryTags.size())
                .collect(Collectors.toSet());
        moveTagsToEndOfTheList(toReturn);
        toReturn.addAll(mandatoryTags);
        LOGGER.debug("Next tag batch will be: {}", toReturn.stream().sorted().toList());
        return new TwitchTagBatch(toReturn);
    }

    private Set<String> getMandatoryTags() {
        return Arrays
                .stream(this.mandatoryTagsString.split(","))
                .map(String::trim)
                .collect(Collectors.toUnmodifiableSet());
    }

    private void moveTagsToEndOfTheList(final Set<String> toReturn) {
        tagsToRotate = tagsToRotate
                .stream()
                .filter(t -> !toReturn.contains(t))
                .collect(Collectors.toList());
        tagsToRotate.addAll(toReturn);
        LOGGER.debug("Moved {} to the end of tag queue.", toReturn);
        LOGGER.trace("Current queue: {}", tagsToRotate);
    }

    List<String> getTagsToRotate() {
        return tagsToRotate;
    }

    private String getNextExecutionTime() {
        return LocalDateTime
                .now()
                .plus(Duration.ofSeconds(tagRotationFrequencySeconds))
                .format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    TwitchTagBatch selectNewTags() {
        return selectNewTags(Collections.emptySet());
    }
}
