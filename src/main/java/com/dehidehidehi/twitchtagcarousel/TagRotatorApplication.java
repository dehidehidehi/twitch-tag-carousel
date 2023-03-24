package com.dehidehidehi.twitchtagcarousel;

import com.dehidehidehi.twitchtagcarousel.domain.TwitchTagEnum;
import com.dehidehidehi.twitchtagcarousel.service.TagRotatorService;
import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.helix.domain.ChannelInformation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import jakarta.inject.Inject;

import java.util.List;

import static com.dehidehidehi.twitchtagcarousel.domain.TwitchTagEnum.*;

@ApplicationScoped
public class TagRotatorApplication {

    private static List<TwitchTagEnum> tagsToRotate = List.of(
            action,
            adhd,
            ama,
            anime,
            anxiety,
            arab,
            asmr,
            british,
            casual,
            chat,
            chatting,
            chatty,
            chill,
            chilled,
            comfy,
            community,
            competitive,
            competitive,
            cozy,
            envtuber,
            fr,
            francais,
            france,
            friendly,
            fun,
            funny,
            furry,
            game,
            gameplay,
            gamer,
            gamergirl,
            gaming,
            girl,
            horror,
            irl,
            justchatting,
            letsplay,
            lgbt,
            lgbtq,
            lgbtqia,
            lgbtqiaplus,
            lol,
            mentalhealth,
            minecraft,
            multiplayer,
            music,
            nobackseating,
            pc,
            playingwithviewers,
            pngtuber,
            retro,
            roleplay,
            rp,
            rpg,
            safespace,
            solo,
            speedrun,
            uk,
            usa,
            variety,
            vtuber,
            woman
    );

    private final TagRotatorService tagRotatorService;
    private final TwitchHelix twitchHelix;

    @Inject
    public TagRotatorApplication(final TagRotatorService tagRotatorService, final TwitchHelix twitchHelix) {
        this.tagRotatorService = tagRotatorService;
        this.twitchHelix = twitchHelix;
    }

    void start() {
        System.out.println("Hello");
        final ChannelInformation channelInformation = new ChannelInformation();
        channelInformation.withTags(tagRotatorService.getTagRotation());
//        twitchHelix.updateChannelInformation(authToken, broadcasterId, channelInformation);
//        System.out.println(tagsToRotate);
    }

    public static void main(String[] args) {
        try (SeContainer container = SeContainerInitializer.newInstance().initialize()) {
            final TagRotatorApplication tagRotatorApplication = container.select(TagRotatorApplication.class).get();
            tagRotatorApplication.start();
        }
    }

}
