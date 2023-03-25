package com.dehidehidehi.twitchtagcarousel.error;
import java.io.IOException;

public class TwitchChannelIdException extends IOException {

	public TwitchChannelIdException() {
	}

	public TwitchChannelIdException(final String message) {
		super(message);
	}

	public TwitchChannelIdException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public TwitchChannelIdException(final Throwable cause) {
		super(cause);
	}
}
