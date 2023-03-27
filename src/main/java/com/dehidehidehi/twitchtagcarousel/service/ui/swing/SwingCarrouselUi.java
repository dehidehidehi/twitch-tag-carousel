package com.dehidehidehi.twitchtagcarousel.service.ui.swing;

import com.dehidehidehi.twitchtagcarousel.service.ui.CarrouselUi;
import com.dehidehidehi.twitchtagcarousel.service.ui.swing.element.ButtonPanel;
import com.dehidehidehi.twitchtagcarousel.service.ui.swing.element.InitialPanel;
import com.dehidehidehi.twitchtagcarousel.service.ui.swing.element.LoggingPanel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Typed;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URI;
import java.net.URISyntaxException;

@com.dehidehidehi.twitchtagcarousel.service.ui.swing.annotation.SwingCarrouselUi
@Typed(CarrouselUi.class)
@ApplicationScoped
class SwingCarrouselUi extends JFrame implements CarrouselUi {

    private InitialPanel initialPanel;
    private ButtonPanel buttonPanel;
    private LoggingPanel loggingPanel;

    SwingCarrouselUi() {
        setUpLoggingPanel();
        setUpInitialPanel();
        setUpButtonPanel();
    }

    @Override
    public void start() {
        initialPanel.setValidationFailed();
        initialPanel.setRequestAccessTokenButtonEnabled(true);
        initialPanel.addRequestAccessTokenButtonListener(this::openBrowerToAuthPage);
        buttonPanel.setButtonsEnabled(false);
        log("Application started.");
    }

    private void openBrowerToAuthPage(final ActionEvent actionEvent) {
        try {
            // Open a web page to a specified URL
            URI uri = new URI("https://example.com/oauth/authorize");
            Desktop.getDesktop().browse(uri);
        } catch (URISyntaxException ex) {
            log("Invalid URL: " + ex.getMessage());
        } catch (Exception ex) {
            log("Failed to open web page: " + ex.getMessage());
        } finally {
            initialPanel.setRequestAccessTokenButtonEnabled(false);
        }
    }

    private void setUpInitialPanel() {
        initialPanel = new InitialPanel();
        setContentPane(initialPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void setUpButtonPanel() {
        buttonPanel = new ButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void setUpLoggingPanel() {
        loggingPanel = new LoggingPanel();
        add(loggingPanel, BorderLayout.CENTER);
    }

    public void log(String message) {
        loggingPanel.log(message);
    }
}
