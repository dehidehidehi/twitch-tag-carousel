package com.dehidehidehi.twitchtagcarousel.service;
import com.dehidehidehi.twitchtagcarousel.dao.PrivateUserPropertiesDao;
import com.dehidehidehi.twitchtagcarousel.dao.UserPropertiesDao;
import com.dehidehidehi.twitchtagcarousel.service.carousel.TagRotatorService;
import com.dehidehidehi.twitchtagcarousel.service.twitch.TwitchApiService;

/**
 * Represents operations with Twitch.tv which are useful for the Tag Carrousel.
 */
public interface TagCarouselService extends TagRotatorService, TwitchApiService, PrivateUserPropertiesDao, UserPropertiesDao {
}
