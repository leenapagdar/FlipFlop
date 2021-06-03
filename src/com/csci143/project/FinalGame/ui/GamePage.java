package com.csci143.project.FinalGame.ui;

import com.csci143.project.FinalGame.model.Card;
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
import java.util.ArrayList;

import static com.csci143.project.FinalGame.ui.RootPage.LEVEL_PAGE;

/**
 * GamePage which lays out all the cards for game play.
 */
public class GamePage extends JPanel implements ActionListener {

	// card button sizes
	private static final int CARD_BUTTON_WIDTH  = 100;
	private static final int CARD_BUTTON_HEIGHT = 120;

	// paths to relevant images
	private static final String PATH_TO_CARD_BACK    = "resources/cardBacks/waves.jpg";
	private static final String PATH_FORMAT_TO_EMOJI = "resources/creatures/%s.png";

	// Game and Time
	Game  game;
	Timer timer;

	// time, score & flipCount labels
	JLabel             timeLabel;
	JLabel             scoreLabel;
	JLabel             flipCountLabel;
	ArrayList<JButton> cardButtons = new ArrayList<>();

	// UI Elements
	private JPanel cardsPanel;
	private JPanel menuPanel;

	public GamePage(int level, String playerName) {
		game = new Game(level, playerName);

		// initialize user interface without cards
		initUserInterface();

		// add cards according to the game
		setupCards();

		// setup timer to update time, score and flip count on screen
		setupTimer();
		timer.start();
	}

	/**
	 * Initialize the user interface for this page.
	 */
	private void initUserInterface() {
		setName("GamePage");

		setLayout(new BorderLayout());
		menuPanel = new JPanel();
		menuPanel.setName("MenuPanel");
		cardsPanel = new JPanel();
		cardsPanel.setName("CardsPanel");

		// Desired font
		Font resizedFont = getFont().deriveFont(20.0f);
		menuPanel.setLayout(new BorderLayout());

		JLabel levelLabel = new JLabel("Level " + game.getLevel());
		levelLabel.setHorizontalAlignment(SwingConstants.CENTER);
		menuPanel.add(levelLabel, BorderLayout.NORTH);

		timeLabel = new JLabel("Time: " + GamePage.convertToHumanTime(game.getSeconds()), SwingConstants.LEFT);
		timeLabel.setFont(resizedFont);
		menuPanel.add(timeLabel, BorderLayout.WEST);

		scoreLabel = new JLabel("Score: " + game.getScore(), SwingConstants.CENTER);
		scoreLabel.setFont(resizedFont);
		menuPanel.add(scoreLabel, BorderLayout.CENTER);

		flipCountLabel = new JLabel("Flip count: " + game.getFlipCount(), SwingConstants.RIGHT);
		flipCountLabel.setFont(resizedFont);
		menuPanel.add(flipCountLabel, BorderLayout.EAST);

		add(menuPanel, BorderLayout.NORTH);
		add(cardsPanel, BorderLayout.CENTER);
	}

	/**
	 * Sets up the cards displayed on screen according to the game.
	 */
	public void setupCards() {
		int totalCards = game.getCards().size();

		// We create 6 columns and in each row. The maximum rows possible will be 5, resulting in 30 cards max.
		BoxLayout mainLayout = new BoxLayout(cardsPanel, BoxLayout.Y_AXIS);
		cardsPanel.add(Box.createVerticalGlue());
		// 5 rows
		for (int i = 0; i <= totalCards / 6; i++) {
			JPanel rowPanel = new JPanel();
			BoxLayout boxLayout = new BoxLayout(rowPanel, BoxLayout.X_AXIS);
			rowPanel.setLayout(boxLayout);
			rowPanel.add(Box.createHorizontalGlue());

			// 6 columns
			for (int j = 0; j < 6; j++) {
				Card card;
				try {
					card = game.getCards().get(i * 6 + j);
				} catch (IndexOutOfBoundsException e) {
					// if the index is out of bounds, skip the loop and continue with the outer loop
					break;
				}
				JButton cardButton = new JButton();
				cardButton.setName(card.getId());
				cardButton.addActionListener(this);
				refreshCardUI(cardButton, card);
				rowPanel.add(cardButton);
				rowPanel.add(Box.createHorizontalGlue());
				cardButtons.add(cardButton);
			}
			cardsPanel.add(rowPanel);
			cardsPanel.add(Box.createVerticalGlue());
		}
		cardsPanel.setLayout(mainLayout);
	}

