package com.dehidehidehi.twitchtagcarousel.swing.frame;
import com.dehidehidehi.twitchtagcarousel.swing.panel.TagTextAreaEditorPanel;

import javax.swing.*;
import java.awt.*;

abstract class TagsFrame extends JFrame {

    private JButton saveButton;
    private JButton cancelButton;
    private TagTextAreaEditorPanel tagTextAreaEditorPanel;

    TagsFrame() {
        setSize(300, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        popUpInCenterOfTheScreen(this);
        setLayout(new GridLayout(3, 1));

        tagTextAreaEditorPanel = new TagTextAreaEditorPanel("[DEFAULT TEXT]");
        add(tagTextAreaEditorPanel);

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

    protected TagTextAreaEditorPanel getTagTextAreaEditorPanel() {
        return tagTextAreaEditorPanel;
    }

    protected JButton getSaveButton() {
        return saveButton;
    }

    protected JButton getCancelButton() {
        return cancelButton;
    }
}
