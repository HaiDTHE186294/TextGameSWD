/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package creature.model;

import creature.observer.ICreatureObserver;
import creature.skill.ISkill;
import creature.state.ICreatureState;
import creature.stat.StatSet;
import item.model.Item;
import item.service.ItemService;
import java.lang.*;
import java.util.*;
import java.io.*;

/**
 * Abstract base class for all creatures in the game
 * @author Ha Duc Hau
 */
public abstract class ACreature {
    protected String id;
    protected String name;
    protected StatSet stats;
    protected List<ISkill> skills = new ArrayList<>();
    protected List<Item> equipment = new ArrayList<>();
    protected List<ICreatureState> states = new ArrayList<>();

    private List<ICreatureObserver> observers = new ArrayList<>();
    private ItemService itemService;

    // Observer pattern methods
    public void attach(ICreatureObserver obs) { 
        if (!observers.contains(obs)) {
            observers.add(obs); 
        }
    }
    
    public void detach(ICreatureObserver obs) { 
        observers.remove(obs); 
    }

    protected void notifyObservers(String type) {
        for (ICreatureObserver o : observers) {
            o.onCreatureChanged(this, type);
        }
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public StatSet getStats() { return stats; }
    public List<ISkill> getSkills() { return skills; }
    public List<Item> getEquipment() { return equipment; }
    public List<ICreatureState> getStates() { return states; }
    public ItemService getItemService() { return itemService; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setStats(StatSet stats) { this.stats = stats; }
    public void setItemService(ItemService itemService) { this.itemService = itemService; }

    // Combat methods
    public void takeDamage(int amount) {
        int oldHp = stats.getHp();
        stats.setHp(stats.getHp() - amount);
        notifyObservers("HPChanged");
        
        if (stats.getHp() <= 0 && oldHp > 0) {
            notifyObservers("Died");
        }
    }

    public void heal(int amount) {
        int oldHp = stats.getHp();
        stats.setHp(stats.getHp() + amount);
        notifyObservers("HPChanged");
        
        if (oldHp <= 0 && stats.getHp() > 0) {
            notifyObservers("Revived");
        } else if (stats.getHp() > oldHp) {
            notifyObservers("Healed");
        }
    }

    public void restoreMp(int amount) {
        stats.setMp(stats.getMp() + amount);
        notifyObservers("MPChanged");
    }

    // Item methods with ItemService integration
    public void equipItem(Item item) {
        equipment.add(item);
        notifyObservers("EquipmentChanged");
    }

    public void unequipItem(Item item) {
        equipment.remove(item);
        notifyObservers("EquipmentChanged");
    }

    public void useItem(Item item) {
        item.use();
        notifyObservers("ItemUsed");
    }

    public void addItem(Item item) {
        if (itemService != null && id != null) {
            itemService.addItemToInventory(id, item.getId(), 1);
        }
        notifyObservers("ItemAdded");
    }

    public void addItem(String itemId, int quantity) {
        if (itemService != null && id != null) {
            itemService.addItemToInventory(id, itemId, quantity);
        }
        notifyObservers("ItemAdded");
    }

    public void removeItem(Item item) {
        if (itemService != null && id != null) {
            itemService.removeItemFromInventory(id, item.getId(), 1);
        }
        notifyObservers("ItemRemoved");
    }

    public void removeItem(String itemId, int quantity) {
        if (itemService != null && id != null) {
            itemService.removeItemFromInventory(id, itemId, quantity);
        }
        notifyObservers("ItemRemoved");
    }

    // Inventory management methods
    public boolean hasItem(String itemId) {
        if (itemService != null && id != null) {
            return itemService.hasItem(id, itemId);
        }
        return false;
    }

    public int getItemQuantity(String itemId) {
        if (itemService != null && id != null) {
            return itemService.getItemQuantity(id, itemId);
        }
        return 0;
    }

    public Map<String, Integer> getInventoryItems() {
        if (itemService != null && id != null) {
            return itemService.getInventoryItems(id);
        }
        return new HashMap<>();
    }

    public int getInventorySize() {
        if (itemService != null && id != null) {
            return itemService.getInventorySize(id);
        }
        return 0;
    }

    public int getMaxInventorySize() {
        if (itemService != null && id != null) {
            return itemService.getMaxInventorySize(id);
        }
        return 0;
    }

    public Item getItemFromService(String itemId) {
        if (itemService != null) {
            return itemService.getItem(itemId);
        }
        return null;
    }

    // Skill methods
    public void learnSkill(ISkill skill) {
        skills.add(skill);
        notifyObservers("SkillLearned");
    }

    public void useSkill(ISkill skill, ACreature target) {
        if (skill.canUse(this)) {
            skill.use(this, target);
            notifyObservers("SkillUsed");
        }
    }

    public void upgradeSkill(ISkill skill) {
        // Skill upgrade logic would go here
        notifyObservers("SkillUpgraded");
    }

    // State methods
    public void addState(ICreatureState state) {
        state.applyEffect(this);
        states.add(state);
        notifyObservers("StateAdded");
    }

    public void removeState(ICreatureState state) {
        states.remove(state);
        notifyObservers("StateRemoved");
    }

    public void updateStates() {
        states.removeIf(state -> {
            state.updateState(this);
            return state.isExpired();
        });
    }

    public void updateSkillCooldowns() {
        for (ISkill skill : skills) {
            skill.updateCooldown();
        }
    }

    // Utility methods
    public boolean isAlive() {
        return stats.isAlive();
    }

    public boolean hasSkill(String skillId) {
        return skills.stream().anyMatch(skill -> skill.getId().equals(skillId));
    }

    public ISkill getSkill(String skillId) {
        return skills.stream()
                .filter(skill -> skill.getId().equals(skillId))
                .findFirst()
                .orElse(null);
    }

    public boolean hasEquipment(String itemId) {
        return equipment.stream().anyMatch(item -> item.getId().equals(itemId));
    }

    public Item getEquipment(String itemId) {
        return equipment.stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElse(null);
    }

    public abstract void act(); // For AI or player override
}