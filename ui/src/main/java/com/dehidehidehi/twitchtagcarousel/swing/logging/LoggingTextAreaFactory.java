package com.dehidehidehi.twitchtagcarousel.swing.logging;
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

    private static final String APPENDER_NAME = "LoggingTextAreaAppender";
    private static final String[] LOGGERS = {
            "com.dehidehidehi.twitchtagcarousel",
    };

    private LoggingTextAreaFactory() {
    }

    static JTextArea ofLogBackAppendedTextArea() {
        final JTextArea jTextArea = new JTextArea();
        for (String logger : LOGGERS) {
            final LoggingTextAreaAppender appender = getLoggingTextAreaAppenderFromLogbackContext(logger);
            appender.setTextArea(jTextArea);
            appender.start();
        }
        return jTextArea;
    }

    private static LoggingTextAreaAppender getLoggingTextAreaAppenderFromLogbackContext(String loggerName) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Appender<ILoggingEvent> appender = context.getLogger(loggerName).getAppender(APPENDER_NAME);
        if (appender instanceof LoggingTextAreaAppender textAreaAppender) {
            return textAreaAppender;
        }
        throw new IllegalStateException(
                "Logback custom LoggingTextAreaAppender not found in Logback context, did you provide the correct LOGGER (%s) in this class %s and in logback.xml?".formatted(
                        loggerName,
                        LoggingTextAreaFactory.class.getSimpleName()));
    }
}
