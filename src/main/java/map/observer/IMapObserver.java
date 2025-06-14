package map.observer;

public interface IMapObserver {
    // Map initialization
    void onMapInitialized();
    
    // Player movement
    void onPlayerMoved(String areaId, String roomId, int row, int col);
    
    // Cell changes
    void onCellChanged(String areaId, String roomId, int row, int col, boolean isWalkable, String content);
    
    // Area/Room transitions
    void onRoomTransitionRequested(String targetAreaId, String targetRoomId, int targetRow, int targetCol);
    void onAreaTransitionRequested(String targetAreaId);
    
    // Map state changes
    void onMapStateChanged(String areaId, String roomId, String state);
    
    // Content interactions
    void onContentEntered(String areaId, String roomId, int row, int col, String contentType);
    void onContentExited(String areaId, String roomId, int row, int col, String contentType);
} 