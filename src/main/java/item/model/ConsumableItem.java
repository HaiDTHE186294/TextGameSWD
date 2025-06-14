package item.model;

import java.util.Map;

public class ConsumableItem extends Item {
    private int quantity;
    private int maxQuantity;
    private ConsumableEffect effect;

    public ConsumableItem(String id, String name, String description, int value, int maxQuantity, Map<String, Integer> attributes, Map<String, Object> extraProperties, ConsumableEffect effect) {
        super(id, name, description, value, true, attributes, extraProperties);
        this.maxQuantity = maxQuantity;
        this.quantity = maxQuantity;
        this.effect = effect;
    }

    @Override
    public void use() {
        if (quantity > 0) {
            System.out.println("Using " + getName());
            if (effect != null) {
                effect.apply();
            }
            quantity--;
        }
    }

    public void addQuantity(int amount) {
        quantity = Math.min(maxQuantity, quantity + amount);
    }

    public void setQuantity(int quantity) {
        this.quantity = Math.min(maxQuantity, Math.max(0, quantity));
    }

    public int getQuantity() {
        return quantity;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public ConsumableEffect getEffect() {
        return effect;
    }

    public void setEffect(ConsumableEffect effect) {
        this.effect = effect;
    }

    public boolean isEmpty() {
        return quantity <= 0;
    }
}