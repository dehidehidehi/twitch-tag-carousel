package com.dehidehidehi.twitchtagcarousel.domain;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a batch of Twitch Tags.
 */
public final class TwitchTagBatch {

	public static final long MAX_NB_TAGS_PER_CHANNEL = 10L;

	private final Set<TwitchTag> unmodifiableTags;

	public TwitchTagBatch(final Set<String> tags) {
		this.unmodifiableTags = tags.stream().map(TwitchTag::new).collect(Collectors.toUnmodifiableSet());
		validateBatch(unmodifiableTags);
	}

	private void validateBatch(final Set<TwitchTag> tags) {
		if (tags.size() > MAX_NB_TAGS_PER_CHANNEL) {
			throw new AssertionError("Only a maximum of %d tags is permitted per batch.".formatted(MAX_NB_TAGS_PER_CHANNEL));
		}
	}

	public Set<TwitchTag> get() {
		return unmodifiableTags;
	}
}
