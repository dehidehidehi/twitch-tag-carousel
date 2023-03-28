package com.dehidehidehi.twitchtagcarousel.service.ui.swing.button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URI;
import java.net.URISyntaxException;

public class OAuthRequestAccessTokenButton extends JButton {

	private static final Logger LOGGER = LoggerFactory.getLogger(OAuthRequestAccessTokenButton.class);

	public OAuthRequestAccessTokenButton() {
		super("Get access token");
		this.addActionListener(this::openBrowserToAuthPage);
	}

	private void openBrowserToAuthPage(final ActionEvent actionEvent) {
		try {
			// Open a web page to a specified URL
			URI uri = new URI("https://example.com/oauth/authorize");
			Desktop.getDesktop().browse(uri);
		} catch (URISyntaxException ex) {
			LOGGER.warn("Invalid URL: " + ex.getMessage());
		} catch (Exception ex) {
			LOGGER.warn("Failed to open web page: " + ex.getMessage());
		}
	}
}
