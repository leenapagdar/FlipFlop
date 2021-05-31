package com.csci143.project.FinalGame.ui;

import com.csci143.project.FinalGame.model.Card;
import com.csci143.project.FinalGame.model.Game;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

	public GameFrame() {
		// TODO: Get the level of the game dynamically from main function
		Game game = new Game(10);

		setTitle("Flip Flop Game");
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(150, 300, 1500, 3000);

		getContentPane().setLayout(new GridLayout(3, 6, 5, 5));

		// Add card buttons
		for (Card card : game.getCards()) {
			add(new CardButton(game, card));
		}
	}

}
	
	
	


