package item.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import item.model.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ItemLoader {
    private static final String ITEMS_FILE = "src/main/resources/items.json";
    private static final String INVENTORY_FILE = "src/main/resources/inventory.json";
    private static final ObjectMapper mapper = new ObjectMapper();

    public static Map<String, Item> loadItems() throws IOException {
        Map<String, Object> jsonData = mapper.readValue(new File(ITEMS_FILE), new TypeReference<Map<String, Object>>() {});
        Map<String, Item> items = new HashMap<>();

        // Load equipment items
        List<Map<String, Object>> equipment = (List<Map<String, Object>>) jsonData.get("equipment");
        for (Map<String, Object> itemData : equipment) {
            Map<String, Integer> attributes = new HashMap<>();
            Map<String, Object> extraProperties = new HashMap<>();
            if (itemData.containsKey("attributes")) {
                Map<String, Object> attrObj = (Map<String, Object>) itemData.get("attributes");
                for (Map.Entry<String, Object> attr : attrObj.entrySet()) {
                    attributes.put(attr.getKey(), ((Number) attr.getValue()).intValue());
                }
            }
            // Lấy các trường động
            for (Map.Entry<String, Object> entry : itemData.entrySet()) {
                String key = entry.getKey();
                if (!key.equals("id") && !key.equals("name") && !key.equals("description") && !key.equals("value") && !key.equals("maxDurability") && !key.equals("slot") && !key.equals("attributes")) {
                    extraProperties.put(key, entry.getValue());
                }
            }
            EquipmentItem item = new EquipmentItem(
                    (String) itemData.get("id"),
                    (String) itemData.get("name"),
                    (String) itemData.get("description"),
                    ((Number) itemData.get("value")).intValue(),
                    ((Number) itemData.get("maxDurability")).intValue(),
                    EquipmentSlot.valueOf((String) itemData.get("slot")),
                    attributes,
                    extraProperties
            );
            items.put(item.getId(), item);
        }

        // Load consumable items
        List<Map<String, Object>> consumables = (List<Map<String, Object>>) jsonData.get("consumables");
        for (Map<String, Object> itemData : consumables) {
            Map<String, Integer> attributes = new HashMap<>();
            Map<String, Object> extraProperties = new HashMap<>();
            if (itemData.containsKey("attributes")) {
                Map<String, Object> attrObj = (Map<String, Object>) itemData.get("attributes");
                for (Map.Entry<String, Object> attr : attrObj.entrySet()) {
                    attributes.put(attr.getKey(), ((Number) attr.getValue()).intValue());
                }
            }
            // Lấy các trường động
            for (Map.Entry<String, Object> entry : itemData.entrySet()) {
                String key = entry.getKey();
                if (!key.equals("id") && !key.equals("name") && !key.equals("description") && !key.equals("value") && !key.equals("maxQuantity") && !key.equals("attributes")) {
                    extraProperties.put(key, entry.getValue());
                }
            }
            ConsumableItem item = new ConsumableItem(
                    (String) itemData.get("id"),
                    (String) itemData.get("name"),
                    (String) itemData.get("description"),
                    ((Number) itemData.get("value")).intValue(),
                    ((Number) itemData.get("maxQuantity")).intValue(),
                    attributes,
                    extraProperties,
                    null
            );
            items.put(item.getId(), item);
        }

        // Load quest items
        List<Map<String, Object>> questItems = (List<Map<String, Object>>) jsonData.get("quest");
        for (Map<String, Object> itemData : questItems) {
            Map<String, Object> extraProperties = new HashMap<>();
            for (Map.Entry<String, Object> entry : itemData.entrySet()) {
                String key = entry.getKey();
                if (!key.equals("id") && !key.equals("name") && !key.equals("description") && !key.equals("value") && !key.equals("questId")) {
                    extraProperties.put(key, entry.getValue());
                }
            }
            QuestItem item = new QuestItem(
                    (String) itemData.get("id"),
                    (String) itemData.get("name"),
                    (String) itemData.get("description"),
                    ((Number) itemData.get("value")).intValue(),
                    (String) itemData.get("questId"),
                    extraProperties
            );
            items.put(item.getId(), item);
        }

        return items;
    }

    public static Map<String, Map<String, Object>> loadInventories() throws IOException {
        Map<String, Object> jsonData = mapper.readValue(new File(INVENTORY_FILE), new TypeReference<Map<String, Object>>() {});
        return (Map<String, Map<String, Object>>) jsonData.get("inventories");
    }

    public static void loadInventoryData(ItemService itemService) throws IOException {
        Map<String, Map<String, Object>> inventoriesData = loadInventories();
        for (Map.Entry<String, Map<String, Object>> entry : inventoriesData.entrySet()) {
            String ownerId = entry.getKey();
            Map<String, Object> inventoryData = entry.getValue();
            int maxSize = ((Number) inventoryData.get("maxSize")).intValue();
            itemService.createInventory(ownerId, maxSize);
            Map<String, Object> items = (Map<String, Object>) inventoryData.get("items");
            for (Map.Entry<String, Object> itemEntry : items.entrySet()) {
                String itemId = itemEntry.getKey();
                Object value = itemEntry.getValue();
                int quantity = 0;
                if (value instanceof Integer) {
                    quantity = (Integer) value;
                } else if (value instanceof Map) {
                    Map<String, Object> itemObj = (Map<String, Object>) value;
                    quantity = ((Number) itemObj.get("quantity")).intValue();
                    // Xử lý thêm các thuộc tính khác nếu cần
                } else {
                    continue;
                }
                itemService.addItemToInventory(ownerId, itemId, quantity);
                // Nếu cần xử lý durability hoặc trạng thái khác, thêm ở đây
            }
        }
    }

    public static void saveInventory(Map<String, Inventory> inventories) throws IOException {
        ObjectNode root = mapper.createObjectNode();
        ObjectNode inventoriesNode = mapper.createObjectNode();
        for (Map.Entry<String, Inventory> entry : inventories.entrySet()) {
            String ownerId = entry.getKey();
            Inventory inv = entry.getValue();
            ObjectNode invNode = mapper.createObjectNode();
            invNode.put("maxSize", inv.getMaxSize());
            ObjectNode itemsNode = mapper.createObjectNode();
            for (Map.Entry<String, Integer> itemEntry : inv.getItems().entrySet()) {
                itemsNode.put(itemEntry.getKey(), itemEntry.getValue());
            }
            invNode.set("items", itemsNode);
            inventoriesNode.set(ownerId, invNode);
        }
        root.set("inventories", inventoriesNode);
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/main/resources/inventory.json"), root);
    }
} 