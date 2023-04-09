package com.dehidehidehi.twitchtagcarousel.swing.panel;
import javax.swing.*;

/**
 * Common panel used for editing any sort of tags.
 */
public class TagTextAreaEditorPanel extends JPanel {

    private JTextArea tagsTextArea;
    private JLabel panelLabel;

    public TagTextAreaEditorPanel(final String tagsLabelText) {
        super();

        panelLabel = new JLabel(tagsLabelText);
        add(panelLabel);

        tagsTextArea = new JTextArea(15, 20);
        tagsTextArea.setSize(250, 350);
        add(tagsTextArea);

        JScrollPane scrollPane = new JScrollPane(tagsTextArea);
        add(scrollPane);
    }

    public JLabel getPanelLabel() {
        return panelLabel;
    }

    public JTextArea getTagsTextArea() {
        return tagsTextArea;
    }
}
