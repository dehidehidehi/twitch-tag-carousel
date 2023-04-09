package com.dehidehidehi.twitchtagcarousel.swing.tageditor;
import com.dehidehidehi.twitchtagcarousel.domain.TwitchTag;
import com.dehidehidehi.twitchtagcarousel.error.TwitchTagValidationException;
import com.dehidehidehi.twitchtagcarousel.service.TagCarouselService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RotatingTagsFrame extends TagsFrame {

	private static final Logger LOGGER = LoggerFactory.getLogger(RotatingTagsFrame.class);

	private final TagCarouselService tagCarouselService;

	public RotatingTagsFrame(final TagCarouselService tagCarouselService) {
		super();
		this.tagCarouselService = tagCarouselService;
		setTitle("Rotating Tags Editor");
		getTagTextAreaEditorPanel().getPanelLabel().setText("Rotating Tags:");
		getTagTextAreaEditorPanel().getTagsTextArea().setText(getUserSavedRotatingTags());
		getCancelButton().addActionListener(e -> this.dispose());
		getSaveButton().addActionListener(e -> setTags(getTagTextAreaEditorPanel().getTagsTextArea().getText()));
	}

	private String getUserSavedRotatingTags() {
		LOGGER.trace("Retrieving user rotating tags.");
		final String userSavedTags = tagCarouselService
				.getRotatingTags()
				.stream()
				.map(TwitchTag::toString)
				.sorted()
				.collect(Collectors.joining(",%n".formatted()));
		LOGGER.trace("User rotating tags : {}", userSavedTags);
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
			tagCarouselService.saveRotatingTags(twitchTags);
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
