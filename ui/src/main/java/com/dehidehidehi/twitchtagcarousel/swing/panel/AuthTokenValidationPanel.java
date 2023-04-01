package com.dehidehidehi.twitchtagcarousel.swing.panel;
import com.dehidehidehi.twitchtagcarousel.service.TwitchTagService;
import com.dehidehidehi.twitchtagcarousel.swing.button.OAuthRequestAccessTokenButton;

import javax.swing.*;

public class AuthTokenValidationPanel extends JPanel {

    private final OAuthRequestAccessTokenButton requestAccessTokenButton;
    private final TwitchTagService twitchTagService;
    
    public AuthTokenValidationPanel(TwitchTagService twitchTagService) {
        super();
        this.requestAccessTokenButton = new OAuthRequestAccessTokenButton(twitchTagService);
        this.twitchTagService = twitchTagService;
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
