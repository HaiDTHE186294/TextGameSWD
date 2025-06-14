package item.example;

import item.model.*;
import item.service.ItemService;
import item.service.ItemLoader;
import item.service.InventoryObserver;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Map;
import item.ui.*;

/**
 * InventoryUIExample - JavaFX demo cho inventory, cập nhật tự động theo Observer Pattern.
 */
public class InventoryUIExample extends Application implements InventoryUIUpdater {
    private ItemService itemService;
    private ListView<String> inventoryList;
    private Label statusLabel;
    private InventoryUIObserver observer;

    @Override
    public void start(Stage primaryStage) {
        try {
            // Khởi tạo ItemService
            itemService = new ItemService();

            // Tải danh sách item từ items.json
            Map<String, Item> items = ItemLoader.loadItems();
            for (Item item : items.values()) {
                itemService.registerItem(item);
            }

            // Tải và khôi phục trạng thái inventory từ inventory.json
            ItemLoader.loadInventoryData(itemService);

            // Đăng ký observer cho owner "player_1" (có thể sửa thành tên khác nếu muốn)
            observer = new InventoryUIObserver("player_1", this);
            itemService.addObserver(observer);

            // Tạo giao diện chính
            VBox root = new VBox(10);
            root.setPadding(new Insets(10));

            // Tiêu đề
            Label titleLabel = new Label("Inventory System");
            titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

            // Danh sách inventory
            inventoryList = new ListView<>();
            inventoryList.setPrefHeight(300);
            updateInventoryList();

            // Panel điều khiển
            HBox controlPanel = new HBox(10);
            controlPanel.setPadding(new Insets(10));

            // Nút thêm item
            Button addButton = new Button("Thêm Item");
            addButton.setOnAction(e -> showAddItemDialog());

            // Nút xóa item
            Button removeButton = new Button("Xóa Item");
            removeButton.setOnAction(e -> showRemoveItemDialog());

            // Nút làm mới
            Button refreshButton = new Button("Làm mới");
            refreshButton.setOnAction(e -> updateInventoryList());

            controlPanel.getChildren().addAll(addButton, removeButton, refreshButton);

            // Label trạng thái
            statusLabel = new Label("Sẵn sàng");
            statusLabel.setStyle("-fx-text-fill: green;");

            root.getChildren().addAll(titleLabel, inventoryList, controlPanel, statusLabel);

            // Hiển thị cửa sổ chính
            Scene scene = new Scene(root, 400, 500);
            primaryStage.setTitle("Inventory System");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showError("Lỗi khởi tạo: " + e.getMessage());
        }
    }

    // Observer gọi hàm này khi inventory thay đổi
    @Override
    public void updateInventoryUI() {
        // Đảm bảo chạy trên JavaFX Application Thread
        Platform.runLater(this::updateInventoryList);
    }

    private void updateInventoryList() {
        inventoryList.getItems().clear();
        Map<String, Inventory> inventories = itemService.getAllInventories();
        for (Map.Entry<String, Inventory> entry : inventories.entrySet()) {
            String ownerId = entry.getKey();
            Inventory inventory = entry.getValue();
            inventoryList.getItems().add("=== " + ownerId + " ===");
            for (Map.Entry<String, Integer> itemEntry : inventory.getItems().entrySet()) {
                String itemId = itemEntry.getKey();
                int quantity = itemEntry.getValue();
                Item item = itemService.getItem(itemId);
                if (item != null) {
                    inventoryList.getItems().add(String.format("- %s (x%d)", item.getName(), quantity));
                }
            }
            inventoryList.getItems().add("");
        }
    }

    private void showRemoveItemDialog() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Xóa Item");
        dialog.setHeaderText("Nhập ID của item muốn xóa");

        ButtonType removeButtonType = new ButtonType("Xóa", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(removeButtonType, ButtonType.CANCEL);

        TextField itemIdField = new TextField();
        itemIdField.setPromptText("Item ID");

        dialog.getDialogPane().setContent(itemIdField);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == removeButtonType) {
                return itemIdField.getText();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(itemId -> {
            try {
                itemService.removeItemFromInventory("player_1", itemId, 1);
                // Không cần gọi updateInventoryList(), đã có observer tự động cập nhật
                showStatus("Đã xóa item thành công");
            } catch (IllegalArgumentException e) {
                showError("Lỗi: " + e.getMessage());
            }
        });
    }

    private void showAddItemDialog() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Thêm Item");
        dialog.setHeaderText("Nhập ID của item muốn thêm");

        ButtonType addButtonType = new ButtonType("Thêm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        TextField itemIdField = new TextField();
        itemIdField.setPromptText("Item ID");

        dialog.getDialogPane().setContent(itemIdField);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return itemIdField.getText();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(itemId -> {
            try {
                itemService.addItemToInventory("player_1", itemId, 1);
                // Không cần gọi updateInventoryList(), đã có observer tự động cập nhật
                showStatus("Đã thêm item thành công");
            } catch (IllegalArgumentException e) {
                showError("Lỗi: " + e.getMessage());
            }
        });
    }


    private void showStatus(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: green;");
    }

    private void showError(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: red;");
    }

    @Override
    public void stop() {
        // Bỏ đăng ký observer khi đóng app
        if (observer != null && itemService != null) {
            itemService.removeObserver(observer);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}