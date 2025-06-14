package dialogue.model;

import game.context.GameContext;

public class DialogueContext {
    private final GameContext gameContext;

    public DialogueContext() {
        this.gameContext = new GameContext();
    }

    public Object getData(String key) {
        return gameContext.getData(key);
    }

    public void setData(String key, Object value) {
        gameContext.setData(key, value);
    }

    public boolean hasData(String key) {
        return gameContext.hasData(key);
    }

    public void removeData(String key) {
        gameContext.removeData(key);
    }

    public void clear() {
        gameContext.clear();
    }
}