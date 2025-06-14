package item.service;

import item.model.Item;
import item.model.Inventory;
import java.util.concurrent.CopyOnWriteArrayList;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import item.service.ItemLoader;



public class ItemService {
    private Map<String, Item> items;
    private Map<String, Inventory> inventories;
    private final java.util.List<InventoryObserver> observers = new CopyOnWriteArrayList<>();

    // Observer methods
    public void addObserver(InventoryObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(InventoryObserver observer) {
        observers.remove(observer);
    }

    private void notifyInventoryChanged(String ownerId) {
        for (InventoryObserver observer : observers) {
            observer.onInventoryChanged(ownerId);
        }
    }

    public ItemService() {
        this.items = new HashMap<>();
        this.inventories = new HashMap<>();
    }

    public void registerItem(Item item) {
        items.put(item.getId(), item);
    }

    public Item getItem(String itemId) {
        return items.get(itemId);
    }

    public void createInventory(String ownerId, int maxSize) {
        inventories.put(ownerId, new Inventory(ownerId, maxSize));
    }

    public Inventory getInventory(String ownerId) {
        return inventories.get(ownerId);
    }

    public boolean addItemToInventory(String ownerId, String itemId, int quantity) {
        if (!items.containsKey(itemId)) {
            throw new IllegalArgumentException("Item không tồn tại: " + itemId);
        }
        Inventory inventory = inventories.computeIfAbsent(ownerId, k -> new Inventory(ownerId, 20));
        inventory.addItem(itemId, quantity);
        try {
            ItemLoader.saveInventory(getAllInventories());
        } catch (IOException e) {
            e.printStackTrace();
        }
        notifyInventoryChanged(ownerId); // thêm dòng này
        return true;
    }

    public boolean removeItemFromInventory(String ownerId, String itemId, int quantity) {
        Inventory inventory = inventories.get(ownerId);
        if (inventory == null) {
            throw new IllegalArgumentException("Inventory không tồn tại cho owner: " + ownerId);
        }
        inventory.removeItem(itemId, quantity);
        try {
            ItemLoader.saveInventory(getAllInventories());
        } catch (IOException e) {
            e.printStackTrace();
        }
        notifyInventoryChanged(ownerId); // thêm dòng này
        return true;
    }

    public int getItemQuantity(String ownerId, String itemId) {
        Inventory inventory = inventories.get(ownerId);
        if (inventory == null) return 0;
        return inventory.getItems().getOrDefault(itemId, 0);
    }

    public boolean hasItem(String ownerId, String itemId) {
        Inventory inventory = inventories.get(ownerId);
        if (inventory == null) return false;
        return inventory.getItems().containsKey(itemId);
    }

    public List<Item> getAllItems() {
        return new ArrayList<>(items.values());
    }

    public Map<String, Integer> getInventoryItems(String ownerId) {
        Inventory inventory = inventories.get(ownerId);
        if (inventory == null) {
            return new HashMap<>();
        }
        return inventory.getItems();
    }

    public int getInventorySize(String ownerId) {
        Inventory inventory = inventories.get(ownerId);
        if (inventory == null) return 0;
        return inventory.getItems().size();
    }

    public int getMaxInventorySize(String ownerId) {
        Inventory inventory = inventories.get(ownerId);
        if (inventory == null) {
            return 0;
        }
        return inventory.getMaxSize();
    }

    public Map<String, Inventory> getAllInventories() {
        return new HashMap<>(inventories);
    }

    public Map<String, Integer> getItemAttributes(String itemId) {
        Item item = items.get(itemId);
        if (item == null) return new HashMap<>();
        return item.getAttributes();
    }
}