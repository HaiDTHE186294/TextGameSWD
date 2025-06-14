package dialogue.ui;

import dialogue.logic.AbstractDialogueController;
import dialogue.logic.DialogueManager;

public class JavaFXDialogueController extends AbstractDialogueController {
    public JavaFXDialogueController(DialogueManager manager) { super(manager); }

    @Override
    public void onUserInput(Object input) {
        if (input instanceof Integer idx) chooseOption(idx);
        // Xử lý input khác nếu cần
    }
}