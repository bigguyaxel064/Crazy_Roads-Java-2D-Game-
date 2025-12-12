package com.example.crazy_roads.models;

public class Bonus {
    private boolean invulnerableActive;
    private long invulnerableStartTime;
    private static int INVULNERABLE_DURATION_MS = 5000;
    private boolean scoreMultipleActive;
    private long scoreMultipleStartTime;
    private int scoreMultiple;
    private static int SCORE_MULTIPLE_DURATION_MS = 2000;
    private int lastScoreMilestone;
    private static int LAST_SCORE_MILESTONE_INTERVAL = 10000;
    private int totalScoreMultipleActivations;
    private int totalInvulnerabilityActivations;
    
    public Bonus() {
        this.invulnerableActive = false;
        this.invulnerableStartTime = 0;
        this.scoreMultipleActive = false;
        this.scoreMultipleStartTime = 1;
        this.scoreMultiple = 1;
        this.lastScoreMilestone = 0;
        this.totalInvulnerabilityActivations = 0;
        this.totalScoreMultipleActivations = 0;
    }
    
    public void activateInvulnerability() {
        this.invulnerableActive = true;
        this.invulnerableStartTime = System.currentTimeMillis();
        this.totalInvulnerabilityActivations++;
        System.out.println("Invulnérabilité pendant 5sec");
    }

    public void activateScoreMultiple() {
        this.scoreMultipleActive = true;
        this.scoreMultiple = 2;
        this.scoreMultipleStartTime = System.currentTimeMillis();
        this.totalScoreMultipleActivations++;
        System.out.println("Score multipliée par 2 pendant 2 sec.");
    }
    
    public void update() {
        long currentTime = System.currentTimeMillis();
        
        if(invulnerableActive) {
            long elapsed = currentTime - invulnerableStartTime;
            if(elapsed > INVULNERABLE_DURATION_MS) {
                invulnerableActive = false;
                System.out.println("Fin de l'invulnérabilité.");
            }
        }
        if(scoreMultipleActive) {
            long elapsed = currentTime - scoreMultipleStartTime;
            if(elapsed > SCORE_MULTIPLE_DURATION_MS) {
                scoreMultipleActive = false;
                scoreMultiple = 1;
                System.out.println("Fin du multiplicateur de score x2");
            }
        }
    }
    
    
    public boolean checkScoreMilestone(int currentScore) {
        int currentMilestone = (currentScore / LAST_SCORE_MILESTONE_INTERVAL) * LAST_SCORE_MILESTONE_INTERVAL;

        if(currentMilestone > lastScoreMilestone && currentScore >= LAST_SCORE_MILESTONE_INTERVAL) {
            lastScoreMilestone = currentMilestone;
            activateScoreMultiple();
            return true;
        }
        return false;
    }
    
    public int applyMultiplier(int score) {
        return score * scoreMultiple;
    }
    
    public boolean shouldBackCollision() {
        return invulnerableActive;
    }
    
    public boolean handleCollision(Car voiture) {
        if(shouldBackCollision()) {
            System.out.println("Collision bloquée par l'invulnérabilité.");
            return false;
        }
        
        voiture.collide();
        activateInvulnerability();
        return true;
    }
    
    public boolean handleCollisionMk(Car voiture) {
        if(shouldBackCollision()) {
            System.out.println("Collision bloquée par l'invulnérabilité.");
            return false;
        }
        
        voiture.collide();
        activateInvulnerability();
        return true;
    }
    
    public long getInvulnerableRemainingsMs() {
        if(!invulnerableActive) return 0;
        long currentTime = System.currentTimeMillis();
        long elapsed = currentTime - invulnerableStartTime;
        return Math.max(0, INVULNERABLE_DURATION_MS - elapsed);
    }
    
    public long getInvulnerableRemainingsSec() {
        return getInvulnerableRemainingsMs() / 1000;
    }
    
    public void reset() {
        invulnerableActive = false;
        invulnerableStartTime = 0;
        scoreMultiple = 1;
        scoreMultipleActive = false;
        scoreMultipleStartTime = 0;
        lastScoreMilestone = 0;
    }
}
