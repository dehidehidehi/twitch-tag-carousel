package com.dehidehidehi.twitchtagcarousel.swing.types;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.OutputStream;

/**
 * An OutputStream implementation that writes to a JTextArea.
 */
public class TextAreaOutputStream extends OutputStream {

    private final JTextArea textArea;

    /**
     * Creates a new TextAreaOutputStream that writes to the specified JTextArea.
     *
     * @param textArea the JTextArea to write to
     */
    public TextAreaOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }

    /**
     * Writes a byte to the output stream. This method appends the byte value to the JTextArea
     * as a character, and sets the caret position of the JTextArea to the end of the document.
     *
     * @param b the byte value to write
     */
    @Override
    public void write(int b) {
        textArea.append(String.valueOf((char) b));
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    /**
     * Writes an array of bytes to the output stream. This method appends the byte values to the
     * JTextArea as characters, and sets the caret position of the JTextArea to the end of the document.
     *
     * @param b   the byte array to write
     * @param off the offset within the byte array to start writing from
     * @param len the number of bytes to write
     */
    @Override
    public void write(byte @NotNull [] b, int off, int len) {
        String s = new String(b, off, len);
        textArea.append(s);
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

}

