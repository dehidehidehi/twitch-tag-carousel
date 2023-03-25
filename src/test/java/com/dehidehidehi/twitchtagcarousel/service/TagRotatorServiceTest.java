package com.dehidehidehi.twitchtagcarousel.service;
import com.dehidehidehi.twitchtagcarousel.util.CDIExtension;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            assertThat(tagRotatorService.selectTags()).hasSize(10);
            assertThat(tagRotatorService.selectTags(mandatoryTags)).hasSize(10);
        }
        
        @Test
        void rotateAtEachInvocation() {
            final Set<String> firstResult = new HashSet<>(tagRotatorService.selectTags());
            final Set<String> secondResult = new HashSet<>(tagRotatorService.selectTags());
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
            final Set<String> tags = tagRotatorService.selectTags(mandatoryTags);
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
