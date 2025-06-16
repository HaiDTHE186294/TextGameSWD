package creature.service;

import creature.model.ACreature;
import creature.model.Player;
import creature.model.Enemy;
import creature.model.Boss;
import item.model.Item;
import item.model.ConsumableItem;
import item.model.EquipmentItem;
import item.service.ItemService;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * Utility class to manage interactions between creatures and items
 * @author Ha Duc Hau
 */
public class CreatureItemManager {
    
    private ItemService itemService;
    
    public CreatureItemManager(ItemService itemService) {
        this.itemService = itemService;
    }
    
    /**
     * Give loot from enemy to player
     */
    public void giveLootFromEnemy(Enemy enemy, Player player) {
        List<String> lootTable = enemy.getLootTable();
        System.out.println(player.getName() + " defeated " + enemy.getName() + " and received:");
        
        for (String lootId : lootTable) {
            player.addItem(lootId, 1);
            Item item = itemService.getItem(lootId);
            if (item != null) {
                System.out.println("  - " + item.getName());
            }
        }
    }
    
    /**
     * Give loot from boss to player (includes special loot)
     */
    public void giveLootFromBoss(Boss boss, Player player) {
        System.out.println("[BOSS DEFEATED] " + player.getName() + " defeated BOSS " + boss.getName() + "!");
        System.out.println("Experience gained: " + boss.getExperienceReward());
        
        // Give regular loot
        List<String> regularLoot = boss.getLootTable();
        if (!regularLoot.isEmpty()) {
            System.out.println("Regular loot:");
            for (String lootId : regularLoot) {
                player.addItem(lootId, 1);
                Item item = itemService.getItem(lootId);
                if (item != null) {
                    System.out.println("  - " + item.getName());
                }
            }
        }
        
        // Give special loot
        List<String> specialLoot = boss.getSpecialLoot();
        if (!specialLoot.isEmpty()) {
            System.out.println("[SPECIAL BOSS LOOT]:");
            for (String lootId : specialLoot) {
                player.addItem(lootId, 1);
                Item item = itemService.getItem(lootId);
                if (item != null) {
                    System.out.println("  [RARE] " + item.getName() + " (RARE)");
                }
            }
        }
        
        // Boss-specific rewards
        if (boss.isEnraged()) {
            System.out.println("[BONUS] Bonus reward for defeating enraged boss!");
            player.addItem("potion_2", 2); // Extra mana potions
        }
        
        if (boss.isInFinalPhase()) {
            System.out.println("[BONUS] Bonus reward for defeating boss in final phase!");
            player.addItem("potion_1", 3); // Extra health potions
        }
    }
    
