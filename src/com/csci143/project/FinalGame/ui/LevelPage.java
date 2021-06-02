package com.csci143.project.FinalGame.ui;

import com.csci143.project.FinalGame.model.Game;
import com.csci143.project.FinalGame.model.utils.GameDataUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LevelPage extends JPanel implements ActionListener {

	private final String playerName;

	public LevelPage(String playerName) {
		super();

		this.playerName = playerName;

		Font font = getFont().deriveFont(20.0f);
		setLayout(new GridLayout(3, 6, 5, 5));

		int highestLevel = GameDataUtils.getHighestLevelForPlayer(playerName);

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
	public void actionPerformed(ActionEvent e) {
		JButton sourceButton = (JButton) e.getSource();
		int level = Integer.parseInt(sourceButton.getName());
		JPanel gamePage = new GamePage(level, playerName);
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
