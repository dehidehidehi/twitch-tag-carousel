package com.dehidehidehi.twitchtagcarousel.error;

public class TwitchAuthTokenQueryException extends RuntimeException {

	public TwitchAuthTokenQueryException() {
	}

	public TwitchAuthTokenQueryException(final String message) {
		super(message);
	}

	public TwitchAuthTokenQueryException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public TwitchAuthTokenQueryException(final Throwable cause) {
		super(cause);
	}

	TwitchAuthTokenQueryException(final String message,
											final Throwable cause,
											final boolean enableSuppression,
											final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
