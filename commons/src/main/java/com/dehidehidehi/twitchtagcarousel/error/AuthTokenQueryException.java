package com.dehidehidehi.twitchtagcarousel.error;

public class AuthTokenQueryException extends RuntimeException {

	public AuthTokenQueryException() {
	}

	public AuthTokenQueryException(final String message) {
		super(message);
	}

	public AuthTokenQueryException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public AuthTokenQueryException(final Throwable cause) {
		super(cause);
	}

	AuthTokenQueryException(final String message,
									final Throwable cause,
									final boolean enableSuppression,
									final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
