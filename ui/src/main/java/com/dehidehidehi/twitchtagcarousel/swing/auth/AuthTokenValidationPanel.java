package com.dehidehidehi.twitchtagcarousel.swing.auth;
import com.dehidehidehi.twitchtagcarousel.service.TagCarouselService;
import com.dehidehidehi.twitchtagcarousel.swing.CommandCenterFrame;

import javax.swing.*;

public class AuthTokenValidationPanel extends JPanel {

    public AuthTokenValidationPanel(TagCarouselService tagCarouselService) {
        super();
        final var requestAccessTokenButton = new OAuthRequestAccessTokenButton(tagCarouselService,
                                                                               () -> new CommandCenterFrame(tagCarouselService));
        add(requestAccessTokenButton);
    }

}
