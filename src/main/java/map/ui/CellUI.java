package map.ui;

import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CellUI {
    private final StackPane node;
    private final Rectangle background;
    private final Label content;
    private boolean isHighlighted;

    public CellUI() {
        this.node = new StackPane();
        this.background = new Rectangle(40, 40);
        this.content = new Label();
        this.isHighlighted = false;
        
        initializeUI();
    }

    private void initializeUI() {
        // Set up background
        background.setFill(Color.WHITE);
        background.setStroke(Color.GRAY);
        
        // Set up content label
        content.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        
        // Add to node
        node.getChildren().addAll(background, content);
    }

    public void update(boolean isWalkable, String elementType) {
        // Update background color based on walkability
        background.setFill(isWalkable ? Color.WHITE : Color.GRAY);
        
        // Update content label with special styling for T and E
        content.setText(elementType);
        
        if (elementType.equals("T")) {
            content.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2196F3;"); // Blue for Transfer
        } else if (elementType.equals("E")) {
            content.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #F44336;"); // Red for Exit
        } else {
            content.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        }
    }

    public void setHighlighted(boolean highlighted) {
        this.isHighlighted = highlighted;
        background.setStroke(highlighted ? Color.BLUE : Color.GRAY);
        background.setStrokeWidth(highlighted ? 2 : 1);
    }

    public void onClick() {
        // Toggle highlight on click
        setHighlighted(!isHighlighted);
    }

    public StackPane getNode() {
        return node;
    }
} 