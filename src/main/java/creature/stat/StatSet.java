/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package creature.stat;

import java.lang.*;
import java.util.*;
import java.io.*;

/**
 * Represents the statistics of a creature
 * @author Ha Duc Hau
 */
public class StatSet {
    private int hp;
    private int maxHp;
    private int mp;
    private int maxMp;
    private int attack;
    private int defense;
    private int speed;
    private int level;
    private int experience;

    public StatSet() {
        this(100, 50, 10, 5, 5, 1, 0);
    }

    public StatSet(int maxHp, int maxMp, int attack, int defense, int speed, int level, int experience) {
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.maxMp = maxMp;
        this.mp = maxMp;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
        this.level = level;
        this.experience = experience;
    }

    // Getters and setters
    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = Math.max(0, Math.min(hp, maxHp));
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
        this.hp = Math.min(this.hp, maxHp);
    }

    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        this.mp = Math.max(0, Math.min(mp, maxMp));
    }

    public int getMaxMp() {
        return maxMp;
    }

    public void setMaxMp(int maxMp) {
        this.maxMp = maxMp;
        this.mp = Math.min(this.mp, maxMp);
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = Math.max(0, attack);
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = Math.max(0, defense);
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = Math.max(0, speed);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = Math.max(1, level);
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = Math.max(0, experience);
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public boolean isFullHp() {
        return hp >= maxHp;
    }

    public boolean isFullMp() {
        return mp >= maxMp;
    }

    public double getHpPercentage() {
        return (double) hp / maxHp;
    }

    public double getMpPercentage() {
        return (double) mp / maxMp;
    }

    @Override
    public String toString() {
        return String.format("HP: %d/%d, MP: %d/%d, ATK: %d, DEF: %d, SPD: %d, LVL: %d, EXP: %d",
                hp, maxHp, mp, maxMp, attack, defense, speed, level, experience);
    }
}
