package com.dehidehidehi.twitchtagcarousel.swing.tageditor;

import javax.swing.*;
import java.awt.*;

abstract class TagsFrame extends JFrame {

    private JButton saveButton;
    private JButton cancelButton;
    private TagEditorPanel tagEditorPanel;

    TagsFrame() {
        setSize(300, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        popUpInCenterOfTheScreen(this);
        setLayout(new GridLayout(3, 1));

        tagEditorPanel = new TagEditorPanel("[DEFAULT TEXT]");
        add(tagEditorPanel);

        final JPanel buttonsPanel = new JPanel();
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);
        add(buttonsPanel);
    }

    private void popUpInCenterOfTheScreen(final JFrame jFrame) {
        jFrame.setLocationRelativeTo(null);
    }

    protected TagEditorPanel getTagTextAreaEditorPanel() {
        return tagEditorPanel;
    }

    protected JButton getSaveButton() {
        return saveButton;
    }

    protected JButton getCancelButton() {
        return cancelButton;
    }
}
