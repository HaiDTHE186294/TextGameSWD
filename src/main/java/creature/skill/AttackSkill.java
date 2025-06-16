package creature.skill;

import creature.model.ACreature;
import java.lang.*;
import java.util.*;
import java.io.*;

/**
 * Basic attack skill implementation
 * @author Ha Duc Hau
 */
public class AttackSkill implements ISkill {
    private String id;
    private String name;
    private String description;
    private int mpCost;
    private int cooldown;
    private int currentCooldown;
    private double damageMultiplier;

    public AttackSkill(String id, String name, String description, int mpCost, int cooldown, double damageMultiplier) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.mpCost = mpCost;
        this.cooldown = cooldown;
        this.currentCooldown = 0;
        this.damageMultiplier = damageMultiplier;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getMpCost() {
        return mpCost;
    }

    @Override
    public int getCooldown() {
        return cooldown;
    }

    @Override
    public int getCurrentCooldown() {
        return currentCooldown;
    }

    @Override
    public boolean canUse(ACreature caster) {
        return !isOnCooldown() && caster.getStats().getMp() >= mpCost;
    }

    @Override
    public void use(ACreature caster, ACreature target) {
        if (!canUse(caster)) {
            System.out.println("Cannot use skill: " + name);
            return;
        }

        // Consume MP
        caster.getStats().setMp(caster.getStats().getMp() - mpCost);
        
        // Calculate damage
        int baseDamage = caster.getStats().getAttack();
        int actualDamage = (int) (baseDamage * damageMultiplier);
        
        // Apply damage to target
        target.takeDamage(actualDamage);
        
        // Set cooldown
        currentCooldown = cooldown;
        
        System.out.println(caster.getName() + " uses " + name + " on " + target.getName() + " for " + actualDamage + " damage!");
    }

    @Override
    public void updateCooldown() {
        if (currentCooldown > 0) {
            currentCooldown--;
        }
    }

    @Override
    public boolean isOnCooldown() {
        return currentCooldown > 0;
    }

    public double getDamageMultiplier() {
        return damageMultiplier;
    }

    public void setDamageMultiplier(double damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }
} 