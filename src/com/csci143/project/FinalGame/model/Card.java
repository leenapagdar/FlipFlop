package com.csci143.project.FinalGame.model;

import java.util.UUID;

public class Card {
	String  emoji;
	boolean isFaceUp;
	boolean isMatched;
	String  id;

	public String getEmoji() {
		return emoji;
	}

	public void setEmoji(String emoji) {
		this.emoji = emoji;
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

	public void setId(String id) {
		this.id = id;
	}

	public Card(String emoji, boolean isFaceUp, boolean isMatched) {
		this.id = UUID.randomUUID().toString();
		this.emoji = emoji;
		this.isFaceUp = isFaceUp;
		this.isMatched = isMatched;
	}

	public void flip() {
		if(isMatched) {
			isFaceUp = true;
		} else {
			isFaceUp = !isFaceUp;
		}
	}

	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Card)) {
			return false;
		}
		Card c = (Card) o;
		return emoji.equals(c.emoji);
	}

}
