package com.csci143.project.FinalGame;

import com.csci143.project.FinalGame.ui.RootPage;

public class FlipFlop {

	public static void main(String[] args) {
		//Schedule a job for the event dispatch thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				RootPage rootPage = new RootPage();
				rootPage.setVisible(true);
			}
		});
	}
}

