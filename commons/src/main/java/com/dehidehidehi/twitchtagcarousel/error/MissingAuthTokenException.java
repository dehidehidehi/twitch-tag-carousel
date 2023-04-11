package com.dehidehidehi.twitchtagcarousel.error;
public class MissingAuthTokenException extends Exception {

	public MissingAuthTokenException() {
	}

	public MissingAuthTokenException(final String message) {
		super(message);
	}

	public MissingAuthTokenException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public MissingAuthTokenException(final Throwable cause) {
		super(cause);
	}

	public MissingAuthTokenException(final String message,
												final Throwable cause,
												final boolean enableSuppression,
												final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
