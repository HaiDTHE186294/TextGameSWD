package map.logic;

import java.util.*;
import map.logic.content.CellContent;

public class Cell {
    private final int row;
    private final int col;
    private boolean walkable;
    private List<CellContent> contents = new ArrayList<>();
    private Room room; // Reference to parent room

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.walkable = true;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
        notifyRoom();
    }

    public List<CellContent> getContents() {
        return contents;
    }

    public void addContent(CellContent content) {
        this.contents.add(content);
        notifyRoom();
    }

    public void removeContent(CellContent content) {
        this.contents.remove(content);
        notifyRoom();
    }

    public void clearContents() {
        this.contents.clear();
        notifyRoom();
    }

    public CellContent getContent() {
        return contents.isEmpty() ? null : contents.get(0);
    }

    public void setContent(CellContent content) {
        clearContents();
        if (content != null) {
            addContent(content);
        }
    }

    private void notifyRoom() {
        if (room != null) {
            String content = contents.isEmpty() ? "" : contents.get(0).render();
            room.onCellChanged(row, col, walkable, content);
        }
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }
}
