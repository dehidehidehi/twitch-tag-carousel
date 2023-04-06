package com.dehidehidehi.twitchtagcarousel.swing.panel.logging;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class LoggingPanel extends JPanel {

    public static final boolean DEFAULT_LINE_WRAP = true;
    public static final int DEFAULT_ROWS = 7;
    public static final int DEFAULT_WIDTH = 450;
    public static final int DEFAULT_HEIGHT = 250;

    public LoggingPanel() {
        this(DEFAULT_ROWS, DEFAULT_LINE_WRAP);
    }

    public LoggingPanel(int rows, boolean lineWrap) {
        final JTextArea logTextArea = buildTextArea(rows, lineWrap);
        final JScrollPane scrollPane = setUpVerticalScrollBar(logTextArea);
        alwaysScrollToBottomOnTextUpdate(logTextArea, scrollPane);
        add(scrollPane, BorderLayout.CENTER);
    }

    @NotNull
    private JTextArea buildTextArea(final int rows, final boolean lineWrap) {
        final JTextArea logTextArea = LoggingTextAreaFactory.ofLogBackAppendedTextArea();
        logTextArea.setEditable(false);
        logTextArea.setLineWrap(lineWrap);
        logTextArea.setWrapStyleWord(true);
        logTextArea.setRows(rows);
        logTextArea.setAutoscrolls(true);
        logTextArea.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        return logTextArea;
    }

    @NotNull
    private static JScrollPane setUpVerticalScrollBar(final JTextArea logTextArea) {
        final JScrollPane scrollPane = new JScrollPane(logTextArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS); // always show vertical scrollbar
        scrollPane.setAutoscrolls(true);
        return scrollPane;
    }

    private void alwaysScrollToBottomOnTextUpdate(final JTextArea logTextArea, final JScrollPane scrollPane) {
        logTextArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(() -> {
                    JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
                    verticalScrollBar.setValue(verticalScrollBar.getMaximum());
                });
            }

            @Override
            public void removeUpdate(DocumentEvent e) {}

            @Override
            public void changedUpdate(DocumentEvent e) {}
        });
    }
}
