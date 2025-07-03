package map.ui;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.paint.Color;

public class RoomUI {
    private final GridPane node;
    private CellUI[][] cellUIs;
    private final int rows;
    private final int cols;

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