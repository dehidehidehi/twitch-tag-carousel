package com.dehidehidehi.twitchtagcarousel.domain;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public final class TwitchTag implements Comparable<TwitchTag> {

	private final String tag;
	
    public TwitchTag(final String tag) {
		 validateTag(tag);
		 this.tag = tag;
    }
	 
	private void validateTag(final String tag) {
		if (tag.length() > 25) {
			throw new AssertionError("Max tag length is 25, bit has length : %d".formatted(tag.length()));
		}
		if (tag.length() == 0) {
			throw new AssertionError("A tag cannot be empty.");
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

	@Override
	public String toString() {
		 return tag;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final TwitchTag twitchTag = (TwitchTag) o;

		return new EqualsBuilder().append(tag, twitchTag.tag).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(tag).toHashCode();
	}

	@Override
	public int compareTo(@NotNull final TwitchTag o) {
		 return tag.compareTo(o.tag);
	}
}
