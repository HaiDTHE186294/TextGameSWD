package dialogue.logic;

import dialogue.model.DialogueCondition;
import dialogue.model.DialogueContext;
import dialogue.model.DialogueOption;

import java.util.List;

public class Dialogue {
    private String id;
    private String speaker;
    private String text;
    private List<DialogueOption> options;
    private List<DialogueCondition> conditions; // Node chỉ hiện khi đủ điều kiện

    // Constructor không tham số (bắt buộc cho Jackson)
    public Dialogue() {}

    public Dialogue(String id, String speaker, String text, List<DialogueOption> options, List<DialogueCondition> conditions) {
        this.id = id;
        this.speaker = speaker;
        this.text = text;
        this.options = options;
        this.conditions = conditions;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; } // Bổ sung

    public String getSpeaker() { return speaker; }
    public void setSpeaker(String speaker) { this.speaker = speaker; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public List<DialogueOption> getOptions() { return options; }
    public void setOptions(List<DialogueOption> options) { this.options = options; }

    public List<DialogueCondition> getConditions() { return conditions; }
    public void setConditions(List<DialogueCondition> conditions) { this.conditions = conditions; } // Bổ sung

    public boolean isAvailable(DialogueContext context) {
        if (conditions == null || conditions.isEmpty()) return true;
        for (DialogueCondition cond : conditions) {
            if (!cond.evaluate(context)) return false;
        }
        return true;
    }
}