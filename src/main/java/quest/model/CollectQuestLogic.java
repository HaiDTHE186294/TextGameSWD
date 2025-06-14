package quest.model;

public class CollectQuestLogic implements QuestLogic {
    @Override
    public boolean updateProgress(QuestProgress progress, Object actionData, Quest quest) {
        int amount = (int) actionData;
        progress.setCurrentAmount(progress.getCurrentAmount() + amount);
        if (isCompleted(progress, quest)) progress.setCompleted(true);
        return progress.isCompleted();
    }
    @Override
    public boolean isCompleted(QuestProgress progress, Quest quest) {
        return progress.getCurrentAmount() >= quest.getRequiredAmount();
    }
}