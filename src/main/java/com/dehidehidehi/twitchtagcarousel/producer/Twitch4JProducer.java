package com.dehidehidehi.twitchtagcarousel.producer;
import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.helix.TwitchHelixBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
class Twitch4JProducer {

    @Produces
    TwitchHelix provideTwitchHelix() {
        return TwitchHelixBuilder
                .builder()
                .withClientId("clientId")
                .withClientSecret("clientSecret")
                .build();

    }
}
