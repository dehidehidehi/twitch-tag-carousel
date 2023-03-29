package com.dehidehidehi.twitchtagcarousel.error;

public class WebServerStartException extends Exception {

	WebServerStartException() {
	}

	WebServerStartException(final String message) {
		super(message);
	}

	WebServerStartException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public WebServerStartException(final Throwable cause) {
		super(cause);
	}

	WebServerStartException(final String message,
									final Throwable cause,
									final boolean enableSuppression,
									final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
