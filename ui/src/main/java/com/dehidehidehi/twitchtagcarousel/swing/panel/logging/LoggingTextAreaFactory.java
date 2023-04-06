package com.dehidehidehi.twitchtagcarousel.swing.panel.logging;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import org.slf4j.LoggerFactory;

import javax.swing.*;

/**
 * Factory for providing logging panels for our GUI.<br>
 * see logback.xml file
 */
class LoggingTextAreaFactory {

    private static final String LOGGER_NAME = "root";
    private static final String APPENDER_NAME = "LoggingTextAreaAppender";

    private LoggingTextAreaFactory() {
    }

    static JTextArea ofLogBackAppendedTextArea() {
        final JTextArea jTextArea = new JTextArea();
        final LoggingTextAreaAppender appender = getLoggingTextAreaAppenderFromLogbackContext();
        appender.setTextArea(jTextArea);
        appender.start();
        return jTextArea;
    }

    private static LoggingTextAreaAppender getLoggingTextAreaAppenderFromLogbackContext() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Appender<ILoggingEvent> appender = context.getLogger(LOGGER_NAME).getAppender(APPENDER_NAME);
        if (appender instanceof LoggingTextAreaAppender textAreaAppender) {
            return textAreaAppender;
        }
        throw new IllegalStateException("Logback custom LoggingTextAreaAppender not found in Logback context.");
    }
}
