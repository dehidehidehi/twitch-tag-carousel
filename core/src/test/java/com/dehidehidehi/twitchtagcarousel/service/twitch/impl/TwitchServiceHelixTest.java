package com.dehidehidehi.twitchtagcarousel.service.twitch.impl;
import com.dehidehidehi.twitchtagcarousel.domain.TwitchTagBatch;
import com.dehidehidehi.twitchtagcarousel.error.TwitchChannelIdException;
import com.dehidehidehi.twitchtagcarousel.error.TwitchTagUpdateException;
import com.dehidehidehi.twitchtagcarousel.service.twitch.TwitchService;
import com.dehidehidehi.twitchtagcarousel.service.twitch.impl.HelixClient;
import com.dehidehidehi.twitchtagcarousel.util.CDIExtension;
import jakarta.inject.Inject;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Set;

import static org.assertj.core.api.Assumptions.assumeThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(CDIExtension.class)
class TwitchServiceHelixTest {

    @HelixClient
    @Inject
    TwitchService helixClient;

    @Test
    void getChannelIdShouldThrowIfBadChannelName() {
        final String randomName = RandomStringUtils.randomAlphabetic(30);
        assumeThatThrownBy(() -> helixClient.getChannelIdFrom(randomName))
                .isExactlyInstanceOf(TwitchChannelIdException.class)
                .hasMessageContaining("No channel information found for channelName");
    }

    /**
     * Making sure we capture the exception.
     */
    @Test
    void shouldThrowTwitchTagUpdateExceptionOnBadRequestWithInvalidTags() {
        final TwitchTagBatch erronousTwitchBatch = mock(TwitchTagBatch.class);
        final String tooLongTag = RandomStringUtils.random(26);
        when(erronousTwitchBatch.get()).thenReturn(Set.of(tooLongTag));
        assumeThatThrownBy(() -> helixClient.updateTags(erronousTwitchBatch))
                .isExactlyInstanceOf(TwitchTagUpdateException.class);
    }
}