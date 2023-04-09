package com.dehidehidehi.twitchtagcarousel.swing.frame;
import com.dehidehidehi.twitchtagcarousel.domain.TwitchTag;
import com.dehidehidehi.twitchtagcarousel.error.TwitchTagValidationException;
import com.dehidehidehi.twitchtagcarousel.service.TagCarouselService;
import com.dehidehidehi.twitchtagcarousel.swing.panel.TagTextAreaEditorPanel;
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
    private TagTextAreaEditorPanel mandatoryTagsTextAreaEditorPanel;
    private JButton saveButton;
    private JButton cancelButton;

    public MandatoryTagsFrame(final TagCarouselService tagCarouselService) {
        this.tagCarouselService = tagCarouselService;
        setTitle("Mandatory Tags, comma separated");
        setSize(300, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        popUpInCenterOfTheScreen(this);
        setLayout(new GridLayout(2, 1));
        
        mandatoryTagsTextAreaEditorPanel = setUpMandatoryTagsEditorPanel();
        add(mandatoryTagsTextAreaEditorPanel);
        
        // setup buttons panel
        final JPanel buttonsPanel = new JPanel();
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);
        add(buttonsPanel);

        cancelButton.addActionListener(e -> this.dispose());
        saveButton.addActionListener(e -> setTags(getTextAreaTags()));
    }

    @NotNull
    private TagTextAreaEditorPanel setUpMandatoryTagsEditorPanel() {
        final TagTextAreaEditorPanel tagTextAreaEditorPanel = new TagTextAreaEditorPanel("Mandatory Tags:");
        tagTextAreaEditorPanel.getTagsTextArea().setText(getUserSavedTags());    
        return tagTextAreaEditorPanel;
    }

    private String getUserSavedTags() {
        LOGGER.trace("Retrieving user saved tags.");
        final String userSavedTags = tagCarouselService
                .getMandatoryTags()
                .stream()
                .map(TwitchTag::toString)
                .sorted()
                .collect(Collectors.joining(",%n".formatted()));
        LOGGER.trace("User saved tags : {}", userSavedTags);
        return userSavedTags;
    }

    private String getTextAreaTags() {
        return mandatoryTagsTextAreaEditorPanel.getTagsTextArea().getText();
    }

    private void setTags(String tags) {
        LOGGER.debug("{}: Attempting to update tags : {} ", mandatoryTagsTextAreaEditorPanel.getClass().getSimpleName(), tags);
        try {
            final List<TwitchTag> twitchTags = Arrays
                    .stream(tags.split(","))
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .map(TwitchTag::new)
                    .collect(Collectors.toList());
            tagCarouselService.saveMandatoryTags(twitchTags);
            mandatoryTagsTextAreaEditorPanel.getTagsTextArea().setText(tags);
            this.dispose();
        } catch (TwitchTagValidationException e) {
            final String errorMessage = """
                                        Warning, please fix this tag before proceeding.
                                        
                                        Tag validation error:
                                        
                                        %s
                                        """.formatted(e.getMessage());
            JOptionPane.showMessageDialog(mandatoryTagsTextAreaEditorPanel, errorMessage, "Tag validation error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void popUpInCenterOfTheScreen(final JFrame jFrame) {
        jFrame.setLocationRelativeTo(null);
    }
}
