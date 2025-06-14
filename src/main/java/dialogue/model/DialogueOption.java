package dialogue.model;

import dialogue.model.DialogueContext;
import java.util.List;
import java.util.ArrayList;

public class DialogueOption {
    private String text;
    private String nextDialogueId;
    private List<DialogueAction> actions;
    private List<DialogueCondition> conditions;

    // Constructor không tham số (bắt buộc cho Jackson)
    public DialogueOption() {
        this.actions = new ArrayList<>();
        this.conditions = new ArrayList<>();
    }

    public DialogueOption(String text, String nextDialogueId, List<DialogueAction> actions, List<DialogueCondition> conditions) {
        this.text = text;
        this.nextDialogueId = nextDialogueId;
        this.actions = actions != null ? actions : new ArrayList<>();
        this.conditions = conditions != null ? conditions : new ArrayList<>();
    }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getNextDialogueId() { return nextDialogueId; }
    public void setNextDialogueId(String nextDialogueId) { this.nextDialogueId = nextDialogueId; }

    public List<DialogueAction> getActions() { return actions; }
    public void setActions(List<DialogueAction> actions) {
        this.actions = actions != null ? actions : new ArrayList<>();
    }

    public List<DialogueCondition> getConditions() { return conditions; }
    public void setConditions(List<DialogueCondition> conditions) {
        this.conditions = conditions != null ? conditions : new ArrayList<>();
    }

    public boolean isAvailable(DialogueContext context) {
        if (conditions == null || conditions.isEmpty()) return true;
        return conditions.stream().allMatch(cond -> cond.evaluate(context));
    }
}