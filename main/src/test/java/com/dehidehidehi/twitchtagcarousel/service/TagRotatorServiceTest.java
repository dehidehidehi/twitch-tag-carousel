package com.dehidehidehi.twitchtagcarousel.service;
import com.dehidehidehi.twitchtagcarousel.dao.UserPropertiesDao;
import com.dehidehidehi.twitchtagcarousel.domain.TwitchTag;
import com.dehidehidehi.twitchtagcarousel.error.TwitchTagValidationException;
import com.dehidehidehi.twitchtagcarousel.util.CDIExtension;
import jakarta.inject.Inject;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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
                .rangeClosed(1, i)
                .mapToObj(j -> RandomStringUtils.randomAlphabetic(10))
                .map(s -> {
                    try {
                        return new TwitchTag(s);
                    } catch (TwitchTagValidationException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();

        @SneakyThrows
        @BeforeEach
        void setUp() {
            userPropertiesDao.saveMandatoryTags(Collections.emptyList());
            userPropertiesDao.saveRotatingTags(Collections.emptyList());
            userPropertiesDao.saveMandatoryTags(makeTags.apply(10));
        }

        @SneakyThrows
        @AfterEach
        void tearDown() {
            userPropertiesDao.saveMandatoryTags(Collections.emptyList());
            userPropertiesDao.saveRotatingTags(Collections.emptyList());
        }

        @SneakyThrows
        @Test
        void selectTagsShouldReturnTenTags() {
            userPropertiesDao.saveMandatoryTags(Collections.emptyList());
            assertThat(tagRotatorService.selectNewTags().get()).isEmpty();
            userPropertiesDao.saveMandatoryTags(makeTags.apply(10));
            assertThat(tagRotatorService.selectNewTags().get()).hasSize(10);
        }

        @SneakyThrows
        @RepeatedTest(5)
        void includeMandatoryTagsAtEachInvocation() {
            final List<TwitchTag> mandatoryTags = makeTags.apply(5);
            userPropertiesDao.saveMandatoryTags(mandatoryTags);
            userPropertiesDao.saveRotatingTags(makeTags.apply(30));
            final Collection<TwitchTag> firstResult = tagRotatorService.selectNewTags().get();
            final Collection<TwitchTag> secondResult = tagRotatorService.selectNewTags().get();
            assertThat(firstResult).containsAll(mandatoryTags);
            assertThat(secondResult).containsAll(mandatoryTags);
        }

        @SneakyThrows
        @Test
        void rotatesRotatingTagsAtEachInvocation() {
            final List<TwitchTag> mandatoryTags = makeTags.apply(5);
            userPropertiesDao.saveMandatoryTags(mandatoryTags);
            userPropertiesDao.saveRotatingTags(makeTags.apply(30));
            final Collection<TwitchTag> firstResult = tagRotatorService.selectNewTags().get();
            final Collection<TwitchTag> secondResult = tagRotatorService.selectNewTags().get();
            mandatoryTags.forEach(firstResult::remove);
            mandatoryTags.forEach(secondResult::remove);
            assertThat(secondResult).isNotEmpty()
                    .as("There should be no common tags between both results.")
                    .doesNotContainAnyElementsOf(firstResult);
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
