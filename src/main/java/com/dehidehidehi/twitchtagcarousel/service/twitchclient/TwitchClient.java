package com.dehidehidehi.twitchtagcarousel.service.twitchclient;
import com.dehidehidehi.twitchtagcarousel.domain.TwitchTagBatch;
import com.dehidehidehi.twitchtagcarousel.error.TwitchChannelIdException;
import com.dehidehidehi.twitchtagcarousel.error.TwitchTagUpdateException;
import com.dehidehidehi.twitchtagcarousel.error.TwitchUserAccessTokenException;

public interface TwitchClient {
    
    boolean isUserAccessTokenValid(String userAccessToken) throws TwitchUserAccessTokenException;
    
    String getChannelIdFrom(String channelName) throws TwitchChannelIdException;
    
    void updateTags(TwitchTagBatch tags) throws TwitchTagUpdateException;
    
}
