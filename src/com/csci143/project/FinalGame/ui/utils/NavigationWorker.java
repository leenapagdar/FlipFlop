package com.csci143.project.FinalGame.ui.utils;

import com.csci143.project.FinalGame.ui.GamePage;
import com.csci143.project.FinalGame.ui.LevelPage;

import javax.swing.*;
import java.awt.*;

import static com.csci143.project.FinalGame.ui.RootPage.GAME_PAGE;
import static com.csci143.project.FinalGame.ui.RootPage.LEVEL_PAGE;

/**
 * This is a worker which runs in background, without blocking UI. This worker
 * is responsible to navigate to the destination page.
 */
public class NavigationWorker extends SwingWorker<Void, Void> {

	private static final String LOADING_ANIMATION_GIF = "resources/loader.gif";

	private final JPanel parentPanel;
	private final String destinationPage;
	private final String playerName;
	private       int    level;

	public NavigationWorker(JPanel parentPanel, String destinationPage, String playerName) {
		this.parentPanel = parentPanel;
		this.destinationPage = destinationPage;
		this.playerName = playerName;
	}

	public NavigationWorker(JPanel parentPanel, String destinationPage, String playerName, int level) {
		this(parentPanel, destinationPage, playerName);
		this.level = level;
	}

	/**
	 * Display loading animation and load the destination page on a background thread.
	 */
	public void navigate() {

		// remove all components on screen
		parentPanel.removeAll();

		// create a label with animation as icon
		Icon imgIcon = new ImageIcon(LOADING_ANIMATION_GIF);
		JLabel label = new JLabel(imgIcon);

		// add the label to parent panel and refresh the look.
		parentPanel.add(label);
		parentPanel.validate();
		parentPanel.repaint();

		execute();
	}


	/**
	 * Main function for the worker which is responsible to perform the job in background.
	 *
	 * @return null
	 * @throws Exception in case the thread is interrupted by OS.
	 */
	@Override
	protected Void doInBackground() throws Exception {
		if (destinationPage.equals(LEVEL_PAGE)) {
			// Navigate to levels page
			removePageIfExists(LevelPage.class);
			JPanel levelPage = new LevelPage(playerName);
			parentPanel.getParent().add(levelPage, LEVEL_PAGE);
		} else if (destinationPage.equals(GAME_PAGE)) {
			// Navigate to game page
			removePageIfExists(GamePage.class);
			JPanel levelPage = new GamePage(level, playerName);
			parentPanel.getParent().add(levelPage, GAME_PAGE);
		}
		CardLayout c = (CardLayout) parentPanel.getParent().getLayout();
		c.show(parentPanel.getParent(), destinationPage);
		return null;
	}

	/**
	 * Helper function to remove a certain type of page from the @parentPanel's hierarchy.
	 *
	 * @param clazz class of the page which needs to be removed.
	 */
	private void removePageIfExists(Class clazz) {
		for (Component component : parentPanel.getParent().getComponents()) {
			if (clazz.isInstance(component)) {
				parentPanel.getParent().remove(component);
			}
		}
	}
}
