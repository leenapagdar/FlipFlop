package com.csci143.project.FinalGame.ui;

import com.csci143.project.FinalGame.model.Game;
import com.csci143.project.FinalGame.model.utils.GameDataUtils;
import com.csci143.project.FinalGame.ui.utils.NavigationWorker;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import static com.csci143.project.FinalGame.ui.RootPage.GAME_PAGE;

/**
 * Level page to list all the levels available to the player.
 */
public class LevelPage extends JPanel implements ActionListener {

	// level icon sizes
	private static final int CARD_BUTTON_WIDTH  = 100;
	private static final int CARD_BUTTON_HEIGHT = 100;

	// Path format for levels image
	private static final String PATH_TO_LEVELS_IMAGE = "resources/Levels/%d.png";

	private final String playerName;
	private JPanel levelsPanel;

	public LevelPage(String playerName) {
		super();

		this.playerName = playerName;

		// setup UI
		initUserInterface();
		setupLevelButtons();
	}

	/**
	 * Initialize the user interface for this page.
	 */
	private void initUserInterface() {
		setLayout(new BorderLayout());
		JPanel titlePanel = new JPanel();
		titlePanel.setName("TitlePanel");
		levelsPanel = new JPanel();
		levelsPanel.setName("LevelsPanel");

		// Desired font
		Font resizedFont = getFont().deriveFont(20.0f);
		titlePanel.setLayout(new BorderLayout());
		JLabel titleLabel = new JLabel("Welcome " + playerName + ", select a level to play", SwingConstants.CENTER);
		titleLabel.setFont(resizedFont);
		titlePanel.add(titleLabel, BorderLayout.CENTER);

		add(titleLabel, BorderLayout.NORTH);
		add(levelsPanel, BorderLayout.CENTER);
	}

	/**
	 * Sets up the levels displayed on screen according to the game.
	 */
	private void setupLevelButtons() {
		// We create 6 columns and in each row. The maximum rows possible will be 5, resulting in 30 cards max.
		BoxLayout mainLayout = new BoxLayout(levelsPanel, BoxLayout.Y_AXIS);
		levelsPanel.add(Box.createVerticalGlue());
		int highestLevel = GameDataUtils.getHighestLevelForPlayer(playerName);
		// 5 rows
		for (int i = 0; i <= Game.MAX_LEVEL / 6; i++) {
			JPanel rowPanel = new JPanel();
			BoxLayout boxLayout = new BoxLayout(rowPanel, BoxLayout.X_AXIS);
			rowPanel.setLayout(boxLayout);
			rowPanel.add(Box.createHorizontalGlue());

			// 6 columns
			for (int j = 0; j < 6; j++) {
				int level = (i * 6 + j) + 1;
				if (level > Game.MAX_LEVEL)
					break;

				// Button settings
				JButton levelButton = new JButton();
				levelButton.setLayout(new BorderLayout());
				setDesiredIcon(levelButton, level);
				levelButton.addActionListener(this);
				levelButton.setName("" + (level));
				levelButton.setEnabled(level <= highestLevel + 1);
				levelButton.setPreferredSize(new Dimension(133, 149));

				// High score label on button
				JLabel highScoreLabel = new JLabel();
				String highScoreText;
				highScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
				highScoreLabel.setVerticalAlignment(SwingConstants.BOTTOM);
				if(level <= highestLevel) {
					String highScoreInThisLevel = String.valueOf(GameDataUtils.getHighScoreForLevelIfExists(playerName, level));
					highScoreText = "HighScore: " + highScoreInThisLevel;
				} else {
					highScoreText = "(Not played)";
				}
				highScoreLabel.setText(highScoreText);
				levelButton.add(highScoreLabel);

				rowPanel.add(levelButton);
				rowPanel.add(Box.createHorizontalGlue());
			}
			levelsPanel.add(rowPanel);
			levelsPanel.add(Box.createVerticalGlue());
		}
		levelsPanel.setLayout(mainLayout);
	}

	/**
	 * Based on the model of card display the relevant icon image. Displays card back when card is face down and the
	 * emoji if caed is face up.
	 *
	 * @param button button for which icon needs to be set.
	 * @param level  level that the button represents.
	 */
	private void setDesiredIcon(JButton button, int level) {
		File file = new File(String.format(PATH_TO_LEVELS_IMAGE, level));
		BufferedImage image = null;
		try {
			image = ImageIO.read(file);
		} catch (Exception e) {
			System.out.println("Error while reading file.");
		}
		Image scaledImage = image.getScaledInstance(CARD_BUTTON_WIDTH, CARD_BUTTON_HEIGHT, Image.SCALE_SMOOTH);
		button.setIcon(new ImageIcon(scaledImage));
	}

	/**
	 * Action listener for the level buttons on the page.
	 *
	 * @param e Action event which caused this method's invocation.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton sourceButton = (JButton) e.getSource();
		int level = Integer.parseInt(sourceButton.getName());

		NavigationWorker gamePageLoader = new NavigationWorker(this, GAME_PAGE, playerName, level);
		gamePageLoader.navigate();
	}
}
