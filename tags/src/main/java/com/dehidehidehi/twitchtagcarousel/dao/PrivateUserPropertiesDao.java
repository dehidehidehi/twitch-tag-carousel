package com.dehidehidehi.twitchtagcarousel.dao;
import com.dehidehidehi.twitchtagcarousel.error.TwitchMissingAuthTokenException;

public interface PrivateUserPropertiesDao {
    
    String getUserAccessToken() throws TwitchMissingAuthTokenException;

    void setUserAccessToken(final String userAccessToken);

}
