package map.ui;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.geometry.Point2D;
import map.logic.Area;
import map.logic.GMap;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import map.observer.IUIObserver;
import javafx.scene.layout.StackPane;

public class AreaMapUI extends Pane {
    private GMap gameMap;
    private final List<IUIObserver> uiObservers;
    private Map<String, Point2D> areaPositions; // To store positions of areas on the map
    private static final double AREA_SIZE = 60;
    private static final double PADDING = 20;

    public AreaMapUI(List<IUIObserver> uiObservers) {
        this.uiObservers = uiObservers;
        this.areaPositions = new HashMap<>();
        
        Rectangle backgroundRect = new Rectangle(800, 600, Color.LIGHTBLUE);
        getChildren().add(backgroundRect);

        setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2))));
        setPrefSize(800, 600);
    }

    public void setGameMap(GMap gameMap) {
        this.gameMap = gameMap;
        drawMap();
    }

    private void drawMap() {
        getChildren().removeIf(node -> !(node instanceof Rectangle && ((Rectangle)node).getFill().equals(Color.LIGHTBLUE)));
        areaPositions.clear();

        if (gameMap == null) return;

        int xOffset = 0;
        int yOffset = 0;

        System.out.println("AreaMapUI: Drawing map with " + gameMap.getAllAreas().size() + " areas."); // Debug log

        for (Area area : gameMap.getAllAreas()) {
            Point2D pos = new Point2D(PADDING + xOffset, PADDING + yOffset);
            areaPositions.put(area.getName(), pos);

            System.out.println("AreaMapUI: Drawing area " + area.getName() + " at " + pos); // Debug log

            Rectangle areaNode = new Rectangle(AREA_SIZE, AREA_SIZE, Color.LIGHTGREEN);
            areaNode.setStroke(Color.DARKGREEN);
            
            Label areaLabel = new Label(area.getName());
            areaLabel.setStyle("-fx-text-fill: black; -fx-font-weight: bold;"); // Ensure text is visible and bold

            StackPane areaContainer = new StackPane();
            areaContainer.getChildren().addAll(areaNode, areaLabel);
            areaContainer.setLayoutX(pos.getX());
            areaContainer.setLayoutY(pos.getY());

            // Add click handler for area selection to the container
            areaContainer.setOnMouseClicked(event -> notifyAreaSelected(area.getName()));
            
            getChildren().add(areaContainer);

            xOffset += AREA_SIZE + PADDING;
            if (xOffset > 300) { 
                xOffset = 0;
                yOffset += AREA_SIZE + PADDING;
            }
        }
    }

    private void notifyAreaSelected(String areaId) {
        System.out.println("AreaMapUI: Area selected: " + areaId); // Debug log
        for (IUIObserver observer : uiObservers) {
            observer.onAreaSelected(areaId);
        }
    }
} 