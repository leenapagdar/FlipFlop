package com.csci143.project.FinalGame.ui;

import com.csci143.project.FinalGame.model.utils.GameDataUtils;
import com.csci143.project.FinalGame.ui.utils.NavigationWorker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.csci143.project.FinalGame.ui.RootPage.*;

/**
 * User page to gather the player's information.
 */
public class UserPage extends JPanel implements ActionListener {
	private static final String ADD_USER_ITEM = "<Add User>";

	// UI elements
	JComboBox<String> userList;

	public UserPage() {
		super();
		initComponents();
	}

	/**
	 * Helper function to load all the UI components for this page.
	 */
	private void initComponents() {
		// set defaults
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

	/**
	 * Helper function to add a new user in the game database. In case this was called from "Let's flip" button, skip
	 * updating Combo Box as it will trigger its action listener.
	 *
	 * @param e action event which caused this action.
	 * @return name of the user which was added.
	 */
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

	/**
	 * Action listener for the "Let's flip" button which triggers navigation to the levels page.
	 *
	 * @param e action event which caused this function being called.
	 */
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

		// Navigate to level page
		NavigationWorker levelPageLoader = new NavigationWorker(this, LEVEL_PAGE, name);
		levelPageLoader.navigate();
	}
}
