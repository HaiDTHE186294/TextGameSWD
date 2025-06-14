package map.logic;

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
}
