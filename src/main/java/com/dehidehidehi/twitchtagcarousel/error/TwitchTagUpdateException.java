package com.dehidehidehi.twitchtagcarousel.error;
import java.io.IOException;

public class TwitchTagUpdateException extends IOException {

    public TwitchTagUpdateException() {
    }

    public TwitchTagUpdateException(final String message) {
        super(message);
    }

    public TwitchTagUpdateException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public TwitchTagUpdateException(final Throwable cause) {
        super(cause);
    }
}
