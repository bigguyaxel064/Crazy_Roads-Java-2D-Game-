package com.example.crazy_roads.gui;

import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import com.example.crazy_roads.managers.ScoreManager;

public class HUD {
    private HBox hudRoot;
    private Text scoreLabel;
    private Text scoreValue;
    private Text bestScoreLabel;
    private Text bestScoreValue;
    private Text livesLabel;
    private Text livesValue;
    private Text timerLabel;
    private Text timerValue;
    private Text boostLabel;
    private Text boostValue;
    private Text modeLabel;
    private VBox boostBox;
    private Text separator2;
    private VBox scoreBox;

    private int currentScore = 0;
    private int bestScore = 0;
    private int lives = 3;
    private long startTimeMs = 0;
    private boolean boostActive = false;
    private String gameMode = "COURSE INFINIE";
    private String currentGameModeKey = ScoreManager.MODE_INFINITE;
    private AnimationTimer updateTimer;

    private boolean isCountdown = false;
    private long totalTimeMs = 60000;
    private boolean timeExpired = false;

    private ScoreManager scoreManager;

    public HUD() {
        scoreManager = ScoreManager.getInstance();
        initializeHUD();
        startUpdateTimer();
        loadBestScore();
    }

    private void initializeHUD() {
        hudRoot = new HBox(30);
        hudRoot.setStyle(
                "-fx-background-color: rgba(0, 0, 0, 0.7);" +
                        "-fx-padding: 15px 20px;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-border-color: #FFEB3B;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 10px;"
        );
        hudRoot.setAlignment(Pos.CENTER_LEFT);

        scoreBox = createHUDSection("SCORE", String.valueOf(currentScore), Color.web("#FFEB3B"));
        scoreLabel = (Text) ((VBox) scoreBox).getChildren().get(0);
        scoreValue = (Text) ((VBox) scoreBox).getChildren().get(1);

        VBox bestScoreBox = createHUDSection("MEILLEUR", String.valueOf(bestScore), Color.web("#FFC107"));
        bestScoreLabel = (Text) ((VBox) bestScoreBox).getChildren().get(0);
        bestScoreValue = (Text) ((VBox) bestScoreBox).getChildren().get(1);

        VBox livesBox = createHUDSection("VIES", String.valueOf(lives), Color.web("#FF5252"));
        livesLabel = (Text) ((VBox) livesBox).getChildren().get(0);
        livesValue = (Text) ((VBox) livesBox).getChildren().get(1);

        Text separator1 = new Text(" | ");
        separator1.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        separator1.setFill(Color.web("#FFFFFF"));

        VBox timerBox = createHUDSection("TEMPS", "00:00", Color.web("#4DD0E1"));
        timerLabel = (Text) ((VBox) timerBox).getChildren().get(0);
        timerValue = (Text) ((VBox) timerBox).getChildren().get(1);

        boostBox = createHUDSection("BOOST", "INACTIF", Color.web("#AB47BC"));
        boostLabel = (Text) ((VBox) boostBox).getChildren().get(0);
        boostValue = (Text) ((VBox) boostBox).getChildren().get(1);

        separator2 = new Text(" | ");
        separator2.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        separator2.setFill(Color.web("#FFFFFF"));

        modeLabel = new Text(gameMode);
        modeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        modeLabel.setFill(Color.web("#4CAF50"));

        VBox modeBox = new VBox(5);
        modeBox.setAlignment(Pos.CENTER);
        modeBox.getChildren().add(modeLabel);
        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        hudRoot.getChildren().addAll(
                scoreBox, bestScoreBox, livesBox,
                separator1, timerBox, boostBox,
                separator2, spacer, modeBox
        );

        boostBox.setVisible(false);
        boostBox.setManaged(false);
        separator2.setVisible(false);
        separator2.setManaged(false);
    }

    private VBox createHUDSection(String label, String value, Color color) {
        VBox section = new VBox(3);
        section.setAlignment(Pos.CENTER);

        Text labelText = new Text(label);
        labelText.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        labelText.setFill(Color.web("#FFFFFF"));
        labelText.setTextAlignment(TextAlignment.CENTER);

        Text valueText = new Text(value);
        valueText.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        valueText.setFill(color);
        valueText.setTextAlignment(TextAlignment.CENTER);

        section.getChildren().addAll(labelText, valueText);
        return section;
    }

