package com.csci143.project.FinalGame.model;

import java.util.*;

/**
 * Model class for Game
 */
public class Game {
	// required constants
	public static final int      MAX_LEVEL       = 30;
	public static final int      POSITIVE_POINTS = 10;
	public static final int      NEGATIVE_POINTS = -5;
	public static final String[] ALLOWED_EMOJIS  = {
			"blowfish", "crab", "dolphin", "fish", "lobster", "octopus", "oyster", "seaOtter", "seal",
			"shark", "shrimp", "spiralShell", "spoutingWhale", "squid", "tropicalFish", "whale"
	};

	// class fields
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
		// pick random cards
		this.cards = pickCards(level);

		// the level is an even number, enable negative points.
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

	/**
	 * Picks random cards for level. The number of cards is decided based on the level. Every odd, even pair of levels
	 * will have the same number of cards. As described above, the even level will have negative points for unsuccessful
	 * match.
	 * Number of emojis in the game = Number of cards in game / 2
	 * For example,
	 *   Level 1 = 2 Cards
	 *   Level 2 = 2 Cards
	 *   Level 3 = 4 Cards
	 *   Level 4 = 4 Cards
	 *   Level 5 = 6 Cards
	 *   Level 6 = 6 Cards
	 *   etc.
	 * @param level level of the game
	 * @return List of selected cards for the game
	 */
	private ArrayList<Card> pickCards(int level) {
		ArrayList<Card> cards = new ArrayList<>();
		int totalEmojis = ((level % 2) == 0 ? level : level + 1) / 2;
		for (String emoji : pickNRandomEmojisWithoutDuplicates(totalEmojis)) {
			// add 2 cards of the same emoji to ensure that there will always be a pair of cards to match.
			cards.add(new Card(emoji, false, false));
			cards.add(new Card(emoji, false, false));
		}
		Collections.shuffle(cards);
		return cards;
	}

	/**
	 * Picks n emojis randomly without duplication. Uses a set to ensure that there is no duplicate entry in the selected
	 * emojis.
	 * @param numberOfEmojis number of emojis to be selected
	 * @return Set of selected emojis
	 */
	private Set<String> pickNRandomEmojisWithoutDuplicates(int numberOfEmojis) {
		HashSet<String> emojis = new HashSet<>();
		Random random = new Random();

		// Keep picking emojis until the we have n distinct emojis
		while (emojis.size() != numberOfEmojis) {
			emojis.add(ALLOWED_EMOJIS[random.nextInt(ALLOWED_EMOJIS.length)]);
		}
		return emojis;
	}

	// Getters for required the properties
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

	public String getPlayerName() {
		return playerName;
	}

	/**
	 * increments the seconds by 1
	 * @return new value of seconds spent in the game.
	 */
	public int incrementSeconds() {
		return seconds++;
	}

	/**
	 * Selects the given card with respect to game. A selected is flipped when selected. If no other card was previously
	 * selected then the function returns. In case there was a card already selected this function will try to match it
	 * with the current one and update the score and state of the card accordingly.
	 * @param selectedCard Card object to be selected.
	 */
	public void selectCard(Card selectedCard) {
		if (lastCard == null) {
			// if no other card was previously selected flip this card and update other cards to face down.
			lastCard = selectedCard;

			// Flip every card to face down position. This is a way to flip back all the unmatched cards until this point.
			for (Card card : cards) {
				// only flip them if they are not matched.
				if (!card.isMatched()) {
					card.setFaceUp(false);
				}
			}
		} else {
			// if there already is a selected card match the new card with existing one.
			if (lastCard.equals(selectedCard)) {
				// successful match
				selectedCard.setMatched(true);
				lastCard.setMatched(true);
				score += POSITIVE_POINTS;
			} else {
				// unsuccessful match
				score += negativePoints;
			}
			lastCard = null;
		}
		flipCount += 1;
		// flip the selected card.
		selectedCard.flip();
	}

	/**
	 * Selects a card using its card id.
	 * @param id id of the card which is being selected.
	 */
	public void selectCard(String id) {
		Card selectedCard = getCardFromId(id);
		assert selectedCard != null;

		selectCard(selectedCard);
	}

	/**
	 * Retrieves card object from the given card id
	 * @param id id of the card which is to be retrieved
	 * @return Card object if the card was found, else null
	 */
	public Card getCardFromId(String id) {
		for (Card card : cards) {
			if (card.getId().equals(id)) {
				return card;
			}
		}
		return null;
	}

	/**
	 * Helper function to evaluate if the game is complete. The game is marked incomplete if there exists _a_ card which
	 * is unmatched.
	 * @return true is the gane is complete else false
	 */
	public boolean isComplete() {
		for (Card card : cards) {
			if (!card.isMatched()) {
				return false;
			}
		}
		return true;
	}
}
