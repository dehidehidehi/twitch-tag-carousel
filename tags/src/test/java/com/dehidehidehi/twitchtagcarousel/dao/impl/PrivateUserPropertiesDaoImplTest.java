package com.dehidehidehi.twitchtagcarousel.dao.impl;
import com.dehidehidehi.twitchtagcarousel.dao.PrivateUserPropertiesDao;
import com.dehidehidehi.twitchtagcarousel.error.MissingAuthTokenException;
import com.dehidehidehi.twitchtagcarousel.util.CDIExtension;
import jakarta.inject.Inject;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import static com.dehidehidehi.twitchtagcarousel.dao.impl.PrivateUserPropertiesDaoImpl.PRIVATE_USER_PROPERTIES_FILE;
import static com.dehidehidehi.twitchtagcarousel.dao.impl.PrivateUserPropertiesDaoImpl.PROPERTY_TWITCH_ACCESS_TOKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * Note: These config files are generated in /test-classes/*
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(CDIExtension.class)
class PrivateUserPropertiesDaoImplTest {
    
    private final String defaultTwitchAccessTokenValue = "this_is_my_access_token!";
    
    @Inject
    private PrivateUserPropertiesDao privateUserPropertiesDao;

    @BeforeEach
    void setUp() throws URISyntaxException, IOException {
        setUpPropertiesFileWithDefaultValues();
    }

    private void setUpPropertiesFileWithDefaultValues() throws URISyntaxException, IOException {
        final URI fileUri = PrivateUserPropertiesDaoImpl.class.getResource(PRIVATE_USER_PROPERTIES_FILE).toURI();
        final File propertiesFile = new File(fileUri);
        propertiesFile.createNewFile();
        try (FileOutputStream fileOutputStream = new FileOutputStream(propertiesFile)) {
            final String defaultContent = "%s=%s".formatted(PROPERTY_TWITCH_ACCESS_TOKEN, defaultTwitchAccessTokenValue);
            fileOutputStream.write(defaultContent.getBytes(StandardCharsets.UTF_8));
        }
    }

    @Order(1)
    @Test
    void getUserAccessTokenShouldReturnAccessTokenFromPropertiesFile() throws MissingAuthTokenException {
        assertThatCode(() -> privateUserPropertiesDao.getUserAccessToken()).doesNotThrowAnyException();
        assertThat(privateUserPropertiesDao.getUserAccessToken())
                .isNotNull()
                .isEqualTo(defaultTwitchAccessTokenValue);
    }

    @Test
    void setUserAccessTokenShouldChangeAccessTokenInPropertiesFile() throws MissingAuthTokenException {
        final String randomToken = RandomStringUtils.random(20);
        assertThatCode(() -> privateUserPropertiesDao.setUserAccessToken(randomToken)).doesNotThrowAnyException();
        assertThat(privateUserPropertiesDao.getUserAccessToken())
                .isEqualTo(randomToken);
    }
}
