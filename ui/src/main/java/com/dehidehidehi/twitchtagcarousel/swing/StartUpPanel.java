package com.dehidehidehi.twitchtagcarousel.swing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class StartUpPanel extends JPanel {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartUpPanel.class);

    private JLabel validationLabel;
    
    public StartUpPanel() {
        setAccessTokenValidationLabel("Validating access token...");
        LOGGER.debug("StartUpPanel done instantiating.");
    }

    private void setAccessTokenValidationLabel(final String label) {
        validationLabel = new JLabel();
        validationLabel.setText(label);
        add(validationLabel, BorderLayout.CENTER);
        LOGGER.debug("StartUpPanel done settingValidationLabel.");
    }
}
