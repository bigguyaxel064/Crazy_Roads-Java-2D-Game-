package com.example.crazy_roads.gui;

import com.example.crazy_roads.managers.Settings;
import com.example.crazy_roads.managers.MusicManager;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.function.Consumer;

public class MenuOptions {
    private final Settings settings;
    private final Scene previousScene;
    private final Stage primaryStage;
    private final double initialMusicVol;
    private final double initialSoundVol;
    private final double initialScreenWidth;
    private final double initialScreenHeight;
    private final KeyCode initialLeftKey;
    private final KeyCode initialRightKey;
    private final KeyCode initialPauseKey;
    private final KeyCode initialBoostKey;

    public MenuOptions(Stage primaryStage, Scene previousScene) {
        this.primaryStage = primaryStage;
        this.previousScene = previousScene;
        this.settings = Settings.getInstance();
        this.initialMusicVol = settings.getMusicVolume();
        this.initialSoundVol = settings.getSoundVolume();
        this.initialScreenWidth = settings.getScreenWidth();
        this.initialScreenHeight = settings.getScreenHeight();
        this.initialLeftKey = settings.getLeftKey();
        this.initialRightKey = settings.getRightKey();
        this.initialPauseKey = settings.getPauseKey();
        this.initialBoostKey = settings.getBoostKey();

        showOptionsMenu();
    }

    private void showOptionsMenu() {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #1a1a2e;");

        VBox mainBox = new VBox(20);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setStyle("-fx-padding: 30px;");

        Text titre = new Text("OPTIONS");
        titre.setFont(Font.font("Courier New", FontWeight.BOLD, 60));
        titre.setFill(Color.web("#ffaa00"));

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setStyle("-fx-font-size: 14px;");

        Tab tabSound = createSoundTab();
        tabSound.setText("SON");

        Tab tabControls = createControlsTab();
        tabControls.setText("CONTRÔLES");

        Tab tabScreen = createScreenTab();
        tabScreen.setText("ÉCRAN");

        tabPane.getTabs().addAll(tabSound, tabControls, tabScreen);
        tabPane.setPrefHeight(420);

        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);

        Button btnSave = createButton("APPLIQUER", "#4CAF50", "#FFFFFF");
        btnSave.setOnAction(e -> {
            settings.saveSettings();
             primaryStage.setWidth(settings.getScreenWidth());
             primaryStage.setHeight(settings.getScreenHeight());
            primaryStage.setScene(previousScene);
        });

        Button btnCancel = createButton("ANNULER", "#F44336", "#FFFFFF");
        btnCancel.setOnAction(e -> {
            settings.setMusicVolume(initialMusicVol);
            settings.setSoundVolume(initialSoundVol);
            settings.setScreenWidth(initialScreenWidth);
            settings.setScreenHeight(initialScreenHeight);
            settings.setLeftKey(initialLeftKey);
            settings.setRightKey(initialRightKey);
            settings.setPauseKey(initialPauseKey);
            settings.setBoostKey(initialBoostKey);
            primaryStage.setScene(previousScene);
            MusicManager.updateVolume();
        });

        buttonBox.getChildren().addAll(btnSave, btnCancel);

        mainBox.getChildren().addAll(titre, tabPane, buttonBox);
        root.getChildren().add(mainBox);

