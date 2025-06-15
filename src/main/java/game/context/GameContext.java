package game.context;

import java.util.HashMap;
import java.util.Map;

public class GameContext {
    private final Map<String, Object> contextData;

    public GameContext() {
        this.contextData = new HashMap<>();
    }

    public void setData(String key, Object value) {
        contextData.put(key, value);
    }

    public Object getData(String key) {
        return contextData.get(key);
    }

    public boolean hasData(String key) {
        return contextData.containsKey(key);
    }

    public void removeData(String key) {
        contextData.remove(key);
    }

    public void clear() {
        contextData.clear();
    }
}