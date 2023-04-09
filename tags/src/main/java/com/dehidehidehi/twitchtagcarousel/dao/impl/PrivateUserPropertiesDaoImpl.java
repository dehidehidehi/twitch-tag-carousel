package com.dehidehidehi.twitchtagcarousel.dao.impl;

import com.dehidehidehi.twitchtagcarousel.dao.PrivateUserPropertiesDao;
import com.dehidehidehi.twitchtagcarousel.error.MissingAuthTokenException;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Typed;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Typed(PrivateUserPropertiesDao.class)
@ApplicationScoped
class PrivateUserPropertiesDaoImpl extends PropertiesDaoUtil implements PrivateUserPropertiesDao {

    static final String PRIVATE_USER_PROPERTIES_FILE = "/private-user.properties";
    static final String PROPERTY_TWITCH_ACCESS_TOKEN = "twitch.access-token";
    private static final Logger LOGGER = LoggerFactory.getLogger(PrivateUserPropertiesDaoImpl.class);
    private Properties properties;
    private InputStream propertiesInputStream;

    @PostConstruct
    void init() {
        properties = new Properties();
        propertiesInputStream = getClass().getResourceAsStream(PRIVATE_USER_PROPERTIES_FILE);
        try {
            properties.load(propertiesInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getUserAccessToken() throws MissingAuthTokenException {
        final String accessToken = properties.getProperty(PROPERTY_TWITCH_ACCESS_TOKEN);
        if (StringUtils.isEmpty(accessToken)) {
            throw new MissingAuthTokenException("Missing user access token! Did you query it before calling this method?");
        }
        return accessToken;
    }

    @Override
    public void setUserAccessToken(final String userAccessToken) {
        final String hiddenToken = StringUtils.abbreviateMiddle(userAccessToken, "******", 12);
        properties.setProperty(PROPERTY_TWITCH_ACCESS_TOKEN, userAccessToken);
        updatePropertiesFile();
        LOGGER.info("Successfully received access token :)");
        LOGGER.debug("Received access token {}", hiddenToken);
    }

    private void updatePropertiesFile() {
        updatePropertiesFile(properties, PROPERTY_TWITCH_ACCESS_TOKEN);
    }

    @Override
    public void close() throws Exception {
        if (propertiesInputStream != null) {
            propertiesInputStream.close();
        }
    }
}
