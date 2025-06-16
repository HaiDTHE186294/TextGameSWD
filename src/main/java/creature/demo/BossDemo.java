package creature.demo;

import creature.model.ACreature;
import creature.model.Player;
import creature.model.Boss;
import creature.service.CreatureFactory;
import creature.service.CreatureItemManager;
import creature.observer.BossObserver;
import creature.skill.ISkill;
import item.service.ItemService;
import item.model.Item;
import item.model.ConsumableItem;
import item.model.EquipmentItem;
import item.model.EquipmentSlot;
import java.util.Map;
import java.util.HashMap;

/**
 * Demo class to test boss functionality
 * @author Ha Duc Hau
 */
public class BossDemo {
    
    public static void main(String[] args) {
        System.out.println("=== BOSS DEMO ===\n");
        
        try {
            // Initialize ItemService with basic items
            ItemService itemService = new ItemService();
            setupBasicItems(itemService);
            
            // Set ItemService in CreatureFactory
            CreatureFactory.setItemService(itemService);
            
            // Create CreatureItemManager
            CreatureItemManager itemManager = new CreatureItemManager(itemService);
            
            // Create a player
            Player player = CreatureFactory.createWarrior("Hero");
            System.out.println("Created player: " + player.getName());
            System.out.println("Initial stats: " + player.getStats() + "\n");
            
            // Create different bosses
            Boss goblinKing = CreatureFactory.createGoblinKing();
            Boss dragonLord = CreatureFactory.createDragonLord();
            Boss darkWizard = CreatureFactory.createDarkWizard();
            
            System.out.println("=== BOSS CREATION ===");
            System.out.println("Created bosses:");
            System.out.println("1. " + goblinKing.getName() + " (Phase " + goblinKing.getCurrentPhase() + "/" + goblinKing.getMaxPhases() + ")");
            System.out.println("2. " + dragonLord.getName() + " (Phase " + dragonLord.getCurrentPhase() + "/" + dragonLord.getMaxPhases() + ")");
            System.out.println("3. " + darkWizard.getName() + " (Phase " + darkWizard.getCurrentPhase() + "/" + darkWizard.getMaxPhases() + ")\n");
            
            // Test Goblin King boss
            System.out.println("=== TESTING GOBLIN KING BOSS ===");
            testBoss(goblinKing, player, itemManager);
            
            // Test Dragon Lord boss
            System.out.println("\n=== TESTING DRAGON LORD BOSS ===");
            testBoss(dragonLord, player, itemManager);
            
            // Test Dark Wizard boss
            System.out.println("\n=== TESTING DARK WIZARD BOSS ===");
            testBoss(darkWizard, player, itemManager);
            
            // Final inventory display
            System.out.println("\n=== FINAL PLAYER INVENTORY ===");
            itemManager.displayDetailedInventory(player);
            
        } catch (Exception e) {
            System.err.println("Error during boss demo: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void setupBasicItems(ItemService itemService) {
        Map<String, Object> emptyProps = new HashMap<>();

        Map<String, Integer> swordAttrs = new HashMap<>();
        swordAttrs.put("ATK", 10);
        EquipmentItem sword = new EquipmentItem("sword_1", "Iron Sword", "A basic iron sword", 50, 100, EquipmentSlot.WEAPON, swordAttrs, emptyProps);
        itemService.registerItem(sword);

        Map<String, Integer> shieldAttrs = new HashMap<>();
        shieldAttrs.put("DEF", 5);
        EquipmentItem shield = new EquipmentItem("shield_1", "Wooden Shield", "A basic wooden shield", 30, 80, EquipmentSlot.SHIELD, shieldAttrs, emptyProps);
        itemService.registerItem(shield);

        Map<String, Integer> healthPotionAttrs = new HashMap<>();
        healthPotionAttrs.put("HP", 30);
        ConsumableItem healthPotion = new ConsumableItem("potion_1", "Health Potion", "Restores 30 HP", 20, 10, healthPotionAttrs, emptyProps, null);
        itemService.registerItem(healthPotion);

        Map<String, Integer> manaPotionAttrs = new HashMap<>();
        manaPotionAttrs.put("MP", 20);
        ConsumableItem manaPotion = new ConsumableItem("potion_2", "Mana Potion", "Restores 20 MP", 25, 10, manaPotionAttrs, emptyProps, null);
        itemService.registerItem(manaPotion);

        // Special boss loot items
        Map<String, Integer> crownAttrs = new HashMap<>();
        crownAttrs.put("ATK", 5);
        crownAttrs.put("DEF", 3);
        EquipmentItem crown = new EquipmentItem("crown_1", "Royal Crown", "A royal crown with magical properties", 200, 60, EquipmentSlot.HEAD, crownAttrs, emptyProps);
        itemService.registerItem(crown);

        Map<String, Integer> royalSwordAttrs = new HashMap<>();
        royalSwordAttrs.put("ATK", 25);
        EquipmentItem royalSword = new EquipmentItem("royal_sword_1", "Royal Sword", "A legendary royal sword", 500, 120, EquipmentSlot.WEAPON, royalSwordAttrs, emptyProps);
        itemService.registerItem(royalSword);

        Map<String, Integer> dragonScalesAttrs = new HashMap<>();
        dragonScalesAttrs.put("DEF", 15);
        EquipmentItem dragonScales = new EquipmentItem("dragon_scales_1", "Dragon Scales", "Armor made from dragon scales", 800, 150, EquipmentSlot.CHEST, dragonScalesAttrs, emptyProps);
        itemService.registerItem(dragonScales);

        Map<String, Integer> legendarySwordAttrs = new HashMap<>();
        legendarySwordAttrs.put("ATK", 50);
        EquipmentItem legendarySword = new EquipmentItem("legendary_sword_1", "Legendary Sword", "A sword of legend", 1000, 200, EquipmentSlot.WEAPON, legendarySwordAttrs, emptyProps);
        itemService.registerItem(legendarySword);

        Map<String, Integer> dragonHeartAttrs = new HashMap<>();
        dragonHeartAttrs.put("HP", 100);
        dragonHeartAttrs.put("MP", 50);
        ConsumableItem dragonHeart = new ConsumableItem("dragon_heart_1", "Dragon Heart", "A powerful magical item", 500, 1, dragonHeartAttrs, emptyProps, null);
        itemService.registerItem(dragonHeart);

        Map<String, Integer> darkStaffAttrs = new HashMap<>();
        darkStaffAttrs.put("ATK", 20);
        darkStaffAttrs.put("MP", 30);
        EquipmentItem darkStaff = new EquipmentItem("dark_staff_1", "Dark Staff", "A staff of dark magic", 400, 90, EquipmentSlot.WEAPON, darkStaffAttrs, emptyProps);
        itemService.registerItem(darkStaff);

        Map<String, Integer> magicRobeAttrs = new HashMap<>();
        magicRobeAttrs.put("DEF", 8);
        magicRobeAttrs.put("MP", 20);
        EquipmentItem magicRobe = new EquipmentItem("magic_robe_1", "Magic Robe", "A robe enchanted with magic", 300, 70, EquipmentSlot.CHEST, magicRobeAttrs, emptyProps);
        itemService.registerItem(magicRobe);

        Map<String, Integer> spellBookAttrs = new HashMap<>();
        spellBookAttrs.put("MP", 40);
        ConsumableItem spellBook = new ConsumableItem("spell_book_1", "Spell Book", "A book of powerful spells", 600, 1, spellBookAttrs, emptyProps, null);
        itemService.registerItem(spellBook);

        Map<String, Integer> golemCoreAttrs = new HashMap<>();
        golemCoreAttrs.put("DEF", 20);
        EquipmentItem golemCore = new EquipmentItem("golem_core_1", "Golem Core", "The core of an ancient golem", 700, 100, EquipmentSlot.CHEST, golemCoreAttrs, emptyProps);
        itemService.registerItem(golemCore);

        Map<String, Integer> ancientArmorAttrs = new HashMap<>();
        ancientArmorAttrs.put("DEF", 25);
        EquipmentItem ancientArmor = new EquipmentItem("ancient_armor_1", "Ancient Armor", "Armor from ancient times", 900, 180, EquipmentSlot.CHEST, ancientArmorAttrs, emptyProps);
        itemService.registerItem(ancientArmor);
    }
    
    private static void testBoss(Boss boss, Player player, CreatureItemManager itemManager) {
        System.out.println("Testing boss: " + boss.getName());
        System.out.println("Initial HP: " + boss.getStats().getHp() + "/" + boss.getStats().getMaxHp());
        System.out.println("Enrage threshold: " + boss.getEnrageThreshold() + "%");
        
        // Display boss info
        itemManager.displayBossInfo(boss);
        
        // Simulate combat
        System.out.println("\n--- COMBAT SIMULATION ---");
        
        // Phase 1 combat
        System.out.println("Phase 1 combat:");
        simulateCombat(boss, player, 40); // Deal 40% damage
        
        // Phase 2 combat
        if (boss.getCurrentPhase() >= 2) {
            System.out.println("\nPhase 2 combat:");
            simulateCombat(boss, player, 30); // Deal 30% more damage
        }
        
        // Phase 3 combat (if applicable)
        if (boss.getCurrentPhase() >= 3) {
            System.out.println("\nPhase 3 combat:");
            simulateCombat(boss, player, 25); // Deal 25% more damage
        }
        
        // Final blow to defeat boss
        System.out.println("\nFinal blow:");
        int finalDamage = boss.getStats().getHp();
        boss.takeDamage(finalDamage);
        
        // Give loot
        System.out.println("\n--- LOOT DISTRIBUTION ---");
        itemManager.giveLootFromBoss(boss, player);
        
        // Display boss status after defeat
        BossObserver bossObserver = new BossObserver();
        bossObserver.displayBossStatus(boss);
    }
    
    private static void simulateCombat(Boss boss, Player player, int damagePercent) {
        int maxHp = boss.getStats().getMaxHp();
        int damage = (int) (maxHp * damagePercent / 100.0);
        
        System.out.println("Dealing " + damage + " damage to " + boss.getName());
        boss.takeDamage(damage);
        
        // Boss acts
        boss.act();
        
        // Use some skills
        for (ISkill skill : boss.getSkills()) {
            if (skill.getMpCost() <= boss.getStats().getMp()) {
                System.out.println(boss.getName() + " uses " + skill.getName());
                boss.useSkill(skill, player);
                break; // Use one skill per turn
            }
        }
        
        // Display current status
        System.out.println("Current HP: " + boss.getStats().getHp() + "/" + boss.getStats().getMaxHp() + 
                          " (" + String.format("%.1f", boss.getHealthPercentage()) + "%)");
        System.out.println("Current Phase: " + boss.getCurrentPhase() + "/" + boss.getMaxPhases());
        System.out.println("Enraged: " + boss.isEnraged());
    }
} 