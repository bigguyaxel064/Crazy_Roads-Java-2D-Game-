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

public class MenuPause {

    private Scene previousScene;
    private IGamePage gameInstance;
    private boolean isMarioKartMode;

    public MenuPause(Stage primaryStage, Scene gameScene, IGamePage gameInstance) {
        this.previousScene = gameScene;
        this.gameInstance = gameInstance;
        this.isMarioKartMode = gameInstance instanceof PageJeuMarioKart;

        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: rgba(26, 26, 46, 0.95);");

        VBox menuBox = new VBox(25);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setMaxWidth(400);

        Text titre = new Text("PAUSE");
        titre.setFont(Font.font("Courier New", FontWeight.BOLD, 80));
        titre.setFill(Color.web("#ffaa00"));

        Text info = new Text("Jeu en pause");
        info.setFont(Font.font("Courier New", FontWeight.NORMAL, 18));
        info.setFill(Color.web("#888888"));

        Button btnResume = createButton("REPRENDRE", "#FF5722", "#FFFFFF");
        btnResume.setOnAction(e -> {
            if (gameInstance != null) {
                gameInstance.resume();
            }
            primaryStage.setScene(previousScene);
        });

        Button btnReplay = createButton("RECOMMENCER", "#FFEB3B", "#000000");
        btnReplay.setOnAction(e -> {
            if (gameInstance != null) {
                gameInstance.stop();
            }
            if (isMarioKartMode) {
                new PageJeuMarioKart(primaryStage);
            } else {
                new PageJeuSubwaySurfers(primaryStage);
            }
        });

        Button btnOptions = createButton("OPTIONS", "#2196F3", "#FFFFFF");
        btnOptions.setOnAction(e -> {
            Scene currentScene = primaryStage.getScene();
            new MenuOptions(primaryStage, currentScene);
        });

        Button btnMenu = createButton("MENU PRINCIPAL", "#F44336", "#FFFFFF");
        btnMenu.setOnAction(e -> {
            if (gameInstance != null) {
                gameInstance.stop();
            }
            new PageAccueil(primaryStage);
        });

        menuBox.getChildren().addAll(titre, info, btnResume, btnReplay, btnOptions, btnMenu);
        root.getChildren().add(menuBox);

        Scene pauseScene = new Scene(root, previousScene.getWidth(), previousScene.getHeight());

        pauseScene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ESCAPE:
                case P:
                    if (gameInstance != null) {
                        gameInstance.resume();
                    }
                    primaryStage.setScene(previousScene);
                    break;
            }
        });

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