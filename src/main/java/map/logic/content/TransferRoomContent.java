package map.logic.content;

import map.logic.content.CellContent;
import map.logic.PlayerPosition;

public class TransferRoomContent implements CellContent {
    private final String targetAreaId;
    private final String targetRoomId;
    private final int targetRow;
    private final int targetCol;
    private PlayerPosition playerPosition;

    public TransferRoomContent(String targetAreaId, String targetRoomId, int targetRow, int targetCol) {
        this.targetAreaId = targetAreaId;
        this.targetRoomId = targetRoomId;
        this.targetRow = targetRow;
        this.targetCol = targetCol;
    }

    @Override
    public void setPlayerPosition(PlayerPosition playerPosition) {
        this.playerPosition = playerPosition;
    }

    @Override
    public void onEnter() {
        if (playerPosition != null) {
            // Get the target cell from the target room
            var targetArea = playerPosition.getCell().getRoom().getArea();
            var targetRoom = targetArea.getRoom(targetRoomId);
            var targetCell = targetRoom.getCell(targetRow, targetCol);
            
            // Move player to the target cell
            playerPosition.moveTo(targetAreaId, targetRoomId, targetCell);
        }
    }

    @Override
    public String render() {
        return "T"; // T for Transfer
    }
} 