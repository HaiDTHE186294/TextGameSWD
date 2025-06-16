/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package creature.service;

import creature.model.ACreature;
import creature.model.Player;
import creature.model.Enemy;
import creature.model.Boss;
import creature.observer.HealthObserver;
import creature.observer.ItemObserver;
import creature.observer.SkillObserver;
import creature.observer.InventoryObserver;
import creature.observer.BossObserver;
import creature.stat.StatSet;
import creature.skill.AttackSkill;
import item.service.ItemService;
import java.lang.*;
import java.util.*;
import java.io.*;

/**
 * Factory for creating creatures with proper observer setup
 * @author Ha Duc Hau
 */
public class CreatureFactory {
    
    private static ItemService itemService;
    
    public static void setItemService(ItemService service) {
        itemService = service;
    }
    
    public static Player createDefaultPlayer(String name) {
        Player player = new Player(name, "Adventurer");
        player.setStats(new StatSet(100, 50, 10, 5, 5, 1, 0));
        player.setId("player_" + name.toLowerCase().replace(" ", "_"));
        
        // Set up ItemService
        if (itemService != null) {
            player.setItemService(itemService);
            itemService.createInventory(player.getId(), 50);
        }
        
        // Attach observers
        player.attach(new HealthObserver());
        player.attach(new ItemObserver());
        player.attach(new SkillObserver());
        if (itemService != null) {
            player.attach(new InventoryObserver(itemService));
        }
        
        // Add basic skills
        AttackSkill basicAttack = new AttackSkill("basic_attack", "Basic Attack", "A simple attack", 0, 0, 1.0);
        player.learnSkill(basicAttack);
        
        return player;
    }

    public static Player createWarrior(String name) {
        Player warrior = new Player(name, "Warrior");
        warrior.setStats(new StatSet(120, 30, 15, 8, 3, 1, 0));
        warrior.setId("warrior_" + name.toLowerCase().replace(" ", "_"));
        
        // Set up ItemService
        if (itemService != null) {
            warrior.setItemService(itemService);
            itemService.createInventory(warrior.getId(), 40);
        }
        
        // Attach observers
        warrior.attach(new HealthObserver());
        warrior.attach(new ItemObserver());
        warrior.attach(new SkillObserver());
        if (itemService != null) {
            warrior.attach(new InventoryObserver(itemService));
        }
        
        // Add warrior skills
        AttackSkill slash = new AttackSkill("slash", "Slash", "A powerful sword slash", 5, 2, 1.5);
        warrior.learnSkill(slash);
        
        return warrior;
    }

    public static Player createMage(String name) {
        Player mage = new Player(name, "Mage");
        mage.setStats(new StatSet(80, 80, 8, 3, 7, 1, 0));
        mage.setId("mage_" + name.toLowerCase().replace(" ", "_"));
        
        // Set up ItemService
        if (itemService != null) {
            mage.setItemService(itemService);
            itemService.createInventory(mage.getId(), 60);
        }
        
        // Attach observers
        mage.attach(new HealthObserver());
        mage.attach(new ItemObserver());
        mage.attach(new SkillObserver());
        if (itemService != null) {
            mage.attach(new InventoryObserver(itemService));
        }
        
        // Add mage skills
        AttackSkill fireball = new AttackSkill("fireball", "Fireball", "A magical fire attack", 15, 3, 2.0);
        mage.learnSkill(fireball);
        
        return mage;
    }

    public static Enemy createGoblin() {
        Enemy goblin = new Enemy("Goblin", "Humanoid", 15);
        goblin.setStats(new StatSet(60, 20, 8, 3, 6, 1, 0));
        goblin.setId("goblin_1");
        
        // Set up ItemService
        if (itemService != null) {
            goblin.setItemService(itemService);
            itemService.createInventory(goblin.getId(), 10);
        }
        
        // Attach observers
        goblin.attach(new HealthObserver());
        goblin.attach(new ItemObserver());
        goblin.attach(new SkillObserver());
        if (itemService != null) {
            goblin.attach(new InventoryObserver(itemService));
        }
        
        // Add goblin skills
        AttackSkill goblinAttack = new AttackSkill("goblin_attack", "Goblin Attack", "A quick goblin attack", 0, 1, 0.8);
        goblin.learnSkill(goblinAttack);
        
        // Add loot
        goblin.addLoot("sword_1");
        goblin.addLoot("potion_1");
        
        return goblin;
    }

