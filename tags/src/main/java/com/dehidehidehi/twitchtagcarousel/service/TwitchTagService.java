package com.dehidehidehi.twitchtagcarousel.service;
import com.dehidehidehi.twitchtagcarousel.domain.TwitchTagBatch;
import com.dehidehidehi.twitchtagcarousel.error.TwitchAuthTokenQueryException;
import com.dehidehidehi.twitchtagcarousel.error.TwitchChannelIdException;
import com.dehidehidehi.twitchtagcarousel.error.TwitchMissingAuthTokenException;
import com.dehidehidehi.twitchtagcarousel.error.TwitchTagUpdateException;

/**
 * Represents operations with Twitch.tv which are useful for the Tag Carrousel.
 */
public interface TwitchTagService {
    
    // TODO move this to AUTH module
    boolean isUserAccessTokenValid(String userAccessToken) throws TwitchAuthTokenQueryException;

    // TODO move this to AUTH module
    String getChannelIdFrom(String channelName) throws TwitchChannelIdException;
    
    void updateTags(TwitchTagBatch tags) throws TwitchTagUpdateException;

    /**
     * Queries the user access token through the implementations' choice of authentication flow.<br>
     * Should register it somewhere accessible for {@link #getUserAccessToken()}
     */
    void queryUserAccessToken() throws TwitchAuthTokenQueryException;
    
    String getUserAccessToken() throws TwitchMissingAuthTokenException;
    
    void setUserAccessToken(final String userAccessToken);

}
