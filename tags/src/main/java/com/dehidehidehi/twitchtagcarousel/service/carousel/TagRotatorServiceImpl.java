package com.dehidehidehi.twitchtagcarousel.service.carousel;

import com.dehidehidehi.twitchtagcarousel.dao.UserPropertiesDao;
import com.dehidehidehi.twitchtagcarousel.domain.TwitchTag;
import com.dehidehidehi.twitchtagcarousel.domain.TwitchTagBatch;
import com.dehidehidehi.twitchtagcarousel.error.MissingAuthTokenException;
import com.dehidehidehi.twitchtagcarousel.error.MissingUserProvidedTagsException;
import com.dehidehidehi.twitchtagcarousel.error.TwitchTagUpdateException;
import com.dehidehidehi.twitchtagcarousel.error.TwitchTagValidationException;
import com.dehidehidehi.twitchtagcarousel.service.TagCarouselService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static com.dehidehidehi.twitchtagcarousel.dao.UserPropertiesDao.PROPERTY_KEY_TAG_ROTATION_FREQUENCY_SECONDS;
import static com.dehidehidehi.twitchtagcarousel.domain.TwitchTagBatch.MAX_NB_TAGS_PER_CHANNEL;


@Dependent
public class TagRotatorServiceImpl implements TagRotatorService {

    public static final long DEFAULT_ROTATION_FREQUENCY_SECONDS = 300L;
    private static final Logger LOGGER = LoggerFactory.getLogger(TagRotatorServiceImpl.class);
    private final TagCarouselService tagCarouselService;
    private final UserPropertiesDao userPropertiesDao;

    @Inject
    public TagRotatorServiceImpl(final TagCarouselService tagCarouselService, final UserPropertiesDao userPropertiesDao) {
        this.tagCarouselService = tagCarouselService;
        this.userPropertiesDao = userPropertiesDao;
    }

    public TagCarouselService getTagCarouselService() {
        return tagCarouselService;
    }

    @PostConstruct
    private void init() {
        try {
            shuffleTagsToRotate();
        } catch (TwitchTagValidationException e) {
            throw new RuntimeException(e);
        }
    }

    private void shuffleTagsToRotate() throws TwitchTagValidationException {
        final List<TwitchTag> rotatingTags = userPropertiesDao.getRotatingTags();
        final List<TwitchTag> shuffledRotatingTags = new ArrayList<>(new HashSet<>(rotatingTags));
        userPropertiesDao.saveRotatingTags(shuffledRotatingTags);
    }

    @Override
    public void updateTags()
    throws MissingUserProvidedTagsException, MissingAuthTokenException, TwitchTagUpdateException, TwitchTagValidationException {
        LOGGER.debug("Entered in updating tags method.");
        final TwitchTagBatch tags = selectNewTags();
        tagCarouselService.updateTags(tags);
        LOGGER.info("Updated stream tags with: {}", tags.get().stream().sorted().toList());
        LOGGER.info("Next update at {}", getNextExecutionTime());
    }

    /**
     * Selects a new batch of tags, rotates tag selection at each invocation.
     */
    TwitchTagBatch selectNewTags() throws TwitchTagValidationException {
        // If too many mandatory tags
        final List<TwitchTag> mandatoryTags = userPropertiesDao.getMandatoryTags();
        final List<TwitchTag> tagBatch = new ArrayList<>(mandatoryTags);
        if (tagBatch.size() >= MAX_NB_TAGS_PER_CHANNEL) {
            final List<TwitchTag> onlyMandatoryTags = tagBatch.stream().limit(MAX_NB_TAGS_PER_CHANNEL).toList();
            return new TwitchTagBatch(onlyMandatoryTags);
        }

        // If user has set tags to rotate
        final List<TwitchTag> rotatingTags = userPropertiesDao.getRotatingTags();
        tagBatch.addAll(rotatingTags);
        final List<TwitchTag> rotatingTagsToUpdate = tagBatch
                .stream()
                .skip(mandatoryTags.size())
                .limit(MAX_NB_TAGS_PER_CHANNEL - mandatoryTags.size())
                .toList();

        // Create batch to send to twitch
        final List<TwitchTag> tagsToSendToUpdateMethod = new ArrayList<>(10);
        tagsToSendToUpdateMethod.addAll(mandatoryTags);
        tagsToSendToUpdateMethod.addAll(rotatingTagsToUpdate);
        final TwitchTagBatch batchToUpdate = new TwitchTagBatch(tagsToSendToUpdateMethod);

        // Rotate tags to rotate.
        Collections.rotate(rotatingTags, rotatingTagsToUpdate.size());
        userPropertiesDao.saveRotatingTags(rotatingTags);
        LOGGER.debug("Moved {} to the end of tag queue.", rotatingTags);
        LOGGER.debug("Next tag batch will be: {}", batchToUpdate);
        return batchToUpdate;
    }

    private String getNextExecutionTime() {
        final String frequencySecondsFromProperties = userPropertiesDao.readUserProperty(
                PROPERTY_KEY_TAG_ROTATION_FREQUENCY_SECONDS);
        final long rotationFrequency = frequencySecondsFromProperties == null
                                       ? DEFAULT_ROTATION_FREQUENCY_SECONDS
                                       : Long.parseLong(frequencySecondsFromProperties);
        final Duration amountToAddInSeconds = Duration.ofSeconds(rotationFrequency);
        final DateTimeFormatter localTime = DateTimeFormatter.ofPattern("HH:mm:ss");
        return LocalDateTime.now().plus(amountToAddInSeconds).format(localTime);
    }
}
