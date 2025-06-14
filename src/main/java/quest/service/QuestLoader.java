package quest.service;

import quest.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.util.*;

public class QuestLoader {
    public static List<Quest> loadQuestsFromJson(String filePath) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> questList = mapper.readValue(new File(filePath), new TypeReference<List<Map<String, Object>>>() {});
        List<Quest> result = new ArrayList<>();
        for (Map<String, Object> questData : questList) {
            QuestType type = QuestType.valueOf((String) questData.get("type"));
            QuestLogic logic;
            switch (type) {
                case THU_THAP: logic = new quest.model.CollectQuestLogic(); break;
                case SAN_LUNG: logic = new quest.model.HuntQuestLogic(); break;
                case NOI_CHUYEN: logic = new quest.model.TalkQuestLogic(); break;
                default: logic = null;
            }
            List<String> reward = (List<String>) questData.get("reward");
            Quest quest = new Quest(
                    (String) questData.get("id"),
                    (String) questData.get("name"),
                    (String) questData.get("description"),
                    type,
                    (String) questData.get("target"),
                    ((Number) questData.get("requiredAmount")).intValue(),
                    reward,
                    logic
            );
            result.add(quest);
        }
        return result;
    }

    public static void saveQuestsToJson(String filePath, Collection<Quest> quests) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> questList = new ArrayList<>();
        for (Quest quest : quests) {
            Map<String, Object> questData = new HashMap<>();
            questData.put("id", quest.getId());
            questData.put("name", quest.getName());
            questData.put("description", quest.getDescription());
            questData.put("type", quest.getType().name());
            questData.put("target", quest.getTarget());
            questData.put("requiredAmount", quest.getRequiredAmount());
            questData.put("reward", quest.getReward());
            questList.add(questData);
        }
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), questList);
    }

    public static Map<String, Map<String, QuestProgress>> loadProgressFromJson(String filePath) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(filePath), new TypeReference<Map<String, Map<String, QuestProgress>>>() {});
    }

    public static void saveProgressToJson(String filePath, Map<String, Map<String, QuestProgress>> playerProgress) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), playerProgress);
    }
}