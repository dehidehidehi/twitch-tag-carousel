package com.dehidehidehi.twitchtagcarousel.domain;

import java.util.Collection;

/**
 * Represents a batch of Twitch Tags.
 */
public final class TwitchTagBatch {

	public static final long MAX_NB_TAGS_PER_CHANNEL = 10L;

	private final Collection<TwitchTag> unmodifiableTags;

	public TwitchTagBatch(final Collection<TwitchTag> tags) {
		this.unmodifiableTags = tags;
		validateBatch(unmodifiableTags);
	}
	
	private void validateBatch(final Collection<TwitchTag> tags) {
		if (tags.size() > MAX_NB_TAGS_PER_CHANNEL) {
			throw new AssertionError("Only a maximum of %d tags is permitted per batch.".formatted(MAX_NB_TAGS_PER_CHANNEL));
		}
	}

	public Collection<TwitchTag> get() {
		return unmodifiableTags;
	}
}
