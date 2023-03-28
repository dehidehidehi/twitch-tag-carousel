package com.dehidehidehi.twitchtagcarousel.service.ui.swing;

import com.dehidehidehi.twitchtagcarousel.service.twitchclient.TwitchClient;
import com.dehidehidehi.twitchtagcarousel.service.ui.CarouselUi;
import com.dehidehidehi.twitchtagcarousel.service.ui.swing.panel.LoggingPanel;
import com.dehidehidehi.twitchtagcarousel.service.ui.swing.panel.StartUpPanel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Typed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

/**
 * Main entrypoint of the Swing UI.
 */
@com.dehidehidehi.twitchtagcarousel.service.ui.swing.annotation.SwingCarouselUi
@Typed(CarouselUi.class)
@ApplicationScoped
final class SwingCarouselUi implements CarouselUi {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwingCarouselUi.class);

    @Override
    public void start(TwitchClient twitchClient) {
        LOGGER.info("SwingCarouselUi#start entry.");
        final JFrame startUpFrame = new JFrame();
        startUpFrame.setVisible(false);
        startUpFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        startUpFrame.setLayout(new BorderLayout());
        popUpInCenterOfTheScreen(startUpFrame);

        addPanels(twitchClient, startUpFrame);

        startUpFrame.pack();
        startUpFrame.setVisible(true);
        LOGGER.info("SwingCarousel JFrame enabled.");
        
//        startUpPanel.startAuthTokenValidation();
    }

    private void addPanels(final TwitchClient twitchClient, final JFrame startUpFrame) {
        final LoggingPanel loggingPanel = new LoggingPanel();
        startUpFrame.add(loggingPanel, BorderLayout.NORTH);
        
        final StartUpPanel startUpPanel = new StartUpPanel(twitchClient);
        startUpFrame.add(startUpPanel, BorderLayout.CENTER);
        
        LOGGER.info("SwingCarouselUi added startUpPanel to Container.");
    }

    private void popUpInCenterOfTheScreen(final JFrame jFrame) {
        jFrame.setLocationRelativeTo(null);
    }
}
