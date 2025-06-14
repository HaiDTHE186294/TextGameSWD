package item.ui;

import item.model.*;
import item.service.ItemService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.Map;

public class InventoryController {
    @FXML private ComboBox<String> ownerComboBox;
    @FXML private ListView<String> itemListView;
    @FXML private VBox itemDetailsBox;
    @FXML private Label itemNameLabel;
    @FXML private Label itemDescriptionLabel;
    @FXML private Label itemValueLabel;
    @FXML private Label itemQuantityLabel;
    @FXML private Label itemTypeLabel;
    @FXML private Label itemStatusLabel;

    private ItemService itemService;
    private String currentOwnerId;
    private Map<String, Integer> currentItems;

    public void initialize() {
        // Thiết lập ComboBox
        ownerComboBox.getItems().addAll("player_1", "npc_1");
        ownerComboBox.setOnAction(e -> switchInventory(ownerComboBox.getValue()));

        // Thiết lập ListView
        itemListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        showItemDetails(newValue);
                    }
                }
        );
    }

    public void setItemService(ItemService itemService) {
        this.itemService = itemService;
        // Mặc định chọn inventory của người chơi
        switchInventory("player_1");
    }

    private void switchInventory(String ownerId) {
        this.currentOwnerId = ownerId;
        refreshInventory();
    }

    private void refreshInventory() {
        itemListView.getItems().clear();
        currentItems = itemService.getInventoryItems(currentOwnerId);

        for (Map.Entry<String, Integer> entry : currentItems.entrySet()) {
            Item item = itemService.getItem(entry.getKey());
            itemListView.getItems().add(String.format("%s (x%d)",
                    item.getName(), entry.getValue()));
        }
    }

    private void showItemDetails(String itemName) {
        // Tìm item tương ứng trong currentItems
        String itemId = null;
        for (Map.Entry<String, Integer> entry : currentItems.entrySet()) {
            Item item = itemService.getItem(entry.getKey());
            if (item.getName().equals(itemName.split(" ")[0])) {
                itemId = entry.getKey();
                break;
            }
        }

        if (itemId == null) return;

        Item item = itemService.getItem(itemId);
        int quantity = currentItems.get(itemId);

        // Hiển thị thông tin cơ bản
        itemNameLabel.setText(item.getName());
        itemDescriptionLabel.setText("Mô tả: " + item.getDescription());
        itemValueLabel.setText("Giá trị: " + item.getValue());
        itemQuantityLabel.setText("Số lượng: " + quantity);

        // Hiển thị thông tin đặc biệt dựa trên loại item
        if (item instanceof EquipmentItem) {
            EquipmentItem equip = (EquipmentItem) item;
            itemTypeLabel.setText("Loại: Trang bị");
            itemStatusLabel.setText(String.format("Độ bền: %d/%d",
                    equip.getDurability(), equip.getMaxDurability()));
        } else if (item instanceof ConsumableItem) {
            ConsumableItem consumable = (ConsumableItem) item;
            itemTypeLabel.setText("Loại: Vật phẩm tiêu hao");
            itemStatusLabel.setText(String.format("Số lượng tối đa: %d",
                    consumable.getMaxQuantity()));
        } else if (item instanceof QuestItem) {
            QuestItem quest = (QuestItem) item;
            itemTypeLabel.setText("Loại: Vật phẩm nhiệm vụ");
            itemStatusLabel.setText("Trạng thái: " +
                    (quest.isDelivered() ? "Đã giao" : "Chưa giao"));
        }
    }

    @FXML
    private void handleUseItem() {
        String selectedItem = itemListView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Lỗi", "Vui lòng chọn một vật phẩm để sử dụng!");
            return;
        }

        // Tìm item tương ứng
        String itemId = null;
        for (Map.Entry<String, Integer> entry : currentItems.entrySet()) {
            Item item = itemService.getItem(entry.getKey());
            if (item.getName().equals(selectedItem.split(" ")[0])) {
                itemId = entry.getKey();
                break;
            }
        }

        if (itemId == null) return;

        Item item = itemService.getItem(itemId);
        item.use();

        // Nếu là vật phẩm tiêu hao, giảm số lượng
        if (item instanceof ConsumableItem) {
            ConsumableItem consumable = (ConsumableItem) item;
            if (consumable.isEmpty()) {
                itemService.removeItemFromInventory(currentOwnerId, itemId, 1);
                showAlert("Thông báo", item.getName() + " đã hết!");
            }
        }

        refreshInventory();
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) itemDetailsBox.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}