package com.dehidehidehi.twitchtagcarousel.service;

import com.dehidehidehi.twitchtagcarousel.annotation.Property;
import com.dehidehidehi.twitchtagcarousel.domain.TwitchTagBatch;
import com.dehidehidehi.twitchtagcarousel.error.TwitchTagUpdateException;
import com.dehidehidehi.twitchtagcarousel.service.twitchclient.TwitchClient;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.dehidehidehi.twitchtagcarousel.domain.TwitchTagBatch.MAX_NB_TAGS_PER_CHANNEL;
import static com.dehidehidehi.twitchtagcarousel.domain.TwitchTagEnum.*;

@ApplicationScoped
public class TagRotatorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TagRotatorService.class);

    private final TwitchClient twitchClient;

    @Inject
    @Property("tag-carousel.mandatory-tags")
    private String mandatoryTagsString;

    private List<String> tagsToRotate = List.of(
            action.name(),
            adhd.name(),
            ama.name(),
            anime.name(),
            anxiety.name(),
            arab.name(),
            asmr.name(),
            british.name(),
            casual.name(),
            chat.name(),
            chatting.name(),
            chatty.name(),
            chill.name(),
            chilled.name(),
            comfy.name(),
            community.name(),
            competitive.name(),
            cozy.name(),
            envtuber.name(),
            fr.name(),
            francais.name(),
            france.name(),
            friendly.name(),
            fun.name(),
            funny.name(),
            furry.name(),
            game.name(),
            gameplay.name(),
            gamer.name(),
            gamergirl.name(),
            gaming.name(),
            girl.name(),
            horror.name(),
            irl.name(),
            justchatting.name(),
            letsplay.name(),
            lgbt.name(),
            lgbtq.name(),
            lgbtqia.name(),
            lgbtqiaplus.name(),
            lol.name(),
            mentalhealth.name(),
            minecraft.name(),
            multiplayer.name(),
            music.name(),
            nobackseating.name(),
            pc.name(),
            playingwithviewers.name(),
            pngtuber.name(),
            retro.name(),
            roleplay.name(),
            rp.name(),
            rpg.name(),
            safespace.name(),
            solo.name(),
            speedrun.name(),
            uk.name(),
            usa.name(),
            variety.name(),
            vtuber.name(),
            woman.name()
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
    
    public void updateTags(TwitchTagBatch tags) throws TwitchTagUpdateException {
        LOGGER.debug("Entered in updating tags method.");
        LOGGER.trace("With params {}: ", tags);
        twitchClient.updateTags(tags);
        LOGGER.info("Updated stream tags with: {}", tags.get().stream().sorted().toList());
    }
    
    public TwitchTagBatch selectNewTags() {
        return selectNewTags(Collections.emptySet());
    }

    /**
     * Selects a new batch of tags, rotates tag selection at each invocation.
     */
    public TwitchTagBatch selectNewTags(Set<String> mandatoryTags) {
        final Set<String> toReturn = tagsToRotate
                .stream()
                .limit(MAX_NB_TAGS_PER_CHANNEL - mandatoryTags.size())
                .collect(Collectors.toSet());
        moveTagsToEndOfTheList(toReturn);
        toReturn.addAll(mandatoryTags);
        LOGGER.info("Selected tags: {}", toReturn.stream().sorted().toList());
        return new TwitchTagBatch(toReturn);
    }

    private void moveTagsToEndOfTheList(final Set<String> toReturn) {
        tagsToRotate = tagsToRotate
                .stream()
                .filter(t -> !toReturn.contains(t))
                .collect(Collectors.toList());
        tagsToRotate.addAll(toReturn);
        LOGGER.debug("Moved {} to the end of tag queue.", toReturn);
        LOGGER.trace("Current queue: {}", tagsToRotate);
    }

    List<String> getTagsToRotate() {
        return tagsToRotate;
    }

    public Set<String> getMandatoryTags() {
        return Arrays
                .stream(this.mandatoryTagsString.split(","))
                .map(String::trim)
                .collect(Collectors.toUnmodifiableSet());
    }
}
