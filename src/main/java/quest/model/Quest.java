package quest.model;

import java.util.List;

public class Quest {
    private String id;
    private String name;
    private String description;
    private QuestType type;
    private String target; // itemId, monsterId, npcId
    private int requiredAmount; // số lượng cần thu thập/giết
    private List<String> reward;
    private QuestLogic logic;

    public Quest(String id, String name, String description, QuestType type, String target, int requiredAmount, List<String> reward, QuestLogic logic) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.target = target;
        this.requiredAmount = requiredAmount;
        this.reward = reward;
        this.logic = logic;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public QuestType getType() { return type; }
    public String getTarget() { return target; }
    public int getRequiredAmount() { return requiredAmount; }
    public List<String> getReward() { return reward; }
    public QuestLogic getLogic() { return logic; }
    public void setLogic(QuestLogic logic) { this.logic = logic; }
}