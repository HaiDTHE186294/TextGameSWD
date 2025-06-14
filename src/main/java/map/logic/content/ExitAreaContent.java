package map.logic.content;

import map.logic.CellContent;
import map.logic.PlayerPosition;
import map.logic.GMap;

public class ExitAreaContent implements CellContent {
    private final GMap gameMap;
    private PlayerPosition playerPosition;
    private boolean isShowingMap = false;

    public ExitAreaContent(GMap gameMap) {
        this.gameMap = gameMap;
    }

    public void setPlayerPosition(PlayerPosition playerPosition) {
        this.playerPosition = playerPosition;
    }

    @Override
    public void onEnter() {
        if (gameMap != null) {
            // Toggle map view state
            isShowingMap = !isShowingMap;
            
            // Notify GMap about map view state change
            if (playerPosition != null) {
                gameMap.notifyMapStateChanged(
                    playerPosition.getAreaId(),
                    playerPosition.getRoomId(),
                    isShowingMap ? "SHOW_MAP" : "HIDE_MAP"
                );
            } else {
                // If no player position, still notify but with default values
                gameMap.notifyMapStateChanged(
                    "default",
                    "default",
                    isShowingMap ? "SHOW_MAP" : "HIDE_MAP"
                );
            }
        }
    }

    @Override
    public String render() {
        return "E"; // E for Exit
    }

    public boolean isShowingMap() {
        return isShowingMap;
    }

    public GMap getGameMap() {
        return gameMap;
    }

    // This method will be called by the UI to handle area selection
    public void selectArea(String areaId) {
        if (isShowingMap && gameMap != null) {
            // Move player to the first room of the selected area
            var targetArea = gameMap.getArea(areaId);
            if (targetArea != null) {
                var firstRoom = targetArea.getAllRooms().iterator().next();
                var startCell = firstRoom.getCell(0, 0); // Assuming (0,0) is a valid starting position
                
                if (playerPosition != null) {
                    playerPosition.moveTo(areaId, firstRoom.getId(), startCell);
                }
                
                // Hide map view after selection
                isShowingMap = false;
                gameMap.notifyMapStateChanged(
                    playerPosition != null ? playerPosition.getAreaId() : "default",
                    playerPosition != null ? playerPosition.getRoomId() : "default",
                    "HIDE_MAP"
                );
            }
        }
    }
} 