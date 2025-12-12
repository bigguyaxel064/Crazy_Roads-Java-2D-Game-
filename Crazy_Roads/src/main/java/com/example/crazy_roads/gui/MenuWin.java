package com.example.crazy_roads.gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MenuWin {
    private Scene previousScene;
    private boolean isMarioKartMode;

    public MenuWin(Stage primaryStage, Scene gameScene, int finalScore, boolean isNewRecord) {
        this(primaryStage, gameScene, finalScore, isNewRecord, false);
    }

    public MenuWin(Stage primaryStage, Scene gameScene, int finalScore, boolean isNewRecord, boolean isMarioKartMode) {
        this.previousScene = gameScene;
        this.isMarioKartMode = isMarioKartMode;

        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: rgba(26, 26, 46, 0.95);");

        VBox menuBox = new VBox(25);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setMaxWidth(400);

        Text titre = new Text("VICTOIRE");
        titre.setFont(Font.font("Courier New", FontWeight.BOLD, 80));
        titre.setFill(Color.web("#4CAF50"));

        Text scoreText = null;
        Text recordText = null;

        if (isMarioKartMode) {
            // Mode Mario Kart: afficher le temps
            int minutes = finalScore / 60000;
            int seconds = (finalScore % 60000) / 1000;
            int millis = (finalScore % 1000) / 10;
            scoreText = new Text(String.format("Temps: %02d:%02d.%02d", minutes, seconds, millis));
            scoreText.setFont(Font.font("Courier New", FontWeight.BOLD, 36));
            scoreText.setFill(Color.web("#FFEB3B"));

            if (isNewRecord) {
                recordText = new Text("🏆 NOUVEAU RECORD ! 🏆");
                recordText.setFont(Font.font("Courier New", FontWeight.BOLD, 24));
                recordText.setFill(Color.web("#FFD700"));
            }
        } else {
            // Mode Infini: afficher le score
            scoreText = new Text("Score: " + finalScore);
            scoreText.setFont(Font.font("Courier New", FontWeight.BOLD, 36));
            scoreText.setFill(Color.web("#FFEB3B"));

            if (isNewRecord) {
                recordText = new Text("🏆 NOUVEAU RECORD ! 🏆");
                recordText.setFont(Font.font("Courier New", FontWeight.BOLD, 24));
                recordText.setFill(Color.web("#FFD700"));
            }
        }

        Button btnReplay = createButton("RECOMMENCER", "#FFEB3B", "#000000");
        btnReplay.setOnAction(e -> {
            if (isMarioKartMode) {
                new PageJeuMarioKart(primaryStage);
            } else {
                new PageJeuSubwaySurfers(primaryStage);
            }
        });

        Button btnMenu = createButton("MENU PRINCIPAL", "#F44336", "#FFFFFF");
        btnMenu.setOnAction(e -> {
            new PageAccueil(primaryStage);
        });

        if (recordText != null) {
            menuBox.getChildren().addAll(titre, recordText, scoreText, btnReplay, btnMenu);
        } else {
            menuBox.getChildren().addAll(titre, scoreText, btnReplay, btnMenu);
        }
        root.getChildren().add(menuBox);

        Scene pauseScene = new Scene(root, previousScene.getWidth(), previousScene.getHeight());
        primaryStage.setScene(pauseScene);
    }

    private Button createButton(String texte, String couleurBg, String couleurTexte) {
        Button btn = new Button(texte);
        btn.setFont(Font.font("Courier New", FontWeight.BOLD, 26));
        btn.setPrefWidth(380);
        btn.setPrefHeight(65);
        btn.setTextFill(Color.web(couleurTexte));
        btn.setStyle(
                "-fx-background-color: " + couleurBg + ";" +
                        "-fx-border-color: #FFFFFF;" +
                        "-fx-border-width: 3px;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 10, 0, 0, 5);"
        );

        btn.setOnMouseEntered(e -> {
            btn.setStyle(
                    "-fx-background-color: " + couleurBg + ";" +
                            "-fx-border-color: #FFFFFF;" +
                            "-fx-border-width: 4px;" +
                            "-fx-border-radius: 10px;" +
                            "-fx-background-radius: 10px;" +
                            "-fx-effect: dropshadow(gaussian, rgba(255,255,255,0.8), 15, 0, 0, 0);" +
                            "-fx-scale-x: 1.05;" +
                            "-fx-scale-y: 1.05;"
            );
        });

        btn.setOnMouseExited(e -> {
            btn.setStyle(
                    "-fx-background-color: " + couleurBg + ";" +
                            "-fx-border-color: #FFFFFF;" +
                            "-fx-border-width: 3px;" +
                            "-fx-border-radius: 10px;" +
                            "-fx-background-radius: 10px;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 10, 0, 0, 5);"
            );
        });

        return btn;
    }
}