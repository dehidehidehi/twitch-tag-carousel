package com.dehidehidehi.twitchtagcarousel.service.twitch;
import com.dehidehidehi.twitchtagcarousel.domain.TwitchTagBatch;
import com.dehidehidehi.twitchtagcarousel.error.*;

public interface TwitchApiService {

    // TODO move this to AUTH module
    boolean isUserAccessTokenValid(String userAccessToken) throws AuthTokenQueryException;

    // TODO move this to AUTH module
    String getBroadcasterIdOf() throws TwitchChannelIdException, MissingAuthTokenException;

    void updateTags(TwitchTagBatch tags) throws MissingUserProvidedTagsException, TwitchTagUpdateException, MissingAuthTokenException;

    /**
     * Queries the user access token through the implementations' choice of authentication flow.
     */
    void queryUserAccessToken() throws AuthTokenQueryException;
}
