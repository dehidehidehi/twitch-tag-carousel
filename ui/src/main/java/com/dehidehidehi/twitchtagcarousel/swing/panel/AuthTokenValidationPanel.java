package com.dehidehidehi.twitchtagcarousel.swing.panel;
import com.dehidehidehi.twitchtagcarousel.service.TagCarouselService;
import com.dehidehidehi.twitchtagcarousel.swing.button.OAuthRequestAccessTokenButton;

import javax.swing.*;

public class AuthTokenValidationPanel extends JPanel {

    public AuthTokenValidationPanel(TagCarouselService tagCarouselService) {
        super();
        final var requestAccessTokenButton = new OAuthRequestAccessTokenButton(tagCarouselService,
                                                                               () -> new CommandCenterPanel(tagCarouselService));
        add(requestAccessTokenButton);
    }

}
