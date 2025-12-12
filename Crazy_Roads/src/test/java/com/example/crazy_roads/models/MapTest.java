package com.example.crazy_roads.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MapTest {
    private Map Map;
    private Map carteMarioKart;

    @BeforeEach
    void setUp() {
        Map = new Map(0);
        carteMarioKart = new Map(true);
    }

    @Test
    void testCarteInitialization() {
        assertNotNull(Map.getFrames(), "Frames list should not be null");
        assertFalse(Map.getFrames().isEmpty(), "Frames list should not be empty");
        assertEquals(16.0, Map.getScrollSpeed(), "Initial speed should be 16.0");
    }

    @Test
    void testMarioKartModeInitialization() {
        assertNotNull(carteMarioKart.getFrames(), "Mario Kart frames should not be null");
        assertFalse(carteMarioKart.getFrames().isEmpty(), "Mario Kart frames should not be empty");
        assertEquals(100, carteMarioKart.getFrames().size(), "Mario Kart should have 100 frames");
        assertEquals(0, carteMarioKart.getFramesScrolled(), "Initial frames scrolled should be 0");
    }

    @Test
    void testSetVitesseDefilement() {
        Map.setScrollSpeed(20.0);
        assertEquals(20.0, Map.getScrollSpeed(), "Speed should be updated to 20.0");
    }

    @Test
    void testUpdateIncrementsFramePosition() {
        int initialSize = Map.getFrames().size();
        double initialY = Map.getFrames().get(0).getPositionY();
        
        Map.update();
        
        double newY = Map.getFrames().get(0).getPositionY();
        assertTrue(newY > initialY, "Frame position should increase after update");
        assertTrue(Map.getFrames().size() >= initialSize - 1, "Frames list should maintain size");
    }

    @Test
    void testMarioKartFramesScrolled() {
        int initialScrolled = carteMarioKart.getFramesScrolled();
        
        // Force frames to move off screen
        for (int i = 0; i < 100; i++) {
            carteMarioKart.update();
        }
        
        assertTrue(carteMarioKart.getFramesScrolled() > initialScrolled, 
                   "Frames scrolled should increase after updates");
    }

    @Test
    void testFinishLineNotReachedInitially() {
        assertFalse(carteMarioKart.isFinishLineReached(), 
                    "Finish line should not be reached initially");
    }

    @Test
    void testIsUseImages() {
        // This depends on if images are loaded successfully
        assertNotNull(Map.isUseImages(), "useImages flag should have a value");
    }

    @Test
    void testFramesListNotNull() {
        assertNotNull(Map.getFrames(), "Frames list should never be null");
        assertNotNull(carteMarioKart.getFrames(), "Mario Kart frames list should never be null");
    }

    @Test
    void testMultipleUpdates() {
        double initialY = Map.getFrames().get(0).getPositionY();
        
        Map.update();
        Map.update();
        Map.update();
        
        double finalY = Map.getFrames().get(0).getPositionY();
        assertTrue(finalY > initialY, "Multiple updates should move frames forward");
    }

    @Test
    void testVitesseDefilementPositive() {
        assertTrue(Map.getScrollSpeed() > 0, "Speed should be positive");
        
        Map.setScrollSpeed(30.0);
        assertTrue(Map.getScrollSpeed() > 0, "Speed should remain positive after setting");
    }

    @Test
    void testMarioKartFrameRemoval() {
        int initialSize = carteMarioKart.getFrames().size();
        
        // Move frames significantly
        for (int i = 0; i < 200; i++) {
            carteMarioKart.update();
        }
        
        assertTrue(carteMarioKart.getFrames().size() < initialSize, 
                   "Frames should be removed as they go off screen");
    }
}
