package com.dehidehidehi.twitchtagcarousel.dao;
import com.dehidehidehi.twitchtagcarousel.domain.TwitchTag;

import java.util.List;

public interface UserPropertiesDao extends AutoCloseable {
    
    String PROPERTY_KEY_TAG_ROTATION_FREQUENCY_SECONDS = "twitch-app.tag-rotation-frequency-seconds";
    String PROPERTY_MANDATORY_TAGS = "tags.mandatory";
    String PROPERTY_ROTATING_TAGS = "tags.rotating";
    
    String readUserProperty(String propertyKey);

    /**
     * Overwrites mandatory tags.
     */
    void saveMandatoryTags(List<TwitchTag> tags);

    /**
     * Lists mandatory tags.
     */
    List<TwitchTag> getMandatoryTags();
    
    int countMandatoryTags();

    /**
     * Overwrites rotating tags.
     */
    void saveRotatingTags(List<TwitchTag> tags);

    /**
     * Lists rotating tags.
     */
    List<TwitchTag> getRotatingTags();
    
    int countRotatingTags();
}
