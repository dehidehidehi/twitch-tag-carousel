package com.dehidehidehi.twitchtagcarousel.ui.swing.button;
import com.dehidehidehi.twitchtagcarousel.error.WebServerStartException;
import com.dehidehidehi.twitchtagcarousel.service.twitchclient.TwitchClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;

public class OAuthRequestAccessTokenButton extends JButton implements ActionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(OAuthRequestAccessTokenButton.class);

    private final URI accessTokenRequestEndpointUri;
    private final TwitchClient twitchClient;

    public OAuthRequestAccessTokenButton(URI accessTokenRequestEndpointUri, final TwitchClient twitchClient) {
        super("Get Access Token");
        this.accessTokenRequestEndpointUri = accessTokenRequestEndpointUri;
        this.twitchClient = twitchClient;
        addActionListener(this);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        LOGGER.info("Action on button triggered : {}", OAuthRequestAccessTokenButton.class.getSimpleName());
        try {
            twitchClient.startAuthServer(); // beward if they spam the button it would start many servers
        } catch (WebServerStartException ex) {
            LOGGER.error(ex.getMessage(), ex);
            JOptionPane.showMessageDialog(this, "Error starting webserver for receiving access token.");
				return;
        }
		  
        openBrowserToAuthPage();
		  
//        try {
//            Thread.currentThread().join(); // prevents server from just shutting down.
//        } catch (InterruptedException ex) {
//            LOGGER.error(ex.getMessage(), ex);
//            JOptionPane.showMessageDialog(this, "Error starting webserver for receiving access token.");
//				return;
//        }
        //		twitchClient.closeAuthServlet();
    }

    /**
     * Open a web page to a specified URL.
     */
    private void openBrowserToAuthPage() {
        try {
            Desktop.getDesktop().browse(accessTokenRequestEndpointUri);
        } catch (Exception ex) {
            LOGGER.warn("Failed to open web page: " + ex.getMessage());
        }
    }
}
