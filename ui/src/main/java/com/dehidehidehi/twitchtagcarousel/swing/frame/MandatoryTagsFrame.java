package com.dehidehidehi.twitchtagcarousel.swing.frame;
import com.dehidehidehi.twitchtagcarousel.domain.TwitchTag;
import com.dehidehidehi.twitchtagcarousel.error.TwitchTagValidationException;
import com.dehidehidehi.twitchtagcarousel.service.TagCarouselService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MandatoryTagsFrame extends JFrame {

    private static final Logger LOGGER = LoggerFactory.getLogger(MandatoryTagsFrame.class);

    private final TagCarouselService tagCarouselService;
    private JPanel mandatoryTagsPanel;
    private JTextArea tagsTextArea;
    private JButton saveButton;
    private JButton cancelButton;

    public MandatoryTagsFrame(final TagCarouselService tagCarouselService) {
        this.tagCarouselService = tagCarouselService;
        setTitle("Mandatory Tags, comma separated");
        setSize(300, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        popUpInCenterOfTheScreen(this);

        mandatoryTagsPanel = new JPanel();
        mandatoryTagsPanel.setLayout(new GridLayout(2, 1));
        
        JLabel instructions = new JLabel("Provide a comma-separated list of tags.");
        add(instructions);

        JPanel tagsPanel = setUpTagsPanel();

        JPanel buttonsPanel = new JPanel();
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);

        mandatoryTagsPanel.add(tagsPanel);
        mandatoryTagsPanel.add(buttonsPanel);
        add(mandatoryTagsPanel);

        cancelButton.addActionListener(e -> this.dispose());

        saveButton.addActionListener(e -> setTags(getTextAreaTags()));
    }

    @NotNull
    private JPanel setUpTagsPanel() {
        JLabel tagsLabel = new JLabel("Mandatory Tags:");
        tagsTextArea = new JTextArea(15, 20);
        tagsTextArea.setText(getUserSavedTags());
        JScrollPane scrollPane = new JScrollPane(tagsTextArea);
        JPanel tagsPanel = new JPanel();
        tagsPanel.add(tagsLabel);
        tagsPanel.add(scrollPane);
        return tagsPanel;
    }

    private String getUserSavedTags() {
        LOGGER.trace("Retrieving user saved tags.");
        final String userSavedTags = tagCarouselService
                .getMandatoryTags()
                .stream()
                .map(TwitchTag::toString)
                .sorted()
                .collect(Collectors.joining(","));
        LOGGER.trace("User saved tags : {}", userSavedTags);
        return userSavedTags;
    }

    private String getTextAreaTags() {
        return tagsTextArea.getText();
    }

    private void setTags(String tags) {
        LOGGER.debug("{}: Attempting to update tags : {} ", mandatoryTagsPanel.getClass().getSimpleName(), tags);
        try {
            final List<TwitchTag> twitchTags = Arrays
                    .stream(tags.split(","))
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .map(TwitchTag::new)
                    .collect(Collectors.toList());
            tagCarouselService.saveMandatoryTags(twitchTags);
            tagsTextArea.setText(tags);
            this.dispose();
        } catch (TwitchTagValidationException e) {
            final String errorMessage = """
                                        Warning, please fix this tag before proceeding.
                                        
                                        Tag validation error:
                                        
                                        %s
                                        """.formatted(e.getMessage());
            JOptionPane.showMessageDialog(mandatoryTagsPanel, errorMessage, "Tag validation error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void popUpInCenterOfTheScreen(final JFrame jFrame) {
        jFrame.setLocationRelativeTo(null);
    }
}
