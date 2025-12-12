package com.example.crazy_roads.models;

import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FrameTest {
    private Frame frame;
    private Image mockImage;
    private final double testPositionY = 100.0;
    private final double testHauteur = 150.0;

    @BeforeEach
    void setUp() {
        // Note: Image nécessite JavaFX toolkit, donc on teste avec null pour les cas simples
        frame = new Frame(null, testPositionY, testHauteur);
    }

    @Test
    void testConstructor() {
        assertEquals(testPositionY, frame.getPositionY(), "Position Y should match constructor value");
        assertEquals(testHauteur, frame.getHeight(), "height should match constructor value");
        assertNull(frame.getImage(), "Image should be null when passed null in constructor");
    }

    @Test
    void testSetImage() {
        Image newImage = null;
        frame.setImage(newImage);
        assertEquals(newImage, frame.getImage(), "Image should be updated");
    }

    @Test
    void testSetPositionY() {
        double newPosition = 250.0;
        frame.setPositionY(newPosition);
        assertEquals(newPosition, frame.getPositionY(), "Position Y should be updated");
    }

    @Test
    void testMove() {
        double speed = 5.0;
        double originalPosition = frame.getPositionY();
        
        frame.move(speed);
        
        assertEquals(originalPosition + speed, frame.getPositionY(), 
                    "Position should increase by speed value");
    }

    @Test
    void testMoveNegativeSpeed() {
        double negativeSpeed = -3.0;
        double originalPosition = frame.getPositionY();
        
        frame.move(negativeSpeed);
        
        assertEquals(originalPosition + negativeSpeed, frame.getPositionY(), 
                    "Position should decrease with negative speed");
    }

    @Test
    void testMultipleMovements() {
        double speed = 2.5;
        double originalPosition = frame.getPositionY();
        
        frame.move(speed);
        frame.move(speed);
        frame.move(speed);
        
        assertEquals(originalPosition + (speed * 3), frame.getPositionY(), 
                    "Position should accumulate after multiple moves");
    }

    @Test
    void testIsOffScreen() {
        // Frame en dessous de l'écran
        Frame offScreenFrame = new Frame(null, 801.0, 150.0);
        assertTrue(offScreenFrame.isOffScreen(), "Frame should be off screen when Y > 800");
        
        // Frame visible
        Frame onScreenFrame = new Frame(null, 100.0, 150.0);
        assertFalse(onScreenFrame.isOffScreen(), "Frame should be on screen when Y <= 800");
    }

    @Test
    void testOffScreenBoundary() {
        // Frame exactement à la limite
        Frame boundaryFrame = new Frame(null, 800.0, 150.0);
        assertFalse(boundaryFrame.isOffScreen(), 
                   "Frame at boundary (Y = 800) should not be considered off screen");
        
        // Frame juste au-dessus de la limite
        Frame justOffFrame = new Frame(null, 800.5, 150.0);
        assertTrue(justOffFrame.isOffScreen(), 
                  "Frame just above boundary should be off screen");
    }

    @Test
    void testHasValidImage() {
        assertFalse(frame.hasValidImage(), "Should return false when image is null");
        
        // Avec une image non-null (commenté car nécessite JavaFX toolkit)
        // Image validImage = new Image("file:test.png");
        // frame.setImage(validImage);
        // assertTrue(frame.hasValidImage(), "Should return true with valid image");
    }

    @Test
    void testGetHauteur() {
        assertEquals(testHauteur, frame.getHeight(), "height should return constructor value");
    }

    @Test
    void testMovementChain() {
        // Test une séquence de mouvements variés
        frame.move(10.0);
        frame.move(-5.0);
        frame.move(3.0);
        
        assertEquals(testPositionY + 8.0, frame.getPositionY(), 
                    "Position should reflect cumulative movements");
    }

    @Test
    void testPositionAfterReset() {
        frame.move(50.0);
        double movedPosition = frame.getPositionY();
        
        frame.setPositionY(0.0);
        assertEquals(0.0, frame.getPositionY(), "Position should be reset to 0");
        
        assertNotEquals(movedPosition, frame.getPositionY(), 
                       "Position after reset should differ from moved position");
    }
}
