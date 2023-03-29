package com.dehidehidehi.twitchtagcarousel.ui.swing;

import com.dehidehidehi.twitchtagcarousel.annotation.Property;
import com.dehidehidehi.twitchtagcarousel.error.TwitchUserAccessTokenException;
import com.dehidehidehi.twitchtagcarousel.service.twitchclient.TwitchClient;
import com.dehidehidehi.twitchtagcarousel.ui.CarouselUi;
import com.dehidehidehi.twitchtagcarousel.ui.swing.label.TwitchTagTitleLabel;
import com.dehidehidehi.twitchtagcarousel.ui.swing.panel.AuthTokenValidationPanel;
import com.dehidehidehi.twitchtagcarousel.ui.swing.panel.CommandCenterPanel;
import com.dehidehidehi.twitchtagcarousel.ui.swing.panel.LoggingPanel;
import com.dehidehidehi.twitchtagcarousel.ui.swing.panel.StartUpPanel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Typed;
import jakarta.inject.Inject;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.net.URI;

/**
 * Main entrypoint of the Swing UI.
 */
@com.dehidehidehi.twitchtagcarousel.ui.swing.annotation.SwingCarouselUi
@Typed(CarouselUi.class)
@ApplicationScoped
final class SwingCarouselUi implements CarouselUi {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwingCarouselUi.class);

    private JFrame startUpFrame;
    private StartUpPanel startUpPanel;
    private LoggingPanel loggingPanel;
    private AuthTokenValidationPanel authTokenValidationPanel;
    private CommandCenterPanel commandCenterPanel;

    @Property("twitch-app.oauth.uri")
    @Inject
    private String accessTokenRequestEndpoint;

    @Override
    public void start(TwitchClient twitchClient) {
        LOGGER.info("SwingCarouselUi#start entry.");
        startUpFrame = new JFrame();
        startUpFrame.setVisible(false);
        startUpFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        startUpFrame.setLayout(new BorderLayout());
        popUpInCenterOfTheScreen(startUpFrame);
        
        final JPanel topPartOfTheApplication = getTopPartOfTheApplication();
        startUpFrame.add(topPartOfTheApplication, BorderLayout.NORTH);
        LOGGER.info("SwingCarouselUi added startUpPanel to Container.");

        startUpPanel = new StartUpPanel();
        startUpFrame.add(startUpPanel, BorderLayout.CENTER);
        
        new Thread(() -> handleTokenValidationUi(twitchClient)).start();

        startUpFrame.pack();
        startUpFrame.setVisible(true);
        LOGGER.info("SwingCarousel JFrame enabled.");
    }


    /**
     * Top part of the application we'd always like to have.
     */
    private JPanel getTopPartOfTheApplication() {
        final JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        final TwitchTagTitleLabel twitchTagTitleLabel = new TwitchTagTitleLabel();
        centerTheTitleLabel(topPanel, twitchTagTitleLabel);

        loggingPanel = new LoggingPanel();
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

    private void handleTokenValidationUi(TwitchClient twitchClient) {
        final boolean userAuthTokenValid = isUserAuthTokenValid(twitchClient);
        if (!userAuthTokenValid) {
            LOGGER.info("userAccessToken is not valid: instantiating AuthTokenValidationPanel");
            authTokenValidationPanel = new AuthTokenValidationPanel(getAccessTokenRequestEndpointUri(), twitchClient);
            startUpFrame.add(authTokenValidationPanel);
            startUpFrame.pack();
        } else {
            LOGGER.info("userAccessToken is valid: instantiating CommandCenterPanel");
            commandCenterPanel = new CommandCenterPanel();
            startUpFrame.add(commandCenterPanel);
            startUpFrame.pack();
        }
        startUpPanel.setVisible(false);
        LOGGER.info("Startup pane: set as disabled");
        LOGGER.info("StartUpPanel done handlingAuthToken.");
    }

    @NotNull
    @SneakyThrows
    private URI getAccessTokenRequestEndpointUri() {
        return new URI(accessTokenRequestEndpoint);
    }

    /**
     * Checks if the user authentication token is valid by using the given TwitchClient object.
     *
     * @param twitchClient the TwitchClient object to use for checking the user authentication token validity
     *
     * @return true if the user authentication token is valid, false otherwise
     */
    public boolean isUserAuthTokenValid(TwitchClient twitchClient) {
        final boolean userAccessTokenValid;
        try {
            userAccessTokenValid = twitchClient.isUserAccessTokenValid(twitchClient.getUserAccessToken());
        } catch (TwitchUserAccessTokenException e) {
            JOptionPane.showMessageDialog(startUpFrame, "An unexpected error occurred:%n%s".formatted(e.getMessage()));
            LOGGER.warn("An unexpected error occurred:%n%s".formatted(e.getMessage()));
            return false;
        }
        return userAccessTokenValid;
    }
}
