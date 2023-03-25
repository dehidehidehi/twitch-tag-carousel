package com.dehidehidehi.twitchtagcarousel.domain;
import org.junit.jupiter.api.Test;

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

}
