package com.csci143.project.FinalGame.ui;

import javax.swing.*;
import java.awt.*;

public class MainPage extends JFrame {

	public static final String USER_PAGE = "USER_PAGE";
	public static final String LEVEL_PAGE = "LEVEL_PAGE";
	public static final String GAME_PAGE = "GAME_PAGE";

	Image backgroundImage = Toolkit.getDefaultToolkit().getImage("resources/background.jpeg");

	JPanel pages;

	public MainPage() {
		CardLayout cardLayout = new CardLayout();
		pages = new JPanel(cardLayout);
		setTitle("Flip Flop Game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 800, 800);

		JPanel levelPage = new LevelPage();
		pages.add(levelPage, LEVEL_PAGE);

		add(pages);
		cardLayout.show(pages, LEVEL_PAGE);
	}

}
