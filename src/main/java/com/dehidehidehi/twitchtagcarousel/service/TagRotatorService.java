package com.dehidehidehi.twitchtagcarousel.service;

import com.dehidehidehi.twitchtagcarousel.domain.TwitchTagEnum;
import com.dehidehidehi.twitchtagcarousel.service.twitchclient.TwitchClient;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.dehidehidehi.twitchtagcarousel.domain.TwitchTagEnum.*;

@ApplicationScoped
public class TagRotatorService {

    public static final long MAX_NB_TAGS_PER_CHANNEL = 10L;
    private final TwitchClient twitchClient;

    private List<TwitchTagEnum> tagsToRotate = List.of(
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
    
    @PostConstruct
    private void shuffleTagsToRotate() {
        tagsToRotate = tagsToRotate
                .stream()
                .collect(Collectors.toUnmodifiableSet())
                .stream()
                .toList();
    }

    public void updateTags(Set<TwitchTagEnum> tags) {
        final Set<String> tagStringSet = tags.stream().map(TwitchTagEnum::name).collect(Collectors.toUnmodifiableSet());
        twitchClient.updateTags(tagStringSet);
    }

    /**
     * Selects a new batch of tags, rotates tag selection at each invocation.
     */
    public Set<TwitchTagEnum> selectTags() {
        final Set<TwitchTagEnum> toReturn = tagsToRotate
                .stream()
                .limit(MAX_NB_TAGS_PER_CHANNEL)
                .collect(Collectors.toUnmodifiableSet());
        moveTagsToEndOfTheList(toReturn);
        return toReturn;
    }

    private void moveTagsToEndOfTheList(final Set<TwitchTagEnum> toReturn) {
        tagsToRotate = tagsToRotate
                .stream()
                .filter(t -> !toReturn.contains(t))
                .collect(Collectors.toList());
        tagsToRotate.addAll(toReturn);
    }

    List<TwitchTagEnum> getTagsToRotate() {
        return tagsToRotate;
    }
}
