package com.dehidehidehi.twitchtagcarousel.swing.tags;
import javax.swing.*;
import java.awt.*;

/**
 * Common panel used for editing any sort of tags.
 */
public class TagEditorPanel extends JPanel {

    private JTextArea tagsTextArea;
    private JLabel panelLabel;

    public TagEditorPanel(final String tagsLabelText) {
        super();
        setLayout(new BorderLayout(50, 20));

        panelLabel = new JLabel(tagsLabelText);
        panelLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        add(panelLabel, BorderLayout.NORTH);

        tagsTextArea = new JTextArea(15, 20);
        tagsTextArea.setLineWrap(true);
        tagsTextArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(tagsTextArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);

    }

    public JLabel getPanelLabel() {
        return panelLabel;
    }

    public JTextArea getTagsTextArea() {
        return tagsTextArea;
    }
}
