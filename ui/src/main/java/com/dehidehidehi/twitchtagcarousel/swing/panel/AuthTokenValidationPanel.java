package com.dehidehidehi.twitchtagcarousel.swing.panel;
import com.dehidehidehi.twitchtagcarousel.service.TwitchTagService;
import com.dehidehidehi.twitchtagcarousel.swing.button.OAuthRequestAccessTokenButton;

import javax.swing.*;

public class AuthTokenValidationPanel extends JPanel {

    public AuthTokenValidationPanel(TwitchTagService twitchTagService) {
        super();
        final var requestAccessTokenButton = new OAuthRequestAccessTokenButton(twitchTagService, CommandCenterPanel::new);
        add(requestAccessTokenButton);
    }

}
