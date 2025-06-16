/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package creature.model;

import java.lang.*;
import java.util.*;
import java.io.*;

/**
 * Player character implementation
 * @author Ha Duc Hau
 */
public class Player extends ACreature {
    
    private int experience;
    private int level;
    private String playerClass;
    private List<String> achievements = new ArrayList<>();

    public Player() {
        super();
        this.playerClass = "Adventurer";
        this.level = 1;
        this.experience = 0;
    }

    public Player(String name, String playerClass) {
        super();
        this.name = name;
        this.playerClass = playerClass;
        this.level = 1;
        this.experience = 0;
    }

    @Override
    public void act() {
        // Player actions are typically controlled by user input
        // This method would be called when it's the player's turn
        System.out.println(name + " is ready to act!");
    }

    public void gainExperience(int exp) {
        this.experience += exp;
        checkLevelUp();
    }

    private void checkLevelUp() {
        int requiredExp = level * 100; // Simple level up formula
        if (experience >= requiredExp) {
            levelUp();
        }
    }

    private void levelUp() {
        level++;
        experience -= (level - 1) * 100;
        
        // Increase stats
        stats.setMaxHp(stats.getMaxHp() + 10);
        stats.setMaxMp(stats.getMaxMp() + 5);
        stats.setAttack(stats.getAttack() + 2);
        stats.setDefense(stats.getDefense() + 1);
        
        // Restore HP and MP to full
        stats.setHp(stats.getMaxHp());
        stats.setMp(stats.getMaxMp());
        
        System.out.println("Level Up! " + name + " reached level " + level + "!");
        notifyObservers("LevelUp");
    }

    public void addAchievement(String achievement) {
        if (!achievements.contains(achievement)) {
            achievements.add(achievement);
            System.out.println("Achievement unlocked: " + achievement);
            notifyObservers("AchievementUnlocked");
        }
    }

    // Getters and setters
    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getPlayerClass() {
        return playerClass;
    }

    public void setPlayerClass(String playerClass) {
        this.playerClass = playerClass;
    }

    public List<String> getAchievements() {
        return new ArrayList<>(achievements);
    }

    public boolean hasAchievement(String achievement) {
        return achievements.contains(achievement);
    }

    @Override
    public String toString() {
        return String.format("Player{name='%s', class='%s', level=%d, exp=%d, %s}", 
                           name, playerClass, level, experience, stats.toString());
    }
}
