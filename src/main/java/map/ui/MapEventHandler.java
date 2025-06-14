package map.ui;

import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.util.List;
import java.util.ArrayList;
import map.observer.IUIObserver;

public class MapEventHandler {
    private final StackPane root;
    private final List<IUIObserver> observers;
    private VBox areaSelectionView;
    private VBox roomTransitionView;
    private String selectedAreaId;

    public MapEventHandler(StackPane root) {
        this.root = root;
        this.observers = new ArrayList<>();
        initializeViews();
    }

    private void initializeViews() {
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
        
        // Add key event handler for Enter key
        root.setOnKeyPressed(this::handleKeyPress);
    }

    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER && areaSelectionView.isVisible() && selectedAreaId != null) {
            notifyAreaSelected(selectedAreaId);
        }
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

    public void addObserver(IUIObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(IUIObserver observer) {
        observers.remove(observer);
    }

    private void notifyAreaSelected(String areaId) {
        for (IUIObserver observer : observers) {
            observer.onAreaSelected(areaId);
        }
    }

    private void notifyRoomSelected(String areaId, String roomId) {
        for (IUIObserver observer : observers) {
            observer.onRoomSelected(areaId, roomId);
        }
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