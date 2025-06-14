package dialogue.ui;

import dialogue.controller.DialogueController;
import dialogue.model.DialogueContext;
import dialogue.service.DialogueService;
import dialogue.service.DialogueObserver;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class DialogueUIService implements DialogueObserver {
    private final Label speakerLabel;
    private final TextArea dialogueText;
    private final VBox optionsBox;
    private final DialogueController controller;

    public DialogueUIService(Label speakerLabel, TextArea dialogueText, VBox optionsBox, DialogueController controller) {
        this.speakerLabel = speakerLabel;
        this.dialogueText = dialogueText;
        this.optionsBox = optionsBox;
        this.controller = controller;
    }

    @Override
    public void onDialogueUpdated(DialogueService service) {
        updateUI(service);
    }

    private void updateUI(DialogueService service) {
        var current = service.getCurrentDialogue();
        optionsBox.getChildren().clear();

        if (current == null) {
            speakerLabel.setText("Kết thúc hội thoại.");
            dialogueText.setText("");
            return;
        }

        speakerLabel.setText("[" + current.getSpeaker() + "]");
        dialogueText.setText(current.getText());

        var options = service.getCurrentOptions();
        for (int i = 0; i < options.size(); i++) {
            var opt = options.get(i);
            Button btn = new Button(opt.getText());
            int idx = i;
            btn.setOnAction(e -> controller.onUserInput(idx));
            optionsBox.getChildren().add(btn);
        }
    }
}