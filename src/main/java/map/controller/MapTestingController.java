package map.controller;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import map.logic.*;
import map.logic.content.*;
import map.observer.*;
import map.ui.*;
import javafx.geometry.Point2D;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import map.jsonloader.JSONMapLoad;

public class MapTestingController extends GameApplication implements IMapObserver, IUIObserver {
    private GMap gameMap;
    private final List<IMapObserver> mapObservers;
    private final List<IUIObserver> uiObservers;
    private PlayerPosition playerPosition;
    private MapUI mapUI;
    private AreaUI currentAreaUI;

    String mapPath = "/maps/testmap.json";

    public MapTestingController() {
        this.gameMap = new GMap();
        this.mapObservers = new ArrayList<>();
        this.uiObservers = new ArrayList<>();
        this.playerPosition = new PlayerPosition("TestArea", "room1", null, gameMap);
        gameMap.addObserver(this);
        addMapObserver(this);
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(600);
        settings.setTitle("RPG Map Viewer");
        settings.setVersion("1.0");
    }

    @Override
    protected void initGame() {
        mapUI = new MapUI();
        currentAreaUI = new AreaUI();
        mapUI.setCurrentArea(currentAreaUI);
        FXGL.getGameScene().addUINode(mapUI.getNode());
        
        // MapTestingController observes MapUI for movement requests from the UI
        mapUI.addObserver(this);
        
        // MapUI observes MapTestingController for player position updates
        this.addUIObserver(mapUI);

        initializeTestMap();
        
        // Set the gameMap for AreaMapUI AFTER areas have been initialized
        mapUI.getAreaMapUI().setGameMap(gameMap);
    }

    public void initializeTestMap() {
        try {
            this.gameMap = JSONMapLoad.loadMapFromJSON(mapPath);
            Area startArea = gameMap.getArea("TestArea");
            Room startRoom = startArea.getRoom("room1");
            Cell startCell = startRoom.getCell(1, 1);

            playerPosition = new PlayerPosition("TestArea", "room1", startCell, gameMap);

            for (IUIObserver observer : uiObservers) {
                observer.onPlayerPositionChanged(new Point2D(startCell.getCol(), startCell.getRow()));
            }

            currentAreaUI.showRoom(startRoom);
            notifyMapInitialized();

        } catch (IOException e) {
            System.err.println("Failed to load map from JSON: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }


    // Observer methods
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

    private void notifyMapInitialized() {
        for (IMapObserver observer : mapObservers) {
            observer.onMapInitialized();
        }
        for (IUIObserver observer : uiObservers) {
            observer.onUIRenderRequested();
        }
    }

    // IMapObserver implementation
    @Override
    public void onMapInitialized() {
        for (IUIObserver observer : uiObservers) {
            observer.onUIRenderRequested();
        }
    }

    @Override
    public void onPlayerMoved(String areaId, String roomId, int row, int col) {
        for (IUIObserver observer : uiObservers) {
            observer.onPlayerPositionChanged(new Point2D(col, row));
        }
    }

    @Override
    public void onCellChanged(String areaId, String roomId, int row, int col, boolean isWalkable, String content) {
        for (IUIObserver observer : uiObservers) {
            observer.onMapElementChanged(row, col, content);
        }
    }

    @Override
    public void onRoomTransitionRequested(String targetAreaId, String targetRoomId, int targetRow, int targetCol) {
        if (playerPosition != null) {
            var targetArea = gameMap.getArea(targetAreaId);
            if (targetArea != null) {
                var targetRoom = targetArea.getRoom(targetRoomId);
                if (targetRoom != null) {
                    var targetCell = targetRoom.getCell(targetRow, targetCol);
                    playerPosition.moveTo(targetAreaId, targetRoomId, targetCell);
                    currentAreaUI.showRoom(targetRoom);
                }
            }
        }
    }

    @Override
    public void onAreaTransitionRequested(String targetAreaId) {
        System.out.println("MapTestingController: Area transition requested to: " + targetAreaId); // Debug log
        if (playerPosition != null) {
            var targetArea = gameMap.getArea(targetAreaId);
            if (targetArea != null) {
                Cell startCell;
                Room targetRoom;

                // Prioritize spawn cell if set for the area
                if (targetArea.getSpawnCell() != null) {
                    startCell = targetArea.getSpawnCell();
                    targetRoom = startCell.getRoom();
                } else {
                    // Fallback to the first room's (0,0) if no spawn cell is set
                    targetRoom = targetArea.getAllRooms().iterator().next();
                    startCell = targetRoom.getCell(0, 0);
                }

                if (targetRoom != null && startCell != null) {
                    playerPosition.moveTo(targetAreaId, targetRoom.getId(), startCell);
                    currentAreaUI.showRoom(targetRoom);

                    // Hide the area map and show the room view
                    mapUI.hideAreaSelection();
                }
            }
        }
    }

    @Override
    public void onMapStateChanged(String areaId, String roomId, String state) {
        for (IUIObserver observer : uiObservers) {
            observer.onUIStateChanged(state);
        }
    }


    // IUIObserver implementation
    @Override
    public void onUIRenderRequested() {
        if (currentAreaUI != null) {
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
        System.out.println("MapTestingController: Area selected received: " + areaId);
        onAreaTransitionRequested(areaId);
    }

    @Override
    public void onRoomSelected(String areaId, String roomId) {
        onRoomTransitionRequested(areaId, roomId, 0, 0);
    }

    @Override
    public void onUIStateChanged(String state) {
        switch (state) {
            case "SHOW_MAP":
                mapUI.showAreaSelection();
                break;
            case "HIDE_MAP":
                mapUI.hideAreaSelection();
                break;
        }
    }

    @Override
    public void onViewTransitionRequested(String viewType) {
        switch (viewType) {
            case "AREA_SELECTION":
                mapUI.showAreaSelection();
                break;
            case "ROOM_VIEW":
                mapUI.hideAreaSelection();
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
        playerPosition.handleMovement(dx, dy);
    }

    protected void onExit() {
        // Cleanup observers
        mapObservers.clear();
        uiObservers.clear();
        
        // Cleanup UI
        if (mapUI != null) {
            mapUI.getNode().getChildren().clear();
        }
        if (currentAreaUI != null) {
            currentAreaUI.getNode().getChildren().clear();
        }
        
        // Cleanup game state
        if (gameMap != null) {
            gameMap.clear();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
} 