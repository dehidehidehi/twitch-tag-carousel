package com.dehidehidehi.twitchtagcarousel.swing.tags;

import javax.swing.*;
import java.awt.*;

abstract class TagsFrame extends JFrame {

    private JButton saveButton;
    private JButton cancelButton;
    private TagEditorPanel tagEditorPanel;

    TagsFrame() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        popUpInCenterOfTheScreen(this);
        setLayout(new BorderLayout(50, 20));
        setSize(250, 200);

        tagEditorPanel = new TagEditorPanel("[DEFAULT TEXT]");
        add(tagEditorPanel, BorderLayout.CENTER);

        final JPanel buttonsPanel = new JPanel();
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);
        add(buttonsPanel, BorderLayout.SOUTH);
        
        pack();
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
