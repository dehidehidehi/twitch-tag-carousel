package com.dehidehidehi.twitchtagcarousel.service;
import com.dehidehidehi.twitchtagcarousel.dao.UserPropertiesDao;
import com.dehidehidehi.twitchtagcarousel.domain.TwitchTag;
import com.dehidehidehi.twitchtagcarousel.util.CDIExtension;
import jakarta.inject.Inject;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(CDIExtension.class)
class TagRotatorServiceTest {

    @Inject
    private TagRotatorService tagRotatorService;

    @Inject
    private UserPropertiesDao userPropertiesDao;

    @Nested
    class SelectTagsShould {

        private final Function<Integer, List<TwitchTag>> makeTags = i -> IntStream
                .range(0, i - 1)
                .mapToObj(j -> RandomStringUtils.randomAlphabetic(10))
                .map(TwitchTag::new)
                .toList();

        @BeforeEach
        void setUp() {
            userPropertiesDao.saveMandatoryTags(Collections.emptyList());
            userPropertiesDao.saveRotatingTags(Collections.emptyList());
            userPropertiesDao.saveMandatoryTags(makeTags.apply(10));
        }

        @AfterEach
        void tearDown() {
            userPropertiesDao.saveMandatoryTags(Collections.emptyList());
            userPropertiesDao.saveRotatingTags(Collections.emptyList());
        }

        @SneakyThrows
        @Test
        void selectTagsShouldReturnTenTags() {
            userPropertiesDao.saveMandatoryTags(Collections.emptyList());
            assertThat(tagRotatorService.selectNewTags().get()).hasSize(0);
            userPropertiesDao.saveMandatoryTags(makeTags.apply(10));
            assertThat(tagRotatorService.selectNewTags().get()).hasSize(10);
        }

        @SneakyThrows
        @Test
        void rotateAtEachInvocation() {
            userPropertiesDao.saveMandatoryTags(makeTags.apply(5));
            userPropertiesDao.saveRotatingTags(makeTags.apply(30));
            final Set<TwitchTag> firstResult = new HashSet<>(tagRotatorService.selectNewTags().get());
            final Set<TwitchTag> secondResult = new HashSet<>(tagRotatorService.selectNewTags().get());
            firstResult.retainAll(secondResult);
            assertThat(firstResult).as("There should be no common tags between both results.").isEmpty();
        }

        @SneakyThrows
        @Test
        void notChangeTagsToRotateListNumberOfElements() {
            final int tagsToRotateUniqueElementCount = new HashSet<>(tagRotatorService.selectNewTags().get()).size();
            tagRotatorService.selectNewTags();
            assertThat(new HashSet<>(tagRotatorService.selectNewTags().get())).hasSize(tagsToRotateUniqueElementCount);
        }

        @SneakyThrows
        @Test
        void returnMandatoryTagsInResponse() {
            final List<TwitchTag> tenTags = makeTags.apply(10);
            userPropertiesDao.saveMandatoryTags(tenTags);
            final Collection<TwitchTag> twitchTags = tagRotatorService.selectNewTags().get();
            assertThat(twitchTags).containsAll(tenTags);
        }
    }
}
