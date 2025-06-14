package dialogue.logic;

@FunctionalInterface
public interface DialogueAction {
    void execute(DialogueContext context);
}