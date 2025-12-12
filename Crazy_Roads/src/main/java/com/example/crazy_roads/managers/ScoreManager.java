package com.example.crazy_roads.managers;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ScoreManager {
    private static final String SCORES_FILE = "scores.dat";
    private static ScoreManager instance;
    private final Map<String, List<Integer>> scoresByMode = new HashMap<>();

    public static final String MODE_INFINITE = "COURSE_INFINIE";
    public static final String MODE_TIME_ATTACK = "CONTRE_LA_MONTRE";

    private ScoreManager() {
        loadScores();
    }

    public static ScoreManager getInstance() {
        if (instance == null) {
            instance = new ScoreManager();
        }
        return instance;
    }

    public void loadScores() {
        scoresByMode.put(MODE_INFINITE, new ArrayList<>());
        scoresByMode.put(MODE_TIME_ATTACK, new ArrayList<>());

        try {
            if (!Files.exists(Paths.get(SCORES_FILE))) return;

            try (BufferedReader reader = new BufferedReader(new FileReader(SCORES_FILE))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.isEmpty() || line.startsWith("#")) continue;
                    String[] parts = line.split("=", 2);
                    if (parts.length != 2) continue;

                    String mode = parts[0].trim();
                    int score = Integer.parseInt(parts[1].trim());
                    scoresByMode.computeIfAbsent(mode, k -> new ArrayList<>()).add(score);
                }
            }
        } catch (IOException | NumberFormatException e) {
        }
    }

    public void saveScore(String mode, int score) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SCORES_FILE, true))) {
            writer.write(mode + "=" + score + "\n"); // append
        } catch (IOException e) {
        }
        scoresByMode.computeIfAbsent(mode, k -> new ArrayList<>()).add(score);
    }

    public int getBestScore(String mode) {
        List<Integer> scores = getTopScores(mode, 1);
        return scores.isEmpty() ? 0 : scores.get(0);
    }

    public boolean updateBestScore(String gameMode, int newScore) {
        int currentBest = getBestScore(gameMode);
        boolean isNewRecord = false;

        if (MODE_INFINITE.equals(gameMode)) {
            if (newScore > currentBest) {
                isNewRecord = true;
            }
        } else {
            if (newScore < currentBest || currentBest == 0) {
                isNewRecord = true;
            }
        }
        
        saveScore(gameMode, newScore);
        return isNewRecord;
    }

    public List<Integer> getTopScores(String mode, int limit) {
        List<Integer> scores = new ArrayList<>(scoresByMode.getOrDefault(mode, new ArrayList<>()));

        scores.removeIf(score -> score <= 0);

        if (MODE_INFINITE.equals(mode)) {
            scores.sort(Collections.reverseOrder());
        } else {
            scores.sort(Integer::compareTo);
        }

        return scores.subList(0, Math.min(limit, scores.size()));
    }

    public void resetAllScores() {
        scoresByMode.clear();
        scoresByMode.put(MODE_INFINITE, new ArrayList<>());
        scoresByMode.put(MODE_TIME_ATTACK, new ArrayList<>());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SCORES_FILE))) {
            writer.write("# Scores Crazy Roads\n");
        } catch (IOException e) {
        }
    }

    public void resetScore(String mode) {
        scoresByMode.put(mode, new ArrayList<>());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SCORES_FILE))) {
            writer.write("# Scores Crazy Roads\n");
            for (Map.Entry<String, List<Integer>> entry : scoresByMode.entrySet()) {
                for (int score : entry.getValue()) {
                    writer.write(entry.getKey() + "=" + score + "\n");
                }
            }
        } catch (IOException e) {
        }
    }
}
