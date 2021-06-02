package com.csci143.project.FinalGame;

import com.csci143.project.FinalGame.ui.MainPage;

public class FlipFlop {

	public static void main(String[] args) {
		//Schedule a job for the event dispatch thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainPage mainPage = new MainPage();
				mainPage.setVisible(true);
			}
		});
	}
}

