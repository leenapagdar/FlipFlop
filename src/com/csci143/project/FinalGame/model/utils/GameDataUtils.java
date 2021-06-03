package com.csci143.project.FinalGame.model.utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Utils functions to read and write from Game data file. The data file uses json to store the data.
 * Format:
 *  {
 *      "<Player>": {
 *      	"<Level>": {
 *      		{
 *       			"Score": long,
 *       			"FlipCount": long,
 *       			"TimeInSec": long
 *     			}
 *      	}
 *      }
 *  }
 */
public final class GameDataUtils {

	// Game data file location
	private static final String PATH_TO_DATA_FILE = "resources/gameData/game_data.json";

	/**
	 * Gets all the players that have played the game before
	 * @return List of player names
	 */
	public static ArrayList<String> getAllPlayers() {
		JSONObject root = getJSONData();
		return new ArrayList<>(root.keySet());
	}

	/**
	 * Gets the highest level played by the specified player
	 * @param playerName name of the player
	 * @return Highest level played by the player
	 * @throws IllegalStateException In case the player is a new player and/or they don't exist in the database.
	 */
	public static int getHighestLevelForPlayer(String playerName) {
		JSONObject root = getJSONData();
		JSONObject playerData = (JSONObject) root.getOrDefault(playerName, null);
		if (playerData == null) {
			throw new IllegalStateException("Player does not exist in data file");
		}
		int maximumLevel = 0;
		for (Object key : playerData.keySet()) {
			int level = Integer.parseInt((String) key);
			if (level > maximumLevel) {
				maximumLevel = level;
			}
		}
		return maximumLevel;
	}

	/**
	 * Gets the highest score for given player in the given level.
	 * @param playerName name of the desired player
	 * @param level level at which we need to score.
	 * @return score of the player at given level
	 * @throws IllegalStateException in case the player either doesn't exist or has not played the given level.
	 */
	public static long getHighScoreFor(String playerName, int level) {
		if (level > getHighestLevelForPlayer(playerName)) {
			throw new IllegalStateException(playerName + " has not played level " + level);
		}
		JSONObject root = getJSONData();
		JSONObject playerData = (JSONObject) root.getOrDefault(playerName, null);
		JSONObject scoreData = (JSONObject) playerData.getOrDefault("" + level, null);
		return (long) scoreData.getOrDefault("Score", 0);
	}

	/**
	 * Gets the high score for a player at a level, if it exists. This function just adds a wrapper of safety around the
	 * getHighScoreFor function.
	 * @param playerName name of the desired player
	 * @param level level at which we need to score.
	 * @return score of the player at given level, if it exists.
	 */
	public static long getHighScoreForLevelIfExists(String playerName, int level) {
		try {
			return getHighScoreFor(playerName, level);
		} catch (IllegalStateException e) {
			return Integer.MIN_VALUE;
		}
	}

	/**
	 * Adds a new player in the database.
	 * @param playerName Name of the player being added.
	 */
	public static void addPlayer(String playerName) {
		JSONObject root = getJSONData();
		root.put(playerName, new JSONObject());
		try {
			FileWriter writer = new FileWriter(PATH_TO_DATA_FILE);
			writer.write(root.toJSONString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Saves the given score and stats for a given player.
	 * @param playerName name of the player for which the score is being saved.
	 * @param level level at which the game was played.
	 * @param seconds seconds it took to finish the game.
	 * @param score score of the player at finish line.
	 * @param flipCount number of flips it took for the player to match all cards.
	 */
	public static void saveScoreFor(String playerName, int level, long seconds, long score, long flipCount) {
		JSONObject root = getJSONData();
		JSONObject playerData = (JSONObject) root.getOrDefault(playerName, null);
		if (playerData == null) {
			throw new IllegalStateException("Player does not exist in data file");
		}
		HashMap<String, Long> scoreMap = new HashMap<>();
		scoreMap.put("TimeInSec", seconds);
		scoreMap.put("Score", score);
		scoreMap.put("FlipCount", flipCount);
		playerData.put("" + level, new JSONObject(scoreMap));
		try {
			FileWriter writer = new FileWriter(PATH_TO_DATA_FILE);
			writer.write(root.toJSONString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Helper function to parse the root data of the JSON file.
	 * @return the root JSONObject of parsed JSON file.
	 */
	private static JSONObject getJSONData() {
		JSONParser jsonParser = new JSONParser();
		FileReader reader;
		JSONObject root = null;
		try {
			reader = new FileReader(PATH_TO_DATA_FILE);
			root = (JSONObject) jsonParser.parse(reader);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return root;
	}

}
