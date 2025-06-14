package map.ui;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.paint.Color;
import javafx.geometry.Point2D;

public class RoomUI {
    private final GridPane node;
    private CellUI[][] cellUIs;
    private final int rows;
    private final int cols;
    private Point2D playerPosition;

    public RoomUI(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.node = new GridPane();
        this.cellUIs = new CellUI[rows][cols];
        initializeUI();
    }

    private void initializeUI() {
        // Add border
        node.setBorder(new Border(new BorderStroke(
            Color.GRAY,
            BorderStrokeStyle.SOLID,
            null,
            BorderStroke.THIN
        )));

        // Create empty cell UIs
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                CellUI cellUI = new CellUI();
                cellUIs[row][col] = cellUI;
                node.add(cellUI.getNode(), col, row);
            }
        }
    }

    public void updateCell(int row, int col, String elementType) {
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
            boolean isWalkable = !elementType.equals("wall");
            cellUIs[row][col].update(isWalkable, elementType);
        }
    }

    public void updatePlayerPosition(Point2D newPosition) {
        this.playerPosition = newPosition;
        // Update cell highlighting or player marker
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                cellUIs[row][col].setHighlighted(
                    newPosition.getX() == col && newPosition.getY() == row
                );
            }
        }
    }

    public void handleCellClick(int row, int col) {
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
            cellUIs[row][col].onClick();
        }
    }

    public void updateElement(String elementId, Object newValue) {
        // Parse elementId to get row and col
        String[] parts = elementId.split("_");
        if (parts.length == 2) {
            try {
                int row = Integer.parseInt(parts[0]);
                int col = Integer.parseInt(parts[1]);
                if (row >= 0 && row < rows && col >= 0 && col < cols) {
                    if (newValue instanceof String) {
                        updateCell(row, col, (String) newValue);
                    }
                }
            } catch (NumberFormatException e) {
                // Invalid elementId format
            }
        }
    }

    public GridPane getNode() {
        return node;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
} 