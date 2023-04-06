package com.dehidehidehi.twitchtagcarousel.swing;

import com.dehidehidehi.twitchtagcarousel.CarouselUi;
import com.dehidehidehi.twitchtagcarousel.annotation.Property;
import com.dehidehidehi.twitchtagcarousel.error.TwitchAuthTokenQueryException;
import com.dehidehidehi.twitchtagcarousel.service.TwitchTagService;
import com.dehidehidehi.twitchtagcarousel.swing.label.TwitchTagTitleLabel;
import com.dehidehidehi.twitchtagcarousel.swing.panel.AuthTokenValidationPanel;
import com.dehidehidehi.twitchtagcarousel.swing.panel.CommandCenterPanel;
import com.dehidehidehi.twitchtagcarousel.swing.panel.logging.LoggingPanel;
import com.dehidehidehi.twitchtagcarousel.swing.panel.StartUpPanel;
import com.dehidehidehi.twitchtagcarousel.ui.BannerUi;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

/**
 * Main entrypoint of the Swing UI.
 */
public final class SwingCarouselUi implements CarouselUi {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwingCarouselUi.class);

    private JFrame startUpFrame;
    private StartUpPanel startUpPanel;

    @Property("twitch-app.auth-uri.implicit-grant-flow")
    @Inject
    private String implicitGrantFlowUriString;

    @Override
    public void start(TwitchTagService twitchTagService) {
        LOGGER.debug("SwingCarouselUi#start entry.");

        startUpFrame = new JFrame();
        startUpFrame.setVisible(false);
        startUpFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        startUpFrame.setLayout(new BorderLayout());
        popUpInCenterOfTheScreen(startUpFrame);
        
        final JPanel topPartOfTheApplication = getTopPartOfTheApplication();
        startUpFrame.add(topPartOfTheApplication, BorderLayout.NORTH);
        LOGGER.debug("SwingCarouselUi added startUpPanel to Container.");

        startUpPanel = new StartUpPanel();
        startUpFrame.add(startUpPanel, BorderLayout.CENTER);
        
        new Thread(() -> handleTokenValidationUi(twitchTagService)).start();

        startUpFrame.pack();
        startUpFrame.setVisible(true);
        
        LOGGER.info(BannerUi.getBanner());
        LOGGER.info("Starting application.");
        LOGGER.debug("SwingCarousel JFrame enabled.");
    }


    private void handleTokenValidationUi(TwitchTagService twitchTagService) {
        final boolean userAuthTokenValid = isUserAuthTokenValid(twitchTagService);
        if (!userAuthTokenValid) {
            LOGGER.info("Your user access token is not valid!");
            LOGGER.info("Please get a valid access token.");
            LOGGER.debug("userAccessToken is not valid: instantiating AuthTokenValidationPanel");
            final AuthTokenValidationPanel authTokenValidationPanel = new AuthTokenValidationPanel(twitchTagService);
            startUpFrame.add(authTokenValidationPanel);
            startUpFrame.pack();
        } else {
            LOGGER.debug("userAccessToken is valid: instantiating CommandCenterPanel");
            CommandCenterPanel commandCenterPanel = new CommandCenterPanel();
            startUpFrame.add(commandCenterPanel);
            startUpFrame.pack();
        }
        startUpPanel.setVisible(false);
        LOGGER.debug("Startup pane: set as disabled");
        LOGGER.debug("StartUpPanel done handlingAuthToken.");
    }

    /**
     * Top part of the application we'd always like to have.
     */
    private JPanel getTopPartOfTheApplication() {
        final JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        final TwitchTagTitleLabel twitchTagTitleLabel = new TwitchTagTitleLabel();
        centerTheTitleLabel(topPanel, twitchTagTitleLabel);

        LoggingPanel loggingPanel = new LoggingPanel();
        topPanel.add(loggingPanel);
        
        return topPanel;
    }

    private void centerTheTitleLabel(final JPanel topPanel, final TwitchTagTitleLabel twitchTagTitleLabel) {
        final JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        labelPanel.add(twitchTagTitleLabel);
        topPanel.add(Box.createVerticalGlue());
        topPanel.add(labelPanel);
        topPanel.add(Box.createVerticalGlue());
    }

    private void popUpInCenterOfTheScreen(final JFrame jFrame) {
        jFrame.setLocationRelativeTo(null);
    }

    /**
     * Checks if the user authentication token is valid by using the given TwitchTagService object.
     *
     * @param twitchTagService the TwitchTagService object to use for checking the user authentication token validity
     *
     * @return true if the user authentication token is valid, false otherwise
     */
    public boolean isUserAuthTokenValid(TwitchTagService twitchTagService) {
        final boolean userAccessTokenValid;
        try {
            userAccessTokenValid = twitchTagService.isUserAccessTokenValid(twitchTagService.getUserAccessToken());
        } catch (TwitchAuthTokenQueryException e) {
            JOptionPane.showMessageDialog(startUpFrame, "An unexpected error occurred:%n%s".formatted(e.getMessage()));
            LOGGER.warn("An unexpected error occurred:%n%s".formatted(e.getMessage()));
            return false;
        }
        return userAccessTokenValid;
    }
}
