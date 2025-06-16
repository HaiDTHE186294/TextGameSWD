package map.ui;

import map.observer.IUIObserver;
import javafx.geometry.Point2D;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import java.util.ArrayList;
import java.util.List;

public class MapUI implements IUIObserver {
    private final StackPane root;
    private final VBox areaSelectionView;
    private final List<IUIObserver> uiObservers;
    private boolean isMapViewActive = false;
    private AreaUI currentAreaUI;
    private static final int CELL_SIZE = 40;
    private static final Color WALL_COLOR = Color.GRAY;
    private static final Color PLAYER_COLOR = Color.BLUE;
    private static final Color EXIT_COLOR = Color.RED;
    private final MapEventHandler eventHandler;
    private final AreaMapUI areaMapUI;

    public MapUI() {
        root = new StackPane();
        uiObservers = new ArrayList<>();
        
        // Initialize area selection view (old map view) - no longer added to root
        areaSelectionView = new VBox(10);
        areaSelectionView.setAlignment(Pos.CENTER);
        areaSelectionView.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8); -fx-padding: 20;");
        areaSelectionView.setVisible(false);
        
        // Initialize new AreaMapUI (graphical map view)
        areaMapUI = new AreaMapUI(uiObservers);
        areaMapUI.setVisible(false);

        // Only add areaMapUI to root initially. currentAreaUI.getNode() will be added/removed dynamically.
        root.getChildren().add(areaMapUI);
        
        // Initialize event handler
        eventHandler = new MapEventHandler(root, this);
    }

    public void addObserver(IUIObserver observer) {
        uiObservers.add(observer);
    }

    public void removeObserver(IUIObserver observer) {
        uiObservers.remove(observer);
    }

    public void showAreaSelection() {
        isMapViewActive = true;
        areaSelectionView.setVisible(false); // Hide the old area selection buttons
        areaMapUI.setVisible(true); // Show the new graphical map
        
        // Hide the current room view if it's active
        if (currentAreaUI != null) {
            boolean removed = root.getChildren().remove(currentAreaUI.getNode());
            System.out.println("MapUI: Removed currentAreaUI.getNode() from root: " + removed); // Debug log
        }
    }

    public void hideAreaSelection() {
        isMapViewActive = false;
        areaMapUI.setVisible(false); // Hide the new graphical map
        areaSelectionView.setVisible(false);
        
        // Re-add the current room view if it was hidden
        if (currentAreaUI != null && !root.getChildren().contains(currentAreaUI.getNode())) {
            root.getChildren().add(currentAreaUI.getNode());
        }
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

    public boolean isMapViewActive() {
        return isMapViewActive;
    }

    public StackPane getNode() {
        return root;
    }

    public AreaMapUI getAreaMapUI() {
        return areaMapUI;
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
        // System.out.println("MapUI: Player position changed to " + newPosition);
        if (currentAreaUI != null) {
            currentAreaUI.updatePlayerPosition(newPosition);
            // root.getChildren().remove(currentAreaUI.getNode()); // Handled by show/hideAreaSelection
            // root.getChildren().add(currentAreaUI.getNode());   // Handled by show/hideAreaSelection
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

    @Override
    public void onMovementRequested(int dx, int dy) {
        // Forward movement request to observers
        for (IUIObserver observer : uiObservers) {
            observer.onMovementRequested(dx, dy);
        }
    }

    public void setCurrentArea(AreaUI areaUI) {
        if (currentAreaUI != null) {
            root.getChildren().remove(currentAreaUI.getNode());
        }
        currentAreaUI = areaUI;
        // Only add if not already in children (might be re-added by hideAreaSelection)
        if (!root.getChildren().contains(areaUI.getNode())) {
            root.getChildren().add(areaUI.getNode());
        }
    }

    private void notifyAreaSelected(String areaId) {
        for (IUIObserver observer : uiObservers) {
            observer.onAreaSelected(areaId);
        }
    }
} 