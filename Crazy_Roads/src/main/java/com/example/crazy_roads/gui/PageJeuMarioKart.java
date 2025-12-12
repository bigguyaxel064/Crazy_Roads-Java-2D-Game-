package com.example.crazy_roads.gui;

import com.example.crazy_roads.managers.RessourceLoader;
import com.example.crazy_roads.managers.ScoreManager;
import com.example.crazy_roads.managers.Settings;
import com.example.crazy_roads.models.Map;
import com.example.crazy_roads.models.Obstacle;
import com.example.crazy_roads.modes.ModeMarioKart;
import com.example.crazy_roads.generation.GenerationProcedurale;
import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class PageJeuMarioKart implements IGamePage {
    private Stage primaryStage;
    private Scene gameScene;
    private BorderPane root;
    private Canvas canvas;
    private GraphicsContext gc;

    private ModeMarioKart gameMode;
    private Map Map;
    private HUD hud;
    private GenerationProcedurale obstacleGenerator;
    private List<Obstacle> obstacles;
    private double obstacleScrollOffset;
    
    private long lastUpdateTime;
    
    private AnimationTimer gameLoop;
    private boolean isPaused = false;
    
    private double canvasWidth;
    private double canvasHeight;
    
    private static final double ROAD_WIDTH = 452;
    private double roadX;
    
    private Image carImage;
    private Image blueBarrelImage;
    private Image redBarrelImage;
    private static final double CAR_WIDTH = 40;
    private static final double CAR_HEIGHT = 65;
    
    private static final int LANE_COUNT = 3;
    private double laneWidth;
    private double[] lanePositions;
    
    private static final double CAR_Y_POSITION = 500;
    private double carYOffset = 0;
    private boolean finishLineVisible = false;
    private boolean scoreSaved = false;

    public PageJeuMarioKart(Stage primaryStage) {
        this.primaryStage = primaryStage;
        
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        
        canvasWidth = bounds.getWidth();
        canvasHeight = bounds.getHeight();
        
        initializeGame();
        setupUI();
        setupControls();
        startGameLoop();
    }
    
    private void initializeGame() {
        gameMode = new ModeMarioKart();
        gameMode.start();
        
        Map = new Map(true);
        Map.setScrollSpeed(5.0);
        
        obstacleGenerator = new GenerationProcedurale();
        obstacles = new ArrayList<>();
        obstacleScrollOffset = -500;
        
        hud = new HUD();
        hud.setTimeAttackMode();
        hud.setLives(gameMode.getLives());
        
        carYOffset = 0;
        finishLineVisible = false;
        
        lastUpdateTime = System.currentTimeMillis();
        
        roadX = (canvasWidth - ROAD_WIDTH) / 2;
        
        double roadMargin = 90;
        double actualRoadWidth = ROAD_WIDTH - (2 * roadMargin);
        
        laneWidth = actualRoadWidth / LANE_COUNT;
        lanePositions = new double[LANE_COUNT];
        for (int i = 0; i < LANE_COUNT; i++) {
            lanePositions[i] = roadX + roadMargin + (i * laneWidth) + (laneWidth / 2) - (CAR_WIDTH / 2);
        }
        
        loadCarImage();
        loadObstacleImages();
    }
    
    private void loadCarImage() {
        try {
            String carPath = "/images/Car/g3219.png";
            carImage = RessourceLoader.loadImage(carPath);
            
            if (!RessourceLoader.isImageValid(carImage)) {
                carImage = null;
            }
        } catch (Exception e) {
            carImage = null;
        }
    }
    
    private void loadObstacleImages() {
        try {
            String bluePath = "/images/Car/obstacles/barrel_blue_down 1.png";
            blueBarrelImage = RessourceLoader.loadImage(bluePath);
            
            String redPath = "/images/Car/obstacles/barrel_red_down 1.png";
            redBarrelImage = RessourceLoader.loadImage(redPath);
            
            if (!RessourceLoader.isImageValid(blueBarrelImage)) {
                blueBarrelImage = null;
            }
            if (!RessourceLoader.isImageValid(redBarrelImage)) {
                redBarrelImage = null;
            }
        } catch (Exception e) {
            blueBarrelImage = null;
            redBarrelImage = null;
        }
    }
    
    private void setupUI() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: #1a1a2e;");
        
        canvas = new Canvas(canvasWidth, canvasHeight);
        gc = canvas.getGraphicsContext2D();
        gc.setImageSmoothing(true);
        
        StackPane canvasContainer = new StackPane(canvas);
        canvasContainer.setAlignment(Pos.CENTER);
        
        root.setCenter(canvasContainer);
        root.setTop(hud.getHUDRoot());
        
        gameScene = new Scene(root, canvasWidth, canvasHeight);
        
        primaryStage.setScene(gameScene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private void setupControls() {
        Settings settings = Settings.getInstance();

        gameScene.setOnKeyPressed(event -> {
            if (isPaused && event.getCode() != settings.getPauseKey()) {
                return;
            }

            KeyCode code = event.getCode();

            if (code == settings.getLeftKey()) {
                gameMode.moveLeft();
            } else if (code == settings.getRightKey()) {
                gameMode.moveRight();
            } else if (code == settings.getBoostKey()) {
                gameMode.activateBoost();
            } else if (code == settings.getPauseKey()) {
                togglePause();
            }
        });
    }

    private void startGameLoop() {
        lastUpdateTime = System.currentTimeMillis();
        
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!isPaused) {
                    update();
                    render();
                }
            }
        };
        gameLoop.start();
    }
    
    private void update() {
        if (gameMode.isGameOver() || gameMode.isRaceFinished()) {
            return;
        }
        
        long currentTime = System.currentTimeMillis();
        double deltaTime = (currentTime - lastUpdateTime) / 1000.0;
        lastUpdateTime = currentTime;
        
        if (deltaTime > 0.033) {
            deltaTime = 0.033;
        }
        if (deltaTime < 0.001) {
            return;
        }
        
        gameMode.update();
        
        int currentFrame = Map.getFramesScrolled();
        boolean onFinishLine = currentFrame >= 98;
        
        double currentSpeed = gameMode.getSpeed();
        
        if (!onFinishLine) {
            Map.setScrollSpeed(currentSpeed);
            Map.update();
            obstacleScrollOffset += currentSpeed;
            finishLineVisible = false;
            carYOffset = 0;
        } else {
            Map.setScrollSpeed(0);
            finishLineVisible = true;
            carYOffset -= currentSpeed;
            
            if (carYOffset <= -600 && !gameMode.isRaceFinished()) {
                gameMode.incrementFrame();
            }
        }
        
        if (!onFinishLine) {
            List<Obstacle> newObstacles = obstacleGenerator.generateObstacles(obstacleScrollOffset);
            if (!newObstacles.isEmpty()) {
                Random random = new Random();
                for (Obstacle obs : newObstacles) {
                    obs.setColorType(random.nextInt(2));
                }
                obstacles.addAll(newObstacles);
            }
            
            updateObstacles(currentSpeed);
        }
        
        checkCollisions();
        
        if (currentFrame >= 99 && !gameMode.isRaceFinished()) {
            gameMode.incrementFrame();
        }
        
        hud.setTime(gameMode.getRaceTime());
        hud.setLives(gameMode.getLives());
        hud.setBoostActive(gameMode.isBoostActive());
    }
    
    private void updateObstacles(double speed) {
        for (Obstacle obstacle : obstacles) {
            obstacle.setPosition(obstacle.getPosition() + speed);
        }
        
        obstacles.removeIf(obstacle -> obstacle.getPosition() > canvasHeight + 100);
    }
    
    private void checkCollisions() {
        int currentLane = gameMode.getLane();
        double carTop = CAR_Y_POSITION + carYOffset;
        double carBottom = CAR_Y_POSITION + carYOffset + CAR_HEIGHT;
        
        Iterator<Obstacle> iterator = obstacles.iterator();
        while (iterator.hasNext()) {
            Obstacle obstacle = iterator.next();
            
            if (obstacle.getLane() == currentLane) {
                double obsTop = obstacle.getPosition();
                double obsBottom = obstacle.getPosition() + obstacle.getSize();
                
                if (!(carBottom < obsTop || carTop > obsBottom)) {
                    gameMode.getCar().collide();
                    iterator.remove();
                    break;
                }
            }
        }
    }
    
    private void render() {
        gc.setFill(Color.web("#2ecc71"));
        gc.fillRect(0, 0, canvasWidth, canvasHeight);
        
        gc.save();
        
        gc.beginPath();
        gc.rect(roadX, 0, ROAD_WIDTH, canvasHeight);
        gc.clip();
        
        Map.render(gc, ROAD_WIDTH, roadX);
        
        drawObstacles();
        
        gc.restore();
        
        drawCar();
        
        int currentFrame = Map.getFramesScrolled();
        if (currentFrame >= 98) {
            drawFinishLineMessage();
        }
        
        if (isPaused) {
            drawPauseOverlay();
        }
        
        if (gameMode.isGameOver() || gameMode.isRaceFinished()) {
            drawGameOverOverlay();
        }
    }
    
    private void drawCar() {
        int currentLane = gameMode.getLane();
        double carX = lanePositions[currentLane];
        double carY = CAR_Y_POSITION + carYOffset;
        
        if (RessourceLoader.isImageValid(carImage)) {
            double renderWidth = CAR_WIDTH * 1.6;
            double renderHeight = CAR_HEIGHT * 1.6;
            gc.drawImage(carImage, carX - (renderWidth - CAR_WIDTH)/2, carY - (renderHeight - CAR_HEIGHT)/2, renderWidth, renderHeight);
        } else {
            gc.setFill(gameMode.isBoostActive() ? Color.GOLD : Color.BROWN);
            gc.fillRoundRect(carX, carY, CAR_WIDTH, CAR_HEIGHT, 10, 10);
        }
    }
    
    private void drawObstacles() {
        for (Obstacle obstacle : obstacles) {
            int lane = obstacle.getLane();
            
            if (lane < 0 || lane >= lanePositions.length) {
                continue;
            }
            
            double obstacleX = lanePositions[lane];
            double obstacleY = obstacle.getPosition();
            double obstacleSize = obstacle.getSize();
            
            if (obstacleY + obstacleSize < 0 || obstacleY > canvasHeight) {
                continue;
            }
            
            Image obstacleImage = (obstacle.getColorType() == 0) ? blueBarrelImage : redBarrelImage;
            
            if (RessourceLoader.isImageValid(obstacleImage)) {
                gc.drawImage(obstacleImage, obstacleX, obstacleY, CAR_WIDTH, CAR_WIDTH);
            } else {
                gc.setFill(Color.ORANGERED);
                gc.fillRect(obstacleX, obstacleY, CAR_WIDTH, obstacleSize);
            }
        }
    }
    
    private void drawFinishLineMessage() {
        gc.setFill(Color.LIME);
        gc.setFont(Font.font("Courier New", FontWeight.BOLD, 48));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("LIGNE D'ARRIVÉE!", canvasWidth / 2, 200);
        gc.setTextAlign(TextAlignment.LEFT);
    }
    
    private void drawPauseOverlay() {
        gc.setFill(Color.rgb(0, 0, 0, 0.7));
        gc.fillRect(0, 0, canvasWidth, canvasHeight);
        
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Courier New", FontWeight.BOLD, 60));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText("PAUSE", canvasWidth / 2, canvasHeight / 2 - 50);
        
        gc.setFont(Font.font("Courier New", FontWeight.NORMAL, 24));
        gc.fillText("Appuyez sur P ou ECHAP pour reprendre", canvasWidth / 2, canvasHeight / 2 + 20);
        
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setTextBaseline(VPos.BASELINE);
    }
    
    private void drawGameOverOverlay() {
        gameLoop.stop();
        
        if (!scoreSaved) {
            ScoreManager scoreManager = ScoreManager.getInstance();
            int raceTime = (int) gameMode.getRaceTime();
            int raceTimeMillis = (int) (System.currentTimeMillis() - gameMode.getRaceStartTime());
            boolean isNewRecord = scoreManager.updateBestScore(ScoreManager.MODE_TIME_ATTACK, raceTime);
            scoreSaved = true;
            
            if (gameMode.isRaceFinished()) {
                new MenuWin(primaryStage, gameScene, raceTimeMillis, isNewRecord, true);
            } else {
                int currentTime = (int) (System.currentTimeMillis() - gameMode.getRaceStartTime());
                new MenuGameOver(primaryStage, gameScene, raceTime, isNewRecord, true, currentTime);
            }
        }
    }
    
    private void togglePause() {
        if (gameMode.isGameOver() || gameMode.isRaceFinished()) return;
        isPaused = !isPaused;
        
        if (isPaused) {
            new MenuPause(primaryStage, gameScene, this);
        } else {
            lastUpdateTime = System.currentTimeMillis();
        }
    }

    public void pause() {
        if (!gameMode.isGameOver() && !gameMode.isRaceFinished()) {
            isPaused = true;
        }
    }

    public void resume() {
        isPaused = false;
        lastUpdateTime = System.currentTimeMillis();
    }

    public void stop() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }

    public Scene getScene() {
        return primaryStage.getScene();
    }
}
