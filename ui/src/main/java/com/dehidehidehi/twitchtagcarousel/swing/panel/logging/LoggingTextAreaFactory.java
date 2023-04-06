package com.dehidehidehi.twitchtagcarousel.swing.panel.logging;
import javax.swing.*;
import java.io.RandomAccessFile;

/**
 * Factory for providing logging panels for our GUI.<br>
 * see logback.xml file
 */
class LoggingTextAreaFactory {

    public static final String TWITCH_TAG_CAROUSEL_LOG = "logs/twitch-tag-carousel.log";
    private final JTextArea textArea;
    private long lastPosition;

    public LoggingTextAreaFactory() {
        textArea = new JTextArea();
        startListeningToLogsFile();
    }

    private void startListeningToLogsFile() {
        final Timer timer = new Timer(1000, e -> {
            try {
                final RandomAccessFile file = new RandomAccessFile(TWITCH_TAG_CAROUSEL_LOG, "r");
                file.seek(lastPosition);
                String line;
                while ((line = file.readLine()) != null) {
                    textArea.append(line + "\n");
                }
                lastPosition = file.getFilePointer();
                file.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        timer.start();
    }


    public JTextArea getTextArea() {
        return textArea;
    }

}
