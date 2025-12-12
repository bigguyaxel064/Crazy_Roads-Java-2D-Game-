package com.example.crazy_roads.gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import com.example.crazy_roads.managers.ScoreManager;

import java.util.List;

public class BestScoresMenu {
    private final Stage primaryStage;
    private final Scene previousScene;
    private final ScoreManager scoreManager;

    public BestScoresMenu(Stage primaryStage, Scene previousScene) {
        this.primaryStage = primaryStage;
        this.previousScene = previousScene;
        this.scoreManager = ScoreManager.getInstance();
        showScoresMenu();
    }

    private void showScoresMenu() {
        scoreManager.loadScores();
        
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #1a1a2e;");

        VBox mainBox = new VBox(40);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setStyle("-fx-padding: 50px;");

        Text titre = new Text("MEILLEURS SCORES");
        titre.setFont(Font.font("Courier New", FontWeight.BOLD, 60));
        titre.setFill(Color.web("#FFD700"));

        VBox scoresBox = new VBox(30);
        scoresBox.setAlignment(Pos.CENTER);
        scoresBox.setStyle(
                "-fx-background-color: rgba(0, 0, 0, 0.5);" +
                        "-fx-padding: 40px;" +
                        "-fx-background-radius: 15px;" +
                        "-fx-border-color: #FFD700;" +
                        "-fx-border-width: 3;" +
                        "-fx-border-radius: 15px;"
        );
        scoresBox.setMaxWidth(700);

        VBox infiniteScoreBox = createScoreDisplay(
                "COURSE INFINIE",
                scoreManager.getBestScore(ScoreManager.MODE_INFINITE),
                "#00ff88",
                "🏁",
                false,
                "points"
        );

        javafx.scene.shape.Line separator = new javafx.scene.shape.Line();
        separator.setEndX(500);
        separator.setStroke(Color.web("#444466"));
        separator.setStrokeWidth(2);

        VBox timeAttackScoreBox = createScoreDisplay(
                "CONTRE LA MONTRE",
                scoreManager.getBestScore(ScoreManager.MODE_TIME_ATTACK),
                "#ffaa00",
                "⏱",
                true,
                "secondes");

        scoresBox.getChildren().addAll(infiniteScoreBox, separator, timeAttackScoreBox);

        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);

        Button btnClassement = createButton("CLASSEMENT", "#2196F3", "#FFFFFF");
        btnClassement.setOnAction(e -> showRankingMenu());

        Button btnRetour = createButton("RETOUR", "#4CAF50", "#FFFFFF");
        btnRetour.setOnAction(e -> {
            primaryStage.setScene(previousScene);
        });

        Button btnReset = createButton("RÉINITIALISER", "#F44336", "#FFFFFF");
        btnReset.setOnAction(e -> {
            scoreManager.resetAllScores();
            showScoresMenu();
        });

        buttonBox.getChildren().addAll(btnRetour, btnReset, btnClassement);

        mainBox.getChildren().addAll(titre, scoresBox, buttonBox);
        root.getChildren().add(mainBox);

        Scene scoresScene = new Scene(root, 1200, 800);

