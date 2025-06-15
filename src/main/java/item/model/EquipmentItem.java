package item.model;

import java.util.Map;

public class EquipmentItem extends Item {
    private int durability;
    private int maxDurability;
    private EquipmentSlot slot;

    public EquipmentItem() {
        super();
    }

    public EquipmentItem(String id, String name, String description, int value, int maxDurability, EquipmentSlot slot, Map<String, Integer> attributes, Map<String, Object> extraProperties) {
        super(id, name, description, value, false, attributes, extraProperties);
        this.maxDurability = maxDurability;
        this.durability = maxDurability;
        this.slot = slot;
    }

    @Override
    public void use() {
        // Logic khi sử dụng trang bị
        System.out.println("Equipping " + getName());
    }

    public void reduceDurability(int amount) {
        durability = Math.max(0, durability - amount);
    }

    public void repair() {
        durability = maxDurability;
    }

    public void setDurability(int durability) {
        this.durability = Math.min(maxDurability, Math.max(0, durability));
    }

    public int getDurability() {
        return durability;
    }

    public int getMaxDurability() {
        return maxDurability;
    }

    public EquipmentSlot getSlot() {
        return slot;
    }

    public boolean isBroken() {
        return durability <= 0;
    }
}