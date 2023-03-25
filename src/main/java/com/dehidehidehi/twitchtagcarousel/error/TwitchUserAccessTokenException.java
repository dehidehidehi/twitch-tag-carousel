package com.dehidehidehi.twitchtagcarousel.error;
import java.io.IOException;

public class TwitchUserAccessTokenException extends IOException {

	public TwitchUserAccessTokenException() {
	}

	public TwitchUserAccessTokenException(final String message) {
		super(message);
	}

	public TwitchUserAccessTokenException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public TwitchUserAccessTokenException(final Throwable cause) {
		super(cause);
	}
}
