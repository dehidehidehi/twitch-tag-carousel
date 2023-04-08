package com.dehidehidehi.twitchtagcarousel.error;
public class MissingUserProvidedTagsException extends Exception {

    public MissingUserProvidedTagsException() {
    }

    public MissingUserProvidedTagsException(final String message) {
        super(message);
    }

    public MissingUserProvidedTagsException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public MissingUserProvidedTagsException(final Throwable cause) {
        super(cause);
    }

    public MissingUserProvidedTagsException(final String message,
                                            final Throwable cause,
                                            final boolean enableSuppression,
                                            final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
