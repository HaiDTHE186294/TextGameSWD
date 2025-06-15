package item.ui;

import item.service.ItemService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class InventoryWindow {
    private final ItemService itemService;
    private Stage stage;

    public InventoryWindow(ItemService itemService) {
        this.itemService = itemService;
    }

    public void show() {
        try {
            // Tạo stage mới
            stage = new Stage();
            stage.setTitle("Inventory");
            stage.initModality(Modality.APPLICATION_MODAL);

            // Load FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/inventory.fxml"));
            Scene scene = new Scene(loader.load());

            // Lấy controller và thiết lập ItemService
            InventoryController controller = loader.getController();
            controller.setItemService(itemService);

            // Hiển thị window
            stage.setScene(scene);
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}