    private void startUpdateTimer() {
        updateTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateDisplay();
            }
        };
        updateTimer.start();
        startTimeMs = System.currentTimeMillis();
    }

    private void updateDisplay() {
        scoreValue.setText(String.valueOf(currentScore));
        if (currentScore > bestScore) {
            bestScore = currentScore;
            bestScoreValue.setText(String.valueOf(bestScore));
            bestScoreValue.setFill(Color.web("#FFD700"));
        } else {
            bestScoreValue.setFill(Color.web("#FFC107"));
        }

        livesValue.setText(String.valueOf(lives));

        updateTimer();
        updateBoostDisplay();
        updateLivesColor();
    }

    private void updateTimer() {
        long elapsedMs = System.currentTimeMillis() - startTimeMs;
        long seconds = elapsedMs / 1000;
        long minutes = seconds / 60;
        long displaySeconds = seconds % 60;
        timerValue.setFill(Color.web("#4DD0E1"));
        timerValue.setText(String.format("%02d:%02d", minutes, displaySeconds));
    }

    private void updateBoostDisplay() {
        if (boostActive) {
            boostValue.setText("ACTIF");
            boostValue.setFill(Color.web("#FFD700"));
        } else {
            boostValue.setText("INACTIF");
            boostValue.setFill(Color.web("#AB47BC"));
        }
    }

    private void updateLivesColor() {
        if (lives >= 3) {
            livesValue.setFill(Color.web("#4CAF50"));
        } else if (lives == 2) {
            livesValue.setFill(Color.web("#FFC107"));
        } else if (lives == 1) {
            livesValue.setFill(Color.web("#FF5252"));
        } else {
            livesValue.setFill(Color.web("#F44336"));
        }
    }

    public void setCountdownMode() {
        this.isCountdown = true;
        this.totalTimeMs = 1000L;
        this.gameMode = "CONTRE LA MONTRE";
        this.currentGameModeKey = ScoreManager.MODE_TIME_ATTACK;

        if (modeLabel != null) {
            modeLabel.setText(gameMode);
        }
        if (boostBox != null) {
            boostBox.setVisible(true);
            boostBox.setManaged(true);
            separator2.setVisible(true);
            separator2.setManaged(true);
        }
        if (scoreBox != null) {
            scoreBox.setVisible(false);
            scoreBox.setManaged(false);
            separator2.setVisible(false);
            separator2.setManaged(false);
        }
        currentScore = 0;
        loadBestScore();
        resetTimer();
    }

    public void setInfiniteMode() {
        this.isCountdown = false;
        this.gameMode = "COURSE INFINIE";
        this.currentGameModeKey = ScoreManager.MODE_INFINITE;

        if (modeLabel != null) {
            modeLabel.setText(gameMode);
        }
        if (boostBox != null) {
            boostBox.setVisible(false);
            boostBox.setManaged(false);
            separator2.setVisible(false);
            separator2.setManaged(false);
        }
        currentScore = 0;
        loadBestScore();
        resetTimer();
    }

    public void setTimeAttackMode() {
        this.isCountdown = false;
        this.gameMode = "CONTRE LA MONTRE";
        this.currentGameModeKey = ScoreManager.MODE_TIME_ATTACK;

        if (modeLabel != null) {
            modeLabel.setText(gameMode);
        }
        if (boostBox != null) {
            boostBox.setVisible(true);
            boostBox.setManaged(true);
            separator2.setVisible(true);
            separator2.setManaged(true);
        }
        if (scoreBox != null) {
            scoreBox.setVisible(false);
            scoreBox.setManaged(false);
        }
        currentScore = 0;
        loadBestScore();
        resetTimer();
    }

    public void setTime(long seconds) {
        long minutes = seconds / 60;
        long displaySeconds = seconds % 60;
        if (timerValue != null) {
            timerValue.setText(String.format("%02d:%02d", minutes, displaySeconds));
        }
    }

    private void loadBestScore() {
        bestScore = scoreManager.getBestScore(currentGameModeKey);
        if (bestScoreValue != null) {
            bestScoreValue.setText(String.valueOf(bestScore));
        }
    }

    public void addScore(int points) {
        currentScore += points;
    }

    public boolean saveScoreIfBest() {
        int scoreToSave = currentScore;
        if (currentGameModeKey.equals(ScoreManager.MODE_TIME_ATTACK)) {
            scoreToSave = (int) getElapsedTimeSeconds();
        }

        return scoreManager.updateBestScore(currentGameModeKey, scoreToSave);
    }

    public boolean isNewRecord() {
        if (currentGameModeKey.equals(ScoreManager.MODE_TIME_ATTACK)) {
            if (bestScore == 0) return false;
            int currentTime = (int) getElapsedTimeSeconds();
            return currentTime < bestScore;
        } else {
            return currentScore > bestScore;
        }
    }

    public String getCurrentGameModeKey() {
        return currentGameModeKey;
    }

    public HBox getHUDRoot() {
        return hudRoot;
    }

    public void setScore(int score) {
        this.currentScore = score;
    }

    public void setBestScore(int bestScore) {
        this.bestScore = bestScore;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void setBoostActive(boolean active) {
        this.boostActive = active;
    }

    public void setGameMode(String mode) {
        this.gameMode = mode;
        if (modeLabel != null) {
            modeLabel.setText(mode);
        }
    }

    public void resetTimer() {
        startTimeMs = System.currentTimeMillis();
        timeExpired = false;
    }

    public boolean isTimeExpired() {
        return timeExpired;
    }

    public long getRemainingTimeSeconds() {
        if (!isCountdown) return -1;
        long elapsedMs = System.currentTimeMillis() - startTimeMs;
        long remainingMs = totalTimeMs - elapsedMs;
        if (remainingMs <= 0) return 0;
        return remainingMs / 1000;
    }

    public long getElapsedTimeSeconds() {
        long elapsedMs = System.currentTimeMillis() - startTimeMs;
        return elapsedMs / 1000;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public int getBestScore() {
        return bestScore;
    }

    public int getLives() {
        return lives;
    }

    public boolean isBoostActive() {
        return boostActive;
    }

    public void stop() {
        if (updateTimer != null) {
            updateTimer.stop();
        }
    }
}