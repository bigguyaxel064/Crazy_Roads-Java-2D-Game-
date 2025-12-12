package com.example.crazy_roads.gui;

import com.example.crazy_roads.managers.MusicManager;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.image.Image;

public class PageAccueil {
    private Stage primaryStage;

    public PageAccueil(Stage primaryStage) {
        this.primaryStage = primaryStage;
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        StackPane root = new StackPane();
        MusicManager.playBackgroundMusic();

        try {
            Image img = new Image(getClass().getResourceAsStream("/images/Crazy_Roads_Logo.png"));
            BackgroundImage bImg = new BackgroundImage(img,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true));
            root.setBackground(new Background(bImg));
        } catch (Exception e) {
            root.setStyle("-fx-background-color: #1a1a2e;");
        }

        VBox menuBox = new VBox(25);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setStyle(
                "-fx-background-color: rgba(0, 0, 0, 0.5);" +
                        "-fx-padding: 40px;" +
                        "-fx-background-radius: 15px;"
        );
        menuBox.maxWidthProperty().bind(primaryStage.widthProperty().multiply(0.9));

        Button btnPlay1 = createButton("COURSE INFINIE", "#FFEB3B", "#000000");
        btnPlay1.setOnAction(e -> {
            new PageJeuSubwaySurfers(primaryStage);
        });

        Button btnPlay2 = createButton("CONTRE LA MONTRE", "#FF5722", "#FFFFFF");
        btnPlay2.setOnAction(e -> {
            new PageJeuMarioKart(primaryStage);
        });


        Button btnScores = createButton("MEILLEURS SCORES", "#80dfb3", "#000000");
        btnScores.setOnAction(e -> {
            Scene currentScene = primaryStage.getScene();
            new BestScoresMenu(primaryStage, currentScene);
        });

        Button btnOptions = createButton("OPTIONS", "#2196F3", "#FFFFFF");
        btnOptions.setOnAction(e -> {
            Scene currentScene = primaryStage.getScene();
            new MenuOptions(primaryStage, currentScene);
        });

        Button btnLeave = createButton("QUITTER", "#F44336", "#FFFFFF");
        btnLeave.setOnAction(e -> {
            primaryStage.close();
        });

        menuBox.getChildren().addAll(btnPlay1, btnPlay2, btnScores, btnOptions, btnLeave);
        root.getChildren().add(menuBox);

        Scene sc = new Scene(root, bounds.getWidth(), bounds.getHeight());
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());
        primaryStage.setTitle("Crazy Roads");
        primaryStage.setScene(sc);
        primaryStage.show();
        primaryStage.setMaximized(true);
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