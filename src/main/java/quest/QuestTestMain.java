package quest;

import quest.model.*;
import quest.service.*;
import quest.ui.QuestUI;

public class QuestTestMain {
    public static void main(String[] args) throws Exception {
        QuestService questService = new QuestService();

        // Load quest từ file JSON
        questService.loadQuestsFromJson("src/main/resources/quest.json");

        // Load tiến trình từ file JSON (nếu có)
        try {
            questService.loadProgressFromJson("src/main/resources/quest_progress.json");
        } catch (Exception e) {
            System.out.println("Chưa có file tiến trình, sẽ tạo mới khi lưu.");
        }

        // Khởi tạo UI cho player_1
        QuestUI questUI = new QuestUI(questService, "player_1");
        questUI.showMenu();

        // Lưu lại tiến trình sau khi thao tác
        questService.saveProgressToJson("src/main/resources/quest_progress.json");
    }
}