    /**
     * Auto-equip best items for a creature
     */
    public void autoEquipBestItems(ACreature creature) {
        Map<String, Integer> inventory = creature.getInventoryItems();
        
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            if (entry.getValue() > 0) {
                Item item = itemService.getItem(entry.getKey());
                if (item instanceof EquipmentItem) {
                    EquipmentItem equip = (EquipmentItem) item;
                    
                    // Check if creature already has equipment in this slot
                    boolean hasSlotOccupied = creature.getEquipment().stream()
                            .anyMatch(e -> e instanceof EquipmentItem && 
                                    ((EquipmentItem) e).getSlot() == equip.getSlot());
                    
                    if (!hasSlotOccupied) {
                        creature.equipItem(equip);
                        System.out.println(creature.getName() + " auto-equipped " + equip.getName());
                    }
                }
            }
        }
    }
    
    /**
     * Use consumable item with proper effect
     */
    public void useConsumableItem(ACreature creature, String itemId) {
        if (!creature.hasItem(itemId)) {
            System.out.println(creature.getName() + " doesn't have " + itemId);
            return;
        }
        
        Item item = itemService.getItem(itemId);
        if (item instanceof ConsumableItem) {
            ConsumableItem consumable = (ConsumableItem) item;
            
            // Apply effects based on item attributes
            Map<String, Integer> attributes = consumable.getAttributes();
            
            if (attributes.containsKey("HP")) {
                int healAmount = attributes.get("HP");
                creature.heal(healAmount);
                System.out.println(creature.getName() + " healed " + healAmount + " HP");
            }
            
            if (attributes.containsKey("MP")) {
                int mpAmount = attributes.get("MP");
                creature.restoreMp(mpAmount);
                System.out.println(creature.getName() + " restored " + mpAmount + " MP");
            }
            
            // Use the item and remove from inventory
            creature.useItem(consumable);
            creature.removeItem(itemId, 1);
            
        } else {
            System.out.println(itemId + " is not a consumable item");
        }
    }
    
    /**
     * Check if creature can equip an item
     */
    public boolean canEquipItem(ACreature creature, String itemId) {
        if (!creature.hasItem(itemId)) {
            return false;
        }
        
        Item item = itemService.getItem(itemId);
        if (item instanceof EquipmentItem) {
            EquipmentItem equip = (EquipmentItem) item;
            
            // Check if slot is already occupied
            return creature.getEquipment().stream()
                    .noneMatch(e -> e instanceof EquipmentItem && 
                            ((EquipmentItem) e).getSlot() == equip.getSlot());
        }
        
        return false;
    }
    
    /**
     * Get total stats from equipped items
     */
    public Map<String, Integer> getEquipmentStats(ACreature creature) {
        Map<String, Integer> totalStats = new java.util.HashMap<>();
        
        for (Item equip : creature.getEquipment()) {
            if (equip instanceof EquipmentItem) {
                Map<String, Integer> itemStats = equip.getAttributes();
                for (Map.Entry<String, Integer> stat : itemStats.entrySet()) {
                    totalStats.merge(stat.getKey(), stat.getValue(), Integer::sum);
                }
            }
        }
        
        return totalStats;
    }
    
    /**
     * Calculate total combat stats (base + equipment)
     */
    public Map<String, Integer> getTotalCombatStats(ACreature creature) {
        Map<String, Integer> totalStats = new java.util.HashMap<>();
        
        // Base stats
        totalStats.put("HP", creature.getStats().getHp());
        totalStats.put("MaxHP", creature.getStats().getMaxHp());
        totalStats.put("MP", creature.getStats().getMp());
        totalStats.put("MaxMP", creature.getStats().getMaxMp());
        totalStats.put("ATK", creature.getStats().getAttack());
        totalStats.put("DEF", creature.getStats().getDefense());
        totalStats.put("SPD", creature.getStats().getSpeed());
        
        // Add equipment stats
        Map<String, Integer> equipStats = getEquipmentStats(creature);
        for (Map.Entry<String, Integer> stat : equipStats.entrySet()) {
            totalStats.merge(stat.getKey(), stat.getValue(), Integer::sum);
        }
        
        return totalStats;
    }
    
    /**
     * Find best items for a creature based on their class/type
     */
    public List<Item> getRecommendedItems(ACreature creature) {
        List<Item> recommendations = new ArrayList<>();
        
        if (creature instanceof Player) {
            Player player = (Player) creature;
            String playerClass = player.getPlayerClass();
            
            // Recommend items based on class
            switch (playerClass.toLowerCase()) {
                case "warrior":
                    recommendations.add(itemService.getItem("sword_1"));
                    recommendations.add(itemService.getItem("shield_1"));
                    break;
                case "mage":
                    recommendations.add(itemService.getItem("potion_2")); // Mana potion
                    break;
                default:
                    recommendations.add(itemService.getItem("potion_1")); // Health potion
                    break;
            }
        } else if (creature instanceof Boss) {
            Boss boss = (Boss) creature;
            
            // Boss-specific recommendations
            if (boss.isEnraged()) {
                recommendations.add(itemService.getItem("potion_1")); // Health potion for survival
            }
            
            if (boss.getCurrentPhase() > 1) {
                recommendations.add(itemService.getItem("potion_2")); // Mana potion for skills
            }
        } else if (creature instanceof Enemy) {
            Enemy enemy = (Enemy) creature;
            String enemyType = enemy.getEnemyType();
            
            // Recommend items based on enemy type
            if ("Humanoid".equals(enemyType)) {
                recommendations.add(itemService.getItem("sword_1"));
            }
        }
        
        // Remove null items
        recommendations.removeIf(item -> item == null);
        return recommendations;
    }
    
    /**
     * Display detailed inventory information
     */
    public void displayDetailedInventory(ACreature creature) {
        System.out.println("\n=== " + creature.getName() + "'s Detailed Inventory ===");
        
        Map<String, Integer> inventory = creature.getInventoryItems();
        System.out.println("Inventory Size: " + creature.getInventorySize() + "/" + creature.getMaxInventorySize());
        
        if (inventory.isEmpty()) {
            System.out.println("Inventory is empty");
        } else {
            System.out.println("\nItems:");
            for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
                Item item = itemService.getItem(entry.getKey());
                if (item != null) {
                    System.out.println("  - " + item.getName() + " x" + entry.getValue());
                    System.out.println("    Description: " + item.getDescription());
                    System.out.println("    Value: " + item.getValue());
                    
                    if (item instanceof EquipmentItem) {
                        EquipmentItem equip = (EquipmentItem) item;
                        System.out.println("    Type: Equipment (" + equip.getSlot() + ")");
                        System.out.println("    Durability: " + equip.getDurability() + "/" + equip.getMaxDurability());
                    } else if (item instanceof ConsumableItem) {
                        ConsumableItem consumable = (ConsumableItem) item;
                        System.out.println("    Type: Consumable");
                        System.out.println("    Max Quantity: " + consumable.getMaxQuantity());
                    }
                    
                    if (!item.getAttributes().isEmpty()) {
                        System.out.println("    Attributes: " + item.getAttributes());
                    }
                    System.out.println();
                }
            }
        }
        
        // Display equipment
        System.out.println("Equipped Items:");
        if (creature.getEquipment().isEmpty()) {
            System.out.println("  No items equipped");
        } else {
            for (Item equip : creature.getEquipment()) {
                System.out.println("  - " + equip.getName() + " (" + equip.getId() + ")");
            }
        }
        
        // Display total stats
        Map<String, Integer> totalStats = getTotalCombatStats(creature);
        System.out.println("\nTotal Combat Stats:");
        System.out.println("  HP: " + totalStats.get("HP") + "/" + totalStats.get("MaxHP"));
        System.out.println("  MP: " + totalStats.get("MP") + "/" + totalStats.get("MaxMP"));
        System.out.println("  ATK: " + totalStats.get("ATK"));
        System.out.println("  DEF: " + totalStats.get("DEF"));
        System.out.println("  SPD: " + totalStats.get("SPD"));
    }
    
    /**
     * Display boss-specific information
     */
    public void displayBossInfo(Boss boss) {
        System.out.println("\n=== " + boss.getName() + " BOSS INFORMATION ===");
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
        
        // Special loot details
        if (!boss.getSpecialLoot().isEmpty()) {
            System.out.println("Special Loot Details:");
            for (String lootId : boss.getSpecialLoot()) {
                Item item = itemService.getItem(lootId);
                if (item != null) {
                    System.out.println("  [RARE] " + item.getName() + " - " + item.getDescription());
                }
            }
        }
    }
} 