package dialogue.logic;

import dialogue.model.DialogueAction;
import dialogue.model.DialogueContext;
import dialogue.model.DialogueOption;

import java.util.*;

public class DialogueManager {
    private final Map<String, Dialogue> dialogueMap;
    private Dialogue currentDialogue;
    private final DialogueContext context;



    public DialogueManager(List<Dialogue> dialogues, DialogueContext context) {
        this.dialogueMap = new HashMap<>();
        for (Dialogue d : dialogues) dialogueMap.put(d.getId(), d);
        this.context = context;
    }

    public Dialogue getCurrentDialogue() { return currentDialogue; }

    public void setCurrentDialogue(Dialogue dialogue) {
        if (dialogue != null && dialogue.isAvailable(context)) {
            currentDialogue = dialogue;
        } else {
            currentDialogue = null;
        }
    }
    public List<DialogueOption> getCurrentOptions() {
        if (currentDialogue == null) return Collections.emptyList();
        return currentDialogue.getOptions().stream()
                .filter(option -> option.isAvailable(context)).toList();
    }
    public void start(String dialogueId) {
        Dialogue dialogue = dialogueMap.get(dialogueId);
        currentDialogue = (dialogue != null && dialogue.isAvailable(context)) ? dialogue : null;
    }
    public boolean chooseOption(int index) {
        List<DialogueOption> options = getCurrentOptions();
        if (options.isEmpty() || index < 0 || index >= options.size()) return false;
        DialogueOption selected = options.get(index);
        if (selected.getActions() != null)
            for (DialogueAction action : selected.getActions()) action.execute(context);
        if (selected.getNextDialogueId() == null || selected.getNextDialogueId().isEmpty()) {
            currentDialogue = null;
        } else {
            Dialogue next = dialogueMap.get(selected.getNextDialogueId());
            currentDialogue = (next != null && next.isAvailable(context)) ? next : null;
        }
        return true;
    }
    public boolean isFinished() { return currentDialogue == null; }
    public DialogueContext getContext() { return context; }
}