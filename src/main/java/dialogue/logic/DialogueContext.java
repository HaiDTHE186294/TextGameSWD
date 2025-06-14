package dialogue.logic;

import java.util.HashMap;
import java.util.Map;

public class DialogueContext {
    private Map<String, Object> state;

    // Constructor mặc định
    public DialogueContext() {
        this.state = new HashMap<>();
    }

    // Constructor nhận Map
    public DialogueContext(Map<String, Object> state) {
        this.state = new HashMap<>(state);
    }

    public Object get(String key) { return state.get(key); }
    public void set(String key, Object value) { state.put(key, value); }
}