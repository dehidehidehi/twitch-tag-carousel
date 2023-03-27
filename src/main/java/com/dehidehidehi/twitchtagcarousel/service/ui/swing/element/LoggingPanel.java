package com.dehidehidehi.twitchtagcarousel.service.ui.swing.element;
import com.dehidehidehi.twitchtagcarousel.service.ui.swing.types.TextAreaOutputStream;

import javax.swing.*;
import java.awt.*;
import java.io.PrintStream;

public class LoggingPanel extends JPanel {

    private final JTextArea logTextArea;

    public LoggingPanel() {
        setLayout(new BorderLayout());
        logTextArea = new JTextArea();
        logTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logTextArea);
        add(scrollPane, BorderLayout.CENTER);
        // Redirect logs to stdout and std err to this box.
        PrintStream printStream = new PrintStream(new TextAreaOutputStream(logTextArea));
        System.setOut(printStream);
        System.setErr(printStream);
    }

    public void log(String message) {
        logTextArea.append(message + "\n");
    }

}
