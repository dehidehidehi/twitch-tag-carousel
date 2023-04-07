package com.dehidehidehi.twitchtagcarousel.dao;
import com.dehidehidehi.twitchtagcarousel.domain.TwitchTag;

import java.util.List;

public interface UserPropertiesDao {

    /**
     * Overwrites all tags.
     */
    void saveAllTags(List<TwitchTag> tags);

    /**
     * Lists all tags.
     */
    List<TwitchTag> getAllTags();
    
}
