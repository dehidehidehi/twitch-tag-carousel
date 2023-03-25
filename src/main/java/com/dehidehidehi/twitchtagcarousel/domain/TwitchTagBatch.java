package com.dehidehidehi.twitchtagcarousel.domain;

import org.apache.commons.lang3.StringUtils;

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
		tags.forEach(this::validateTag);
	}

	private void validateTag(final String tag) {
		if (tag.length() > 25) {
			throw new AssertionError("Max tag length is 25, found tag with length : %d".formatted(tag.length()));
		}
		if (tag.length() == 0) {
			throw new AssertionError(("No empty tags allowed, but found at least one."));
		}
		if (!StringUtils.isAsciiPrintable(tag)) {
			throw new AssertionError("Found non ascii printable char in tag : %s".formatted(tag));
		}
		if (StringUtils.containsWhitespace(tag)) {
			throw new AssertionError("No whitespace allowed in tags, found in : %s".formatted(tag));
		}
		if (!StringUtils.isAlphanumeric(tag)) {
			throw new AssertionError("Found non alpha numeric character in tag : %s".formatted(tag));
		}
	}

	public Set<String> get() {
		return unmodifiableTags;
	}
}
