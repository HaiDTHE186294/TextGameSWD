/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package creature.state;

import creature.model.ACreature;
import java.lang.*;
import java.util.*;
import java.io.*;

/**
 * Burning state effect implementation
 * @author Ha Duc Hau
 */
public class BurningState implements ICreatureState {
    private int duration;
    private int damagePerTurn;
    private String name;

    public BurningState(int duration, int damagePerTurn) {
        this.duration = duration;
        this.damagePerTurn = damagePerTurn;
        this.name = "Burning";
    }

    @Override
    public void applyEffect(ACreature creature) {
        System.out.println(creature.getName() + " is now " + name + " for " + duration + " turns!");
    }

    @Override
    public void updateState(ACreature creature) {
        if (duration > 0) {
            // Apply burning damage
            creature.takeDamage(damagePerTurn);
            System.out.println(creature.getName() + " takes " + damagePerTurn + " burning damage!");
            duration--;
        }
    }

    @Override
    public boolean isExpired() {
        return duration <= 0;
    }

    public int getDuration() {
        return duration;
    }

    public int getDamagePerTurn() {
        return damagePerTurn;
    }

    public String getName() {
        return name;
    }
}
