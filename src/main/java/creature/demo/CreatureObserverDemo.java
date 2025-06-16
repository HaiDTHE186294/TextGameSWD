package creature.demo;

import creature.model.ACreature;
import creature.model.Player;
import creature.model.Enemy;
import creature.service.CreatureFactory;
import creature.skill.ISkill;
import creature.state.BurningState;
import item.model.ConsumableItem;
import item.model.EquipmentItem;
import item.model.EquipmentSlot;
import java.util.HashMap;
import java.util.Map;

/**
 * Demo class to test the Observer pattern implementation
 * @author Ha Duc Hau
 */
public class CreatureObserverDemo {
    
    public static void main(String[] args) {
        System.out.println("=== CREATURE OBSERVER PATTERN DEMO ===\n");
        
        // Create a player with observers
        Player player = CreatureFactory.createWarrior("Hero");
        System.out.println("Created player: " + player.getName());
        System.out.println("Initial stats: " + player.getStats() + "\n");
        
        // Create an enemy
        Enemy goblin = CreatureFactory.createGoblin();
        System.out.println("Created enemy: " + goblin.getName());
        System.out.println("Initial stats: " + goblin.getStats() + "\n");
        
        // Test health changes
        System.out.println("--- Testing Health Changes ---");
        player.takeDamage(30);
        player.heal(10);
        player.takeDamage(80); // Should trigger critical health warning
        System.out.println();
        
        // Test skill usage
        System.out.println("--- Testing Skill Usage ---");
        ISkill slash = player.getSkill("slash");
        if (slash != null) {
            player.useSkill(slash, goblin);
        }
        System.out.println();
        
        // Test equipment changes
        System.out.println("--- Testing Equipment Changes ---");
        Map<String, Integer> swordAttrs = new HashMap<>();
        swordAttrs.put("ATK", 10);
        EquipmentItem sword = new EquipmentItem("sword_1", "Iron Sword", "A basic sword", 100, 100, EquipmentSlot.WEAPON, swordAttrs, null);
        player.equipItem(sword);
        player.unequipItem(sword);
        System.out.println();
        
        // Test state effects
        System.out.println("--- Testing State Effects ---");
        BurningState burning = new BurningState(3, 5);
        goblin.addState(burning);
        goblin.updateStates(); // Apply burning damage
        goblin.updateStates(); // Apply burning damage again
        System.out.println();
        
        // Test level up
        System.out.println("--- Testing Level Up ---");
        player.gainExperience(100);
        System.out.println();
        
        // Test achievement
        System.out.println("--- Testing Achievement ---");
        player.addAchievement("First Blood");
        System.out.println();
        
        // Test enemy behavior
        System.out.println("--- Testing Enemy Behavior ---");
        goblin.act();
        goblin.setAggressive(false);
        goblin.act();
        System.out.println();
        
        // Test item usage
        System.out.println("--- Testing Item Usage ---");
        Map<String, Integer> potionAttrs = new HashMap<>();
        potionAttrs.put("HP", 50);
        ConsumableItem potion = new ConsumableItem("potion_1", "Health Potion", "Restores health", 20, 10, potionAttrs, null, () -> {
            player.heal(50);
        });
        player.useItem(potion);
        System.out.println();
        
        // Final status
        System.out.println("--- Final Status ---");
        System.out.println("Player: " + player);
        System.out.println("Goblin: " + goblin);
        System.out.println("Player achievements: " + player.getAchievements());
        System.out.println("Goblin loot table: " + goblin.getLootTable());
    }
} 