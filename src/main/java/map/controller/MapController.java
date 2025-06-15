package map.controller;

import map.logic.GMap;
import map.logic.PlayerPosition;
import map.logic.Cell;
import map.logic.Room;
import map.observer.IMapObserver;
import map.observer.IUIObserver;
import javafx.geometry.Point2D;
import java.util.ArrayList;
import java.util.List;

public class MapController implements IMapObserver, IUIObserver {
    private final GMap gameMap;
    private final List<IMapObserver> mapObservers;
    private final List<IUIObserver> uiObservers;
    private PlayerPosition playerPosition;

    public MapController() {
        this.gameMap = new GMap();
        this.mapObservers = new ArrayList<>();
        this.uiObservers = new ArrayList<>();
        addMapObserver(this);
        addUIObserver(this);
    }

    // Observer registration methods
    public void addMapObserver(IMapObserver observer) {
        mapObservers.add(observer);
    }

    public void removeMapObserver(IMapObserver observer) {
        mapObservers.remove(observer);
    }

    public void addUIObserver(IUIObserver observer) {
        uiObservers.add(observer);
    }

    public void removeUIObserver(IUIObserver observer) {
        uiObservers.remove(observer);
    }

    // IMapObserver implementation
    @Override
    public void onMapInitialized() {
        // Forward to UI observers
        for (IUIObserver observer : uiObservers) {
            observer.onUIRenderRequested();
        }
    }

    @Override
    public void onPlayerMoved(String areaId, String roomId, int row, int col) {
        // Forward to UI observers
        for (IUIObserver observer : uiObservers) {
            observer.onPlayerPositionChanged(new Point2D(col, row));
        }
    }

    @Override
    public void onCellChanged(String areaId, String roomId, int row, int col, boolean isWalkable, String content) {
        // Forward to UI observers
        for (IUIObserver observer : uiObservers) {
            observer.onMapElementChanged(row, col, content);
        }
    }

    @Override
    public void onRoomTransitionRequested(String targetAreaId, String targetRoomId, int targetRow, int targetCol) {
        // Handle room transition logic
        if (playerPosition != null) {
            var targetArea = gameMap.getArea(targetAreaId);
            if (targetArea != null) {
                var targetRoom = targetArea.getRoom(targetRoomId);
                if (targetRoom != null) {
                    var targetCell = targetRoom.getCell(targetRow, targetCol);
                    playerPosition.moveTo(targetAreaId, targetRoomId, targetCell);
                }
            }
        }
    }

    @Override
    public void onAreaTransitionRequested(String targetAreaId) {
        // Handle area transition logic
        if (playerPosition != null) {
            var targetArea = gameMap.getArea(targetAreaId);
            if (targetArea != null) {
                Cell spawnCell = targetArea.getSpawnCell();
                if (spawnCell != null) {
                    playerPosition.moveTo(
                        targetAreaId,
                        spawnCell.getRoom().getId(),
                        spawnCell
                    );
                } else {
                    // Fallback to first room if no spawn point is set
                    var firstRoom = targetArea.getAllRooms().iterator().next();
                    var startCell = firstRoom.getCell(0, 0);
                    playerPosition.moveTo(targetAreaId, firstRoom.getId(), startCell);
                }
            }
        }
    }

    @Override
    public void onMapStateChanged(String areaId, String roomId, String state) {
        // Forward to UI observers
        for (IUIObserver observer : uiObservers) {
            observer.onUIStateChanged(state);
        }
    }

    @Override
    public void onContentEntered(String areaId, String roomId, int row, int col, String contentType) {
        // Handle content interaction logic
        if (contentType.equals("T")) {
            // Trigger room transition
            onRoomTransitionRequested(areaId, roomId, row, col);
        } else if (contentType.equals("E")) {
            // Show area selection view
            for (IUIObserver observer : uiObservers) {
                observer.onUIStateChanged("SHOW_MAP");
            }
        }
    }

    @Override
    public void onContentExited(String areaId, String roomId, int row, int col, String contentType) {
        // Handle content exit logic
        if (contentType.equals("E")) {
            // Hide area selection view
            for (IUIObserver observer : uiObservers) {
                observer.onUIStateChanged("HIDE_MAP");
            }
        }
    }

    @Override
    public void onMovementRequested(int dx, int dy) {
        if (playerPosition != null) {
            Room currentRoom = gameMap.getArea(playerPosition.getAreaId())
                                    .getRoom(playerPosition.getRoomId());
            
            Cell currentCell = playerPosition.getCell();
            int newRow = currentCell.getRow() + dy;
            int newCol = currentCell.getCol() + dx;
            
            if (newRow >= 0 && newRow < currentRoom.getRows() &&
                newCol >= 0 && newCol < currentRoom.getCols()) {
                
                Cell newCell = currentRoom.getCell(newRow, newCol);
                if (newCell.isWalkable()) {
                    playerPosition.moveTo(
                        playerPosition.getAreaId(),
                        playerPosition.getRoomId(),
                        newCell
                    );
                    
                    // Check for content interaction
                    if (newCell.getContent() != null) {
                        newCell.getContent().onEnter();
                    }
                }
            }
        }
    }

    // IUIObserver implementation
    @Override
    public void onUIRenderRequested() {
        // Handle UI render requests
        // Forward to map observers if needed
    }

    @Override
    public void onPlayerPositionChanged(Point2D newPosition) {
        // Handle player position changes from UI
        // Forward to map observers if needed
    }

    @Override
    public void onMapElementChanged(int row, int col, String elementType) {
        // Handle map element changes from UI
        // Forward to map observers if needed
    }

    @Override
    public void onAreaSelected(String areaId) {
        // Handle area selection from UI
        onAreaTransitionRequested(areaId);
    }

    @Override
    public void onRoomSelected(String areaId, String roomId) {
        // Handle room selection from UI
        onRoomTransitionRequested(areaId, roomId, 0, 0);
    }

    @Override
    public void onCellClicked(int row, int col) {
        // Handle cell clicks from UI
        // Forward to map observers if needed
    }

    @Override
    public void onUIStateChanged(String state) {
        // Handle UI state changes
        // Forward to map observers if needed
    }

    @Override
    public void onViewTransitionRequested(String viewType) {
        // Handle view transition requests from UI
        // Forward to map observers if needed
    }

    @Override
    public void onUIElementUpdated(String elementId, Object newValue) {
        // Handle UI element updates
        // Forward to map observers if needed
    }

    // Getters
    public GMap getGameMap() {
        return gameMap;
    }

    public PlayerPosition getPlayerPosition() {
        return playerPosition;
    }

    public void setPlayerPosition(PlayerPosition playerPosition) {
        this.playerPosition = playerPosition;
    }
} 