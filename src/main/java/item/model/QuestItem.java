package item.model;

import java.util.Map;

public class QuestItem extends Item {
    private String questId;
    private boolean isDelivered;

    public QuestItem() {
        super();
    }

    public QuestItem(String id, String name, String description, int value, String questId, Map<String, Object> extraProperties) {
        super(id, name, description, value, false, null, extraProperties);
        this.questId = questId;
        this.isDelivered = false;
    }

    @Override
    public void use() {
        // Logic khi sử dụng vật phẩm nhiệm vụ
        System.out.println("This item is for quest: " + questId);
    }

    public String getQuestId() {
        return questId;
    }

    public boolean isDelivered() {
        return isDelivered;
    }

    public void setDelivered(boolean delivered) {
        isDelivered = delivered;
    }
}