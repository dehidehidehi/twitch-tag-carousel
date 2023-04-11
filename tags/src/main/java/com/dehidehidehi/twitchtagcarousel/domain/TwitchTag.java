package com.dehidehidehi.twitchtagcarousel.domain;
import com.dehidehidehi.twitchtagcarousel.error.TwitchTagValidationException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;

public final class TwitchTag implements Comparable<TwitchTag> {

	private final String tag;
	
    public TwitchTag(final String tag) throws TwitchTagValidationException {
		 validateTag(tag);
		 this.tag = tag;
    }
	 
	private void validateTag(final String tag) throws TwitchTagValidationException {
		if (tag.length() > 25) {
			throw new TwitchTagValidationException("%s: Max tag length is 25, tag has length : %d".formatted(tag, tag.length()));
		}
		if (tag.length() == 0) {
			throw new TwitchTagValidationException("%s: A tag cannot be empty.".formatted(tag));
		}
		if (!StringUtils.isAsciiPrintable(tag)) {
			throw new TwitchTagValidationException("%s: Found non ascii printable char in tag.".formatted(tag));
		}
		if (StringUtils.containsWhitespace(tag)) {
			throw new TwitchTagValidationException("%s: No whitespace allowed in tags.".formatted(tag));
		}
		if (!StringUtils.isAlphanumeric(tag)) {
			throw new TwitchTagValidationException("%s: Found non alpha numeric character in tag.".formatted(tag));
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
