package com.dehidehidehi.twitchtagcarousel.dao.impl;

import com.dehidehidehi.twitchtagcarousel.dao.UserPropertiesDao;
import com.dehidehidehi.twitchtagcarousel.domain.TwitchTag;
import com.dehidehidehi.twitchtagcarousel.error.MissingAuthTokenException;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Typed;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * IO operations on the user properties file located next to the compiled .jar.
 */
@Typed(UserPropertiesDao.class)
@ApplicationScoped
class UserPropertiesDaoImpl extends PropertiesDaoUtil implements UserPropertiesDao {

    static final String USER_PROPERTIES_FILE_NEXT_TO_JAR = "/user.properties".replace("/", File.separator);
    private static final Logger LOGGER = LoggerFactory.getLogger(UserPropertiesDaoImpl.class);
    private Properties properties;
    private InputStream propertiesInputStream;

    @PostConstruct
    void init() {
        properties = new Properties();
        final File userPropertiesFile = new File(getDirPathOfThisJar() + USER_PROPERTIES_FILE_NEXT_TO_JAR);
        try {
            userPropertiesFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            propertiesInputStream = new FileInputStream(userPropertiesFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            properties.load(propertiesInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public String readUserProperty(final String propertyKey) {
        return properties.getProperty(propertyKey);
    }

    @Override
    public void saveMandatoryTags(final List<TwitchTag> tags) {
        final String concatenated = tags.stream().map(TwitchTag::toString).collect(Collectors.joining(","));
        properties.setProperty(PROPERTY_MANDATORY_TAGS, concatenated);
        updatePropertiesFile();
    }

    @Override
    public List<TwitchTag> getMandatoryTags() {
        final String[] unparsedTags = Optional
                .ofNullable(properties.get(PROPERTY_MANDATORY_TAGS))
                .map(String.class::cast)
                .map(tags -> tags.split(","))
                .orElseGet(() -> new String[0]);
        return Arrays
                .stream(unparsedTags)
                .filter(StringUtils::isNotEmpty)
                .map(TwitchTag::new)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public int countMandatoryTags() {
        return getMandatoryTags().size();
    }

    @Override
    public void saveRotatingTags(final List<TwitchTag> tags) {
        final String concatenated = tags.stream().map(TwitchTag::toString).collect(Collectors.joining(","));
        properties.setProperty(PROPERTY_ROTATING_TAGS, concatenated);
        updatePropertiesFile();
    }

    @Override
    public List<TwitchTag> getRotatingTags() {
        final String[] unparsedTags = Optional
                .ofNullable(properties.get(PROPERTY_ROTATING_TAGS))
                .map(String.class::cast)
                .map(tags -> tags.split(","))
                .orElseGet(() -> new String[0]);
        return Arrays
                .stream(unparsedTags)
                .filter(StringUtils::isNotEmpty)
                .map(TwitchTag::new)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public int countRotatingTags() {
        return getRotatingTags().size();
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
        final String pathname = (getDirPathOfThisJar() + USER_PROPERTIES_FILE_NEXT_TO_JAR).replace("/", File.separator);
        updatePropertiesFileOutsideOfJar(properties, pathname);
    }

    @Override
    public void close() throws Exception {
        if (propertiesInputStream != null) {
            propertiesInputStream.close();
        }
    }
}
