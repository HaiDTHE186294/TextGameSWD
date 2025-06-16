/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package creature.observer;

import creature.model.ACreature;
import java.lang.*;
import java.util.*;
import java.io.*;

/**
 * Observer for skill-related changes in creatures
 * @author Ha Duc Hau
 */
public class SkillObserver implements ICreatureObserver {
    
    @Override
    public void onCreatureChanged(ACreature creature, String changeType) {
        switch (changeType) {
            case "SkillLearned":
                handleSkillLearned(creature);
                break;
            case "SkillUsed":
                handleSkillUsed(creature);
                break;
            case "SkillUpgraded":
                handleSkillUpgraded(creature);
                break;
            case "MPChanged":
                handleMpChanged(creature);
                break;
            default:
                // Ignore other change types
                break;
        }
    }
    
    private void handleSkillLearned(ACreature creature) {
        System.out.println("[SkillObserver] " + creature.getName() + " learned a new skill!");
        System.out.println("Current skill count: " + creature.getSkills().size());
        
        // Log latest skill
        if (!creature.getSkills().isEmpty()) {
            var latestSkill = creature.getSkills().get(creature.getSkills().size() - 1);
            System.out.println("  - " + latestSkill.getName() + " (MP Cost: " + latestSkill.getMpCost() + ")");
        }
    }
    
    private void handleSkillUsed(ACreature creature) {
        System.out.println("[SkillObserver] " + creature.getName() + " used a skill!");
        System.out.println("Remaining MP: " + creature.getStats().getMp() + "/" + creature.getStats().getMaxMp());
    }
    
    private void handleSkillUpgraded(ACreature creature) {
        System.out.println("[SkillObserver] " + creature.getName() + " upgraded a skill!");
    }
    
    private void handleMpChanged(ACreature creature) {
        System.out.println("[SkillObserver] " + creature.getName() + " MP changed to: " + 
                          creature.getStats().getMp() + "/" + creature.getStats().getMaxMp());
    }
}
