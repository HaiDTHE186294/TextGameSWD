package map.controller;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.dsl.FXGL; 
import javafx.scene.input.KeyCode;
import map.logic.*;
import map.logic.content.CellContent;
import map.logic.content.ExitAreaContent;
import map.observer.IMapObserver;
import map.observer.IUIObserver;
import map.ui.MapUI;
import map.ui.AreaUI;
import javafx.geometry.Point2D;
import java.util.ArrayList;
import java.util.List;

public class MapTestingController extends GameApplication implements IMapObserver, IUIObserver {
    private final GMap gameMap;
    private final List<IMapObserver> mapObservers;
    private final List<IUIObserver> uiObservers;
    private PlayerPosition playerPosition;
    private MapUI mapUI;
    private AreaUI currentAreaUI;

    public MapTestingController() {
        this.gameMap = new GMap();
        this.mapObservers = new ArrayList<>();
        this.uiObservers = new ArrayList<>();
        this.playerPosition = new PlayerPosition("TestArea", "room1", null, gameMap);
        addMapObserver(this);
        addUIObserver(this);
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
        FXGL.getGameWorld().addUINode(mapUI.getNode());
        
        initializeTestMap();
    }

    public void initializeTestMap() {
        // Create a test area and room
        Area testArea = new Area("TestArea", gameMap);
        Room testRoom = new Room("room1", 10, 15); // 10 rows, 15 columns
        
        // Add some walls
        for (int i = 0; i < 10; i++) {
            testRoom.getCell(0, i).setWalkable(false);
            testRoom.getCell(9, i).setWalkable(false);
        }
        for (int i = 0; i < 15; i++) {
            testRoom.getCell(i, 0).setWalkable(false);
            testRoom.getCell(i, 14).setWalkable(false);
        }
        
        // Add an exit
        Cell exitCell = testRoom.getCell(5, 13);
        exitCell.setContent((CellContent) new ExitAreaContent(gameMap));
        
        testArea.addRoom(testRoom);
        gameMap.addArea(testArea);
        
        // Set initial player position
        playerPosition = new PlayerPosition("TestArea", "room1", testRoom.getCell(1, 1), gameMap);
        
        // Show the initial room
        currentAreaUI.showRoom(testRoom);
        
        // Notify observers
        notifyMapInitialized();
    }

    @Override
    protected void initInput() {
        Input input = FXGL.getInput();
        
        input.addAction(new UserAction("Move Up") {
            @Override
            protected void onActionBegin() {
                movePlayer(0, -1);
            }
        }, KeyCode.W);
        
        input.addAction(new UserAction("Move Down") {
            @Override
            protected void onActionBegin() {
                movePlayer(0, 1);
            }
        }, KeyCode.S);
        
        input.addAction(new UserAction("Move Left") {
            @Override
            protected void onActionBegin() {
                movePlayer(-1, 0);
            }
        }, KeyCode.A);
        
        input.addAction(new UserAction("Move Right") {
            @Override
            protected void onActionBegin() {
                movePlayer(1, 0);
            }
        }, KeyCode.D);
    }

    public void movePlayer(int dx, int dy) {
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
        if (playerPosition != null) {
            var targetArea = gameMap.getArea(targetAreaId);
            if (targetArea != null) {
                var firstRoom = targetArea.getAllRooms().iterator().next();
                var startCell = firstRoom.getCell(0, 0);
                playerPosition.moveTo(targetAreaId, firstRoom.getId(), startCell);
                currentAreaUI.showRoom(firstRoom);
            }
        }
    }

    @Override
    public void onMapStateChanged(String areaId, String roomId, String state) {
        for (IUIObserver observer : uiObservers) {
            observer.onUIStateChanged(state);
        }
    }

    @Override
    public void onContentEntered(String areaId, String roomId, int row, int col, String contentType) {
        if (contentType.equals("T")) {
            onRoomTransitionRequested(areaId, roomId, row, col);
        } else if (contentType.equals("E")) {
            for (IUIObserver observer : uiObservers) {
                observer.onUIStateChanged("SHOW_MAP");
            }
        }
    }

    @Override
    public void onContentExited(String areaId, String roomId, int row, int col, String contentType) {
        if (contentType.equals("E")) {
            for (IUIObserver observer : uiObservers) {
                observer.onUIStateChanged("HIDE_MAP");
            }
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
        onAreaTransitionRequested(areaId);
    }

    @Override
    public void onRoomSelected(String areaId, String roomId) {
        onRoomTransitionRequested(areaId, roomId, 0, 0);
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

    public GMap getGameMap() {
        return gameMap;
    }

    public PlayerPosition getPlayerPosition() {
        return playerPosition;
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