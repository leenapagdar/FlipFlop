package com.csci143.project.FinalGame.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Game {
	public static final int      MAX_LEVEL      = 15;
	public static final String[] ALLOWED_EMOJIS = {
			"blowfish", "crab", "dolphin", "fish", "lobster", "octopus", "oyster", "seaOtter", "seal",
			"shark", "shrimp", "spiralShell", "spoutingWhale", "squid", "tropicalFish", "whale"
	};

	private final int             level;
	private final ArrayList<Card> cards;
	private       Card            lastCard;
	private       int             seconds;
	private       int             score;
	private       int             flipCount;
	private       String          playerName;


	public Game(int level) {
		if (level > MAX_LEVEL) {
			throw new IllegalArgumentException("Level cannot be greater than " + MAX_LEVEL);
		}
		this.level = level;
		this.cards = pickCards(level);
		lastCard = null;
		seconds = 0;
		score = 0;
		flipCount = 0;
		this.playerName = "";
	}

	public Game(int level, String playerName) {
		this(level);
		this.playerName = playerName;
	}

	private ArrayList<Card> pickCards(int level) {
		ArrayList<Card> cards = new ArrayList<>();
		Random random = new Random();
		for (int i = 0; i < level; i++) {
			String emoji = ALLOWED_EMOJIS[random.nextInt(ALLOWED_EMOJIS.length)];
			cards.add(new Card(emoji, false, false));
			cards.add(new Card(emoji, false, false));
		}
		Collections.shuffle(cards);
		return cards;
	}

	public int getLevel() {
		return level;
	}

	public ArrayList<Card> getCards() {
		return cards;
	}

	public int getSeconds() {
		return seconds;
	}

	public int getScore() {
		return score;
	}

	public int getFlipCount() {
		return flipCount;
	}

	public int incrementSeconds() {
		return seconds++;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void selectCard(Card selectedCard) {
		if (lastCard == null) {
			lastCard = selectedCard;
			for (Card card : cards) {
				if (!card.isMatched) {
					card.isFaceUp = false;
				}
			}
		} else {
			if (lastCard.equals(selectedCard)) {
				selectedCard.isMatched = true;
				lastCard.isMatched = true;
				score += 10;
			}
			lastCard = null;
		}
		flipCount += 1;
		selectedCard.flip();
	}

	public void selectCard(String id) {
		Card selectedCard = getCardFromId(id);
		assert selectedCard != null;

		selectCard(selectedCard);
	}

	public Card getCardFromId(String id) {
		for (Card card : cards) {
			if (card.id.equals(id)) {
				return card;
			}
		}
		return null;
	}

	public boolean isComplete() {
		for (Card card : cards) {
			if (!card.isMatched) {
				return false;
			}
		}
		return true;
	}
}
