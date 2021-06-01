package com.csci143.project.FinalGame.model.utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public final class GameDataUtils {
	private static final String PATH_TO_DATA_FILE = "resources/gameData/game_data.json";

	public static ArrayList<String> getAllPlayers() {
		JSONObject root = getJSONData();
		return new ArrayList<>(root.keySet());
	}

	public static int getHighestLevelForPlayer(String playerName) {
		JSONObject root = getJSONData();
		JSONObject playerData = (JSONObject) root.getOrDefault(playerName, null);
		if (playerData == null) {
			throw new IllegalStateException("Player does not exist in data file");
		}
		int maximumLevel = 0;
		for(Object key: playerData.keySet()) {
			int level = Integer.parseInt((String) key);
			if(level > maximumLevel) {
				maximumLevel = level;
			}
		}
		return maximumLevel;
	}

	public static long getHighScoreFor(String playerName, int level) {
		if (level > getHighestLevelForPlayer(playerName)) {
			throw new IllegalStateException(playerName + " has not played level " + level);
		}
		JSONObject root = getJSONData();
		JSONObject playerData = (JSONObject) root.getOrDefault(playerName, null);
		if (playerData == null) {
			throw new IllegalStateException("Player does not exist in data file");
		}
		JSONObject scoreData = (JSONObject) playerData.getOrDefault("" + level, null);
		return (long) scoreData.getOrDefault("Score", 0);
	}

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
