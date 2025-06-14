package map.logic;

import java.util.*;
import map.observer.IMapObserver;

public class Room {
    private final String id;
    private final int rows;
    private final int cols;
    private final Cell[][] cells;
    private Area area; // Reference to parent area
    private final List<IMapObserver> observers = new ArrayList<>();

    public Room(String id, int rows, int cols) {
        this.id = id;
        this.rows = rows;
        this.cols = cols;
        this.cells = new Cell[rows][cols];
        initializeCells();
    }

    private void initializeCells() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                cells[row][col] = new Cell(row, col);
                cells[row][col].setRoom(this);
            }
        }
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Area getArea() {
        return area;
    }

    public String getId() {
        return id;
    }

    public Cell getCell(int row, int col) {
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
            return cells[row][col];
        }
        return null;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public void addObserver(IMapObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(IMapObserver observer) {
        observers.remove(observer);
    }

    public void onCellChanged(int row, int col, boolean isWalkable, String content) {
        if (area != null) {
            area.onCellChanged(id, row, col, isWalkable, content);
        }
    }
}
