package com.csci143.project.FinalGame.model;

import java.util.*;

public class Game {
	public static final int      MAX_LEVEL       = 30;
	public static final int      POSITIVE_POINTS = 10;
	public static final int      NEGATIVE_POINTS = -5;
	public static final String[] ALLOWED_EMOJIS  = {
			"blowfish", "crab", "dolphin", "fish", "lobster", "octopus", "oyster", "seaOtter", "seal",
			"shark", "shrimp", "spiralShell", "spoutingWhale", "squid", "tropicalFish", "whale"
	};

	private final int             level;
	private final ArrayList<Card> cards;
	private       Card            lastCard;
	private       int             seconds;
	private       int             score;
	private       int             negativePoints = 0;
	private       int             flipCount;
	private       String          playerName;


	public Game(int level) {
		if (level > MAX_LEVEL) {
			throw new IllegalArgumentException("Level cannot be greater than " + MAX_LEVEL);
		}
		this.level = level;
		this.cards = pickCards(level);
		if (level % 2 == 0) {
			negativePoints = NEGATIVE_POINTS;
		}
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
		int totalEmojis = ((level % 2) == 0 ? level : level + 1) / 2;
		for (String emoji : pickNRandomEmojisWithoutDuplicates(totalEmojis)) {
			cards.add(new Card(emoji, false, false));
			cards.add(new Card(emoji, false, false));
		}
		Collections.shuffle(cards);
		return cards;
	}

	private Set<String> pickNRandomEmojisWithoutDuplicates(int numberOfEmojis) {
		HashSet<String> emojis = new HashSet<>();
		Random random = new Random();
		while (emojis.size() != numberOfEmojis) {
			emojis.add(ALLOWED_EMOJIS[random.nextInt(ALLOWED_EMOJIS.length)]);
		}
		return emojis;
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
				score += POSITIVE_POINTS;
			} else {
				score += negativePoints;
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
