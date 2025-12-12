package com.example.crazy_roads.gui;

import com.example.crazy_roads.models.Map;
import com.example.crazy_roads.models.Obstacle;
import com.example.crazy_roads.models.Car;
import javafx.animation.AnimationTimer;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Jeu Crazy Roads - Mode de jeu jouable
 */
public class PageJeu {
    
    private Stage primaryStage;
    private String modeDeJeu;
    private Canvas canvas;
    private GraphicsContext gc;
    private AnimationTimer gameLoop;
    
    // Éléments du jeu
    private Car Car;
    private Map Map;
    private List<Obstacle> obstacles;
    private Random random;
    
    // État du jeu
    private boolean isPaused = false;
    private boolean isGameOver = false;
    private int score = 0;
    private long lastTime = 0;
    private long obstacleSpawnTimer = 0;
    private long gameStartTime = 0;
    private long gameDuration = 0; // Pour mode contre la montre
    
    // Gestion des touches
    private Set<KeyCode> pressedKeys = new HashSet<>();
    
    // Constantes
    private static final double CANVAS_WIDTH = 800;
    private static final double CANVAS_HEIGHT = 600;
    private static final int LANE_WIDTH = 200;
    private static final int LANE_OFFSET = 100;
    private static final long OBSTACLE_SPAWN_INTERVAL_MS = 2000; // Toutes les 2 secondes

    public PageJeu(Stage primaryStage, String modeDeJeu) {
        this.primaryStage = primaryStage;
        this.modeDeJeu = modeDeJeu;
        this.random = new Random();
        
        // Durée du jeu pour mode contre la montre (60 secondes)
        if (modeDeJeu.equals("CONTRE LA MONTRE")) {
            this.gameDuration = 60000; // 60 secondes
        }
        
        initializeGame();
        afficherPageJeu();
    }

    private void initializeGame() {
        Car = new Car();
        Map = new Map(1);
        obstacles = new ArrayList<>();
        score = 0;
        isPaused = false;
        isGameOver = false;
        gameStartTime = System.currentTimeMillis();
    }

    private void afficherPageJeu() {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #1a1a2e;");

        canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        gc = canvas.getGraphicsContext2D();
        
        root.getChildren().add(canvas);

        Scene gameScene = new Scene(root, CANVAS_WIDTH, CANVAS_HEIGHT);
        
        // Gestion des touches
        gameScene.setOnKeyPressed(event -> {
            pressedKeys.add(event.getCode());
            
            // Actions immédiates
            if (event.getCode() == KeyCode.ESCAPE || event.getCode() == KeyCode.P) {
                togglePause();
            }
            if (event.getCode() == KeyCode.R && isGameOver) {
                restart();
            }
            if (event.getCode() == KeyCode.M && isGameOver) {
                new PageAccueil(primaryStage);
            }
        });
        
        gameScene.setOnKeyReleased(event -> {
            pressedKeys.remove(event.getCode());
        });

        primaryStage.setScene(gameScene);
        
        // Démarrer la boucle de jeu
        demarrerGameLoop();
    }

