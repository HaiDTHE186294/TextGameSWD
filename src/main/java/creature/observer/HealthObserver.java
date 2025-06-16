package creature.observer;

import creature.model.ACreature;
import java.lang.*;
import java.util.*;
import java.io.*;

/**
 * Observer for health-related changes in creatures
 * @author Ha Duc Hau
 */
public class HealthObserver implements ICreatureObserver {
    
    @Override
    public void onCreatureChanged(ACreature creature, String changeType) {
        switch (changeType) {
            case "HPChanged":
                handleHpChanged(creature);
                break;
            case "Healed":
                handleHealed(creature);
                break;
            case "Died":
                handleDied(creature);
                break;
            case "Revived":
                handleRevived(creature);
                break;
            default:
                // Ignore other change types
                break;
        }
    }
    
    private void handleHpChanged(ACreature creature) {
        System.out.println("[HealthObserver] " + creature.getName() + " HP changed to: " + 
                          creature.getStats().getHp() + "/" + creature.getStats().getMaxHp());
        
        double hpPercentage = creature.getStats().getHpPercentage();
        if (hpPercentage <= 0.25) {
            System.out.println("  WARNING: " + creature.getName() + " is critically low on health!");
        } else if (hpPercentage <= 0.5) {
            System.out.println("  " + creature.getName() + " is low on health!");
        }
    }
    
    private void handleHealed(ACreature creature) {
        System.out.println("[HealthObserver] " + creature.getName() + " was healed!");
        System.out.println("Current HP: " + creature.getStats().getHp() + "/" + creature.getStats().getMaxHp());
    }
    
    private void handleDied(ACreature creature) {
        System.out.println("[HealthObserver] " + creature.getName() + " has died!");
    }
    
    private void handleRevived(ACreature creature) {
        System.out.println("[HealthObserver] " + creature.getName() + " has been revived!");
        System.out.println("Current HP: " + creature.getStats().getHp() + "/" + creature.getStats().getMaxHp());
    }
} 