package com.dehidehidehi.twitchtagcarousel.domain;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertThrows;

class TwitchTagBatchTest {

    @Test
    void shouldThrowIfTooManyTagsAtInstanciation() {
        final Set<String> strings = IntStream.range(0, 12).mapToObj(String::valueOf).collect(Collectors.toSet());
        assertThrows(AssertionError.class, () -> new TwitchTagBatch(strings));
    }

    @Test
    void shouldThrowIfTagLongerThan25Chars() {
        final String tooLongString = RandomStringUtils.random(26);
        assertThrows(AssertionError.class, () -> new TwitchTagBatch(Set.of(tooLongString, "validTag")));
    }

    @Test
    void shouldThrowIfEmptyTagWasFound() {
        assertThrows(AssertionError.class, () -> new TwitchTagBatch(Set.of("", "validTag")));
    }

    @Test
    void shouldThrowIfNonAsciiPrintableCharWasFound() {
        assertThrows(AssertionError.class, () -> new TwitchTagBatch(Set.of("日", "validTag")));
    }

    @Test
    void shouldThrowIfNonAlphaNumericCharWasFound() {
        assertThrows(AssertionError.class, () -> new TwitchTagBatch(Set.of("!", "validTag")));
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "\t", "\n"})
    void shouldThrowIfAnyTagContainsWhiteSpace(String whiteSpace) {
        assertThrows(AssertionError.class, () -> new TwitchTagBatch(Set.of(whiteSpace, "validTag")));
    }
}
