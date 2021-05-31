package com.csci143.project.FinalGame.ui;

import com.csci143.project.FinalGame.model.Card;
import com.csci143.project.FinalGame.model.Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CardButton extends JButton implements ActionListener {

	private static final String PATH_TO_CARD_BACK    = "resources/cardBacks/waves.jpg";
	private static final String PATH_FORMAT_TO_EMOJI = "resources/creatures/%s.png";

	private Card card;
	private Game game;

	public CardButton(Game game, Card card) {
		this.game = game;
		this.card = card;
		setName(card.getId());
		addActionListener(this);

		refreshLook();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// do nothing if the card is already matched or faces up
		if (this.card.isMatched() || this.card.isFaceUp()) {
			return;
		}
		game.selectCard(card);
		refreshLook();

		// refresh all other cards based on the update for this card.
		for(Component component : this.getParent().getComponents()) {
			if(!(component instanceof CardButton)) {
				continue;
			}
			((CardButton) component).refreshLook();
		}
	}

	public void refreshLook() {
		setIcon(getDesiredIcon());
	}

	private Icon getDesiredIcon() {
		if (card.isFaceUp()) {
			File file = new File(String.format(PATH_FORMAT_TO_EMOJI, card.getEmoji()));
			BufferedImage image = null;
			try {
				image = ImageIO.read(file);
			} catch(Exception e) {
				System.out.println("Error while reading file.");
			}
			int min = Math.min(this.getWidth(), this.getHeight());
			Image scaledImage = image.getScaledInstance(min, min, Image.SCALE_SMOOTH);
			return new ImageIcon(scaledImage);
		} else {
			return new ImageIcon(PATH_TO_CARD_BACK);
		}
	}
}

