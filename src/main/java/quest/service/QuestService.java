package quest.service;

import quest.model.*;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;

public class QuestService {
    private final Map<String, Quest> quests = new HashMap<>();
    private final Map<String, Map<String, QuestProgress>> playerProgress = new HashMap<>(); // playerId -> (questId -> progress)
    private final List<QuestObserver> observers = new ArrayList<>();

    public void registerQuest(Quest quest) {
        quests.put(quest.getId(), quest);
    }

    public List<Quest> getAllQuests() {
        return new ArrayList<>(quests.values());
    }

    public Quest getQuest(String questId) {
        return quests.get(questId);
    }

    public QuestProgress getProgress(String questId, String playerId) {
        return playerProgress.getOrDefault(playerId, new HashMap<>()).get(questId);
    }

    public boolean startQuest(String questId, String playerId) {
        Quest quest = quests.get(questId);
        if (quest == null) return false;
        playerProgress.putIfAbsent(playerId, new HashMap<>());
        Map<String, QuestProgress> progressMap = playerProgress.get(playerId);
        if (progressMap.containsKey(questId)) return false; // đã nhận
        QuestProgress progress = new QuestProgress();
        progress.setStarted(true);
        progressMap.put(questId, progress);
        return true;
    }

    public boolean updateProgress(String questId, String playerId, Object actionData) {
        Map<String, QuestProgress> progressMap = playerProgress.get(playerId);
        if (progressMap == null || !progressMap.containsKey(questId)) return false;
        QuestProgress progress = progressMap.get(questId);
        if (progress.isCompleted()) return false;
        Quest quest = quests.get(questId);
        if (quest == null) return false;
        boolean changed = quest.getLogic().updateProgress(progress, actionData, quest);
        notifyProgressChanged(questId, playerId, progress);
        return changed;
    }

    public boolean completeQuest(String questId, String playerId) {
        Map<String, QuestProgress> progressMap = playerProgress.get(playerId);
        if (progressMap == null || !progressMap.containsKey(questId)) return false;
        QuestProgress progress = progressMap.get(questId);
        if (!progress.isCompleted()) return false;
        // Có thể phát thưởng ở đây
        return true;
    }

    public boolean isQuestCompleted(String questId, String playerId) {
        Map<String, QuestProgress> progressMap = playerProgress.get(playerId);
        if (progressMap == null || !progressMap.containsKey(questId)) return false;
        return progressMap.get(questId).isCompleted();
    }

    public void addObserver(QuestObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(QuestObserver observer) {
        observers.remove(observer);
    }

    private void notifyProgressChanged(String questId, String playerId, QuestProgress progress) {
        for (QuestObserver obs : observers) {
            obs.onQuestProgressChanged(questId, playerId, progress);
        }
    }

    public void setPlayerProgress(Map<String, Map<String, QuestProgress>> progress) {
        playerProgress.clear();
        if (progress != null) playerProgress.putAll(progress);
    }

    public Map<String, Map<String, QuestProgress>> getPlayerProgress() {
        return playerProgress;
    }

    public void loadQuestsFromJson(String filePath) throws Exception {
        List<Quest> loaded = QuestLoader.loadQuestsFromJson(filePath);
        for (Quest q : loaded) {
            registerQuest(q);
        }
    }

    public void saveQuestsToJson(String filePath) throws Exception {
        QuestLoader.saveQuestsToJson(filePath, quests.values());
    }

    public void loadProgressFromJson(String filePath) throws Exception {
        setPlayerProgress(QuestLoader.loadProgressFromJson(filePath));
    }

    public void saveProgressToJson(String filePath) throws Exception {
        QuestLoader.saveProgressToJson(filePath, playerProgress);
    }
}