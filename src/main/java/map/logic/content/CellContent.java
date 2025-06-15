package map.logic.content;

import map.logic.PlayerPosition;

public interface CellContent {
    void onEnter();
    String render();
    void setPlayerPosition(PlayerPosition playerPosition);
} 