        scoresScene.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
                primaryStage.setScene(previousScene);
            }
        });

        primaryStage.setScene(scoresScene);
    }

    private VBox createScoreDisplay(String modeName, int score, String color, String emoji, boolean isTimeAttack, String str) {
        VBox box = new VBox(15);
        box.setAlignment(Pos.CENTER);

        HBox headerBox = new HBox(15);
        headerBox.setAlignment(Pos.CENTER);

        Text emojiText = new Text(emoji);
        emojiText.setFont(Font.font(40));
        emojiText.setFill(Color.web(color));

        Text modeText = new Text(modeName);
        modeText.setFont(Font.font("Courier New", FontWeight.BOLD, 28));
        modeText.setFill(Color.web("#FFFFFF"));

        headerBox.getChildren().addAll(emojiText, modeText);

        Text scoreText;
        if (isTimeAttack && score > 0) {
            int minutes = score / 60;
            int seconds = score % 60;
            scoreText = new Text(minutes + "." + String.format("%02d", seconds));
            str = "minutes";
        } else {
            scoreText = new Text(String.format("%,d", score));
        }
        scoreText.setFont(Font.font("Courier New", FontWeight.BOLD, 50));
        scoreText.setFill(Color.web(color));

        Text strText = new Text(str);
        strText.setFont(Font.font("Courier New", FontWeight.BOLD, 20));
        strText.setFill(Color.web("#FFFFFF"));

        box.getChildren().addAll(headerBox, scoreText, strText);
        return box;
    }

    private Button createButton(String texte, String couleurBg, String couleurTexte) {
        Button btn = new Button(texte);
        btn.setFont(Font.font("Courier New", FontWeight.BOLD, 20));
        btn.setPrefWidth(250);
        btn.setPrefHeight(50);
        btn.setTextFill(Color.web(couleurTexte));
        btn.setStyle(
                "-fx-background-color: " + couleurBg + ";" +
                        "-fx-border-color: #FFFFFF;" +
                        "-fx-border-width: 2px;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-background-radius: 10px;"
        );

        btn.setOnMouseEntered(e -> {
            btn.setScaleX(1.05);
            btn.setScaleY(1.05);
        });

        btn.setOnMouseExited(e -> {
            btn.setScaleX(1.0);
            btn.setScaleY(1.0);
        });

        return btn;
    }

    private void showRankingMenu() {
        scoreManager.loadScores();
        
        VBox rankingBox = new VBox(20);
        rankingBox.setAlignment(Pos.CENTER);
        rankingBox.setStyle("-fx-padding: 40px;");

        Text titre = new Text("🏆 CLASSEMENT TOP 5");
        titre.setFont(Font.font("Courier New", FontWeight.BOLD, 40));
        titre.setFill(Color.web("#FFD700"));

        List<Integer> topInfinite = scoreManager.getTopScores(ScoreManager.MODE_INFINITE, 5);
        VBox infiniteRanking = createScoreList("COURSE INFINIE", topInfinite, "#00ff88", "🏁", false);

        List<Integer> topTimeAttack = scoreManager.getTopScores(ScoreManager.MODE_TIME_ATTACK, 5);
        VBox timeAttackRanking = createScoreList("CONTRE LA MONTRE", topTimeAttack, "#ffaa00", "⏱", true);

        Button btnRetour = createButton("RETOUR", "#4CAF50", "#FFFFFF");
        btnRetour.setOnAction(e -> primaryStage.setScene(previousScene));

        rankingBox.getChildren().addAll(titre, infiniteRanking, timeAttackRanking, btnRetour);

        Scene rankingScene = new Scene(rankingBox, 1200, 800);
        rankingBox.setStyle("-fx-background-color: #1a1a2e;");
        primaryStage.setScene(rankingScene);
    }

    private VBox createScoreList(String modeName, List<Integer> scores, String color, String emoji, boolean isTimeAttack) {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setStyle(
                "-fx-background-color: rgba(0, 0, 0, 0.5);" +
                        "-fx-padding: 20px;" +
                        "-fx-background-radius: 15px;" +
                        "-fx-border-color: " + color + ";" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 15px;"
        );
        box.setMaxWidth(600);

        HBox headerBox = new HBox(15);
        headerBox.setAlignment(Pos.CENTER);

        Text emojiText = new Text(emoji);
        emojiText.setFont(Font.font(40));
        emojiText.setFill(Color.web(color));

        Text modeText = new Text(modeName + " - Top 5");
        modeText.setFont(Font.font("Courier New", FontWeight.BOLD, 24));
        modeText.setFill(Color.web("#FFFFFF"));

        headerBox.getChildren().addAll(emojiText, modeText);
        box.getChildren().add(headerBox);

        if (scores.isEmpty()) {
            Text noScoreText = new Text("Aucun score enregistré");
            noScoreText.setFont(Font.font("Courier New", FontWeight.NORMAL, 18));
            noScoreText.setFill(Color.web("#888888"));
            box.getChildren().add(noScoreText);
        } else {
            int rank = 1;
            for (int score : scores) {
                String text;
                if (isTimeAttack) {
                    int minutes = score / 60;
                    int seconds = score % 60;
                    text = rank + ". " + minutes + "." + String.format("%02d", seconds) + " minutes";
                } else {
                    text = rank + ". " + String.format("%,d", score) + " points";
                }

                Text scoreText = new Text(text);
                scoreText.setFont(Font.font("Courier New", FontWeight.BOLD, 20));
                scoreText.setFill(Color.web(color));
                box.getChildren().add(scoreText);
                rank++;
            }
        }

        return box;
    }
}