package com.dehidehidehi.twitchtagcarousel.swing.auth;
import com.dehidehidehi.twitchtagcarousel.error.AuthTokenQueryException;
import com.dehidehidehi.twitchtagcarousel.service.TagCarouselService;
import com.dehidehidehi.twitchtagcarousel.swing.CommandCenterFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class OAuthRequestAccessTokenButton extends JButton implements ActionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(OAuthRequestAccessTokenButton.class);

    private final TagCarouselService tagCarouselService;

    /**
     * When actionPerformed finishes without an exception, then display this panel.
     */
    private final Supplier<CommandCenterFrame> onSuccessCommandCenterPanelSupplier;

    public OAuthRequestAccessTokenButton(final TagCarouselService tagCarouselService, 
                                         final Supplier<CommandCenterFrame> onSuccessCommandCenterPanelSupplier) {
        super("Get Access Token");
        this.tagCarouselService = tagCarouselService;
        this.onSuccessCommandCenterPanelSupplier = onSuccessCommandCenterPanelSupplier;
        addActionListener(this);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        LOGGER.debug("Action on button triggered : {}", OAuthRequestAccessTokenButton.class.getSimpleName());
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                tagCarouselService.queryUserAccessToken();
                getParent().setEnabled(false);
                getParent().setVisible(false);
                ((JFrame) getTopLevelAncestor()).dispose();
                onSuccessCommandCenterPanelSupplier.get();  // create new frame
            } catch (AuthTokenQueryException ex) {
                LOGGER.error(ex.getMessage(), ex);
                JOptionPane.showMessageDialog(this, "Error starting webserver for receiving access token.");
            }
        });
    }
}
