package com.dehidehidehi.twitchtagcarousel.swing.panel.logging;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

/**
 * Factory for providing logging panels for our GUI.<br>
 * see logback.xml file
 */
class LoggingTextAreaFactory {

    public static final String TWITCH_TAG_CAROUSEL_LOG = "twitch-tag-carousel.log";
    private final JTextArea textArea;

    public LoggingTextAreaFactory() {
        textArea = new JTextArea();
        startListeningToLogsFile();
    }

    private void startListeningToLogsFile() {
        final SwingWorker<Void, String> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                final FileReader fileReader = new FileReader(TWITCH_TAG_CAROUSEL_LOG);
                final BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    publish(line);
                }
                bufferedReader.close();
                fileReader.close();
                return null;
            }

            @Override
            protected void process(List<String> chunks) {
                for (String chunk : chunks) {
                    textArea.append(chunk + "\n");
                }
            }
        };
        worker.execute();

    }

    public JTextArea getTextArea() {
        return textArea;
    }

}
