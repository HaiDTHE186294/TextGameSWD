package item.model;

import java.util.HashMap;
import java.util.Map;

public abstract class Item {
    private String id;
    private String name;
    private String description;
    private int value;
    private boolean stackable;
    private Map<String, Integer> attributes;
    private Map<String, Object> extraProperties;

    public Item() {
        this.attributes = new HashMap<>();
        this.extraProperties = new HashMap<>();
    }

    public Item(String id, String name, String description, int value, boolean stackable, Map<String, Integer> attributes, Map<String, Object> extraProperties) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.value = value;
        this.stackable = stackable;
        this.attributes = attributes != null ? new HashMap<>(attributes) : new HashMap<>();
        this.extraProperties = extraProperties != null ? new HashMap<>(extraProperties) : new HashMap<>();
    }

    // Abstract method that must be implemented by subclasses
    public abstract void use();

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isStackable() {
        return stackable;
    }

    public void setStackable(boolean stackable) {
        this.stackable = stackable;
    }

    public Map<String, Integer> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Integer> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Object> getExtraProperties() {
        return extraProperties;
    }

    public void setExtraProperties(Map<String, Object> extraProperties) {
        this.extraProperties = extraProperties;
    }

    public Object getProperty(String key) {
        return extraProperties.get(key);
    }

    public void setProperty(String key, Object value) {
        extraProperties.put(key, value);
    }
}