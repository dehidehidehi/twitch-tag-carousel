package com.dehidehidehi.twitchtagcarousel;

import com.dehidehidehi.twitchtagcarousel.annotation.qualifier.ApplicationProperty;
import com.dehidehidehi.twitchtagcarousel.error.AuthTokenQueryException;
import com.dehidehidehi.twitchtagcarousel.error.MissingAuthTokenException;
import com.dehidehidehi.twitchtagcarousel.service.TagCarouselService;
import com.dehidehidehi.twitchtagcarousel.swing.CommandCenterPanel;
import com.dehidehidehi.twitchtagcarousel.swing.auth.AuthTokenPanel;
import com.dehidehidehi.twitchtagcarousel.swing.auth.AuthTokenValidationPanel;
import com.dehidehidehi.twitchtagcarousel.swing.util.BasicTagCarouselFrame;
import com.dehidehidehi.twitchtagcarousel.ui.BannerUi;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;

/**
 * Main entrypoint of the Swing UI.
 */
public final class SwingCarouselStartUpFrame extends BasicTagCarouselFrame implements CarouselUi {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwingCarouselStartUpFrame.class);

    private AuthTokenPanel authTokenPanel;

    @ApplicationProperty("twitch-app.auth-uri.implicit-grant-flow")
    @Inject
    private String implicitGrantFlowUriString;

    public SwingCarouselStartUpFrame() {
        super();
        setVisible(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        authTokenPanel = new AuthTokenPanel();
        add(authTokenPanel, BorderLayout.CENTER);
        pack();

        LOGGER.debug("SwingCarousel JFrame enabled.");
    }

    @Override
    public void start(TagCarouselService tagCarouselService) {
        setVisible(true);
        LOGGER.debug("SwingCarouselStartUpFrame#start entry.");
        LOGGER.info(BannerUi.getBanner());
        Executors.newSingleThreadExecutor().execute(() -> {
            handleTokenValidationUi(tagCarouselService);
        });
    }

    private void handleTokenValidationUi(TagCarouselService tagCarouselService) {
        final boolean userAuthTokenValid = isUserAuthTokenValid(tagCarouselService);
        if (!userAuthTokenValid) {
            LOGGER.info("Your user access token is not valid!");
            LOGGER.info("Please get a valid access token.");
            LOGGER.debug("userAccessToken is not valid: instantiating AuthTokenValidationPanel");
            final AuthTokenValidationPanel authTokenValidationPanel = new AuthTokenValidationPanel(tagCarouselService);
            add(authTokenValidationPanel);
            pack();
        } else {
            LOGGER.debug("userAccessToken is valid: instantiating CommandCenterPanel");
            CommandCenterPanel commandCenterPanel = new CommandCenterPanel(tagCarouselService);
            add(commandCenterPanel);
            pack();
        }
        authTokenPanel.setVisible(false);
        LOGGER.debug("Startup pane: set as disabled");
        LOGGER.debug("AuthTokenPanel done handlingAuthToken.");
    }

    /**
     * Checks if the user authentication token is valid by using the given TagCarouselService object.
     *
     * @param tagCarouselService the TagCarouselService object to use for checking the user authentication token validity
     *
     * @return true if the user authentication token is valid, false otherwise
     */
    public boolean isUserAuthTokenValid(TagCarouselService tagCarouselService) {
        final boolean userAccessTokenValid;
        try {
            final String userAccessToken = tagCarouselService.getUserAccessToken();
            userAccessTokenValid = tagCarouselService.isUserAccessTokenValid(userAccessToken);
        } catch (AuthTokenQueryException e) {
            JOptionPane.showMessageDialog(this, "An unexpected error occurred:%n%s".formatted(e.getMessage()));
            LOGGER.warn("An unexpected error occurred:%n%s".formatted(e.getMessage()));
            return false;
        } catch (MissingAuthTokenException e) {
            LOGGER.warn("We couldn't find your access token.");
            return false;
        }
        return userAccessTokenValid;
    }
}
