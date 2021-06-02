package com.csci143.project.FinalGame.ui;

import com.csci143.project.FinalGame.model.utils.GameDataUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.csci143.project.FinalGame.ui.MainPage.*;

public class UserPage extends JPanel implements ActionListener {
	private static final String ADD_USER_ITEM = "<Add User>";

	JComboBox<String> userList;

	public UserPage() {
		super();
		initComponents();
	}

	private void initComponents() {
		setSize(PAGE_WIDTH, PAGE_HEIGHT);
		setLayout(new BorderLayout());

		Font font = getFont().deriveFont(20.0f);

		// Panel to house the user name fields
		JPanel mainPanel = new JPanel();

		JLabel nameLabel = new JLabel("Name:");
		nameLabel.setForeground(Color.BLACK);
		nameLabel.setFont(font);

		userList = new JComboBox(GameDataUtils.getAllPlayers().toArray());
		userList.setFont(font);
		userList.addItem(ADD_USER_ITEM);
		userList.addActionListener(e -> {
			if (userList.getSelectedItem().equals(ADD_USER_ITEM))
				addNewUser(e);
		});

		JButton nextPage = new JButton("Let's Flip");
		nextPage.setFont(font);
		nextPage.addActionListener(this);
		mainPanel.add(nameLabel);
		mainPanel.add(userList);
		mainPanel.add(nextPage);

		add(mainPanel, BorderLayout.CENTER);
	}

	private String addNewUser(ActionEvent e) {
		String name = "";
		while (name.isEmpty()) {
			name = JOptionPane.showInputDialog("Welcome mate!\n What shall I call you?");
		}
		GameDataUtils.addPlayer(name);

		if (e.getSource() instanceof JButton) {
			return name;
		}

		userList.addItem(name);
		userList.setSelectedItem(name);

		return name;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!(e.getSource() instanceof JButton)) {
			return;
		}
		String name;
		// If the current item selected is add user, add one.
		if (userList.getSelectedItem().toString().equals(ADD_USER_ITEM)) {
			name = addNewUser(e);
		} else {
			name = (String) userList.getSelectedItem();
		}

		// Navigate to levels pages
		removeLevelPageIfExists();
		JPanel levelPage = new LevelPage(name);
		getParent().add(levelPage, LEVEL_PAGE);
		CardLayout c = (CardLayout) getParent().getLayout();
		c.show(getParent(), LEVEL_PAGE);
	}

	private void removeLevelPageIfExists() {
		for (Component component : getParent().getComponents()) {
			if (component instanceof LevelPage) {
				getParent().remove(component);
			}
		}
	}
}
