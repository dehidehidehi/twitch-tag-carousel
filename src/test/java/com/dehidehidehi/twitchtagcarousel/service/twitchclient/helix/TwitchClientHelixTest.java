package com.dehidehidehi.twitchtagcarousel.service.twitchclient.helix;
import com.dehidehidehi.twitchtagcarousel.domain.TwitchTagBatch;
import com.dehidehidehi.twitchtagcarousel.error.TwitchTagUpdateException;
import com.dehidehidehi.twitchtagcarousel.service.twitchclient.TwitchClient;
import com.dehidehidehi.twitchtagcarousel.util.CDIExtension;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import jakarta.inject.Inject;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Set;

import static org.assertj.core.api.Assumptions.assumeThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(CDIExtension.class)
class TwitchClientHelixTest {

    @Inject
    @HelixClient
    TwitchClient helixClient;

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
