package com.csci143.project.FinalGame.ui.utils;

import javax.swing.*;
import java.awt.*;

public final class UIUtils {

	public static final Color TRANSPARENT_COLOR = new Color(0,0,0,0);

	public static JPanel getTransparentPanel() {
		JPanel panel = new JPanel();
		UIUtils.makeComponentTransparent(panel);
		return panel;
	}

	public static void makeComponentTransparent(JComponent component) {
		component.setOpaque(false);
		component.setBackground(TRANSPARENT_COLOR);
	}
}
