package com.csci143.project.FinalGame.ui;

import javax.swing.*;
import java.awt.*;

public class MainPage extends JFrame {

	public static final String USER_PAGE  = "USER_PAGE";
	public static final String LEVEL_PAGE = "LEVEL_PAGE";
	public static final String GAME_PAGE  = "GAME_PAGE";

	public static final int PAGE_WIDTH  = 800;
	public static final int PAGE_HEIGHT = 800;

	Image backgroundImage = Toolkit.getDefaultToolkit().getImage("resources/background.jpeg");

	JPanel pages;

	public MainPage() {
		CardLayout cardLayout = new CardLayout();
		pages = new JPanel(cardLayout);
		setTitle("Flip Flop Game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, PAGE_WIDTH, PAGE_HEIGHT);
		setResizable(false);

		JPanel userPage = new UserPage();
		pages.add(userPage, USER_PAGE);

		add(pages);
		cardLayout.show(pages, USER_PAGE);
	}

}
