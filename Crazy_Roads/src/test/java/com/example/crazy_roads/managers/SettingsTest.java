package com.example.crazy_roads.managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SettingsTest {
    private Settings settings;

    @BeforeEach
    void setUp() {
        settings = Settings.getInstance();
    }

    @Test
    void testSingletonInstance() {
        Settings instance1 = Settings.getInstance();
        Settings instance2 = Settings.getInstance();
        
        assertSame(instance1, instance2, "Settings should be a singleton");
    }

    @Test
    void testDefaultValues() {
        assertNotNull(settings.getMusicVolume(), "Music volume should have a default value");
        assertNotNull(settings.getSoundVolume(), "Sound volume should have a default value");
        assertTrue(settings.getScreenWidth() > 0, "Screen width should be positive");
        assertTrue(settings.getScreenHeight() > 0, "Screen height should be positive");
        assertNotNull(settings.getLeftKey(), "Left key should have a default value");
        assertNotNull(settings.getRightKey(), "Right key should have a default value");
        assertNotNull(settings.getPauseKey(), "Pause key should have a default value");
    }

    @Test
    void testMusicVolumeRange() {
        settings.setMusicVolume(0.75);
        assertEquals(0.75, settings.getMusicVolume(), 0.01, "Music volume should be 0.75");
        
        settings.setMusicVolume(1.5);
        assertEquals(1.0, settings.getMusicVolume(), 0.01, "Music volume should be clamped to 1.0");
        
        settings.setMusicVolume(-0.5);
        assertEquals(0.0, settings.getMusicVolume(), 0.01, "Music volume should be clamped to 0.0");
    }

    @Test
    void testSoundVolumeRange() {
        settings.setSoundVolume(0.6);
        assertEquals(0.6, settings.getSoundVolume(), 0.01, "Sound volume should be 0.6");
        
        settings.setSoundVolume(2.0);
        assertEquals(1.0, settings.getSoundVolume(), 0.01, "Sound volume should be clamped to 1.0");
        
        settings.setSoundVolume(-1.0);
        assertEquals(0.0, settings.getSoundVolume(), 0.01, "Sound volume should be clamped to 0.0");
    }

    @Test
    void testScreenDimensions() {
        settings.setScreenWidth(1920);
        settings.setScreenHeight(1080);
        
        assertEquals(1920, settings.getScreenWidth(), "Screen width should be 1920");
        assertEquals(1080, settings.getScreenHeight(), "Screen height should be 1080");
    }

    @Test
    void testMinimumScreenWidth() {
        settings.setScreenWidth(500);
        assertTrue(settings.getScreenWidth() >= 800, "Screen width should be at least 800");
        
        settings.setScreenWidth(1000);
        assertEquals(1000, settings.getScreenWidth(), "Screen width should be 1000");
    }

    @Test
    void testMinimumScreenHeight() {
        settings.setScreenHeight(400);
        assertTrue(settings.getScreenHeight() >= 600, "Screen height should be at least 600");
        
        settings.setScreenHeight(900);
        assertEquals(900, settings.getScreenHeight(), "Screen height should be 900");
    }

    @Test
    void testKeyBindings() {
        settings.setLeftKey(javafx.scene.input.KeyCode.A);
        assertEquals(javafx.scene.input.KeyCode.A, settings.getLeftKey(), "Left key should be A");
        
        settings.setRightKey(javafx.scene.input.KeyCode.D);
        assertEquals(javafx.scene.input.KeyCode.D, settings.getRightKey(), "Right key should be D");
        
        settings.setPauseKey(javafx.scene.input.KeyCode.ESCAPE);
        assertEquals(javafx.scene.input.KeyCode.ESCAPE, settings.getPauseKey(), "Pause key should be ESCAPE");
    }

    @Test
    void testMultipleSettingsChanges() {
        settings.setMusicVolume(0.8);
        settings.setSoundVolume(0.7);
        settings.setScreenWidth(1600);
        settings.setScreenHeight(900);
        
        assertEquals(0.8, settings.getMusicVolume(), 0.01);
        assertEquals(0.7, settings.getSoundVolume(), 0.01);
        assertEquals(1600, settings.getScreenWidth());
        assertEquals(900, settings.getScreenHeight());
    }
}
