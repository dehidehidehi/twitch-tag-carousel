package com.dehidehidehi.twitchtagcarousel.service.twitchclient;
import com.dehidehidehi.twitchtagcarousel.domain.TwitchTagBatch;
import com.dehidehidehi.twitchtagcarousel.error.TwitchChannelIdException;
import com.dehidehidehi.twitchtagcarousel.error.TwitchTagUpdateException;
import com.dehidehidehi.twitchtagcarousel.error.TwitchUserAccessTokenException;
import com.dehidehidehi.twitchtagcarousel.error.WebServerStartException;
import com.sun.net.httpserver.HttpServer;

public interface TwitchClient {
    
    boolean isUserAccessTokenValid(String userAccessToken) throws TwitchUserAccessTokenException;
    
    String getUserAccessToken();
    
    String getChannelIdFrom(String channelName) throws TwitchChannelIdException;
    
    void updateTags(TwitchTagBatch tags) throws TwitchTagUpdateException;

    HttpServer startAuthServer() throws WebServerStartException;

    void stopAuthServer(HttpServer httpServer);

    void setAccessToken(String safeAccessToken);
}
