package com.dehidehidehi.twitchtagcarousel.swing.util;
import com.dehidehidehi.twitchtagcarousel.swing.logging.LoggingPanel;
import com.dehidehidehi.twitchtagcarousel.swing.util.label.TwitchTagTitleLabel;

import javax.swing.*;
import java.awt.*;

public abstract class BasicTagCarouselFrame extends JFrame {

    protected BasicTagCarouselFrame() {
        super();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        popUpInCenterOfTheScreen();

        add(getTopPartOfTheApplication(), BorderLayout.NORTH);
        pack();
    }

    /**
     * Top part of the application we'd always like to have.
     */
    private JPanel getTopPartOfTheApplication() {
        final JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        final TwitchTagTitleLabel twitchTagTitleLabel = new TwitchTagTitleLabel();
        centerTheTitleLabel(topPanel, twitchTagTitleLabel);

        final LoggingPanel loggingPanel = new LoggingPanel();
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

    private void popUpInCenterOfTheScreen() {
        setLocationRelativeTo(null);
    }

}
