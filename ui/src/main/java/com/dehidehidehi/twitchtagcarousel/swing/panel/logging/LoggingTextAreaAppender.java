package com.dehidehidehi.twitchtagcarousel.swing.panel.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.encoder.Encoder;

import javax.swing.*;

/**
 * Captures logging events from logback and publishes messages to the JTextArea.
 */
public class LoggingTextAreaAppender extends AppenderBase<ILoggingEvent> {

	private JTextArea textArea;
	private Encoder<ILoggingEvent> encoder;

	@Override
	protected void append(ILoggingEvent event) {
		String message;
		message = event.getFormattedMessage();
		textArea.append(message + "\n");
	}


	public void setTextArea(final JTextArea textArea) {
		this.textArea = textArea;
	}

	public void setEncoder(Encoder<ILoggingEvent> encoder) {
		this.encoder = encoder;
	}
}
