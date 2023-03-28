package com.dehidehidehi.twitchtagcarousel.service.ui.swing.panel;
import com.dehidehidehi.twitchtagcarousel.error.TwitchUserAccessTokenException;
import com.dehidehidehi.twitchtagcarousel.service.twitchclient.TwitchClient;
import com.dehidehidehi.twitchtagcarousel.service.ui.swing.button.ExitButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class StartUpPanel extends JPanel {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartUpPanel.class);

    private final TwitchClient twitchClient;
    private JLabel validationLabel;
    
    public StartUpPanel(final TwitchClient twitchClient) {
        this.twitchClient = twitchClient;
        setCenteredTitle("Twitch Tag Carousel");
        setValidationLabel("Validating access token...");
        setExitButton();
        LOGGER.info("StartUpPanel done instantiating.");
    }
    
    public void startAuthTokenValidation() {
        final boolean userAccessTokenValid;
        try {
            userAccessTokenValid = twitchClient.isUserAccessTokenValid(twitchClient.getUserAccessToken());
        } catch (TwitchUserAccessTokenException e) {
            validationLabel.setText("An unexpected error occurred:%n%s".formatted(e.getMessage()));
            LOGGER.warn("An unexpected error occurred:%n%s".formatted(e.getMessage()));
            return;
        }
        if (userAccessTokenValid) {
            LOGGER.info("userAccessToken is valid: instantiating CommandCenterPanel");
            add(new CommandCenterPanel());
            setVisible(false);
            setEnabled(false);
            LOGGER.info("Startup pane: set as disabled");
        } else {
            LOGGER.info("userAccessToken is not valid: instantiating AuthTokenValidationPanel");
            add(new AuthTokenValidationPanel());
            setVisible(false);
            setEnabled(false);
            LOGGER.info("Startup pane: set as disabled");
        }
        LOGGER.info("StartUpPanel done handlingAuthToken.");
    }

    private void setCenteredTitle(final String title) {
        final JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);
        LOGGER.info("StartUpPanel done centering title.");
    }

    private void setValidationLabel(final String label) {
        validationLabel = new JLabel();
        validationLabel.setText(label);
        add(validationLabel, BorderLayout.CENTER);
        LOGGER.info("StartUpPanel done settingValidationLabel.");
    }

    private void setExitButton() {
        final ExitButton exitButton = new ExitButton();
        add(exitButton, BorderLayout.EAST);
        LOGGER.info("StartUpPanel setExitButton.");
    }
}
