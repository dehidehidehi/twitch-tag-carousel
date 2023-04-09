package com.dehidehidehi.twitchtagcarousel.swing.util.label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class TwitchTagTitleLabel extends JLabel {

	private static final Logger LOGGER = LoggerFactory.getLogger(TwitchTagTitleLabel.class);

	public TwitchTagTitleLabel() {
		super("Twitch Tag Carousel", SwingConstants.CENTER);
		this.setFont(new Font("Arial", Font.BOLD, 24));
		LOGGER.trace("AuthTokenPanel done centering title.");
	}
}
