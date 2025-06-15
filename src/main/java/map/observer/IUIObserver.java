package map.observer;

import javafx.geometry.Point2D;

public interface IUIObserver {
    // UI rendering
    void onUIRenderRequested();
    
    // Player position updates
    void onPlayerPositionChanged(Point2D newPosition);
    
    // Player movement
    void onMovementRequested(int dx, int dy);
    
    // Map element updates
    void onMapElementChanged(int row, int col, String elementType);
    
    // Area selection
    void onAreaSelected(String areaId);
    
    // Room selection
    void onRoomSelected(String areaId, String roomId);
    
    // Cell interaction
    void onCellClicked(int row, int col);
    
    // UI state changes
    void onUIStateChanged(String state);
    
    // View transitions
    void onViewTransitionRequested(String viewType);
    
    // UI element updates
    void onUIElementUpdated(String elementId, Object newValue);
} 