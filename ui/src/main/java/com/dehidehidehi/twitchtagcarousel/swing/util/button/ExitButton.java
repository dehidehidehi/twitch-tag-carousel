package com.dehidehidehi.twitchtagcarousel.swing.util.button;
import javax.swing.*;

public class ExitButton extends JButton {

	public ExitButton() {
		super("Exit");
		this.addActionListener(e -> System.exit(0));
	}
}
