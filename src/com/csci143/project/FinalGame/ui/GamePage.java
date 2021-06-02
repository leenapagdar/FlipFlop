package com.csci143.project.FinalGame.ui;

import com.csci143.project.FinalGame.model.Card;
import com.csci143.project.FinalGame.model.Game;
import com.csci143.project.FinalGame.model.utils.GameDataUtils;
import com.csci143.project.FinalGame.ui.utils.UIUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class GamePage extends JPanel implements ActionListener {

	private static final String PATH_TO_CARD_BACK    = "resources/cardBacks/waves.jpg";
	private static final String PATH_FORMAT_TO_EMOJI = "resources/creatures/%s.png";

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

	public GamePage() {
	}

	public GamePage(int level, String playerName) {
		game = new Game(level, playerName);

		initUserInterface();

		setupCards();

		setupTimer();
		timer.start();
	}

	private static String convertToHumanTime(int seconds) {
		return String.format("%02d:%02d", (seconds / 60), seconds % 60);
	}

	public void setupCards() {
		int totalCards = game.getCards().size();
		// We create 6 cols of all the cards available
		BoxLayout mainLayout = new BoxLayout(cardsPanel, BoxLayout.Y_AXIS);
		cardsPanel.add(Box.createVerticalGlue());
		for (int i = 0; i <= totalCards / 6; i++) {
			JPanel jPanel = new JPanel();
			BoxLayout boxLayout = new BoxLayout(jPanel, BoxLayout.X_AXIS);
			jPanel.setLayout(boxLayout);
			jPanel.add(Box.createHorizontalGlue());
			for (int j = 0; j < 6; j++) {
				Card card;
				try {
					card = game.getCards().get(i * 6 + j);
				} catch (IndexOutOfBoundsException e) {
					break;
				}
				JButton cardButton = new JButton();
				cardButton.setName(card.getId());
				cardButton.addActionListener(this);
				refreshLook(cardButton, card);
				jPanel.add(cardButton);
				jPanel.add(Box.createHorizontalGlue());
				cardButtons.add(cardButton);
			}
			cardsPanel.add(jPanel);
			cardsPanel.add(Box.createVerticalGlue());
		}
		cardsPanel.setLayout(mainLayout);
	}

	public void setLevel(int level) {
		game = new Game(level);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	private void initUserInterface() {
		setName("GamePage");

		UIUtils.makeComponentTransparent(this);
		setLayout(new BorderLayout());
		menuPanel = UIUtils.getTransparentPanel();
		menuPanel.setName("MenuPanel");
		cardsPanel = UIUtils.getTransparentPanel();
		cardsPanel.setName("CardPanel");

		// Desired font
		Font resizedFont = getFont().deriveFont(20.0f);
		menuPanel.setLayout(new BorderLayout());
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

	private void setupTimer() {
		timer = new Timer(1000, e -> {
			game.incrementSeconds();

			timeLabel.setText("Time: " + GamePage.convertToHumanTime(game.getSeconds()));
			scoreLabel.setText("Score: " + game.getScore());
			flipCountLabel.setText("Flip count: " + game.getFlipCount());
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!(e.getSource() instanceof JButton)) {
			return;
		}
		JButton clickedButton = (JButton) e.getSource();
		Card card = game.getCardFromId(clickedButton.getName());
		// do nothing if the card is already matched or faces up
		if (card.isMatched() || card.isFaceUp()) {
			return;
		}
		game.selectCard(clickedButton.getName());
		refreshLook(clickedButton, card);

		// refresh all other cards based on the update for this card.
		for (JButton cardButton : cardButtons) {
			refreshLook(cardButton, game.getCardFromId(cardButton.getName()));
		}
		if (game.isComplete()) {
			timer.stop();
			String message = "Bravo!\n\nFor this level, your score is as following:\n" +
					"Time: " + convertToHumanTime(game.getSeconds()) + "\n" +
					"Score: " + game.getScore() + "\n" +
					"FlipCount: " + game.getFlipCount() + "\n";
			if (game.getScore() > GameDataUtils.getHighScoreForIfExists(game.getPlayerName(), game.getLevel())) {
				message += "ITS A NEW HIGH SCORE!\n\n";
				GameDataUtils.saveScoreFor(game.getPlayerName(), game.getLevel(), game.getSeconds(), game.getScore(), game.getFlipCount());
			}

			JOptionPane.showMessageDialog(this, message, "Summary", JOptionPane.INFORMATION_MESSAGE);

			// Navigate to levels page
			removeLevelPageIfExists();
			getParent().add(new LevelPage(game.getPlayerName()), MainPage.LEVEL_PAGE);
			CardLayout c = (CardLayout) getParent().getLayout();
			c.show(getParent(), MainPage.LEVEL_PAGE);
		}
	}

	private void removeLevelPageIfExists() {
		for (Component component : getParent().getComponents()) {
			if (component instanceof LevelPage) {
				getParent().remove(component);
			}
		}
	}

	public void refreshLook(JButton button, Card card) {
		setDesiredIcon(button, card);
		button.setPreferredSize(new Dimension(100, 120));
	}

	private void setDesiredIcon(JButton button, Card card) {
		if (card.isFaceUp()) {
			File file = new File(String.format(PATH_FORMAT_TO_EMOJI, card.getEmoji()));
			BufferedImage image = null;
			try {
				image = ImageIO.read(file);
			} catch (Exception e) {
				System.out.println("Error while reading file.");
			}
			int min = Math.min(button.getWidth(), button.getHeight());
			Image scaledImage = image.getScaledInstance(min, min, Image.SCALE_SMOOTH);
			button.setIcon(new ImageIcon(scaledImage));
		} else {
			button.setIcon(new ImageIcon(PATH_TO_CARD_BACK));
		}
	}
}
