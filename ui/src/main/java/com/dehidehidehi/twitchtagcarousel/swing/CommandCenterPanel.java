package com.dehidehidehi.twitchtagcarousel.swing;
import com.dehidehidehi.twitchtagcarousel.service.TagCarouselService;
import com.dehidehidehi.twitchtagcarousel.swing.tags.MandatoryTagsFrame;
import com.dehidehidehi.twitchtagcarousel.swing.tags.RotatingTagsFrame;
import com.dehidehidehi.twitchtagcarousel.swing.tags.TagUpdaterTagCarouselFrame;

import javax.swing.*;
import java.awt.*;

public class CommandCenterPanel extends JPanel {

    private final TagCarouselService tagCarouselService;
    private JButton editMandatoryTagsButton;
    private JButton editRotatingTagsButton;
    private JButton startCarouselButton;
    private JRadioButton autoFetchPopularTagsRadioButton;

    public CommandCenterPanel(final TagCarouselService tagCarouselService) {
        this.tagCarouselService = tagCarouselService;
        setLayout(new GridLayout(3, 2));
        setUpEditMandatoryTagsButton();
        setUpEditRotatingTagsButton();
        setUpStartCarouselButton();
        //        setUpAutoFetchPopularTagsRadioButton();
    }

    private void setUpStartCarouselButton() {
        startCarouselButton = new JButton("Start tag updater");
        startCarouselButton.addActionListener(e -> {
            final boolean noTags = tagCarouselService.getMandatoryTags().isEmpty() && tagCarouselService.getRotatingTags().isEmpty();
            if (noTags) {
                final String message = "You must set at least one tag before using the auto updater.";
                JOptionPane.showMessageDialog(this, message, "Reminder", JOptionPane.INFORMATION_MESSAGE);
            } else {
                new TagUpdaterTagCarouselFrame(tagCarouselService);
                remove(this);
            }
        });
        add(startCarouselButton);
    }

    private void setUpEditRotatingTagsButton() {
        editRotatingTagsButton = new JButton("Set rotating tags");
        editRotatingTagsButton.addActionListener(e -> {
            final RotatingTagsFrame rotatingTagsFrame = new RotatingTagsFrame(tagCarouselService);
            rotatingTagsFrame.setVisible(true);
        });
        add(editRotatingTagsButton);
    }

    private void setUpEditMandatoryTagsButton() {
        editMandatoryTagsButton = new JButton("Set mandatory tags");
        editMandatoryTagsButton.addActionListener(e -> {
            final MandatoryTagsFrame mandatoryTagsFrame = new MandatoryTagsFrame(tagCarouselService);
            mandatoryTagsFrame.setVisible(true);
        });
        add(editMandatoryTagsButton);
    }

    private void setUpAutoFetchPopularTagsRadioButton() {
        autoFetchPopularTagsRadioButton = new JRadioButton("Auto-fetch popular tags instead");
        autoFetchPopularTagsRadioButton.setEnabled(false);
        autoFetchPopularTagsRadioButton.addActionListener(e -> {
            editRotatingTagsButton.setEnabled(!autoFetchPopularTagsRadioButton.isSelected());
        });
        add(autoFetchPopularTagsRadioButton);
    }

}
