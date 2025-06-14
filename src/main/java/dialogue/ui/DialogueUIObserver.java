package dialogue.ui;

import dialogue.logic.DialogueManager;
import dialogue.logic.DialogueObserver;

public class DialogueUIObserver implements DialogueObserver {
    @Override
    public void onDialogueUpdated(DialogueManager manager) {
        // Cập nhật UI JavaFX ở đây (Label, Button, ...)
    }
}