package com.example.crazy_roads.managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ScoreManagerTest {
    private ScoreManager scoreManager;

    @BeforeEach
    void setUp() {
        scoreManager = ScoreManager.getInstance();
        scoreManager.resetAllScores();
    }

    @Test
    void testSingletonInstance() {
        ScoreManager instance1 = ScoreManager.getInstance();
        ScoreManager instance2 = ScoreManager.getInstance();
        
        assertSame(instance1, instance2, "ScoreManager should be a singleton");
    }

    @Test
    void testInitialScores() {
        scoreManager.resetAllScores();
        
        assertEquals(0, scoreManager.getBestScore(ScoreManager.MODE_INFINITE), 
                "Initial infinite mode score should be 0");
        assertEquals(0, scoreManager.getBestScore(ScoreManager.MODE_TIME_ATTACK), 
                "Initial time attack mode score should be 0");
    }

    @Test
    void testUpdateBestScoreInfiniteMode() {
        boolean isNewRecord = scoreManager.updateBestScore(ScoreManager.MODE_INFINITE, 100);
        
        assertTrue(isNewRecord, "First score should be a new record");
        assertEquals(100, scoreManager.getBestScore(ScoreManager.MODE_INFINITE), 
                "Best score should be updated to 100");
    }

    @Test
    void testUpdateBestScoreInfiniteModeHigher() {
        scoreManager.updateBestScore(ScoreManager.MODE_INFINITE, 100);
        boolean isNewRecord = scoreManager.updateBestScore(ScoreManager.MODE_INFINITE, 150);
        
        assertTrue(isNewRecord, "Higher score should be a new record");
        assertEquals(150, scoreManager.getBestScore(ScoreManager.MODE_INFINITE), 
                "Best score should be updated to 150");
    }

    @Test
    void testUpdateBestScoreInfiniteModeLower() {
        scoreManager.updateBestScore(ScoreManager.MODE_INFINITE, 100);
        boolean isNewRecord = scoreManager.updateBestScore(ScoreManager.MODE_INFINITE, 50);
        
        assertFalse(isNewRecord, "Lower score should not be a new record");
        assertEquals(100, scoreManager.getBestScore(ScoreManager.MODE_INFINITE), 
                "Best score should remain 100");
    }

    @Test
    void testUpdateBestScoreTimeAttackMode() {
        boolean isNewRecord = scoreManager.updateBestScore(ScoreManager.MODE_TIME_ATTACK, 60);
        
        assertTrue(isNewRecord, "First time should be a new record");
        assertEquals(60, scoreManager.getBestScore(ScoreManager.MODE_TIME_ATTACK), 
                "Best time should be 60");
    }

    @Test
    void testUpdateBestScoreTimeAttackModeFaster() {
        scoreManager.updateBestScore(ScoreManager.MODE_TIME_ATTACK, 60);
        boolean isNewRecord = scoreManager.updateBestScore(ScoreManager.MODE_TIME_ATTACK, 45);
        
        assertTrue(isNewRecord, "Faster time should be a new record");
        assertEquals(45, scoreManager.getBestScore(ScoreManager.MODE_TIME_ATTACK), 
                "Best time should be updated to 45");
    }

    @Test
    void testResetAllScores() {
        scoreManager.updateBestScore(ScoreManager.MODE_INFINITE, 100);
        scoreManager.updateBestScore(ScoreManager.MODE_TIME_ATTACK, 60);
        
        scoreManager.resetAllScores();
        
        assertEquals(0, scoreManager.getBestScore(ScoreManager.MODE_INFINITE), 
                "Infinite mode score should be reset to 0");
        assertEquals(0, scoreManager.getBestScore(ScoreManager.MODE_TIME_ATTACK), 
                "Time attack mode score should be reset to 0");
    }

    @Test
    void testResetSingleScore() {
        scoreManager.updateBestScore(ScoreManager.MODE_INFINITE, 100);
        scoreManager.updateBestScore(ScoreManager.MODE_TIME_ATTACK, 60);
        
        scoreManager.resetScore(ScoreManager.MODE_INFINITE);
        
        assertEquals(0, scoreManager.getBestScore(ScoreManager.MODE_INFINITE), 
                "Infinite mode score should be reset");
        assertEquals(60, scoreManager.getBestScore(ScoreManager.MODE_TIME_ATTACK), 
                "Time attack score should remain unchanged");
    }

    @Test
    void testIsNewRecord() {
        scoreManager.updateBestScore(ScoreManager.MODE_INFINITE, 100);
        
        assertTrue(150 > scoreManager.getBestScore(ScoreManager.MODE_INFINITE), 
                "150 should be higher than current best");
        assertFalse(50 > scoreManager.getBestScore(ScoreManager.MODE_INFINITE), 
                "50 should not be higher than current best");
    }

    @Test
    void testGetBestScoreNonExistentMode() {
        int score = scoreManager.getBestScore("NON_EXISTENT_MODE");
        assertEquals(0, score, "Non-existent mode should return 0");
    }
}
