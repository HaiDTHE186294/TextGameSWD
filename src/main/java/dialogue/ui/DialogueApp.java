package dialogue.ui;

import dialogue.logic.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;

public class DialogueApp extends Application {
    private JavaFXDialogueController controller;
    private Label speakerLabel;
    private TextArea dialogueText;
    private VBox optionsBox;


    @Override
    public void start(Stage primaryStage) throws Exception {
        List<Dialogue> dialogues = DialogueLoader.loadFromJson("dialogue_data.json");
        DialogueContext context = new DialogueContext();
        DialogueManager manager = new DialogueManager(dialogues, context);
        controller = new JavaFXDialogueController(manager);

        speakerLabel = new Label();
        dialogueText = new TextArea();
        dialogueText.setEditable(false);
        dialogueText.setWrapText(true);
        optionsBox = new VBox(10);

        VBox root = new VBox(10, speakerLabel, dialogueText, optionsBox);
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Hội thoại JavaFX");
        primaryStage.show();

        controller.addObserver(mgr -> updateUI()); // UI observer
        controller.start("start");
        updateUI();
    }

    private void updateUI() {
        DialogueManager manager = controller.getManager();
        Dialogue current = manager.getCurrentDialogue();
        optionsBox.getChildren().clear();

        if (current == null) {
            speakerLabel.setText("Kết thúc hội thoại.");
            dialogueText.setText("");
            return;
        }
        speakerLabel.setText("[" + current.getSpeaker() + "]");
        dialogueText.setText(current.getText());

        List<DialogueOption> options = manager.getCurrentOptions();
        for (int i = 0; i < options.size(); i++) {
            DialogueOption opt = options.get(i);
            Button btn = new Button(opt.getText());
            int idx = i;
            btn.setOnAction(e -> controller.onUserInput(idx));
            optionsBox.getChildren().add(btn);
        }
    }

    public static void main(String[] args) { launch(args); }
}