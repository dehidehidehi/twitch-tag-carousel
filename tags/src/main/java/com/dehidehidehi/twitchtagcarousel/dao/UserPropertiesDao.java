package com.dehidehidehi.twitchtagcarousel.dao;
import com.dehidehidehi.twitchtagcarousel.domain.TwitchTag;
import com.dehidehidehi.twitchtagcarousel.error.MissingAuthTokenException;
import com.dehidehidehi.twitchtagcarousel.error.TwitchTagValidationException;

import java.util.List;

public interface UserPropertiesDao extends AutoCloseable {

    String PROPERTY_KEY_TAG_ROTATION_FREQUENCY_SECONDS = "twitch-app.tag-rotation-frequency-seconds";
    String PROPERTY_MANDATORY_TAGS = "tags.mandatory";
    String PROPERTY_ROTATING_TAGS = "tags.rotating";
    String PROPERTY_TWITCH_ACCESS_TOKEN = "twitch.access-token";

    String readUserProperty(String propertyKey);

    /**
     * Overwrites mandatory tags.
     */
    void saveMandatoryTags(List<TwitchTag> tags) throws TwitchTagValidationException;

    /**
     * Lists mandatory tags.
     */
    List<TwitchTag> getMandatoryTags();

    int countMandatoryTags();

    /**
     * Overwrites rotating tags.
     */
    void saveRotatingTags(List<TwitchTag> tags) throws TwitchTagValidationException;

    /**
     * Lists rotating tags.
     */
    List<TwitchTag> getRotatingTags();

    int countRotatingTags();

    String getUserAccessToken() throws MissingAuthTokenException;

    void setUserAccessToken(final String userAccessToken);
}
