package com.dehidehidehi.twitchtagcarousel.error;
public class TwitchMissingAuthTokenException extends RuntimeException {

	public TwitchMissingAuthTokenException() {
	}

	public TwitchMissingAuthTokenException(final String message) {
		super(message);
	}

	public TwitchMissingAuthTokenException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public TwitchMissingAuthTokenException(final Throwable cause) {
		super(cause);
	}

	public TwitchMissingAuthTokenException(final String message,
											  final Throwable cause,
											  final boolean enableSuppression,
											  final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
