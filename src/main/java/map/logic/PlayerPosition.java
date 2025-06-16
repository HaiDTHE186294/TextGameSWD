package map.logic;

import map.logic.content.CellContent;

public class PlayerPosition {
    private String areaId;
    private String roomId;
    private Cell currentCell;
    private GMap gameMap;

    public PlayerPosition(String areaId, String roomId, Cell cell, GMap gameMap) {
        this.areaId = areaId;
        this.roomId = roomId;
        this.currentCell = cell;
        this.gameMap = gameMap;
    }

    public void moveTo(String areaId, String roomId, Cell cell) {
        this.areaId = areaId;
        this.roomId = roomId;
        this.currentCell = cell;
        
        // Notify GMap about player movement
        if (gameMap != null) {
            gameMap.notifyPlayerMoved(areaId, roomId, cell.getRow(), cell.getCol());
        }

        // Set player position in the content of the new cell and trigger onEnter
        if (cell.getContent() != null && cell.getContent() instanceof CellContent) {
            CellContent content = (CellContent) cell.getContent();
            content.setPlayerPosition(this);
            content.onEnter();
        }
    }

    public Cell getCell() {
        return currentCell;
    }

    public String getAreaId() {
        return areaId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void handleMovement(int dx, int dy) {
        Room currentRoom = gameMap.getArea(areaId).getRoom(roomId);
        Cell currentCell = this.currentCell;
        int newRow = currentCell.getRow() + dy;
        int newCol = currentCell.getCol() + dx;
        
        if (newRow >= 0 && newRow < currentRoom.getRows() &&
            newCol >= 0 && newCol < currentRoom.getCols()) {
            
            Cell newCell = currentRoom.getCell(newRow, newCol);
            if (newCell.isWalkable()) {
                moveTo(areaId, roomId, newCell);
            }
        }
    }
}
