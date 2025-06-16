package map.controller;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import map.logic.*;
import map.logic.content.*;
import map.observer.*;
import map.ui.*;
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
        // Create a test area and room
        Area testArea = new Area("TestArea", gameMap);
        Room testRoom = new Room("room1", 10, 15); // 10 rows, 15 columns
        
        // Add some walls
        for (int i = 0; i < 15; i++) {
            testRoom.getCell(0, i).setWalkable(false);  // Top wall
            testRoom.getCell(9, i).setWalkable(false);  // Bottom wall
        }
        for (int i = 0; i < 10; i++) {
            testRoom.getCell(i, 0).setWalkable(false);  // Left wall
            testRoom.getCell(i, 14).setWalkable(false); // Right wall
        }
        
        // Add an exit
        Cell exitCell = testRoom.getCell(5, 13);
        exitCell.setContent(new ExitAreaContent(gameMap));
        
        testArea.addRoom(testRoom);
        gameMap.addArea(testArea);
        
        // Set a walkable spawn cell for TestArea
        testArea.setSpawnCell(testRoom.getCell(1, 1));

        // Add a second test area
        Area areaTwo = new Area("AreaTwo", gameMap);
        Room roomTwo = new Room("roomA", 8, 12);
        areaTwo.addRoom(roomTwo);
        gameMap.addArea(areaTwo);

        // Add an exit to AreaTwo
        Cell exitCellTwo = roomTwo.getCell(1, 1); // Assuming (1,1) is walkable
        exitCellTwo.setContent(new ExitAreaContent(gameMap));

        // Add a third test area
        Area areaThree = new Area("AreaThree", gameMap);
        Room roomThree = new Room("roomX", 7, 10);
        areaThree.addRoom(roomThree);
        gameMap.addArea(areaThree);

        // Add an exit to AreaThree
        Cell exitCellThree = roomThree.getCell(1, 1); // Assuming (1,1) is walkable
        exitCellThree.setContent(new ExitAreaContent(gameMap));
        
        // Set initial player position
        playerPosition = new PlayerPosition("TestArea", "room1", testRoom.getCell(1, 1), gameMap);
        
        // Notify UI about initial player position
        for (IUIObserver observer : uiObservers) {
            observer.onPlayerPositionChanged(new Point2D(playerPosition.getCell().getCol(), playerPosition.getCell().getRow()));
        }
        
        // Show the initial room
        currentAreaUI.showRoom(testRoom);
        
        // Notify observers
        notifyMapInitialized();
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
        System.out.println("MapTestingController: Area selected received: " + areaId);
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

    @Override
    public void onMovementRequested(int dx, int dy) {
        movePlayer(dx, dy);
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