    public static Enemy createOrc() {
        Enemy orc = new Enemy("Orc", "Humanoid", 25);
        orc.setStats(new StatSet(100, 30, 12, 6, 4, 2, 0));
        orc.setId("orc_1");
        
        // Set up ItemService
        if (itemService != null) {
            orc.setItemService(itemService);
            itemService.createInventory(orc.getId(), 15);
        }
        
        // Attach observers
        orc.attach(new HealthObserver());
        orc.attach(new ItemObserver());
        orc.attach(new SkillObserver());
        if (itemService != null) {
            orc.attach(new InventoryObserver(itemService));
        }
        
        // Add orc skills
        AttackSkill orcSmash = new AttackSkill("orc_smash", "Orc Smash", "A powerful orc attack", 5, 2, 1.3);
        orc.learnSkill(orcSmash);
        
        // Add loot
        orc.addLoot("shield_1");
        orc.addLoot("potion_2");
        
        return orc;
    }

    public static Enemy createDragon() {
        Enemy dragon = new Enemy("Dragon", "Beast", 100);
        dragon.setStats(new StatSet(200, 80, 20, 12, 8, 5, 0));
        dragon.setId("dragon_1");
        
        // Set up ItemService
        if (itemService != null) {
            dragon.setItemService(itemService);
            itemService.createInventory(dragon.getId(), 30);
        }
        
        // Attach observers
        dragon.attach(new HealthObserver());
        dragon.attach(new ItemObserver());
        dragon.attach(new SkillObserver());
        if (itemService != null) {
            dragon.attach(new InventoryObserver(itemService));
        }
        
        // Add dragon skills
        AttackSkill dragonBreath = new AttackSkill("dragon_breath", "Dragon Breath", "A devastating fire breath", 20, 4, 2.5);
        dragon.learnSkill(dragonBreath);
        
        // Add loot
        dragon.addLoot("sword_1");
        dragon.addLoot("shield_1");
        dragon.addLoot("potion_1");
        dragon.addLoot("potion_2");
        
        return dragon;
    }

    // Boss creation methods
    public static Boss createGoblinKing() {
        Boss goblinKing = new Boss("Goblin King", "Humanoid", 50, 2);
        goblinKing.setStats(new StatSet(150, 40, 15, 8, 7, 3, 0));
        goblinKing.setId("goblin_king_1");
        goblinKing.setEnrageThreshold(30);
        
        // Set up ItemService
        if (itemService != null) {
            goblinKing.setItemService(itemService);
            itemService.createInventory(goblinKing.getId(), 20);
        }
        
        // Attach observers
        goblinKing.attach(new HealthObserver());
        goblinKing.attach(new ItemObserver());
        goblinKing.attach(new SkillObserver());
        goblinKing.attach(new BossObserver());
        if (itemService != null) {
            goblinKing.attach(new InventoryObserver(itemService));
        }
        
        // Add boss skills
        AttackSkill royalStrike = new AttackSkill("royal_strike", "Royal Strike", "A powerful royal attack", 10, 3, 2.0);
        goblinKing.learnSkill(royalStrike);
        
        // Add loot
        goblinKing.addLoot("sword_1");
        goblinKing.addLoot("potion_1");
        goblinKing.addSpecialLoot("crown_1");
        goblinKing.addSpecialLoot("royal_sword_1");
        
        return goblinKing;
    }

