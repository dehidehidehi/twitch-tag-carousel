package com.dehidehidehi.twitchtagcarousel.swing.tags;
import com.dehidehidehi.twitchtagcarousel.domain.TwitchTag;
import com.dehidehidehi.twitchtagcarousel.error.TwitchTagValidationException;
import com.dehidehidehi.twitchtagcarousel.service.TagCarouselService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MandatoryTagsFrame extends TagsFrame {

    private static final Logger LOGGER = LoggerFactory.getLogger(MandatoryTagsFrame.class);

    private final TagCarouselService tagCarouselService;

    public MandatoryTagsFrame(final TagCarouselService tagCarouselService) {
        super();
        this.tagCarouselService = tagCarouselService;
        setTitle("Mandatory Tags Editor");
        getTagTextAreaEditorPanel().getPanelLabel().setText("Mandatory Tags:");
        getTagTextAreaEditorPanel().getTagsTextArea().setText(getUserSavedMandatoryTags());
        getCancelButton().addActionListener(e -> this.dispose());
        getSaveButton().addActionListener(e -> setTags(getTagTextAreaEditorPanel().getTagsTextArea().getText()));
        pack();
    }

    private String getUserSavedMandatoryTags() {
        LOGGER.trace("Retrieving user mandatory tags.");
        final String userSavedTags = tagCarouselService
                .getMandatoryTags()
                .stream()
                .map(TwitchTag::toString)
                .sorted()
                .collect(Collectors.joining(",%n".formatted()));
        LOGGER.trace("User mandatory tags : {}", userSavedTags);
        return userSavedTags;
    }

    private void setTags(String tags) {
        LOGGER.debug("{}: Attempting to update tags : {} ", getTagTextAreaEditorPanel().getClass().getSimpleName(), tags);
        try {
            final List<TwitchTag> twitchTags = Arrays
                    .stream(tags.split(","))
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .map(TwitchTag::new)
                    .collect(Collectors.toList());
            tagCarouselService.saveMandatoryTags(twitchTags);
            getTagTextAreaEditorPanel().getTagsTextArea().setText(tags);
            this.dispose();
        } catch (TwitchTagValidationException e) {
            final String errorMessage = """
                                        Warning, please fix this tag before proceeding.
                                                                                
                                        Tag validation error:
                                                                                
                                        %s
                                        """.formatted(e.getMessage());
            JOptionPane.showMessageDialog(getTagTextAreaEditorPanel(),
                                          errorMessage,
                                          "Tag validation error",
                                          JOptionPane.WARNING_MESSAGE);
        }
    }
}
