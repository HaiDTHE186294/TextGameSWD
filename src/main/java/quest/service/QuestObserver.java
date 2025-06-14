package quest.service;

import quest.model.QuestProgress;

public interface QuestObserver {
    void onQuestProgressChanged(String questId, String playerId, QuestProgress progress);
}