package com.example.crazy_roads.gui;

import com.example.crazy_roads.managers.MusicManager;
import com.example.crazy_roads.managers.RessourceLoader;
import com.example.crazy_roads.managers.ScoreManager;
import com.example.crazy_roads.managers.Settings;
import com.example.crazy_roads.models.Map;
import com.example.crazy_roads.models.Obstacle;
import com.example.crazy_roads.modes.ModeSubwaySurfers;
import com.example.crazy_roads.generation.GenerationProcedurale;
import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class PageJeuSubwaySurfers implements IGamePage {
    private Stage primaryStage;
    private Scene gameScene;
    private BorderPane root;
    private Canvas canvas;
    private GraphicsContext gc;

    private ModeSubwaySurfers gameMode;
    private Map Map;
    private HUD hud;
    private GenerationProcedurale obstacleGenerator;
    private List<Obstacle> obstacles;
    private double obstacleScrollOffset;
    
    private long gameStartTime;
    private int currentScore;
    private double baseSpeed = 5.0;
    private double currentSpeed = 5.0;
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

    public PageJeuSubwaySurfers(Stage primaryStage) {
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
        gameMode = new ModeSubwaySurfers();
        
        Map = new Map(1);
        Map.setScrollSpeed(baseSpeed);
        
        obstacleGenerator = new GenerationProcedurale();
        obstacles = new ArrayList<>();
        obstacleScrollOffset = -500;
        
        hud = new HUD();
        hud.setInfiniteMode();
        hud.setLives(gameMode.getCar().getLife());
        
        gameStartTime = System.currentTimeMillis();
        lastUpdateTime = gameStartTime;
        currentScore = 0;
        currentSpeed = baseSpeed;
        
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
        
        gameMode.start();
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
        if (gameMode.isGameOver()) {
            handleGameOver();
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
        
        double elapsedTime = (currentTime - gameStartTime) / 1000.0;
        
        currentSpeed = baseSpeed * Math.exp(0.01 * elapsedTime);
        
        if (currentSpeed > 12.0) {
            currentSpeed = 12.0;
        }
        
        double targetSpeed = currentSpeed;
        double currentCarteSpeed = Map.getScrollSpeed();
        double smoothSpeed = currentCarteSpeed + (targetSpeed - currentCarteSpeed) * 0.05;
        Map.setScrollSpeed(smoothSpeed);
        
        currentScore = (int)(10 * (Math.exp(0.05 * elapsedTime) - 1));
        
        gameMode.update();
        
        Map.update();
        
        obstacleScrollOffset += smoothSpeed;
        
        List<Obstacle> newObstacles = obstacleGenerator.generateObstacles(obstacleScrollOffset);
        if (!newObstacles.isEmpty()) {
            Random random = new Random();
            for (Obstacle obs : newObstacles) {
                obs.setColorType(random.nextInt(2));
            }
            obstacles.addAll(newObstacles);
        }
        
        updateObstacles(smoothSpeed);
        
        checkCollisions();
        
        hud.setScore(currentScore);
        hud.setLives(gameMode.getCar().getLife());
        hud.setBoostActive(gameMode.isInProgress());
    }
    
    private void updateObstacles(double speed) {
        for (Obstacle obstacle : obstacles) {
            obstacle.setPosition(obstacle.getPosition() + speed);
        }
        
        obstacles.removeIf(obstacle -> obstacle.getPosition() > canvasHeight + 100);
    }
    
    private void checkCollisions() {
        int currentLane = gameMode.getLane();
        double carTop = CAR_Y_POSITION;
        double carBottom = CAR_Y_POSITION + CAR_HEIGHT;
        
        Iterator<Obstacle> iterator = obstacles.iterator();
        while (iterator.hasNext()) {
            Obstacle obstacle = iterator.next();
            
            if (obstacle.getLane() == currentLane) {
                double obstacleTop = obstacle.getPosition();
                double obstacleBottom = obstacle.getPosition() + obstacle.getSize();
                
                if (!(carBottom < obstacleTop || carTop > obstacleBottom)) {
                    MusicManager.playSound();
                    
                    gameMode.getCar().collide();
                    
                    int newLives = gameMode.getCar().getLife();
                    hud.setLives(newLives);
                    iterator.remove();
                    
                    if (newLives <= 0) {
                        handleGameOver();
                        return;
                    }
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
        
        drawLanes();
        
        drawObstacles();
        
        gc.restore();
        
        drawCar();
        
        drawDebugInfo();
    }
    
    private void drawLanes() {
    }
    
    private void drawCar() {
        int currentLane = gameMode.getLane();
        double carX = lanePositions[currentLane];
        double carY = CAR_Y_POSITION;
        
        if (RessourceLoader.isImageValid(carImage)) {
            double renderWidth = CAR_WIDTH * 1.6;
            double renderHeight = CAR_HEIGHT * 1.6;
            gc.drawImage(carImage, carX - (renderWidth - CAR_WIDTH)/2, carY - (renderHeight - CAR_HEIGHT)/2, renderWidth, renderHeight);
        } else {
            gc.setFill(Color.RED);
            gc.fillRect(carX, carY, CAR_WIDTH * 2, CAR_HEIGHT * 2);
            
            gc.setFill(Color.DARKRED);
            gc.fillRect(carX + 20, carY + 40, CAR_WIDTH * 2 - 40, 60);
            
            gc.setFill(Color.LIGHTBLUE);
            gc.fillRect(carX + 30, carY + 60, 40, 40);
            gc.fillRect(carX + CAR_WIDTH * 2 - 70, carY + 60, 40, 40);
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
                
                gc.setStroke(Color.DARKRED);
                gc.setLineWidth(3);
                gc.strokeRect(obstacleX, obstacleY, CAR_WIDTH, obstacleSize);
                
                gc.setFill(Color.YELLOW);
                gc.setFont(new javafx.scene.text.Font(20));
                gc.fillText("⚠", obstacleX + CAR_WIDTH/2 - 10, obstacleY + obstacleSize/2 + 7);
            }
        }
    }
    
    private void drawDebugInfo() {
        gc.setFill(Color.WHITE);
        gc.setFont(new javafx.scene.text.Font(14));
        gc.fillText("Lane: " + gameMode.getLane(), 20, canvasHeight - 100);
        gc.fillText("Speed: " + String.format("%.1f", currentSpeed) + "x", 20, canvasHeight - 80);
        gc.fillText("Score: " + currentScore, 20, canvasHeight - 60);
        gc.fillText("Obstacles: " + obstacles.size(), 20, canvasHeight - 40);
        gc.fillText("Scroll Offset: " + String.format("%.0f", obstacleScrollOffset), 20, canvasHeight - 20);
    }
    
    private void togglePause() {
        isPaused = !isPaused;
        
        if (isPaused) {
            new MenuPause(primaryStage, gameScene, this);
        }
    }
    
    public void resume() {
        isPaused = false;
    }
    
    private void handleGameOver() {
        gameLoop.stop();
        hud.stop();
        ScoreManager scoreManager = ScoreManager.getInstance();
        boolean isNewRecord = scoreManager.updateBestScore("COURSE_INFINIE", currentScore);
        if (isNewRecord) {
        }
        
        new MenuGameOver(primaryStage, gameScene, currentScore, isNewRecord);
    }
    
    public void stop() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
        if (hud != null) {
            hud.stop();
        }
    }
}
