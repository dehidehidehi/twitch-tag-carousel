package com.dehidehidehi.twitchtagcarousel.service;
import com.dehidehidehi.twitchtagcarousel.domain.TwitchTagEnum;
import com.dehidehidehi.twitchtagcarousel.util.CDIExtension;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

@ExtendWith(CDIExtension.class)
class TagRotatorServiceTest {

    @Inject
    private TagRotatorService tagRotatorService;

    @Test
    void selectTagsShouldReturnTenTags() {
        assertThat(tagRotatorService.selectTags()).hasSize(10);
    }

    @Test
    void selectTagsShouldRotateAtEachInvocation() {
        final Set<TwitchTagEnum> firstResult = new HashSet<>(tagRotatorService.selectTags());  // required as returns immutable set
        final Set<TwitchTagEnum> secondResult = new HashSet<>(tagRotatorService.selectTags());
        firstResult.retainAll(secondResult);
        assertThat(firstResult)
                .as("There should be no common tags between both results.")
                .isEmpty();
    }

    @Test
    void selectTagsShouldNotChangeTagsToRotateListNumberOfElements() {
        final int tagsToRotateUniqueElementCount = tagRotatorService.getTagsToRotate().stream().collect(toSet()).size();
        tagRotatorService.getTagsToRotate();
        assertThat(tagRotatorService.getTagsToRotate().stream().collect(toSet()).size()).isEqualTo(tagsToRotateUniqueElementCount);
    }

    @Test
    void getTagsToRotateShouldBePackagePrivate() throws NoSuchMethodException {
        final int getTagsToRotateModifiers = tagRotatorService
                .getClass()
                .getDeclaredMethod("getTagsToRotate")
                .getModifiers();
        assertThat(getTagsToRotateModifiers)
                .as("Access to all tags to rotate should remain somewhat hidden, for now.")
                .isZero();
    }

    @Test
    void tagsToRotateShouldBeShuffledAfterServiceInstanciation() {
        final List<String> naturallySortedTags = tagRotatorService
                .getTagsToRotate()
                .stream()
                .map(TwitchTagEnum::name)
                .sorted()
                .toList();
        final List<String> maybeShuffledTags = tagRotatorService.getTagsToRotate().stream().map(TwitchTagEnum::name).toList();
        assumeThat(naturallySortedTags)
                .as("Assuming tags were hardcoded in alphabetical order, this test should be useful, otherwise it won't be of any use.")
                .isNotEqualTo(maybeShuffledTags);
    }
}
