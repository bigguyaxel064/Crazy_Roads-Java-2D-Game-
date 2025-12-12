package com.example.crazy_roads;

import com.example.crazy_roads.gui.PageAccueil;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        PageAccueil pageAccueil = new PageAccueil(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}