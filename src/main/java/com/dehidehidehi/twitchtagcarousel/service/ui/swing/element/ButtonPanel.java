package com.dehidehidehi.twitchtagcarousel.service.ui.swing.element;
import javax.swing.*;
import java.awt.*;

public class ButtonPanel extends JPanel {

    private JButton setMandatoryTagsButton;
    private JButton setRotatingTagsButton;
    private JRadioButton autoFetchPopularTagsRadioButton;

    public ButtonPanel() {
        setLayout(new GridLayout(2, 2));
        setUpMandatoryTagsButton();
        setUpRotatingTagsButton();
        setUpAutoFetchPopularTagsRadioButton();
    }

    private void setUpAutoFetchPopularTagsRadioButton() {
        autoFetchPopularTagsRadioButton = new JRadioButton("Auto-fetch popular tags instead");
        autoFetchPopularTagsRadioButton.setEnabled(false);
        autoFetchPopularTagsRadioButton.addActionListener(e -> {
			  setRotatingTagsButton.setEnabled(!autoFetchPopularTagsRadioButton.isSelected());
        });
        add(autoFetchPopularTagsRadioButton);
    }

    private void setUpRotatingTagsButton() {
        setRotatingTagsButton = new JButton("Set rotating tags");
        setRotatingTagsButton.setEnabled(false);
        setRotatingTagsButton.addActionListener(e -> {
            // TODO: Implement set rotating tags functionality
        });
        add(setRotatingTagsButton);
    }

    private void setUpMandatoryTagsButton() {
        setMandatoryTagsButton = new JButton("Set mandatory tags");
        setMandatoryTagsButton.setEnabled(false);
        setMandatoryTagsButton.addActionListener(e -> {
            // TODO: Implement set mandatory tags functionality
        });
        add(setMandatoryTagsButton);
    }

    public void setButtonsEnabled(boolean enabled) {
        setMandatoryTagsButton.setEnabled(enabled);
        setRotatingTagsButton.setEnabled(enabled);
        autoFetchPopularTagsRadioButton.setEnabled(enabled);
    }

}
