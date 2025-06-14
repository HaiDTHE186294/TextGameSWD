package quest.controller;

import quest.service.QuestService;
import quest.model.Quest;
import java.util.List;

public class QuestController {
    private final QuestService service;

    public QuestController(QuestService service) {
        this.service = service;
    }

    public List<Quest> getAllQuests() {
        return service.getAllQuests();
    }

    public Quest getQuest(String questId) {
        return service.getQuest(questId);
    }

    public boolean startQuest(String questId, String playerId) {
        return service.startQuest(questId, playerId);
    }

    public boolean completeQuest(String questId, String playerId) {
        return service.completeQuest(questId, playerId);
    }

    public boolean isQuestCompleted(String questId, String playerId) {
        return service.isQuestCompleted(questId, playerId);
    }
}