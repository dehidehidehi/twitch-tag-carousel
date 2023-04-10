package com.dehidehidehi.twitchtagcarousel.swing.tags;
import com.dehidehidehi.twitchtagcarousel.error.MissingAuthTokenException;
import com.dehidehidehi.twitchtagcarousel.error.MissingUserProvidedTagsException;
import com.dehidehidehi.twitchtagcarousel.error.TwitchTagUpdateException;
import com.dehidehidehi.twitchtagcarousel.service.TagCarouselService;
import com.dehidehidehi.twitchtagcarousel.swing.CommandCenterFrame;
import com.dehidehidehi.twitchtagcarousel.swing.util.BasicTagCarouselFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TagUpdaterTagCarouselFrame extends BasicTagCarouselFrame {

    public static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;
    private static final Logger LOGGER = LoggerFactory.getLogger(TagUpdaterTagCarouselFrame.class);

    private final long startDelaySeconds = 1;
    private final long tagRotationFrequencySeconds = 300;
    private final TagCarouselService tagCarouselService;

    private JButton goBackButton;
    private JButton resumeButton;
    private JButton pauseButton;
    private ExecutorService tagUpdaterExecutorService;

    public TagUpdaterTagCarouselFrame(final TagCarouselService tagCarouselService) {
        super();
        this.tagCarouselService = tagCarouselService;
        setVisible(true);

        final JPanel buttonsPanel = new JPanel();
        setUpGoBackButton();
        buttonsPanel.add(goBackButton);
        setUpPauseButton();
        buttonsPanel.add(pauseButton);
        setUpResumeButton();
        buttonsPanel.add(resumeButton);
        add(buttonsPanel);

        pack();

        tagUpdaterExecutorService = startAutoTagUpdaterExecutor();
    }

    private JButton setUpPauseButton() {
        pauseButton = new JButton("Pause");
        pauseButton.addActionListener(e -> {
            LOGGER.info("Pausing in progress...");
            pauseButton.setEnabled(false);
            resumeButton.setEnabled(false);
            goBackButton.setEnabled(false);
            if (tagUpdaterExecutorService != null) {
                Executors.newSingleThreadExecutor().execute(() -> {
                    try {
                        tagUpdaterExecutorService.awaitTermination(3, TimeUnit.SECONDS);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    } finally {
                        LOGGER.info("Successfully paused auto tag updater.");
                        resumeButton.setEnabled(true);
                        goBackButton.setEnabled(true);
                    }
                });
            }
        });
        return pauseButton;
    }

    private JButton setUpResumeButton() {
        resumeButton = new JButton("Resume");
        resumeButton.setEnabled(false);
        resumeButton.addActionListener(e -> {
            LOGGER.info("Starting auto tag updater");
            pauseButton.setEnabled(true);
            goBackButton.setEnabled(false);
            tagUpdaterExecutorService = startAutoTagUpdaterExecutor();
        });
        return resumeButton;
    }

    private ExecutorService startAutoTagUpdaterExecutor() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        Runnable startUpdatingTags = () -> {
            try {
                LOGGER.info("Starting auto-update tag service.");
                tagCarouselService.updateTags();
            } catch (MissingAuthTokenException | TwitchTagUpdateException | MissingUserProvidedTagsException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), e.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "An unhandled error occurred: %n %s".formatted(e.getMessage()), e.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
            }
        };
        executorService.scheduleAtFixedRate(startUpdatingTags, startDelaySeconds, tagRotationFrequencySeconds, TIME_UNIT);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Stopped auto-update tag service.");
            LOGGER.debug("Shutting down executor service.");
            executorService.shutdown();
        }));
        return executorService;
    }

    private JButton setUpGoBackButton() {
        goBackButton = new JButton("Go back");
        goBackButton.setEnabled(false);
        goBackButton.addActionListener(e -> {
            Executors.newSingleThreadExecutor().execute(() -> {
                new CommandCenterFrame(tagCarouselService);
            });
            dispose();
        });
        return goBackButton;
    }
}
