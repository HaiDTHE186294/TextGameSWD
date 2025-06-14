package dialogue.ui;

import dialogue.controller.DialogueController;
import dialogue.service.DialogueService;

public class JavaFXDialogueController extends DialogueController {
    public JavaFXDialogueController(DialogueService service) {
        super(service);
    }

    @Override
    public void onUserInput(Object input) {
        if (input instanceof Integer idx) {
            chooseOption(idx);
        }
    }
}