    private void demarrerGameLoop() {
        lastTime = System.nanoTime();
        
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (isPaused || isGameOver) {
                    return;
                }
                
                long deltaMs = (now - lastTime) / 1_000_000; // Convertir en millisecondes
                lastTime = now;
                
                update(deltaMs);
                render();
            }
        };
        gameLoop.start();
    }

    private void update(long deltaMs) {
        // Vérifier la fin du mode contre la montre
        if (modeDeJeu.equals("CONTRE LA MONTRE")) {
            long elapsed = System.currentTimeMillis() - gameStartTime;
            if (elapsed >= gameDuration) {
                gameOver(true); // Victoire
                return;
            }
        }
        
        // Déplacer la Car
        if (pressedKeys.contains(KeyCode.LEFT) || pressedKeys.contains(KeyCode.Q) || pressedKeys.contains(KeyCode.A)) {
            Car.moveLeft();
            pressedKeys.remove(KeyCode.LEFT);
            pressedKeys.remove(KeyCode.Q);
            pressedKeys.remove(KeyCode.A);
        }
        if (pressedKeys.contains(KeyCode.RIGHT) || pressedKeys.contains(KeyCode.D)) {
            Car.moveRight();
            pressedKeys.remove(KeyCode.RIGHT);
            pressedKeys.remove(KeyCode.D);
        }
        
        // Mettre à jour la Map
        Map.update();
        
        // Mettre à jour le boost
        Car.updateBoost();
        Car.invulnerable();
        
        // Générer des obstacles
        obstacleSpawnTimer += deltaMs;
        if (obstacleSpawnTimer >= OBSTACLE_SPAWN_INTERVAL_MS) {
            genererObstacle();
            obstacleSpawnTimer = 0;
        }
        
        // Mettre à jour les obstacles
        for (int i = obstacles.size() - 1; i >= 0; i--) {
            Obstacle obs = obstacles.get(i);
            
            // Déplacer l'obstacle vers le bas
            obs.setPosition(obs.getPosition() + Map.getScrollSpeed());
            
            // Vérifier collision
            if (isObstacleNearCar(obs)) {
                if (obs.getLane() == Car.getLane() && !Car.isCollide()) {
                    Car.collide();
                    obstacles.remove(i);
                    
                    // Game over si plus de vie
                    if (!Car.isAlive()) {
                        gameOver(false);
                    }
                }
            }
            // Supprimer si hors écran
            else if (obs.getPosition() > CANVAS_HEIGHT + 100) {
                obstacles.remove(i);
                score += 10; // Points pour avoir évité l'obstacle
            }
        }
    }

    private void genererObstacle() {
        int lane = random.nextInt(3); // 0, 1 ou 2
        double position = -100; // Hors écran en haut
        double size = 50 + random.nextDouble() * 50; // Taille variable
        
        Obstacle obs = new Obstacle(lane, Map.getScrollSpeed(), position, size, 1);
        obstacles.add(obs);
    }

    private boolean isObstacleNearCar(Obstacle obs) {
        double carY = CANVAS_HEIGHT - 150; // Position fixe de la Car
        return obs.getPosition() >= carY - 100 && obs.getPosition() <= carY + 100;
    }

    private void render() {
        Map.render(gc, CANVAS_WIDTH, 0);
        
        dessinerVoies();
        
        // Dessiner les obstacles
        for (Obstacle obs : obstacles) {
            dessinerObstacle(obs);
        }
        
        // Dessiner la Car
        dessinerVoiture();
        
        // Dessiner l'interface (score, vie, temps)
        dessinerHUD();
        
        // Afficher pause ou game over
        if (isPaused) {
            afficherPause();
        }
        if (isGameOver) {
            afficherGameOver();
        }
    }

    private void dessinerVoies() {
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(3);
        gc.setLineDashes(20, 20);
        
        // Ligne entre voie 0 et 1
        double x1 = LANE_OFFSET + LANE_WIDTH;
        gc.strokeLine(x1, 0, x1, CANVAS_HEIGHT);
        
        // Ligne entre voie 1 et 2
        double x2 = LANE_OFFSET + LANE_WIDTH * 2;
        gc.strokeLine(x2, 0, x2, CANVAS_HEIGHT);
        
        gc.setLineDashes(null);
    }

    private void dessinerVoiture() {
        double carX = LANE_OFFSET + (Car.getLane() * LANE_WIDTH) + LANE_WIDTH / 2;
        double carY = CANVAS_HEIGHT - 150;
        
        // Effet clignotant si collision
        if (Car.isCollide() && System.currentTimeMillis() % 400 < 200) {
            gc.setFill(Color.RED);
        } else if (Car.isBoostActive()) {
            gc.setFill(Color.GOLD);
        } else {
            gc.setFill(Color.LIMEGREEN);
        }
        
        // Carrosserie
        gc.fillRoundRect(carX - 40, carY - 60, 80, 120, 10, 10);
        
        // Toit
        gc.setFill(Color.DARKBLUE);
        gc.fillRoundRect(carX - 35, carY - 40, 70, 60, 10, 10);
        
        // Roues
        gc.setFill(Color.BLACK);
        gc.fillOval(carX - 45, carY - 50, 20, 30);
        gc.fillOval(carX + 25, carY - 50, 20, 30);
        gc.fillOval(carX - 45, carY + 20, 20, 30);
        gc.fillOval(carX + 25, carY + 20, 20, 30);
    }

    private void dessinerObstacle(Obstacle obs) {
        double obsX = LANE_OFFSET + (obs.getLane() * LANE_WIDTH) + LANE_WIDTH / 2;
        double obsY = obs.getPosition();
        double size = obs.getSize();
        
        // Obstacle rouge
        gc.setFill(Color.ORANGERED);
        gc.fillRect(obsX - size / 2, obsY - size / 2, size, size);
        
        // Bordure
        gc.setStroke(Color.DARKRED);
        gc.setLineWidth(3);
        gc.strokeRect(obsX - size / 2, obsY - size / 2, size, size);
    }

    private void dessinerHUD() {
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Courier New", FontWeight.BOLD, 24));
        
        // Score
        gc.fillText("SCORE: " + score, 20, 40);
        
        // Vies
        gc.fillText("VIES: " + "❤️".repeat(Math.max(0, Car.getLife())), 20, 70);
        
        // Vitesse
        if (Car.isBoostActive()) {
            gc.setFill(Color.GOLD);
            gc.fillText("⚡ BOOST! ⚡", CANVAS_WIDTH - 180, 40);
            gc.setFill(Color.WHITE);
        }
        
        // Temps pour mode contre la montre
        if (modeDeJeu.equals("CONTRE LA MONTRE")) {
            long elapsed = System.currentTimeMillis() - gameStartTime;
            long remaining = (gameDuration - elapsed) / 1000;
            gc.fillText("TEMPS: " + remaining + "s", CANVAS_WIDTH - 200, 70);
        }
    }

    private void afficherPause() {
        gc.setFill(Color.rgb(0, 0, 0, 0.7));
        gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Courier New", FontWeight.BOLD, 60));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText("PAUSE", CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2 - 50);
        
        gc.setFont(Font.font("Courier New", FontWeight.NORMAL, 24));
        gc.fillText("Appuyez sur P ou ECHAP pour reprendre", CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2 + 20);
        
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setTextBaseline(VPos.BASELINE);
    }

    private void afficherGameOver() {
        gc.setFill(Color.rgb(0, 0, 0, 0.8));
        gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        
        gc.setFill(Color.RED);
        gc.setFont(Font.font("Courier New", FontWeight.BOLD, 70));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText("GAME OVER", CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2 - 100);
        
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Courier New", FontWeight.BOLD, 36));
        gc.fillText("Score Final: " + score, CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2 - 20);
        
        gc.setFont(Font.font("Courier New", FontWeight.NORMAL, 20));
        gc.fillText("R - Recommencer", CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2 + 40);
        gc.fillText("M - Menu Principal", CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2 + 70);
        
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setTextBaseline(VPos.BASELINE);
    }

    private void togglePause() {
        if (isGameOver) return;
        isPaused = !isPaused;
        
        if (!isPaused) {
            lastTime = System.nanoTime(); // Réinitialiser le temps
        }
    }

    private void gameOver(boolean victory) {
        isGameOver = true;
        gameLoop.stop();
        
        if (victory) {
            // Affichage victoire pour mode contre la montre
            gc.setFill(Color.rgb(0, 0, 0, 0.8));
            gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
            
            gc.setFill(Color.GOLD);
            gc.setFont(Font.font("Courier New", FontWeight.BOLD, 70));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(VPos.CENTER);
            gc.fillText("🏆 VICTOIRE! 🏆", CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2 - 100);
            
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Courier New", FontWeight.BOLD, 36));
            gc.fillText("Score Final: " + score, CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2 - 20);
            
            gc.setFont(Font.font("Courier New", FontWeight.NORMAL, 20));
            gc.fillText("R - Recommencer", CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2 + 40);
            gc.fillText("M - Menu Principal", CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2 + 70);
            
            gc.setTextAlign(TextAlignment.LEFT);
            gc.setTextBaseline(VPos.BASELINE);
        }
    }

    private void restart() {
        gameLoop.stop();
        new PageJeu(primaryStage, modeDeJeu);
    }
}
