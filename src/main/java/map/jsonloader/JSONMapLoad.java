package map.jsonloader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import map.logic.*;
import map.logic.content.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class JSONMapLoad {
    public static GMap loadMapFromJSON(String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        // Load from resources
        InputStream input = JSONMapLoad.class.getResourceAsStream(path);
        if (input == null) {
            throw new FileNotFoundException("Resource not found: " + path);
        }

        JsonNode root = mapper.readTree(input);

        GMap gameMap = new GMap();

        for (JsonNode areaNode : root.get("areas")) {
            String areaName = areaNode.get("name").asText();
            Area area = new Area(areaName, gameMap);

            for (JsonNode roomNode : areaNode.get("rooms")) {
                String roomName = roomNode.get("name").asText();
                int rows = roomNode.get("rows").asInt();
                int cols = roomNode.get("cols").asInt();
                Room room = new Room(roomName, rows, cols);

                // Add walls if defined
                if (roomNode.has("walls")) {
                    JsonNode walls = roomNode.get("walls");
                    if (walls.has("top") && walls.get("top").asBoolean()) {
                        for (int i = 0; i < cols; i++)
                            room.getCell(0, i).setWalkable(false);
                    }
                    if (walls.has("bottom") && walls.get("bottom").asBoolean()) {
                        for (int i = 0; i < cols; i++)
                            room.getCell(rows - 1, i).setWalkable(false);
                    }
                    if (walls.has("left") && walls.get("left").asBoolean()) {
                        for (int i = 0; i < rows; i++)
                            room.getCell(i, 0).setWalkable(false);
                    }
                    if (walls.has("right") && walls.get("right").asBoolean()) {
                        for (int i = 0; i < rows; i++)
                            room.getCell(i, cols - 1).setWalkable(false);
                    }
                }

                // Add exits
                if (roomNode.has("exits")) {
                    for (JsonNode exit : roomNode.get("exits")) {
                        int row = exit.get("row").asInt();
                        int col = exit.get("col").asInt();
                        room.getCell(row, col).setContent(new ExitAreaContent(gameMap));
                    }
                }

                area.addRoom(room);
            }

            // Add spawn cell
            if (areaNode.has("spawn")) {
                JsonNode spawn = areaNode.get("spawn");
                String spawnRoom = spawn.get("room").asText();
                int spawnRow = spawn.get("row").asInt();
                int spawnCol = spawn.get("col").asInt();
                Room room = area.getRoom(spawnRoom);
                if (room != null) {
                    area.setSpawnCell(room.getCell(spawnRow, spawnCol));
                }
            }

            gameMap.addArea(area);
        }

        return gameMap;
    }
}
