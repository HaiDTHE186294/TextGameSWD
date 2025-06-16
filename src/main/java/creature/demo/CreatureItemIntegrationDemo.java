package creature.demo;

import creature.model.ACreature;
import creature.model.Player;
import creature.model.Enemy;
import creature.service.CreatureFactory;
import creature.service.CreatureItemManager;
import creature.skill.ISkill;
import creature.state.BurningState;
import item.model.ConsumableItem;
import item.model.EquipmentItem;
import item.model.EquipmentSlot;
import item.model.Item;
import item.service.ItemService;
import item.service.ItemLoader;
import java.util.HashMap;
import java.util.Map;

/**
 * Demo class to test the integration between creatures and ItemService
 * @author Ha Duc Hau
 */
public class CreatureItemIntegrationDemo {
    
    public static void main(String[] args) {
        System.out.println("=== CREATURE & ITEM SERVICE INTEGRATION DEMO ===\n");
        
        try {
            // Initialize ItemService with items from JSON
            ItemService itemService = new ItemService();
            Map<String, Item> items = ItemLoader.loadItems();
            items.values().forEach(itemService::registerItem);
            
            // Set ItemService in CreatureFactory
            CreatureFactory.setItemService(itemService);
            
            // Create CreatureItemManager
            CreatureItemManager itemManager = new CreatureItemManager(itemService);
            
            // Create a player with inventory
            Player player = CreatureFactory.createWarrior("Hero");
            System.out.println("Created player: " + player.getName() + " (ID: " + player.getId() + ")");
            System.out.println("Initial stats: " + player.getStats() + "\n");
            
            // Create an enemy
            Enemy goblin = CreatureFactory.createGoblin();
            System.out.println("Created enemy: " + goblin.getName() + " (ID: " + goblin.getId() + ")");
            System.out.println("Initial stats: " + goblin.getStats() + "\n");
            
            // Test adding items to inventory
            System.out.println("--- Testing Inventory Management ---");
            player.addItem("sword_1", 1);
            player.addItem("potion_1", 3);
            player.addItem("shield_1", 1);
            player.addItem("potion_2", 2);
            System.out.println();
            
            // Display detailed inventory
            System.out.println("--- Detailed Inventory Display ---");
            itemManager.displayDetailedInventory(player);
            System.out.println();
            
            // Test auto-equip functionality
            System.out.println("--- Testing Auto-Equip ---");
            itemManager.autoEquipBestItems(player);
            System.out.println();
            
            // Test recommended items
            System.out.println("--- Testing Recommended Items ---");
            System.out.println("Recommended items for " + player.getName() + ":");
            for (Item item : itemManager.getRecommendedItems(player)) {
                System.out.println("  - " + item.getName() + " (" + item.getDescription() + ")");
            }
            System.out.println();
            
            // Test combat with items
            System.out.println("--- Testing Combat with Items ---");
            player.takeDamage(50);
            System.out.println("Player took damage, current HP: " + player.getStats().getHp());
            
            // Use healing potion using item manager
            if (player.hasItem("potion_1")) {
                System.out.println("Using healing potion...");
                itemManager.useConsumableItem(player, "potion_1");
            }
            System.out.println();
            
            // Test skill usage
            System.out.println("--- Testing Skill Usage ---");
            ISkill slash = player.getSkill("slash");
            if (slash != null) {
                player.useSkill(slash, goblin);
            }
            System.out.println();
            
            // Test enemy loot using item manager
            System.out.println("--- Testing Enemy Loot with Item Manager ---");
            itemManager.giveLootFromEnemy(goblin, player);
            System.out.println();
            
            // Test equipment stats calculation
            System.out.println("--- Testing Equipment Stats ---");
            Map<String, Integer> equipStats = itemManager.getEquipmentStats(player);
            System.out.println("Equipment stats: " + equipStats);
            
            Map<String, Integer> totalStats = itemManager.getTotalCombatStats(player);
            System.out.println("Total combat stats: " + totalStats);
            System.out.println();
            
            // Test item validation
            System.out.println("--- Testing Item Validation ---");
            System.out.println("Can equip sword_1: " + itemManager.canEquipItem(player, "sword_1"));
            System.out.println("Can equip potion_1: " + itemManager.canEquipItem(player, "potion_1"));
            System.out.println();
            
            // Final detailed inventory display
            System.out.println("--- Final Detailed Inventory ---");
            itemManager.displayDetailedInventory(player);
            
        } catch (Exception e) {
            System.err.println("Error during demo: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 