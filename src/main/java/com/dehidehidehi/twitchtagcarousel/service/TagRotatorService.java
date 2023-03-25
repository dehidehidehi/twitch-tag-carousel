package com.dehidehidehi.twitchtagcarousel.service;

import com.dehidehidehi.twitchtagcarousel.domain.TwitchTagEnum;
import com.dehidehidehi.twitchtagcarousel.service.twitchclient.TwitchClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Set;
import java.util.stream.Collectors;

import static com.dehidehidehi.twitchtagcarousel.domain.TwitchTagEnum.*;

@ApplicationScoped
public class TagRotatorService {

    private final TwitchClient twitchClient;

    private static Set<TwitchTagEnum> tagsToRotate = Set.of(
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

    public void updateTags(Set<String> tags) {
        twitchClient.updateTags(tags);
    }

    public Set<String> selectTags() {
        return tagsToRotate
                  .stream()
                  .map(TwitchTagEnum::name)
                  .limit(10L)
                  .collect(Collectors.toUnmodifiableSet());
    }
}
