package item.model;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private final String ownerId;
    private final int maxSize;
    private final Map<String, Integer> items;

    public Inventory(String ownerId, int maxSize) {
        this.ownerId = ownerId;
        this.maxSize = maxSize;
        this.items = new HashMap<>();
    }

    public String getOwnerId() {
        return ownerId;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public Map<String, Integer> getItems() {
        return new HashMap<>(items);
    }

    public void addItem(String itemId, int quantity) {
        if (items.size() >= maxSize && !items.containsKey(itemId)) {
            throw new IllegalArgumentException("Inventory đã đầy");
        }
        items.merge(itemId, quantity, Integer::sum);
    }

    public void removeItem(String itemId, int quantity) {
        if (!items.containsKey(itemId)) {
            throw new IllegalArgumentException("Item không tồn tại trong inventory: " + itemId);
        }
        int currentQuantity = items.get(itemId);
        if (currentQuantity < quantity) {
            throw new IllegalArgumentException("Không đủ số lượng item để xóa");
        }
        if (currentQuantity == quantity) {
            items.remove(itemId);
        } else {
            items.put(itemId, currentQuantity - quantity);
        }
    }
}