package com.dehidehidehi.twitchtagcarousel.swing.panel;
import com.dehidehidehi.twitchtagcarousel.swing.types.TextAreaOutputStream;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.io.PrintStream;

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
        redirectLogsToStdoutAndStderr(logTextArea);
        final JScrollPane scrollPane = buildScrollPane(logTextArea);
        add(scrollPane, BorderLayout.CENTER);
    }

    @NotNull
    private static JScrollPane buildScrollPane(final JTextArea logTextArea) {
        final JScrollPane scrollPane = new JScrollPane(logTextArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS); // always show vertical scrollbar
        scrollPane.setAutoscrolls(true);
        return scrollPane;
    }

    @NotNull
    private JTextArea buildTextArea(final int rows, final boolean lineWrap) {
        final JTextArea logTextArea;
        logTextArea = new JTextArea();
        logTextArea.setEditable(false);
        logTextArea.setLineWrap(lineWrap);
        logTextArea.setWrapStyleWord(true);
        logTextArea.setRows(rows);
        logTextArea.setAutoscrolls(true);
        logTextArea.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        return logTextArea;
    }

    private void redirectLogsToStdoutAndStderr(JTextArea logTextArea) {
        PrintStream printStream = new PrintStream(new TextAreaOutputStream(logTextArea));
//        System.setOut(printStream);
//        System.setErr(printStream);
    }
}
