package com.dehidehidehi.twitchtagcarousel.service.twitchclient.helix;
import com.dehidehidehi.twitchtagcarousel.service.twitchclient.TwitchClient;
import com.dehidehidehi.twitchtagcarousel.util.CDIExtension;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

@ExtendWith(CDIExtension.class)
class TwitchClientHelixTest {
    
    @Inject
    @HelixClient
    TwitchClient helixClient;

    @Test
    void shouldUpdateTags() {
        helixClient.updateTags(List.of("vtuber", "envtuber"));
    }

}
