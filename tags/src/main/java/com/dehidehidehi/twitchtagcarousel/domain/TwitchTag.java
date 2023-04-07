package com.dehidehidehi.twitchtagcarousel.domain;
import org.apache.commons.lang3.StringUtils;

public final class TwitchTag {

	private final String tag;
	
    TwitchTag(final String tag) {
		 validateTag(tag);
		 this.tag = tag;
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

	@Override
	public String toString() {
		 return tag;
	}
}