    public static Boss createDragonLord() {
        Boss dragonLord = new Boss("Dragon Lord", "Beast", 200, 3);
        dragonLord.setStats(new StatSet(300, 100, 25, 15, 10, 8, 0));
        dragonLord.setId("dragon_lord_1");
        dragonLord.setEnrageThreshold(25);
        
        // Set up ItemService
        if (itemService != null) {
            dragonLord.setItemService(itemService);
            itemService.createInventory(dragonLord.getId(), 50);
        }
        
        // Attach observers
        dragonLord.attach(new HealthObserver());
        dragonLord.attach(new ItemObserver());
        dragonLord.attach(new SkillObserver());
        dragonLord.attach(new BossObserver());
        if (itemService != null) {
            dragonLord.attach(new InventoryObserver(itemService));
        }
        
        // Add boss skills
        AttackSkill infernoBreath = new AttackSkill("inferno_breath", "Inferno Breath", "A devastating inferno attack", 25, 4, 3.0);
        dragonLord.learnSkill(infernoBreath);
        
        // Add loot
        dragonLord.addLoot("sword_1");
        dragonLord.addLoot("shield_1");
        dragonLord.addLoot("potion_1");
        dragonLord.addLoot("potion_2");
        dragonLord.addSpecialLoot("dragon_scales_1");
        dragonLord.addSpecialLoot("legendary_sword_1");
        dragonLord.addSpecialLoot("dragon_heart_1");
        
        return dragonLord;
    }

    public static Boss createDarkWizard() {
        Boss darkWizard = new Boss("Dark Wizard", "Humanoid", 150, 3);
        darkWizard.setStats(new StatSet(120, 150, 12, 5, 8, 6, 0));
        darkWizard.setId("dark_wizard_1");
        darkWizard.setEnrageThreshold(20);
        
        // Set up ItemService
        if (itemService != null) {
            darkWizard.setItemService(itemService);
            itemService.createInventory(darkWizard.getId(), 40);
        }
        
        // Attach observers
        darkWizard.attach(new HealthObserver());
        darkWizard.attach(new ItemObserver());
        darkWizard.attach(new SkillObserver());
        darkWizard.attach(new BossObserver());
        if (itemService != null) {
            darkWizard.attach(new InventoryObserver(itemService));
        }
        
        // Add boss skills
        AttackSkill darkMagic = new AttackSkill("dark_magic", "Dark Magic", "A powerful dark spell", 20, 3, 2.5);
        darkWizard.learnSkill(darkMagic);
        
        // Add loot
        darkWizard.addLoot("potion_2");
        darkWizard.addSpecialLoot("dark_staff_1");
        darkWizard.addSpecialLoot("magic_robe_1");
        darkWizard.addSpecialLoot("spell_book_1");
        
        return darkWizard;
    }

    public static Boss createAncientGolem() {
        Boss ancientGolem = new Boss("Ancient Golem", "Construct", 120, 2);
        ancientGolem.setStats(new StatSet(400, 20, 18, 20, 3, 7, 0));
        ancientGolem.setId("ancient_golem_1");
        ancientGolem.setEnrageThreshold(40);
        
        // Set up ItemService
        if (itemService != null) {
            ancientGolem.setItemService(itemService);
            itemService.createInventory(ancientGolem.getId(), 25);
        }
        
        // Attach observers
        ancientGolem.attach(new HealthObserver());
        ancientGolem.attach(new ItemObserver());
        ancientGolem.attach(new SkillObserver());
        ancientGolem.attach(new BossObserver());
        if (itemService != null) {
            ancientGolem.attach(new InventoryObserver(itemService));
        }
        
        // Add boss skills
        AttackSkill earthShatter = new AttackSkill("earth_shatter", "Earth Shatter", "A devastating ground attack", 15, 3, 2.2);
        ancientGolem.learnSkill(earthShatter);
        
        // Add loot
        ancientGolem.addLoot("shield_1");
        ancientGolem.addSpecialLoot("golem_core_1");
        ancientGolem.addSpecialLoot("ancient_armor_1");
        
        return ancientGolem;
    }
}
