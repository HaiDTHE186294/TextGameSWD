package quest.model;

public interface QuestLogic {
    boolean updateProgress(QuestProgress progress, Object actionData, Quest quest);
    boolean isCompleted(QuestProgress progress, Quest quest);
}