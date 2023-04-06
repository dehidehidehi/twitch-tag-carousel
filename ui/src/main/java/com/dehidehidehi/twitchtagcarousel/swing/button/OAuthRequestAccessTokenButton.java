package com.dehidehidehi.twitchtagcarousel.swing.button;
import com.dehidehidehi.twitchtagcarousel.error.TwitchAuthTokenQueryException;
import com.dehidehidehi.twitchtagcarousel.service.TwitchTagService;
import com.dehidehidehi.twitchtagcarousel.swing.panel.CommandCenterPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class OAuthRequestAccessTokenButton extends JButton implements ActionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(OAuthRequestAccessTokenButton.class);

    private final TwitchTagService twitchTagService;

    /**
     * When actionPerformed finishes without an exception, then display this panel.
     */
    private final Supplier<CommandCenterPanel> onSuccessCommandCenterPanelSupplier;

    public OAuthRequestAccessTokenButton(final TwitchTagService twitchTagService, 
                                         final Supplier<CommandCenterPanel> onSuccessCommandCenterPanelSupplier) {
        super("Get Access Token");
        this.twitchTagService = twitchTagService;
        this.onSuccessCommandCenterPanelSupplier = onSuccessCommandCenterPanelSupplier;
        addActionListener(this);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        LOGGER.info("Action on button triggered : {}", OAuthRequestAccessTokenButton.class.getSimpleName());
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                twitchTagService.queryUserAccessToken();
                getParent().setEnabled(false);
                getParent().setVisible(false);
                getTopLevelAncestor().add(onSuccessCommandCenterPanelSupplier.get());
            } catch (TwitchAuthTokenQueryException ex) {
                LOGGER.error(ex.getMessage(), ex);
                JOptionPane.showMessageDialog(this, "Error starting webserver for receiving access token.");
            }
        });
    }
}
