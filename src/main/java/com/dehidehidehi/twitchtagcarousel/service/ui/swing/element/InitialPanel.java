package com.dehidehidehi.twitchtagcarousel.service.ui.swing.element;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class InitialPanel extends JPanel {

    private final JLabel titleLabel;
    private final JLabel validationLabel;
    private final JButton exitButton;
    private JButton requestAccessTokenButton;

    public InitialPanel() {
        setLayout(new BorderLayout());
        titleLabel = new JLabel("Twitch Tag Caroussel", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);
        validationLabel = new JLabel("Validating access token before application start...", SwingConstants.CENTER);
        validationLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        add(validationLabel, BorderLayout.CENTER);
        exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));
        add(exitButton, BorderLayout.SOUTH);
    }

    public void setValidationFailed() {
        validationLabel.setText("<html><font color='red'>Your access token could not be verified.</font></html>");
        requestAccessTokenButton = new JButton("Request access token");
        add(requestAccessTokenButton, BorderLayout.SOUTH);
    }

    public void setRequestAccessTokenButtonEnabled(boolean enabled) {
        requestAccessTokenButton.setEnabled(enabled);
    }

    public void addRequestAccessTokenButtonListener(ActionListener listener) {
        requestAccessTokenButton.addActionListener(listener);
    }
}