	/**
	 * Setup time to refresh the score labels periodically. We refresh the labels very second.
	 */
	private void setupTimer() {
		// delay in ms: 1000
		// delay in sec: 1
		timer = new Timer(1000, e -> {
			game.incrementSeconds();
			refreshScoreLabels();
		});
	}

	/**
	 * Converts time in seconds to human readable format.
	 *
	 * @param seconds seconds to be converted
	 * @return human-readable time format
	 */
	private static String convertToHumanTime(int seconds) {
		return String.format("%02d:%02d", (seconds / 60), seconds % 60);
	}

	/**
	 * Helper function to update score labels.
	 */
	private void refreshScoreLabels() {
		timeLabel.setText("Time: " + GamePage.convertToHumanTime(game.getSeconds()));
		scoreLabel.setText("Score: " + game.getScore());
		flipCountLabel.setText("Flip count: " + game.getFlipCount());
	}

	/**
	 * Action listener for the card buttons on the page.
	 *
	 * @param e Action event which caused this method's invocation.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// In case the event generated from anything else then JButton, ignore.
		if (!(e.getSource() instanceof JButton)) {
			return;
		}
		JButton clickedButton = (JButton) e.getSource();
		Card card = game.getCardFromId(clickedButton.getName());

		// do nothing if the card is already matched or faces up
		if (card.isMatched() || card.isFaceUp()) {
			return;
		}

		// update game model to select the card.
		game.selectCard(clickedButton.getName());

		// refresh all cards based on game model update.
		for (JButton cardButton : cardButtons) {
			refreshCardUI(cardButton, game.getCardFromId(cardButton.getName()));
		}

		// if the game is complete initiate end game sequence
		if (game.isComplete()) {
			finishGame();
		}
	}

	/**
	 * Helper function for ending the game
	 */
	private void finishGame() {
		// stop the timer which updates score labels
		timer.stop();
		refreshScoreLabels();

		// Display the score to the player via dialog box.
		String message = "Bravo!\n\nFor this level, your score is as following:\n" +
				"Time: " + convertToHumanTime(game.getSeconds()) + "\n" +
				"Score: " + game.getScore() + "\n" +
				"FlipCount: " + game.getFlipCount() + "\n";

		// In case its a high score, append relevant message.
		if (game.getScore() > GameDataUtils.getHighScoreForLevelIfExists(game.getPlayerName(), game.getLevel())) {
			message += "ITS A NEW HIGH SCORE!\n\n";
			GameDataUtils.saveScoreFor(
					game.getPlayerName(), game.getLevel(), game.getSeconds(), game.getScore(), game.getFlipCount()
			);
		}
		JOptionPane.showMessageDialog(this, message, "Summary", JOptionPane.INFORMATION_MESSAGE);

		// Navigate to levels page
		NavigationWorker levelPageLoader = new NavigationWorker(this, LEVEL_PAGE, game.getPlayerName());
		levelPageLoader.navigate();
	}

	/**
	 * Refresh the UI for card buttons
	 *
	 * @param button button needing refresh of UI
	 * @param card   card model for updating UI
	 */
	public void refreshCardUI(JButton button, Card card) {
		setDesiredIcon(button, card);
		button.setPreferredSize(new Dimension(CARD_BUTTON_WIDTH, CARD_BUTTON_HEIGHT));
	}

	/**
	 * Based on the model of card display the relevant icon image. Displays card back when card is face down and the
	 * emoji if caed is face up.
	 *
	 * @param button button for which icon needs to be set.
	 * @param card   card model for updating icon.
	 */
	private void setDesiredIcon(JButton button, Card card) {
		if (card.isFaceUp()) {
			File file = new File(String.format(PATH_FORMAT_TO_EMOJI, card.getEmoji()));
			BufferedImage image = null;
			try {
				image = ImageIO.read(file);
			} catch (Exception e) {
				System.out.println("Error while reading file.");
			}

			// Scale the icon with respect to smallest edge.
			int min = Math.min(button.getWidth(), button.getHeight());
			Image scaledImage = image.getScaledInstance(min, min, Image.SCALE_SMOOTH);
			button.setIcon(new ImageIcon(scaledImage));
		} else {
			button.setIcon(new ImageIcon(PATH_TO_CARD_BACK));
		}
	}
}
