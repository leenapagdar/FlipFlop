package com.csci143.project.FinalGame.ui;

import com.csci143.project.FinalGame.model.Game;
import com.csci143.project.FinalGame.model.utils.GameDataUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class LevelPage extends JPanel implements ActionListener {

	private static final String PATH_TO_LEVELS_IMAGE = "resources/Levels/%d.png";

	private JPanel titlePanel;
	private JPanel levelsPanel;

	private final String playerName;

	public LevelPage(String playerName) {
		super();

		this.playerName = playerName;

		initUserInterface();
		setupCards();
	}

	private void setupCards() {
		// We create 6 cols of all the cards available
		BoxLayout mainLayout = new BoxLayout(levelsPanel, BoxLayout.Y_AXIS);
		levelsPanel.add(Box.createVerticalGlue());
		Font font = getFont().deriveFont(20.0f);
		int highestLevel = GameDataUtils.getHighestLevelForPlayer(playerName);
		for (int i = 0; i <= Game.MAX_LEVEL / 6; i++) {
			JPanel jPanel = new JPanel();
			BoxLayout boxLayout = new BoxLayout(jPanel, BoxLayout.X_AXIS);
			jPanel.setLayout(boxLayout);
			jPanel.add(Box.createHorizontalGlue());
			for (int j = 0; j < 6; j++) {
				int level = (i * 6 + j) + 1;
				if(level > Game.MAX_LEVEL)
					break;
				JButton levelButton = new JButton();
				setDesiredIcon(levelButton, level);
				levelButton.setSize(100, 120);
				levelButton.setFont(font);
				levelButton.addActionListener(this);
				levelButton.setName("" + (level));
				levelButton.setEnabled(level <= highestLevel + 1);

				jPanel.add(levelButton);
				jPanel.add(Box.createHorizontalGlue());
			}
			levelsPanel.add(jPanel);
			levelsPanel.add(Box.createVerticalGlue());
		}
		levelsPanel.setLayout(mainLayout);
	}

	private void initUserInterface() {
		setLayout(new BorderLayout());
		titlePanel = new JPanel();
		titlePanel.setName("TitlePanel");
		levelsPanel = new JPanel();
		levelsPanel.setName("LevelsPanel");

		// Desired font
		Font resizedFont = getFont().deriveFont(20.0f);
		titlePanel.setLayout(new BorderLayout());
		JLabel titleLabel = new JLabel("Select a level to play", SwingConstants.CENTER);
		titleLabel.setFont(resizedFont);
		titlePanel.add(titleLabel, BorderLayout.CENTER);

		add(titleLabel, BorderLayout.NORTH);
		add(levelsPanel, BorderLayout.CENTER);
	}

	private void setDesiredIcon(JButton button, int level) {
		File file = new File(String. format(PATH_TO_LEVELS_IMAGE, level));
		BufferedImage image = null;
		try {
			image = ImageIO.read(file);
		} catch (Exception e) {
			System.out.println("Error while reading file.");
		}
		int min = Math.min(button.getWidth(), button.getHeight());
		Image scaledImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		button.setIcon(new ImageIcon(scaledImage));
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
