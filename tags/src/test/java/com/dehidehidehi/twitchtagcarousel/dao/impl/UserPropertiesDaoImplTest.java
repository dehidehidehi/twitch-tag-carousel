package com.dehidehidehi.twitchtagcarousel.dao.impl;
import com.dehidehidehi.twitchtagcarousel.dao.UserPropertiesDao;
import com.dehidehidehi.twitchtagcarousel.domain.TwitchTag;
import com.dehidehidehi.twitchtagcarousel.error.MissingAuthTokenException;
import com.dehidehidehi.twitchtagcarousel.util.CDIExtension;
import jakarta.inject.Inject;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.dehidehidehi.twitchtagcarousel.dao.impl.UserPropertiesDaoImpl.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * Note: These config files are generated in /test-classes/*
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(CDIExtension.class)
class UserPropertiesDaoImplTest extends PropertiesDaoUtil {

    private final String defaultMandatoryTags = "vtuber,envtuber,cool";
    private final String defaultRotatingTags = "chatting,eating,relaxed";
    private final String defaultTwitchAccessTokenValue = "this_is_my_access_token!";

    @Inject
    private UserPropertiesDao userPropertiesDao;

    @BeforeEach
    void setUp() throws IOException {
        setUpPropertiesFileWithDefaultValues();
    }

    private void setUpPropertiesFileWithDefaultValues() throws IOException {
        final File propertiesFile = new File(getDirPathOfThisJar() + USER_PROPERTIES_FILE_NEXT_TO_JAR);
        propertiesFile.createNewFile();
        try (FileOutputStream fileOutputStream = new FileOutputStream(propertiesFile)) {
            final String mandatoryTags = "%s=%s".formatted(PROPERTY_MANDATORY_TAGS, defaultMandatoryTags);
            final String rotatingTags = "%s=%s".formatted(PROPERTY_ROTATING_TAGS, defaultRotatingTags);
            final String defaultContent = "%s=%s".formatted(PROPERTY_TWITCH_ACCESS_TOKEN, defaultTwitchAccessTokenValue);
            final String properties = String.join(System.lineSeparator(), defaultContent, mandatoryTags, rotatingTags);
            fileOutputStream.write(properties.getBytes(StandardCharsets.UTF_8));
        }
    }

    @Order(1)
    @Test
    void getUserAccessTokenShouldReturnAccessTokenFromPropertiesFile() throws MissingAuthTokenException {
        assertThatCode(() -> userPropertiesDao.getUserAccessToken()).doesNotThrowAnyException();
        assertThat(userPropertiesDao.getUserAccessToken()).isNotNull().isEqualTo(defaultTwitchAccessTokenValue);
    }

    @Test
    void setUserAccessTokenShouldChangeAccessTokenInPropertiesFile() throws MissingAuthTokenException {
        final String randomToken = RandomStringUtils.random(20);
        assertThatCode(() -> userPropertiesDao.setUserAccessToken(randomToken)).doesNotThrowAnyException();
        assertThat(userPropertiesDao.getUserAccessToken()).isEqualTo(randomToken);
    }

    @Order(1)
    @Test
    void getMandatoryTagsShouldReturnMandatoryTagsFromPropertiesFile() {
        assertThatCode(() -> userPropertiesDao.getMandatoryTags()).doesNotThrowAnyException();
        final List<String> expected = Arrays.stream(defaultMandatoryTags.split(",")).toList();
        final List<String> result = userPropertiesDao.getMandatoryTags().stream().map(TwitchTag::toString).toList();
        assertThat(result).isNotNull().containsAll(expected);
    }

    @Test
    void getMandatoryTagsShouldReturnMutableCollection() {
        final List<TwitchTag> mandatoryTags = userPropertiesDao.getMandatoryTags();
        int size = mandatoryTags.size();
        assertThatCode(() -> mandatoryTags.add(null)).doesNotThrowAnyException();
        assertThat(mandatoryTags).hasSize(size + 1);
    }

    @Order(1)
    @Test
    void getRotatingTagsShouldReturnRotatingTagsFromPropertiesFile() {
        assertThatCode(() -> userPropertiesDao.getRotatingTags()).doesNotThrowAnyException();
        final List<String> expected = Arrays.stream(defaultRotatingTags.split(",")).toList();
        final List<String> result = userPropertiesDao.getRotatingTags().stream().map(TwitchTag::toString).toList();
        assertThat(result).isNotNull().containsAll(expected);
    }

    @Test
    void getRotatingTagsShouldReturnMutableCollection() {
        final List<TwitchTag> rotatingTags = userPropertiesDao.getRotatingTags();
        int size = rotatingTags.size();
        assertThatCode(() -> rotatingTags.add(null)).doesNotThrowAnyException();
        assertThat(rotatingTags).hasSize(size + 1);
    }

    @SneakyThrows
    @Test
    void setMandatoryTagsShouldOverwriteInPropertiesFile() {
        final List<TwitchTag> newMandatoryTags = List.of(new TwitchTag("english"),
                                                         new TwitchTag("uk"),
                                                         new TwitchTag("supercool"));
        userPropertiesDao.saveMandatoryTags(newMandatoryTags);
        final String expected = newMandatoryTags.stream().map(TwitchTag::toString).collect(Collectors.joining(","));
        final String result = userPropertiesDao
                .getMandatoryTags()
                .stream()
                .map(TwitchTag::toString)
                .collect(Collectors.joining(","));
        assertThat(result).isEqualTo(expected);
    }

    @SneakyThrows
    @Test
    void setRotatingTagsShouldOverwriteInPropertiesFile() {
        final List<TwitchTag> newRotatingTags = List.of(new TwitchTag("italian"), new TwitchTag("it"), new TwitchTag("pizza"));
        userPropertiesDao.saveRotatingTags(newRotatingTags);
        final String expected = newRotatingTags.stream().map(TwitchTag::toString).collect(Collectors.joining(","));
        final String result = userPropertiesDao
                .getRotatingTags()
                .stream()
                .map(TwitchTag::toString)
                .collect(Collectors.joining(","));
        assertThat(result).isEqualTo(expected);
    }
}
