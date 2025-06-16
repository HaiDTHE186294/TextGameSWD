package creature.observer;

import creature.model.ACreature;
import item.service.ItemService;
import item.model.Item;
import java.lang.*;
import java.util.*;
import java.io.*;

/**
 * Observer for inventory-related changes in creatures
 * @author Ha Duc Hau
 */
public class InventoryObserver implements ICreatureObserver {
    
    private ItemService itemService;
    
    public InventoryObserver(ItemService itemService) {
        this.itemService = itemService;
    }
    
    @Override
    public void onCreatureChanged(ACreature creature, String changeType) {
        switch (changeType) {
            case "ItemAdded":
                handleItemAdded(creature);
                break;
            case "ItemRemoved":
                handleItemRemoved(creature);
                break;
            case "ItemUsed":
                handleItemUsed(creature);
                break;
            case "EquipmentChanged":
                handleEquipmentChanged(creature);
                break;
            default:
                // Ignore other change types
                break;
        }
    }
    
    private void handleItemAdded(ACreature creature) {
        System.out.println("[InventoryObserver] " + creature.getName() + " received a new item!");
        displayInventoryStatus(creature);
    }
    
    private void handleItemRemoved(ACreature creature) {
        System.out.println("[InventoryObserver] " + creature.getName() + " lost an item!");
        displayInventoryStatus(creature);
    }
    
    private void handleItemUsed(ACreature creature) {
        System.out.println("[InventoryObserver] " + creature.getName() + " used an item!");
        displayInventoryStatus(creature);
    }
    
    private void handleEquipmentChanged(ACreature creature) {
        System.out.println("[InventoryObserver] " + creature.getName() + " changed equipment!");
        System.out.println("Current equipment count: " + creature.getEquipment().size());
        
        // Log equipment details
        creature.getEquipment().forEach(item -> 
            System.out.println("  - " + item.getName() + " (ID: " + item.getId() + ")")
        );
        
        displayInventoryStatus(creature);
    }
    
    private void displayInventoryStatus(ACreature creature) {
        String ownerId = creature.getId();
        if (ownerId == null) return;
        
        Map<String, Integer> inventoryItems = itemService.getInventoryItems(ownerId);
        int inventorySize = itemService.getInventorySize(ownerId);
        int maxSize = itemService.getMaxInventorySize(ownerId);
        
        System.out.println("  Inventory Status: " + inventorySize + "/" + maxSize + " items");
        
        if (!inventoryItems.isEmpty()) {
            System.out.println("  Items in inventory:");
            for (Map.Entry<String, Integer> entry : inventoryItems.entrySet()) {
                Item item = itemService.getItem(entry.getKey());
                if (item != null) {
                    System.out.println("    - " + item.getName() + " x" + entry.getValue());
                }
            }
        }
    }
    
    public ItemService getItemService() {
        return itemService;
    }
} 