        Scene optionsScene = new Scene(root, 1200, 800);
        primaryStage.setScene(optionsScene);
    }

    private Tab createSoundTab() {
        Tab tab = new Tab();
        tab.setClosable(false);

        VBox content = new VBox(20);
        content.setStyle("-fx-padding: 20px; -fx-background-color: #2a2a4e;");
        content.setAlignment(Pos.CENTER);

        HBox musicVolumeBox = createVolumeControl(
                "Volume Musique",
                settings.getMusicVolume(),
                value -> settings.setMusicVolume(value)
        );

        HBox soundVolumeBox = createVolumeControl(
                "Volume Effets Sonores",
                settings.getSoundVolume(),
                value -> settings.setSoundVolume(value)
        );

        content.getChildren().addAll( musicVolumeBox, soundVolumeBox);
        tab.setContent(content);
        return tab;
    }

    private Tab createControlsTab() {
        Tab tab = new Tab("Contrôles"); // Donne un titre au Tab
        tab.setClosable(false);

        VBox content = new VBox(20);
        content.setStyle("-fx-padding: 20px; -fx-background-color: #2a2a4e;");
        content.setAlignment(Pos.CENTER);

        HBox leftKeyBox = createKeyBindingControl(
                "Gauche",
                settings.getLeftKey(),
                newKey -> settings.setLeftKey(newKey)
        );

        HBox rightKeyBox = createKeyBindingControl(
                "Droite",
                settings.getRightKey(),
                newKey -> settings.setRightKey(newKey)
        );

        HBox pauseKeyBox = createKeyBindingControl(
                "Pause",
                settings.getPauseKey(),
                newKey -> settings.setPauseKey(newKey)
        );

        HBox boostKeyBox = createKeyBindingControl(
                "Boost",
                settings.getBoostKey(),
                newKey -> settings.setBoostKey(newKey)
        );

        content.getChildren().addAll(leftKeyBox, rightKeyBox, boostKeyBox, pauseKeyBox);
        tab.setContent(content);
        return tab;
    }


    private Tab createScreenTab() {
        Tab tab = new Tab();
        tab.setClosable(false);

        VBox content = new VBox(20);
        content.setStyle("-fx-padding: 20px; -fx-background-color: #2a2a4e;");
        content.setAlignment(Pos.CENTER);

        HBox resolutionBox = new HBox(20);
        resolutionBox.setAlignment(Pos.CENTER);
        resolutionBox.setStyle("-fx-border-color: #444466; -fx-border-width: 1; -fx-padding: 15px; -fx-border-radius: 5;");

        Label labelResolution = createLabel("Résolution :");

        ComboBox<String> resolutionCombo = new ComboBox<>();
        resolutionCombo.setPrefWidth(300);
        resolutionCombo.getItems().addAll(
                "1280x720",
                "1366x768",
                "1600x900",
                "1920x1080",
                "2560x1440"
        );
        String currentRes = (int)settings.getScreenWidth() + "x" + (int)settings.getScreenHeight();
        if (resolutionCombo.getItems().contains(currentRes)) {
            resolutionCombo.setValue(currentRes);
        }
        else {
            resolutionCombo.setValue("1920x1080");
        }

        resolutionCombo.setOnAction(e -> {
            String selected = resolutionCombo.getValue();
            try {
                String[] parts = selected.split("x");
                double w = Double.parseDouble(parts[0]);
                double h = Double.parseDouble(parts[1]);
                settings.setScreenWidth(w);
                settings.setScreenHeight(h);
            } catch (Exception ex) {
            }
        });

        resolutionBox.getChildren().addAll(labelResolution, resolutionCombo);

        content.getChildren().addAll(resolutionBox);
        tab.setContent(content);
        return tab;
    }

    private HBox createVolumeControl(String label, double currentValue, Consumer<Double> onChange) {
        HBox box = new HBox(20);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-border-color: #444466; -fx-border-width: 1; -fx-padding: 15px; -fx-border-radius: 5;");

        Label labelText = createLabel(label + " :");

        Slider slider = new Slider(0, 1, currentValue);
        slider.setPrefWidth(400);
        slider.setShowTickLabels(false);
        slider.setShowTickMarks(true);
        slider.setBlockIncrement(0.1);

        Label percentageText = new Label((int) (currentValue * 100) + "%");
        percentageText.setFont(Font.font("Courier New", FontWeight.BOLD, 18));
        percentageText.setTextFill(Color.web("#ffaa00"));
        percentageText.setMinWidth(50);

        slider.valueProperty().addListener((obs, oldVal, newVal) -> {
            percentageText.setText((int) (newVal.doubleValue() * 100) + "%");
            onChange.accept(newVal.doubleValue());
            if (label.contains("Musique")) {
                MusicManager.updateVolume();
            }
        });

        box.getChildren().addAll(labelText, slider, percentageText);
        return box;
    }

    private HBox createKeyBindingControl(String label, KeyCode currentKey, Consumer<KeyCode> onChange) {
        HBox box = new HBox(20);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-border-color: #444466; -fx-border-width: 1; -fx-padding: 15px; -fx-border-radius: 5;");

        Label labelText = createLabel(label + " :");

        Button keyButton = new Button(currentKey.toString());
        keyButton.setPrefWidth(250);
        keyButton.setPrefHeight(40);
        keyButton.setFont(Font.font("Courier New", FontWeight.BOLD, 16));
        keyButton.setStyle(
                "-fx-background-color: #ffaa00; " +
                        "-fx-text-fill: #000000; " +
                        "-fx-font-weight: bold; " +
                        "-fx-border-radius: 5; " +
                        "-fx-padding: 10px;"
        );

        keyButton.setOnAction(e -> {
            keyButton.setText("Appuyez sur une touche");
            keyButton.setStyle(
                    "-fx-background-color: #ff6b6b; " +
                            "-fx-text-fill: #ffffff; " +
                            "-fx-font-weight: bold; " +
                            "-fx-border-radius: 5; " +
                            "-fx-padding: 10px;"
            );

            Scene scene = keyButton.getScene();
            if (scene == null) return;

            EventHandler<? super KeyEvent> oldHandler = scene.getOnKeyPressed();

            scene.setOnKeyPressed(event -> {
                KeyCode newKey = event.getCode();
                keyButton.setText(newKey.toString());
                keyButton.setStyle(
                        "-fx-background-color: #4CAF50; " +
                                "-fx-text-fill: #ffffff; " +
                                "-fx-font-weight: bold; " +
                                "-fx-border-radius: 5; " +
                                "-fx-padding: 10px;"
                );
                onChange.accept(newKey);

                scene.setOnKeyPressed(oldHandler);
            });
        });

        box.getChildren().addAll(labelText, keyButton);
        return box;
    }


    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Courier New", FontWeight.BOLD, 18));
        label.setTextFill(Color.web("#ffffff"));
        label.setMinWidth(150);
        return label;
    }

    private Button createButton(String texte, String couleurBg, String couleurTexte) {
        Button btn = new Button(texte);
        btn.setFont(Font.font("Courier New", FontWeight.BOLD, 20));
        btn.setPrefWidth(200);
        btn.setPrefHeight(50);
        btn.setTextFill(Color.web(couleurTexte));
        btn.setStyle(
                "-fx-background-color: " + couleurBg + ";" +
                        "-fx-border-color: #FFFFFF;" +
                        "-fx-border-width: 2px;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 10, 0, 0, 5);"
        );

        btn.setOnMouseEntered(e -> btn.setScaleX(1.03));
        btn.setOnMouseExited(e -> btn.setScaleX(1.0));

        return btn;
    }
}
