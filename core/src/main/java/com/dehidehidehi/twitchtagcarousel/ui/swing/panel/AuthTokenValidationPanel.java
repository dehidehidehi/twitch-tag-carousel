package com.dehidehidehi.twitchtagcarousel.ui.swing.panel;
import com.dehidehidehi.twitchtagcarousel.service.twitch.TwitchService;
import com.dehidehidehi.twitchtagcarousel.ui.swing.button.OAuthRequestAccessTokenButton;

import javax.swing.*;

public class AuthTokenValidationPanel extends JPanel {

    private final OAuthRequestAccessTokenButton requestAccessTokenButton;
    private final TwitchService twitchService;
    
    public AuthTokenValidationPanel(TwitchService twitchService) {
        super();
        this.requestAccessTokenButton = new OAuthRequestAccessTokenButton(twitchService);
        this.twitchService = twitchService;
        add(requestAccessTokenButton);
    }

    //    private JLabel validationLabel;
//
////    requestAccessTokenButton = new OAuthRequestAccessTokenButton();
////    add(requestAccessTokenButton, BorderLayout.CENTER);
////            validationLabel.setText("<html><font color='red'>Your access token could not be verified.</font></html>");
//
//    private final JLabel validationLabel;
//    AuthValidationPanel(){
//
//        validationLabel = new JLabel("Validating access token before application start...", SwingConstants.CENTER);
//        validationLabel.setFont(new Font("Arial", Font.PLAIN, 16));
//        add(validationLabel, BorderLayout.CENTER);
//    }
}
