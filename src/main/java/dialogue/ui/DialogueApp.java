package dialogue.ui;

import game.context.GameContext;
import dialogue.model.DialogueContext;
import dialogue.model.DialogueModel;
import dialogue.service.DialogueService;
import dialogue.controller.DialogueController;
import dialogue.logic.DialogueLoader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DialogueApp extends Application {
    private DialogueController controller;
    private DialogueUIService uiService;
    private Label speakerLabel;
    private TextArea dialogueText;
    private VBox optionsBox;

    @Override
    public void start(Stage primaryStage) {
        try {
            System.out.println("Starting application...");

            System.out.println("Loading dialogues from JSON...");
            var dialogues = DialogueLoader.loadFromJson("dialogue_data.json");
            System.out.println("Loaded " + dialogues.size() + " dialogues");

            System.out.println("Creating context and service...");
            var context = new DialogueContext();
            var service = new DialogueService(dialogues, context);

            System.out.println("Creating controller...");
            controller = new JavaFXDialogueController(service);

            System.out.println("Setting up UI components...");
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

            System.out.println("Setting up UI service...");
            uiService = new DialogueUIService(speakerLabel, dialogueText, optionsBox, controller);
            service.addObserver(uiService);

            System.out.println("Starting dialogue...");
            controller.start("start");

            System.out.println("Application started successfully!");
        } catch (Exception e) {
            System.err.println("Error starting application:");
            e.printStackTrace();
            throw new RuntimeException("Failed to start application", e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}