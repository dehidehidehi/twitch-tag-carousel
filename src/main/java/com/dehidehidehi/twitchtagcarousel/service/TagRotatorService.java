package com.dehidehidehi.twitchtagcarousel.service;

import com.dehidehidehi.twitchtagcarousel.service.twitchclient.TwitchClient;
import com.dehidehidehi.twitchtagcarousel.domain.TwitchTagEnum;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

import static com.dehidehidehi.twitchtagcarousel.domain.TwitchTagEnum.*;
import static com.dehidehidehi.twitchtagcarousel.domain.TwitchTagEnum.woman;

@ApplicationScoped
public class TagRotatorService {

    private final TwitchClient twitchClient;

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

    @Inject
    public TagRotatorService(final TwitchClient twitchClient) {
        this.twitchClient = twitchClient;
    }

    public void updateTags(List<String> tags) {
        twitchClient.updateTags(tags);
    }
}
