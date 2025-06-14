package dialogue.logic;

import java.util.List;

public class DialogueOption {
    private String text;
    private String nextDialogueId;
    private List<DialogueAction> actions;
    private List<DialogueCondition> conditions;

    public DialogueOption() {}

    public DialogueOption(String text, String nextDialogueId, List<DialogueAction> actions, List<DialogueCondition> conditions) {
        this.text = text;
        this.nextDialogueId = nextDialogueId;
        this.actions = actions;
        this.conditions = conditions;
    }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public String getNextDialogueId() { return nextDialogueId; }
    public void setNextDialogueId(String nextDialogueId) { this.nextDialogueId = nextDialogueId; }
    public List<DialogueAction> getActions() { return actions; }
    public void setActions(List<DialogueAction> actions) { this.actions = actions; }
    public List<DialogueCondition> getConditions() { return conditions; }
    public void setConditions(List<DialogueCondition> conditions) { this.conditions = conditions; }

    public boolean isAvailable(DialogueContext context) {
        if (conditions == null || conditions.isEmpty()) return true;
        for (DialogueCondition cond : conditions) if (!cond.evaluate(context)) return false;
        return true;
    }
}