package map.ui;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import map.logic.Room;
import map.logic.Cell;

public class AreaUI {
    private final StackPane root;
    private final GridPane roomGrid;
    private Room currentRoom;
    private Point2D playerPosition;
    private static final int CELL_SIZE = 40;
    private static final Color WALL_COLOR = Color.GRAY;
    private static final Color PLAYER_COLOR = Color.BLUE;
    private static final Color EXIT_COLOR = Color.RED;

    public AreaUI() {
        root = new StackPane();
        roomGrid = new GridPane();
        roomGrid.setHgap(2);
        roomGrid.setVgap(2);
        root.getChildren().add(roomGrid);
    }

    public void showRoom(Room room) {
        this.currentRoom = room;
        renderRoom();
    }

    public void updatePlayerPosition(Point2D position) {
        this.playerPosition = position;
        renderRoom();
    }

    public void updateMapElement(int row, int col, String elementType) {
        renderRoom();
    }

    public void handleCellClick(int row, int col) {
        if (currentRoom != null) {
            Cell cell = currentRoom.getCell(row, col);
            if (cell != null && cell.isWalkable()) {
                // Handle cell click logic
            }
        }
    }

    public void updateElement(String elementId, Object newValue) {
        // Handle specific element updates
    }

    public void handleRoomSelection(String roomId) {
        // Find the room in the current area and show it
        if (currentRoom != null && currentRoom.getId().equals(roomId)) {
            showRoom(currentRoom);
        }
    }

    private void renderRoom() {
        if (currentRoom == null) return;

        roomGrid.getChildren().clear();
        
        for (int row = 0; row < currentRoom.getRows(); row++) {
            for (int col = 0; col < currentRoom.getCols(); col++) {
                Cell cell = currentRoom.getCell(row, col);
                Rectangle cellRect = new Rectangle(CELL_SIZE, CELL_SIZE);
                
                boolean isPlayer = playerPosition != null && 
                                 playerPosition.getX() == col && 
                                 playerPosition.getY() == row;
                
                if (!cell.isWalkable()) {
                    cellRect.setFill(WALL_COLOR);
                } else if (isPlayer) {
                    cellRect.setFill(PLAYER_COLOR);
                } else if (cell.getContent() != null && cell.getContent().render().equals("E")) {
                    cellRect.setFill(EXIT_COLOR);
                } else {
                    cellRect.setFill(Color.WHITE);
                    cellRect.setStroke(Color.LIGHTGRAY);
                }
                
                roomGrid.add(cellRect, col, row);
            }
        }
    }

    public StackPane getNode() {
        return root;
    }
} 