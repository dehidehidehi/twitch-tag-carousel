package com.dehidehidehi.twitchtagcarousel.service;
import com.dehidehidehi.twitchtagcarousel.domain.TwitchTag;
import com.dehidehidehi.twitchtagcarousel.util.CDIExtension;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

@ExtendWith(CDIExtension.class)
class TagRotatorServiceTest {

    @Inject
    private TagRotatorService tagRotatorService;

    @Nested
    class SelectTagsShould {

        @Test
        void selectTagsShouldReturnTenTags() {
            final Set<String> mandatoryTags = Set.of("123435", "sdlk");
            assertThat(tagRotatorService.selectNewTags().get()).hasSize(10);
            assertThat(tagRotatorService.selectNewTags(mandatoryTags).get()).hasSize(10);
        }
        
        @Test
        void rotateAtEachInvocation() {
            final Set<TwitchTag> firstResult = new HashSet<>(tagRotatorService.selectNewTags().get());
            final Set<TwitchTag> secondResult = new HashSet<>(tagRotatorService.selectNewTags().get());
            firstResult.retainAll(secondResult);
            assertThat(firstResult)
                    .as("There should be no common tags between both results.")
                    .isEmpty();
        }

        @Test
        void notChangeTagsToRotateListNumberOfElements() {
            final int tagsToRotateUniqueElementCount = new HashSet<>(tagRotatorService.getTagsToRotate()).size();
            tagRotatorService.getTagsToRotate();
            assertThat(new HashSet<>(tagRotatorService.getTagsToRotate())).hasSize(tagsToRotateUniqueElementCount);
        }

        @Test
        void returnMandatoryTagsInResponse() {
            final Set<String> mandatoryTags = Set.of("abdcef", "123465", "asddfskler");
            final Set<String> tags = tagRotatorService.selectNewTags(mandatoryTags).get().stream().map(TwitchTag::toString).collect(Collectors.toSet());
            assertThat(tags).containsAll(mandatoryTags);
        }
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
                .sorted()
                .toList();
        final List<String> maybeShuffledTags = tagRotatorService.getTagsToRotate().stream().toList();
        assumeThat(naturallySortedTags)
                .as("Assuming tags were hardcoded in alphabetical order, this test should be useful, otherwise it won't be of any use.")
                .isNotEqualTo(maybeShuffledTags);
    }
}
