package com.csci143.project.FinalGame.model;

import java.util.UUID;

/**
 * Model class for a Card
 */
public class Card {
	private String  emoji;
	private boolean isFaceUp;
	private boolean isMatched;
	private String  id;

	public Card(String emoji, boolean isFaceUp, boolean isMatched) {
		this.id = UUID.randomUUID().toString();
		this.emoji = emoji;
		this.isFaceUp = isFaceUp;
		this.isMatched = isMatched;
	}

	// Getters and Setters
	public String getEmoji() {
		return emoji;
	}

	public boolean isFaceUp() {
		return isFaceUp;
	}

	public void setFaceUp(boolean faceUp) {
		isFaceUp = faceUp;
	}

	public boolean isMatched() {
		return isMatched;
	}

	public void setMatched(boolean matched) {
		isMatched = matched;
	}

	public String getId() {
		return id;
	}

	/**
	 * Flips the card to its opposite side. Updates the card to always face up in case its matched.
	 */
	public void flip() {
		if (isMatched) {
			isFaceUp = true;
		} else {
			isFaceUp = !isFaceUp;
		}
	}

	/**
	 * Matches this card with other card.
	 * @param otherCard Other card which needs to be matched with this card.
	 * @return True if the card matches, else false
	 */
	public boolean equals(Object otherCard) {
		if (otherCard == this) {
			return true;
		}
		if (!(otherCard instanceof Card)) {
			return false;
		}
		Card c = (Card) otherCard;
		// consider the cards as equal if their emojis match.
		return emoji.equals(c.emoji);
	}

}
