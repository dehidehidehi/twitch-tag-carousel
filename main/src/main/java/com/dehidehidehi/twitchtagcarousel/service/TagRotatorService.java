package com.dehidehidehi.twitchtagcarousel.service;

import com.dehidehidehi.twitchtagcarousel.dao.UserPropertiesDao;
import com.dehidehidehi.twitchtagcarousel.domain.TwitchTag;
import com.dehidehidehi.twitchtagcarousel.domain.TwitchTagBatch;
import com.dehidehidehi.twitchtagcarousel.error.MissingAuthTokenException;
import com.dehidehidehi.twitchtagcarousel.error.MissingUserProvidedTagsException;
import com.dehidehidehi.twitchtagcarousel.error.TwitchTagUpdateException;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.dehidehidehi.twitchtagcarousel.dao.UserPropertiesDao.PROPERTY_KEY_TAG_ROTATION_FREQUENCY_SECONDS;
import static com.dehidehidehi.twitchtagcarousel.domain.TwitchTagBatch.MAX_NB_TAGS_PER_CHANNEL;


@Dependent
public class TagRotatorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TagRotatorService.class);

    private final TagCarouselService tagCarouselService;
    private final UserPropertiesDao userPropertiesDao;

    @Inject
    public TagRotatorService(final TagCarouselService tagCarouselService, final UserPropertiesDao userPropertiesDao) {
        this.tagCarouselService = tagCarouselService;
        this.userPropertiesDao = userPropertiesDao;
    }

    public TagCarouselService getTagCarouselService() {
        return tagCarouselService;
    }
    
    @PostConstruct
    private void init() {
        shuffleTagsToRotate();
    }

    private void shuffleTagsToRotate() {
        final List<TwitchTag> rotatingTags = userPropertiesDao.getRotatingTags();
        final List<TwitchTag> shuffledRotatingTags = new ArrayList<>(new HashSet<>(rotatingTags));
        userPropertiesDao.saveRotatingTags(shuffledRotatingTags);
    }

    public void updateTags() throws MissingUserProvidedTagsException, MissingAuthTokenException, TwitchTagUpdateException {
        LOGGER.debug("Entered in updating tags method.");
        final TwitchTagBatch tags = selectNewTags();
        tagCarouselService.updateTags(tags);
        LOGGER.info("Updated stream tags with: {}", tags.get().stream().sorted().toList());
        LOGGER.info("Next update at {}", getNextExecutionTime());
    }

    /**
     * Selects a new batch of tags, rotates tag selection at each invocation.
     */
    TwitchTagBatch selectNewTags() {
        final List<TwitchTag> tagBatch = new ArrayList<>();
        tagBatch.addAll(userPropertiesDao.getMandatoryTags());
        tagBatch.addAll(userPropertiesDao.getRotatingTags());
        final List<TwitchTag> rotatingTags = tagBatch.subList(userPropertiesDao.getMandatoryTags().size(), tagBatch.size());
        Collections.rotate(rotatingTags, 1);
        userPropertiesDao.saveRotatingTags(rotatingTags);
        LOGGER.debug("Moved {} to the end of tag queue.", rotatingTags);
        final List<TwitchTag> limitedTagBatch = tagBatch.stream().limit(MAX_NB_TAGS_PER_CHANNEL).collect(Collectors.toList());
        LOGGER.debug("Next tag batch will be: {}", limitedTagBatch.stream().sorted().toList());
        return new TwitchTagBatch(limitedTagBatch);
    }

    private String getNextExecutionTime() {
        final String rotationFrequency = userPropertiesDao.readUserProperty(PROPERTY_KEY_TAG_ROTATION_FREQUENCY_SECONDS);
        final Duration amountToAddInSeconds = Duration.ofSeconds(Long.parseLong(rotationFrequency));
        final DateTimeFormatter localTime = DateTimeFormatter.ofPattern("HH:mm:ss");
        return LocalDateTime.now().plus(amountToAddInSeconds).format(localTime);
    }
}
