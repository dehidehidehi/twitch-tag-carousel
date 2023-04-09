package com.dehidehidehi.twitchtagcarousel.swing.auth;
import com.dehidehidehi.twitchtagcarousel.service.TagCarouselService;
import com.dehidehidehi.twitchtagcarousel.swing.CommandCenterPanel;

import javax.swing.*;

public class AuthTokenValidationPanel extends JPanel {

    public AuthTokenValidationPanel(TagCarouselService tagCarouselService) {
        super();
        final var requestAccessTokenButton = new OAuthRequestAccessTokenButton(tagCarouselService,
                                                                               () -> new CommandCenterPanel(tagCarouselService));
        add(requestAccessTokenButton);
    }

}