package quest.model;

public class TalkQuestLogic implements QuestLogic {
    @Override
    public boolean updateProgress(QuestProgress progress, Object actionData, Quest quest) {
        progress.setTalked(true);
        progress.setCompleted(true);
        return true;
    }
    @Override
    public boolean isCompleted(QuestProgress progress, Quest quest) {
        return progress.isTalked();
    }
}