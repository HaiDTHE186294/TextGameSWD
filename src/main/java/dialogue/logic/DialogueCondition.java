package dialogue.logic;

@FunctionalInterface
public interface DialogueCondition {
    boolean evaluate(DialogueContext context);
}