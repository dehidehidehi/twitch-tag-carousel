package com.dehidehidehi.twitchtagcarousel.dao;
import com.dehidehidehi.twitchtagcarousel.domain.TwitchTag;

import java.util.List;

public interface UserPropertiesDao {

    /**
     * Overwrites mandatory tags.
     */
    void saveMandatoryTags(List<TwitchTag> tags);

    /**
     * Lists mandatory tags.
     */
    List<TwitchTag> getMandatoryTags();

    /**
     * Overwrites rotating tags.
     */
    void saveRotatingTags(List<TwitchTag> tags);

    /**
     * Lists rotating tags.
     */
    List<TwitchTag> getRotatingTags();
}
