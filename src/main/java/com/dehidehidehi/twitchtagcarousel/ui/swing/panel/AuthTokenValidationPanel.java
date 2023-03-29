package com.dehidehidehi.twitchtagcarousel.ui.swing.panel;
import com.dehidehidehi.twitchtagcarousel.service.twitchclient.TwitchClient;
import com.dehidehidehi.twitchtagcarousel.ui.swing.button.OAuthRequestAccessTokenButton;

import javax.swing.*;
import java.net.URI;

public class AuthTokenValidationPanel extends JPanel {

    private final OAuthRequestAccessTokenButton requestAccessTokenButton;
    private final TwitchClient twitchClient;
    
    public AuthTokenValidationPanel(URI accessTokenRequestEndpointUri, TwitchClient twitchClient) {
        super();
        this.requestAccessTokenButton = new OAuthRequestAccessTokenButton(accessTokenRequestEndpointUri, twitchClient);
        this.twitchClient = twitchClient;
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
