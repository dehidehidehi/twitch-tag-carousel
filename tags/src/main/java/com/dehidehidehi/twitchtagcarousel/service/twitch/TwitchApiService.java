package com.dehidehidehi.twitchtagcarousel.service.twitch;
import com.dehidehidehi.twitchtagcarousel.domain.TwitchTagBatch;
import com.dehidehidehi.twitchtagcarousel.error.TwitchAuthTokenQueryException;
import com.dehidehidehi.twitchtagcarousel.error.TwitchChannelIdException;
import com.dehidehidehi.twitchtagcarousel.error.TwitchMissingAuthTokenException;
import com.dehidehidehi.twitchtagcarousel.error.TwitchTagUpdateException;

public interface TwitchApiService {

    // TODO move this to AUTH module
    boolean isUserAccessTokenValid(String userAccessToken) throws TwitchAuthTokenQueryException;

    // TODO move this to AUTH module
    String getChannelIdFrom(String channelName) throws TwitchChannelIdException, TwitchMissingAuthTokenException;

    void updateTags(TwitchTagBatch tags) throws TwitchTagUpdateException, TwitchMissingAuthTokenException;

    /**
     * Queries the user access token through the implementations' choice of authentication flow.
     */
    void queryUserAccessToken() throws TwitchAuthTokenQueryException;
}
