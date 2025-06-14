package map.logic;

import java.util.*;

public class Area {
    private final String name;
    private final Map<String, Room> rooms = new HashMap<>();
    private GMap gameMap;
    private Cell spawnCell;

    public Area(String name, GMap gameMap) {
        this.name = name;
        this.gameMap = gameMap;
    }

    public String getName() {
        return name;
    }

    public void addRoom(Room room) {
        rooms.put(room.getId(), room);
        room.setArea(this);
    }

    public Room getRoom(String roomId) {
        return rooms.get(roomId);
    }

    public Collection<Room> getAllRooms() {
        return rooms.values();
    }

    public void setSpawnCell(Cell cell) {
        if (cell != null && cell.getRoom() != null && cell.getRoom().getArea() == this) {
            this.spawnCell = cell;
        } else {
            throw new IllegalArgumentException("Spawn cell must belong to this area");
        }
    }

    public Cell getSpawnCell() {
        return spawnCell;
    }

    public String getSpawnRoomId() {
        return spawnCell != null ? spawnCell.getRoom().getId() : null;
    }

    public int getSpawnRow() {
        return spawnCell != null ? spawnCell.getRow() : 0;
    }

    public int getSpawnCol() {
        return spawnCell != null ? spawnCell.getCol() : 0;
    }

    public void onCellChanged(String roomId, int row, int col, boolean isWalkable, String content) {
        if (gameMap != null) {
            gameMap.notifyCellChanged(name, roomId, row, col, isWalkable, content);
        }
    }
}
