package item.ui;

import item.model.*;
import item.service.ItemService;
import java.util.Map;
import java.util.Scanner;

public class InventoryUI {
    private final ItemService itemService;
    private final Scanner scanner;
    private String currentOwnerId;

    public InventoryUI(ItemService itemService) {
        this.itemService = itemService;
        this.scanner = new Scanner(System.in);
        this.currentOwnerId = "player_1"; // Mặc định là inventory của người chơi
    }

    public void showMainMenu() {
        while (true) {
            System.out.println("\n=== MENU CHÍNH ===");
            System.out.println("1. Xem Inventory");
            System.out.println("2. Xem chi tiết item");
            System.out.println("3. Sử dụng item");
            System.out.println("4. Chuyển đổi inventory (Player/NPC)");
            System.out.println("0. Quay lại");
            System.out.print("Chọn: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Đọc bỏ dòng mới

            switch (choice) {
                case 1:
                    showInventory();
                    break;
                case 2:
                    showItemDetails();
                    break;
                case 3:
                    useItem();
                    break;
                case 4:
                    switchInventory();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void showInventory() {
        System.out.println("\n=== INVENTORY ===");
        System.out.println("Chủ sở hữu: " + currentOwnerId);
        System.out.printf("Kích thước: %d/%d\n",
                itemService.getInventorySize(currentOwnerId),
                itemService.getMaxInventorySize(currentOwnerId));

        Map<String, Integer> items = itemService.getInventoryItems(currentOwnerId);
        if (items.isEmpty()) {
            System.out.println("Inventory trống!");
            return;
        }

        int index = 1;
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            Item item = itemService.getItem(entry.getKey());
            System.out.printf("%d. %s (x%d)\n",
                    index++, item.getName(), entry.getValue());
        }
    }

    private void showItemDetails() {
        System.out.println("\n=== CHI TIẾT ITEM ===");
        System.out.print("Nhập số thứ tự item (0 để quay lại): ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Đọc bỏ dòng mới

        if (choice == 0) return;

        Map<String, Integer> items = itemService.getInventoryItems(currentOwnerId);
        if (choice < 1 || choice > items.size()) {
            System.out.println("Lựa chọn không hợp lệ!");
            return;
        }

        String itemId = items.keySet().toArray(new String[0])[choice - 1];
        Item item = itemService.getItem(itemId);

        System.out.println("\nThông tin chi tiết:");
        System.out.println("Tên: " + item.getName());
        System.out.println("Mô tả: " + item.getDescription());
        System.out.println("Giá trị: " + item.getValue());
        System.out.println("Số lượng: " + items.get(itemId));

        if (item instanceof EquipmentItem) {
            EquipmentItem equip = (EquipmentItem) item;
            System.out.println("Loại: Trang bị");
            System.out.println("Vị trí: " + equip.getSlot());
            System.out.printf("Độ bền: %d/%d\n",
                    equip.getDurability(), equip.getMaxDurability());
        } else if (item instanceof ConsumableItem) {
            ConsumableItem consumable = (ConsumableItem) item;
            System.out.println("Loại: Vật phẩm tiêu hao");
            System.out.printf("Số lượng tối đa: %d\n", consumable.getMaxQuantity());
        } else if (item instanceof QuestItem) {
            QuestItem quest = (QuestItem) item;
            System.out.println("Loại: Vật phẩm nhiệm vụ");
            System.out.println("ID nhiệm vụ: " + quest.getQuestId());
            System.out.println("Trạng thái: " + (quest.isDelivered() ? "Đã giao" : "Chưa giao"));
        }
    }

    private void useItem() {
        System.out.println("\n=== SỬ DỤNG ITEM ===");
        System.out.print("Nhập số thứ tự item (0 để quay lại): ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Đọc bỏ dòng mới

        if (choice == 0) return;

        Map<String, Integer> items = itemService.getInventoryItems(currentOwnerId);
        if (choice < 1 || choice > items.size()) {
            System.out.println("Lựa chọn không hợp lệ!");
            return;
        }

        String itemId = items.keySet().toArray(new String[0])[choice - 1];
        Item item = itemService.getItem(itemId);

        System.out.println("\nSử dụng " + item.getName() + "...");
        item.use();

        // Nếu là vật phẩm tiêu hao, giảm số lượng
        if (item instanceof ConsumableItem) {
            ConsumableItem consumable = (ConsumableItem) item;
            if (consumable.isEmpty()) {
                itemService.removeItemFromInventory(currentOwnerId, itemId, 1);
                System.out.println(item.getName() + " đã hết!");
            }
        }
    }

    private void switchInventory() {
        System.out.println("\n=== CHUYỂN ĐỔI INVENTORY ===");
        System.out.println("1. Player Inventory");
        System.out.println("2. NPC Inventory");
        System.out.print("Chọn: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Đọc bỏ dòng mới

        switch (choice) {
            case 1:
                currentOwnerId = "player_1";
                System.out.println("Đã chuyển sang Player Inventory");
                break;
            case 2:
                currentOwnerId = "npc_1";
                System.out.println("Đã chuyển sang NPC Inventory");
                break;
            default:
                System.out.println("Lựa chọn không hợp lệ!");
        }
    }
}