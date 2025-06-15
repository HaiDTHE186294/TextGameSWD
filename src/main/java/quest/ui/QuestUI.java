package quest.ui;

import quest.model.*;
import quest.service.QuestService;
import quest.service.QuestObserver;
import java.util.*;

public class QuestUI implements QuestObserver {
    private final QuestService questService;
    private final Scanner scanner;
    private final String playerId;

    public QuestUI(QuestService questService, String playerId) {
        this.questService = questService;
        this.playerId = playerId;
        this.scanner = new Scanner(System.in);
        questService.addObserver(this);
    }

    public void showMenu() throws Exception {
        while (true) {
            System.out.println("\n=== QUEST MENU ===");
            System.out.println("1. Xem danh sách quest");
            System.out.println("2. Xem chi tiết quest");
            System.out.println("3. Nhận quest");
            System.out.println("4. Cập nhật tiến độ quest");
            System.out.println("5. Xem tiến trình quest");
            System.out.println("6. Hoàn thành quest");
            System.out.println("0. Thoát");
            System.out.print("Chọn: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1: showQuestList(); break;
                case 2: showQuestDetail(); break;
                case 3: startQuest(); break;
                case 4: updateQuestProgress(); break;
                case 5: showQuestProgress(); break;
                case 6: completeQuest(); break;
                case 0: return;
                default: System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void showQuestList() {
        List<Quest> quests = questService.getAllQuests();
        System.out.println("\n--- Danh sách quest ---");
        for (int i = 0; i < quests.size(); i++) {
            Quest q = quests.get(i);
            System.out.printf("%d. %s [%s]\n", i + 1, q.getName(), q.getType());
        }
    }

    private void showQuestDetail() {
        List<Quest> quests = questService.getAllQuests();
        System.out.print("Nhập số thứ tự quest: ");
        int idx = scanner.nextInt() - 1;
        scanner.nextLine();
        if (idx < 0 || idx >= quests.size()) {
            System.out.println("Không hợp lệ!");
            return;
        }
        Quest q = quests.get(idx);
        System.out.println("Tên: " + q.getName());
        System.out.println("Mô tả: " + q.getDescription());
        System.out.println("Loại: " + q.getType());
        System.out.println("Mục tiêu: " + q.getTarget());
        System.out.println("Số lượng cần: " + q.getRequiredAmount());
        System.out.println("Phần thưởng: " + q.getReward());
    }

    private void startQuest() {
        List<Quest> quests = questService.getAllQuests();
        System.out.print("Nhập số thứ tự quest muốn nhận: ");
        int idx = scanner.nextInt() - 1;
        scanner.nextLine();
        if (idx < 0 || idx >= quests.size()) {
            System.out.println("Không hợp lệ!");
            return;
        }
        Quest q = quests.get(idx);
        boolean ok = questService.startQuest(q.getId(), playerId);
        System.out.println(ok ? "Đã nhận quest!" : "Không thể nhận quest (đã nhận hoặc lỗi)!");
    }

    private void updateQuestProgress() throws Exception {
        List<Quest> quests = questService.getAllQuests();
        System.out.print("Nhập số thứ tự quest muốn cập nhật: ");
        int idx = scanner.nextInt() - 1;
        scanner.nextLine();
        if (idx < 0 || idx >= quests.size()) {
            System.out.println("Không hợp lệ!");
            return;
        }
        Quest q = quests.get(idx);
        if (q.getType() == QuestType.NOI_CHUYEN) {
            questService.updateProgress(q.getId(), playerId, null);
            System.out.println("Đã nói chuyện với NPC!");
        } else {
            System.out.print("Nhập số lượng muốn cập nhật: ");
            int amount = scanner.nextInt();
            scanner.nextLine();
            questService.updateProgress(q.getId(), playerId, amount);
            try {
                questService.saveProgressToJson("src/main/resources/quest_progress.json");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            System.out.println("Đã cập nhật tiến độ!");
        }
    }

    private void showQuestProgress() {
        List<Quest> quests = questService.getAllQuests();
        System.out.print("Nhập số thứ tự quest: ");
        int idx = scanner.nextInt() - 1;
        scanner.nextLine();
        if (idx < 0 || idx >= quests.size()) {
            System.out.println("Không hợp lệ!");
            return;
        }
        Quest q = quests.get(idx);
        QuestProgress progress = questService.getProgress(q.getId(), playerId);
        if (progress == null) {
            System.out.println("Chưa nhận quest này!");
            return;
        }
        System.out.println("Đã bắt đầu: " + progress.isStarted());
        System.out.println("Đã hoàn thành: " + progress.isCompleted());
        System.out.println("Tiến độ: " + progress.getCurrentAmount() + "/" + q.getRequiredAmount());
        if (q.getType() == QuestType.NOI_CHUYEN) {
            System.out.println("Đã nói chuyện: " + progress.isTalked());
        }
    }

    private void completeQuest() {
        List<Quest> quests = questService.getAllQuests();
        System.out.print("Nhập số thứ tự quest muốn hoàn thành: ");
        int idx = scanner.nextInt() - 1;
        scanner.nextLine();
        if (idx < 0 || idx >= quests.size()) {
            System.out.println("Không hợp lệ!");
            return;
        }
        Quest q = quests.get(idx);
        boolean ok = questService.completeQuest(q.getId(), playerId);
        System.out.println(ok ? "Đã hoàn thành quest!" : "Chưa đủ điều kiện hoàn thành!");
    }

    @Override
    public void onQuestProgressChanged(String questId, String playerId, QuestProgress progress) {
        if (this.playerId.equals(playerId)) {
            System.out.println("[Thông báo] Tiến độ quest " + questId + " đã thay đổi: " + progress.getCurrentAmount());
        }
    }
}