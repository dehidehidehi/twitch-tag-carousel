package com.dehidehidehi.twitchtagcarousel.swing;
import com.dehidehidehi.twitchtagcarousel.service.TagCarouselService;
import com.dehidehidehi.twitchtagcarousel.swing.tags.MandatoryTagsFrame;
import com.dehidehidehi.twitchtagcarousel.swing.tags.RotatingTagsFrame;
import com.dehidehidehi.twitchtagcarousel.swing.tags.TagUpdaterTagCarouselFrame;
import com.dehidehidehi.twitchtagcarousel.swing.util.BasicTagCarouselFrame;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;

public class CommandCenterFrame extends BasicTagCarouselFrame {

    private final TagCarouselService tagCarouselService;
    private JPanel commandCenterPanel;
    private JButton editMandatoryTagsButton;
    private JButton editRotatingTagsButton;
    private JButton startCarouselButton;
    private JRadioButton autoFetchPopularTagsRadioButton;

    public CommandCenterFrame(final TagCarouselService tagCarouselService) {
        this.tagCarouselService = tagCarouselService;

        commandCenterPanel = new JPanel();
        commandCenterPanel.setLayout(new GridLayout(3, 2));
        setUpEditMandatoryTagsButton();
        setUpEditRotatingTagsButton();
        setUpStartCarouselButton();
        //        setUpAutoFetchPopularTagsRadioButton();
        add(commandCenterPanel);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void setUpStartCarouselButton() {
        startCarouselButton = new JButton("Start tag updater");
        startCarouselButton.addActionListener(e -> {
            final boolean noTags =
                    tagCarouselService.getMandatoryTags().isEmpty() && tagCarouselService.getRotatingTags().isEmpty();
            if (noTags) {
                final String message = "You must set at least one tag before using the auto updater.";
                JOptionPane.showMessageDialog(this, message, "Reminder", JOptionPane.INFORMATION_MESSAGE);
            } else {
                Executors.newSingleThreadExecutor().execute(() -> {
                    new TagUpdaterTagCarouselFrame(tagCarouselService);
                });
                dispose();
            }
        });
        commandCenterPanel.add(startCarouselButton);
    }

    private void setUpEditRotatingTagsButton() {
        editRotatingTagsButton = new JButton("Set rotating tags");
        editRotatingTagsButton.addActionListener(e -> {
            final RotatingTagsFrame rotatingTagsFrame = new RotatingTagsFrame(tagCarouselService);
            rotatingTagsFrame.setVisible(true);
        });
        commandCenterPanel.add(editRotatingTagsButton);
    }

    private void setUpEditMandatoryTagsButton() {
        editMandatoryTagsButton = new JButton("Set mandatory tags");
        editMandatoryTagsButton.addActionListener(e -> {
            final MandatoryTagsFrame mandatoryTagsFrame = new MandatoryTagsFrame(tagCarouselService);
            mandatoryTagsFrame.setVisible(true);
        });
        commandCenterPanel.add(editMandatoryTagsButton);
    }

    private void setUpAutoFetchPopularTagsRadioButton() {
        autoFetchPopularTagsRadioButton = new JRadioButton("Auto-fetch popular tags instead");
        autoFetchPopularTagsRadioButton.setEnabled(false);
        autoFetchPopularTagsRadioButton.addActionListener(e -> {
            editRotatingTagsButton.setEnabled(!autoFetchPopularTagsRadioButton.isSelected());
        });
        commandCenterPanel.add(autoFetchPopularTagsRadioButton);
    }

}
