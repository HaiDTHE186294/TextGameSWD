package map.ui;

import javafx.scene.layout.GridPane;
import map.observer.IUIObserver;
import javafx.geometry.Point2D;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.ArrayList;
import java.util.List;

public class MapUI implements IUIObserver {
    private final GridPane areaGrid;
    private final StackPane root;
    private final VBox areaSelectionView;
    private final List<IUIObserver> uiObservers;
    private boolean isMapViewActive = false;
    private AreaUI currentAreaUI;
    private static final int CELL_SIZE = 40;
    private static final Color WALL_COLOR = Color.GRAY;
    private static final Color PLAYER_COLOR = Color.BLUE;
    private static final Color EXIT_COLOR = Color.RED;

    public MapUI() {
        root = new StackPane();
        areaGrid = new GridPane();
        areaGrid.setHgap(2);
        areaGrid.setVgap(2);
        uiObservers = new ArrayList<>();
        
        // Initialize area selection view
        areaSelectionView = new VBox(10);
        areaSelectionView.setAlignment(Pos.CENTER);
        areaSelectionView.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8); -fx-padding: 20;");
        areaSelectionView.setVisible(false);
        
        root.getChildren().addAll(areaGrid, areaSelectionView);
    }

    public void addObserver(IUIObserver observer) {
        uiObservers.add(observer);
    }

    public void removeObserver(IUIObserver observer) {
        uiObservers.remove(observer);
    }

    public void showAreaSelection() {
        isMapViewActive = true;
        areaGrid.getChildren().clear();
        areaSelectionView.setVisible(true);
    }

    public void hideAreaSelection() {
        isMapViewActive = false;
        areaSelectionView.setVisible(false);
    }

    public void addAreaButton(String areaId, String areaName) {
        Button areaButton = new Button(areaName);
        areaButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        areaButton.setPrefWidth(200);
        areaButton.setOnAction(e -> notifyAreaSelected(areaId));
        areaSelectionView.getChildren().add(areaButton);
    }

    public void clearAreaButtons() {
        areaSelectionView.getChildren().clear();
    }

    public void updateMapCell(int row, int col, boolean isWall, boolean isPlayer, boolean isExit) {
        Rectangle cell = new Rectangle(CELL_SIZE, CELL_SIZE);
        if (isWall) {
            cell.setFill(WALL_COLOR);
        } else if (isPlayer) {
            cell.setFill(PLAYER_COLOR);
        } else if (isExit) {
            cell.setFill(EXIT_COLOR);
        } else {
            cell.setFill(Color.WHITE);
            cell.setStroke(Color.LIGHTGRAY);
        }
        areaGrid.add(cell, col, row);
    }

    public void clearMap() {
        areaGrid.getChildren().clear();
    }

    public boolean isMapViewActive() {
        return isMapViewActive;
    }

    public StackPane getNode() {
        return root;
    }

    // IUIObserver implementation
    @Override
    public void onUIRenderRequested() {
        if (isMapViewActive) {
            showAreaSelection();
        } else if (currentAreaUI != null) {
            currentAreaUI.getNode().requestLayout();
        }
    }

    @Override
    public void onPlayerPositionChanged(Point2D newPosition) {
        if (currentAreaUI != null) {
            currentAreaUI.updatePlayerPosition(newPosition);
        }
    }

    @Override
    public void onMapElementChanged(int row, int col, String elementType) {
        if (currentAreaUI != null) {
            currentAreaUI.updateMapElement(row, col, elementType);
        }
    }

    @Override
    public void onAreaSelected(String areaId) {
        if (isMapViewActive) {
            hideAreaSelection();
        }
    }

    @Override
    public void onRoomSelected(String areaId, String roomId) {
        if (currentAreaUI != null) {
            currentAreaUI.handleRoomSelection(roomId);
        }
    }

    @Override
    public void onCellClicked(int row, int col) {
        if (currentAreaUI != null) {
            currentAreaUI.handleCellClick(row, col);
        }
    }

    @Override
    public void onUIStateChanged(String state) {
        switch (state) {
            case "SHOW_MAP":
                showAreaSelection();
                break;
            case "HIDE_MAP":
                hideAreaSelection();
                break;
        }
    }

    @Override
    public void onViewTransitionRequested(String viewType) {
        switch (viewType) {
            case "AREA_SELECTION":
                showAreaSelection();
                break;
            case "ROOM_VIEW":
                hideAreaSelection();
                break;
        }
    }

    @Override
    public void onUIElementUpdated(String elementId, Object newValue) {
        if (currentAreaUI != null) {
            currentAreaUI.updateElement(elementId, newValue);
        }
    }

    public void setCurrentArea(AreaUI areaUI) {
        if (currentAreaUI != null) {
            root.getChildren().remove(currentAreaUI.getNode());
        }
        currentAreaUI = areaUI;
        root.getChildren().add(areaUI.getNode());
    }

    private void notifyAreaSelected(String areaId) {
        for (IUIObserver observer : uiObservers) {
            observer.onAreaSelected(areaId);
        }
    }
} 