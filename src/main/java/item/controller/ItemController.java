package item.controller;

import item.model.Item;
import item.service.ItemService;
import java.util.List;
import java.util.Map;

public class ItemController {
    private final ItemService service;

    public ItemController(ItemService service) {
        this.service = service;
    }

    public List<Item> getAllItems() {
        return service.getAllItems();
    }

    public Item getItem(String itemId) {
        return service.getItem(itemId);
    }

    public boolean addItemToInventory(String ownerId, String itemId, int quantity) {
        return service.addItemToInventory(ownerId, itemId, quantity);
    }

    public boolean removeItemFromInventory(String ownerId, String itemId, int quantity) {
        return service.removeItemFromInventory(ownerId, itemId, quantity);
    }

    public int getItemQuantity(String ownerId, String itemId) {
        return service.getItemQuantity(ownerId, itemId);
    }

    public boolean hasItem(String ownerId, String itemId) {
        return service.hasItem(ownerId, itemId);
    }

    public Map<String, Integer> getInventoryItems(String ownerId) {
        return service.getInventoryItems(ownerId);
    }

    public int getInventorySize(String ownerId) {
        return service.getInventorySize(ownerId);
    }

    public int getMaxInventorySize(String ownerId) {
        return service.getMaxInventorySize(ownerId);
    }
}