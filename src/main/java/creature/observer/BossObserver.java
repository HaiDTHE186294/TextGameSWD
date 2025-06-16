package creature.observer;

import creature.model.ACreature;
import creature.model.Boss;
import java.lang.*;
import java.util.*;
import java.io.*;

/**
 * Observer for boss-specific changes in creatures
 * @author Ha Duc Hau
 */
public class BossObserver implements ICreatureObserver {
    
    @Override
    public void onCreatureChanged(ACreature creature, String changeType) {
        if (!(creature instanceof Boss)) {
            return; // Only observe Boss creatures
        }
        
        Boss boss = (Boss) creature;
        
        switch (changeType) {
            case "PhaseChanged":
                handlePhaseChanged(boss);
                break;
            case "Enraged":
                handleEnraged(boss);
                break;
            case "HPChanged":
                handleHpChanged(boss);
                break;
            case "Died":
                handleBossDefeated(boss);
                break;
            default:
                // Ignore other change types
                break;
        }
    }
    
    private void handlePhaseChanged(Boss boss) {
        System.out.println("[BossObserver] " + boss.getName() + " entered Phase " + boss.getCurrentPhase() + "!");
        System.out.println("  Current HP: " + boss.getStats().getHp() + "/" + boss.getStats().getMaxHp() + 
                          " (" + String.format("%.1f", boss.getHealthPercentage()) + "%)");
        System.out.println("  Phase Skills: " + boss.getPhaseSkills());
        
        // Phase-specific warnings
        switch (boss.getCurrentPhase()) {
            case 2:
                System.out.println("  [WARNING] Boss is now more aggressive!");
                break;
            case 3:
                System.out.println("  [DANGER] Boss has entered final phase!");
                break;
        }
    }
    
    private void handleEnraged(Boss boss) {
        System.out.println("[BossObserver] " + boss.getName() + " is ENRAGED!");
        System.out.println("  [CRITICAL] Boss attack power and speed increased!");
        System.out.println("  Current HP: " + String.format("%.1f", boss.getHealthPercentage()) + "%");
        System.out.println("  Enrage threshold: " + boss.getEnrageThreshold() + "%");
    }
    
    private void handleHpChanged(Boss boss) {
        double hpPercent = boss.getHealthPercentage();
        
        // Phase transition warnings
        if (boss.getCurrentPhase() < boss.getMaxPhases()) {
            double nextPhaseThreshold = (boss.getCurrentPhase() + 1) * (100.0 / boss.getMaxPhases());
            if (hpPercent <= nextPhaseThreshold + 5 && hpPercent > nextPhaseThreshold) {
                System.out.println("[BossObserver] " + boss.getName() + " is approaching Phase " + (boss.getCurrentPhase() + 1) + "!");
            }
        }
        
        // Enrage warnings
        if (!boss.isEnraged() && hpPercent <= boss.getEnrageThreshold() + 5 && hpPercent > boss.getEnrageThreshold()) {
            System.out.println("[BossObserver] " + boss.getName() + " is approaching enrage threshold!");
        }
        
        // Critical health warnings
        if (hpPercent <= 10) {
            System.out.println("[BossObserver] " + boss.getName() + " is critically wounded!");
        } else if (hpPercent <= 25) {
            System.out.println("[BossObserver] " + boss.getName() + " is severely damaged!");
        }
    }
    
    private void handleBossDefeated(Boss boss) {
        System.out.println("[BossObserver] " + boss.getName() + " has been DEFEATED!");
        System.out.println("  Final Phase: " + boss.getCurrentPhase() + "/" + boss.getMaxPhases());
        System.out.println("  Was Enraged: " + boss.isEnraged());
        System.out.println("  Experience Reward: " + boss.getExperienceReward());
        System.out.println("  Special Loot Available: " + boss.getSpecialLoot().size() + " items");
        
        // Display loot information
        if (!boss.getAllLoot().isEmpty()) {
            System.out.println("  Loot Table:");
            for (String lootId : boss.getAllLoot()) {
                System.out.println("    - " + lootId);
            }
        }
    }
    
    public void displayBossStatus(Boss boss) {
        System.out.println("\n=== " + boss.getName() + " BOSS STATUS ===");
        System.out.println("Type: " + boss.getEnemyType());
        System.out.println("Phase: " + boss.getCurrentPhase() + "/" + boss.getMaxPhases());
        System.out.println("HP: " + boss.getStats().getHp() + "/" + boss.getStats().getMaxHp() + 
                          " (" + String.format("%.1f", boss.getHealthPercentage()) + "%)");
        System.out.println("Enraged: " + boss.isEnraged());
        System.out.println("Enrage Threshold: " + boss.getEnrageThreshold() + "%");
        System.out.println("Experience Reward: " + boss.getExperienceReward());
        
        // Skills
        System.out.println("Skills: " + boss.getSkills().size());
        for (var skill : boss.getSkills()) {
            System.out.println("  - " + skill.getName() + " (MP: " + skill.getMpCost() + ")");
        }
        
        // Phase skills
        if (!boss.getPhaseSkills().isEmpty()) {
            System.out.println("Phase Skills: " + boss.getPhaseSkills());
        }
        
        // Loot
        System.out.println("Regular Loot: " + boss.getLootTable().size() + " items");
        System.out.println("Special Loot: " + boss.getSpecialLoot().size() + " items");
    }
} 