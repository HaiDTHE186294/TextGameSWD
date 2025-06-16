/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package creature.model;

import java.lang.*;
import java.util.*;
import java.io.*;

/**
 * Enemy creature implementation
 * @author Ha Duc Hau
 */
public class Enemy extends ACreature {
    
    private String enemyType;
    private int experienceReward;
    private List<String> lootTable = new ArrayList<>();
    private boolean isAggressive;

    public Enemy() {
        super();
        this.enemyType = "Generic";
        this.experienceReward = 10;
        this.isAggressive = true;
    }

    public Enemy(String name, String enemyType, int experienceReward) {
        super();
        this.name = name;
        this.enemyType = enemyType;
        this.experienceReward = experienceReward;
        this.isAggressive = true;
    }

    @Override
    public void act() {
        // Simple AI behavior
        if (isAggressive) {
            System.out.println(name + " is looking for a target to attack!");
        } else {
            System.out.println(name + " is patrolling the area.");
        }
    }

    public void setAggressive(boolean aggressive) {
        this.isAggressive = aggressive;
    }

    public void addLoot(String itemId) {
        if (!lootTable.contains(itemId)) {
            lootTable.add(itemId);
        }
    }

    public List<String> getLootTable() {
        return new ArrayList<>(lootTable);
    }

    public String getEnemyType() {
        return enemyType;
    }

    public void setEnemyType(String enemyType) {
        this.enemyType = enemyType;
    }

    public int getExperienceReward() {
        return experienceReward;
    }

    public void setExperienceReward(int experienceReward) {
        this.experienceReward = experienceReward;
    }

    public boolean isAggressive() {
        return isAggressive;
    }

    @Override
    public String toString() {
        return String.format("Enemy{name='%s', type='%s', expReward=%d, %s}", 
                           name, enemyType, experienceReward, stats.toString());
    }
}
