package com.dehidehidehi.twitchtagcarousel.swing.button;
import com.dehidehidehi.twitchtagcarousel.error.TwitchAuthTokenQueryException;
import com.dehidehidehi.twitchtagcarousel.service.TwitchTagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OAuthRequestAccessTokenButton extends JButton implements ActionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(OAuthRequestAccessTokenButton.class);

    private final TwitchTagService twitchTagService;

    public OAuthRequestAccessTokenButton(final TwitchTagService twitchTagService) {
        super("Get Access Token");
        this.twitchTagService = twitchTagService;
        addActionListener(this);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        LOGGER.info("Action on button triggered : {}", OAuthRequestAccessTokenButton.class.getSimpleName());
        try {
            twitchTagService.queryUserAccessToken();
        } catch (TwitchAuthTokenQueryException ex) {
            LOGGER.error(ex.getMessage(), ex);
            JOptionPane.showMessageDialog(this, "Error starting webserver for receiving access token.");
        }
    }
}
