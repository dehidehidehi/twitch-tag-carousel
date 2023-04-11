package com.dehidehidehi.twitchtagcarousel.swing.auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class AuthTokenPanel extends JPanel {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthTokenPanel.class);

    private JLabel validationLabel;
    
    public AuthTokenPanel() {
        setAccessTokenValidationLabel("Validating access token...");
        LOGGER.debug("AuthTokenPanel done instantiating.");
    }

    private void setAccessTokenValidationLabel(final String label) {
        validationLabel = new JLabel();
        validationLabel.setText(label);
        add(validationLabel, BorderLayout.CENTER);
        LOGGER.debug("AuthTokenPanel done settingValidationLabel.");
    }
}
