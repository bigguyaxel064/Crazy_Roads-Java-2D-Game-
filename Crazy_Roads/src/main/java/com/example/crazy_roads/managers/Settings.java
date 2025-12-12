package com.example.crazy_roads.managers;

import javafx.scene.input.KeyCode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Settings {
    private static final String SETTINGS_FILE = "settings.ini";
    private static Settings instance;
    private double musicVolume = 0.5;
    private double soundVolume = 0.5;
    private double screenWidth = 1920;
    private double screenHeight = 1080;
    private KeyCode leftKey = KeyCode.LEFT;
    private KeyCode rightKey = KeyCode.RIGHT;
    private KeyCode pauseKey = KeyCode.P;
    private KeyCode boostKey = KeyCode.SPACE;

    private Settings() {
        loadSettings();
    }

    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    private void loadSettings() {
        try {
            if (!Files.exists(Paths.get(SETTINGS_FILE))) return;
            try (BufferedReader reader = new BufferedReader(new FileReader(SETTINGS_FILE))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.isEmpty() || line.startsWith("#")) continue;
                    String[] parts = line.split("=", 2);
                    if (parts.length != 2) continue;
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    switch (key) {
                        case "musicVolume":
                            musicVolume = Double.parseDouble(value);
                            break;
                        case "soundVolume":
                            soundVolume = Double.parseDouble(value);
                            break;
                        case "screenWidth":
                            screenWidth = Double.parseDouble(value);
                            break;
                        case "screenHeight":
                            screenHeight = Double.parseDouble(value);
                            break;
                        case "leftKey":
                            leftKey = KeyCode.valueOf(value);
                            break;
                        case "rightKey":
                            rightKey = KeyCode.valueOf(value);
                            break;
                        case "pauseKey":
                            pauseKey = KeyCode.valueOf(value);
                            break;
                        case "boostKey":
                            boostKey = KeyCode.valueOf(value);
                            break;

                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
        }
    }

    public void saveSettings() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SETTINGS_FILE))) {
            writer.write("# Paramètres du jeu Crazy Roads\n");
            writer.write("musicVolume=" + musicVolume + "\n");
            writer.write("soundVolume=" + soundVolume + "\n");
            writer.write("screenWidth=" + screenWidth + "\n");
            writer.write("screenHeight=" + screenHeight + "\n");
            writer.write("leftKey=" + leftKey + "\n");
            writer.write("rightKey=" + rightKey + "\n");
            writer.write("pauseKey=" + pauseKey + "\n");
            writer.write("boostKey=" + boostKey + "\n");

        } catch (IOException e) {
        }
    }

    public double getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(double volume) {
        this.musicVolume = Math.max(0.0, Math.min(1.0, volume));
    }

    public double getSoundVolume() {
        return soundVolume;
    }

    public void setSoundVolume(double volume) {
        this.soundVolume = Math.max(0.0, Math.min(1.0, volume));
    }

    public double getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(double width) {
        this.screenWidth = Math.max(800, width);
    }

    public double getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(double height) {
        this.screenHeight = Math.max(600, height);
    }

    public KeyCode getLeftKey() {
        return leftKey;
    }

    public void setLeftKey(KeyCode key) {
        this.leftKey = key;
    }

    public KeyCode getRightKey() {
        return rightKey;
    }

    public void setRightKey(KeyCode key) {
        this.rightKey = key;
    }

    public KeyCode getPauseKey() {
        return pauseKey;
    }

    public void setPauseKey(KeyCode key) {
        this.pauseKey = key;
    }

    public KeyCode getBoostKey() {
        return boostKey;
    }

    public void setBoostKey(KeyCode key) {
        this.boostKey = key;
    }

}
