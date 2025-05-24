package Levels;

import java.util.HashMap;
import java.util.Map;

public class LevelProgressTracker {
    // Singleton instance
    private static LevelProgressTracker instance;
    
    // Map to store level statuses (0 = locked, 1 = unlocked, 2 = completed)
    private Map<Integer, Integer> levelStatus = new HashMap<>();
    
    // Total number of levels in the game
    private final int totalLevels = 20;
    
    private LevelProgressTracker() {
        // Initialize all levels as locked
        for (int i = 1; i <= totalLevels; i++) {
            levelStatus.put(i, 0);
        }
        // Set level 1 as unlocked by default
        levelStatus.put(1, 1);
    }
    
    public static LevelProgressTracker getInstance() {
        if (instance == null) {
            instance = new LevelProgressTracker();
        }
        return instance;
    }
    
    public void completeLevel(int levelNumber) {
        // Mark current level as completed
        levelStatus.put(levelNumber, 2);
        
        // Unlock next level if it exists
        if (levelNumber < totalLevels) {
            levelStatus.put(levelNumber + 1, 1);
        }
    }
    
    /**
     * Unlocks all levels in the game for testing purposes
     */
    public void unlockAllLevels() {
        for (int i = 1; i <= totalLevels; i++) {
            // Set all levels to unlocked (1) status
            levelStatus.put(i, 1);
        }
        System.out.println("All levels unlocked for testing");
    }
    
    public int getLevelStatus(int levelNumber) {
        return levelStatus.getOrDefault(levelNumber, 0);
    }
    
    public boolean isLevelLocked(int levelNumber) {
        return getLevelStatus(levelNumber) == 0;
    }
    
    public boolean isLevelUnlocked(int levelNumber) {
        return getLevelStatus(levelNumber) >= 1;
    }
    
    public boolean isLevelCompleted(int levelNumber) {
        return getLevelStatus(levelNumber) == 2;
    }
    
    public int getCompletedLevelsCount() {
        int count = 0;
        for (int i = 1; i <= totalLevels; i++) {
            if (isLevelCompleted(i)) {
                count++;
            }
        }
        return count;
    }
}