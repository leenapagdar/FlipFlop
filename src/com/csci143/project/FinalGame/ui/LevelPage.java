package com.csci143.project.FinalGame.ui;

import com.csci143.project.FinalGame.model.Game;
import com.csci143.project.FinalGame.model.utils.GameDataUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.csci143.project.FinalGame.ui.MainPage.DEFAULT_PLAYER;

public class LevelPage extends JPanel implements ActionListener {

	public LevelPage() {
		// set background to transparent
		// Any color with alpha 0 will result in transparent color.
		setBackground(new Color(0,0,0,0));

		Font font = getFont().deriveFont(20.0f);
		setLayout(new GridLayout(3, 6, 5, 5));

		int highestLevel = GameDataUtils.getHighestLevelForPlayer(DEFAULT_PLAYER);

		for (int i = 0; i < Game.MAX_LEVEL; i++) {
			JButton levelButton = new JButton();
			levelButton.setText("Level: " + (i + 1));
			levelButton.setFont(font);
			levelButton.addActionListener(this);
			levelButton.setName("" + (i + 1));
			levelButton.setEnabled((i) <= highestLevel);
			add(levelButton);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// set background to transparent
		setOpaque(false);
		// Any color with alpha 0 will result in transparent color.
		setBackground(new Color(0, 0, 0, 0));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton sourceButton = (JButton) e.getSource();
		int level = Integer.parseInt(sourceButton.getName());
		JPanel gamePage = new GamePage(level, DEFAULT_PLAYER);
		removeGamePageIfExists();

		getParent().add(gamePage, MainPage.GAME_PAGE);

		CardLayout c = (CardLayout) getParent().getLayout();
		c.show(getParent(), MainPage.GAME_PAGE);
	}

	private void removeGamePageIfExists() {
		for (Component component : getParent().getComponents()) {
			if (component instanceof GamePage) {
				getParent().remove(component);
			}
		}
	}
}
