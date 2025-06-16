package map.ui;

import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.util.List;

public class MapEventHandler {
    private final StackPane root;
    private final MapUI mapUI;
    private VBox areaSelectionView;
    private VBox roomTransitionView;
    private String selectedAreaId;

    public MapEventHandler(StackPane root, MapUI mapUI) {
        this.root = root;
        this.mapUI = mapUI;
        initializeViews();
    }

    private void initializeViews() {
        // Make root focusable
        root.setFocusTraversable(true);
        
        // Initialize area selection view
        areaSelectionView = new VBox(10);
        areaSelectionView.setVisible(false);
        areaSelectionView.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8); -fx-padding: 20;");
        
        // Initialize room transition view
        roomTransitionView = new VBox(10);
        roomTransitionView.setVisible(false);
        roomTransitionView.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8); -fx-padding: 20;");
        
        // Add views to root
        root.getChildren().addAll(areaSelectionView, roomTransitionView);
        
        // Add key event handler for movement and Enter key
        root.setOnKeyPressed(this::handleKeyPress);
        
        // Request focus when the scene is shown
        root.setOnMouseClicked(e -> root.requestFocus());
        
        // Request initial focus
        root.requestFocus();
    }

    private void handleKeyPress(KeyEvent event) {
        // System.out.println("Key pressed: " + event.getCode()); // Debug log
        
        if (areaSelectionView.isVisible()) {
            if (event.getCode() == KeyCode.ENTER && selectedAreaId != null) {
                notifyAreaSelected(selectedAreaId);
            }
            return;
        }

        switch (event.getCode()) {
            case W:
                notifyMovement(0, -1);
                break;
            case S:
                notifyMovement(0, 1);
                break;
            case A:
                notifyMovement(-1, 0);
                break;
            case D:
                notifyMovement(1, 0);
                break;
        }
    }

    private void notifyMovement(int dx, int dy) {
        // System.out.println("Movement requested: dx=" + dx + ", dy=" + dy); // Debug log
        mapUI.onMovementRequested(dx, dy);
    }

    public void showAreaSelection() {
        areaSelectionView.setVisible(true);
        roomTransitionView.setVisible(false);
    }

    public void hideAreaSelection() {
        areaSelectionView.setVisible(false);
        roomTransitionView.setVisible(false);
    }

    public void populateAreaSelection(List<String> availableAreas) {
        areaSelectionView.getChildren().clear();
        for (String areaId : availableAreas) {
            Button areaButton = new Button(areaId);
            areaButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
            areaButton.setOnAction(e -> {
                selectedAreaId = areaId;
                notifyAreaSelected(areaId);
            });
            areaButton.setOnMouseEntered(e -> selectedAreaId = areaId);
            areaSelectionView.getChildren().add(areaButton);
        }
    }

    public void populateRoomSelection(List<String> availableRooms) {
        roomTransitionView.getChildren().clear();
        for (String roomId : availableRooms) {
            Button roomButton = new Button(roomId);
            roomButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px;");
            roomButton.setOnAction(e -> notifyRoomSelected(selectedAreaId, roomId));
            roomTransitionView.getChildren().add(roomButton);
        }
    }

    private void notifyAreaSelected(String areaId) {
        mapUI.onAreaSelected(areaId);
    }

    private void notifyRoomSelected(String areaId, String roomId) {
        mapUI.onRoomSelected(areaId, roomId);
    }

    public void handleUIStateChange(String state) {
        switch (state) {
            case "SHOW_MAP":
                showAreaSelection();
                break;
            case "HIDE_MAP":
                hideAreaSelection();
                break;
        }
    }
} 