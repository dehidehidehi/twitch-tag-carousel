package com.dehidehidehi.twitchtagcarousel.domain;

import java.util.Collections;
import java.util.Set;

/**
 * Represents a batch of Twitch Tags.
 */
public final class TwitchTagBatch {

	public static final long MAX_NB_TAGS_PER_CHANNEL = 10L;

	// Mutable
	private final Set<String> unmodifiableTags;

	public TwitchTagBatch(final Set<String> tags) {
		validateBatch(tags);
		this.unmodifiableTags = Collections.unmodifiableSet(tags);
	}

	private void validateBatch(final Set<String> tags) {
		if (tags.size() > MAX_NB_TAGS_PER_CHANNEL) {
			throw new AssertionError("Only a maximum of %d tags is permitted per batch.".formatted(MAX_NB_TAGS_PER_CHANNEL));
		}
	}

	public Set<String> get() {
		return unmodifiableTags;
	}
}
