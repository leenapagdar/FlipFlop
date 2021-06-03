package com.csci143.project.FinalGame.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Root page for the FlipFlop game.
 */
public class RootPage extends JFrame {

	public static final String USER_PAGE  = "USER_PAGE";
	public static final String LEVEL_PAGE = "LEVEL_PAGE";
	public static final String GAME_PAGE  = "GAME_PAGE";

	public static final int PAGE_WIDTH  = 800;
	public static final int PAGE_HEIGHT = 800;

	JPanel pages;

	public RootPage() {
		CardLayout cardLayout = new CardLayout();
		pages = new JPanel(cardLayout);
		setTitle("Flip Flop Game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, PAGE_WIDTH, PAGE_HEIGHT);
		setResizable(false);

		// Entry page of the game will be the User Page.
		JPanel userPage = new UserPage();
		pages.add(userPage, USER_PAGE);

		// add the UserPage and show it on screen
		add(pages);
		cardLayout.show(pages, USER_PAGE);
	}

}
