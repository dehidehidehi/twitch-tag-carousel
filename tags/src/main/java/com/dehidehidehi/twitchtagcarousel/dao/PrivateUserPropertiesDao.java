package com.dehidehidehi.twitchtagcarousel.dao;
import com.dehidehidehi.twitchtagcarousel.error.MissingAuthTokenException;

public interface PrivateUserPropertiesDao extends AutoCloseable {
    
    String getUserAccessToken() throws MissingAuthTokenException;

    void setUserAccessToken(final String userAccessToken);

}
