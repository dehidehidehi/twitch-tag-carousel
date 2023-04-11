package com.dehidehidehi.twitchtagcarousel.service.carousel;
import com.dehidehidehi.twitchtagcarousel.error.MissingAuthTokenException;
import com.dehidehidehi.twitchtagcarousel.error.MissingUserProvidedTagsException;
import com.dehidehidehi.twitchtagcarousel.error.TwitchTagUpdateException;
import com.dehidehidehi.twitchtagcarousel.error.TwitchTagValidationException;

public interface TagRotatorService {

    void updateTags()
    throws MissingUserProvidedTagsException, MissingAuthTokenException, TwitchTagUpdateException, TwitchTagValidationException;
}
