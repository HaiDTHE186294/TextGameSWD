package dialogue.logic;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDialogueController {
    protected DialogueManager manager;
    private final List<DialogueObserver> observers = new ArrayList<>();

    public DialogueManager getManager() {
        return manager;
    }

    public AbstractDialogueController(DialogueManager manager) {
        this.manager = manager;
    }

    public void addObserver(DialogueObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(DialogueObserver observer) {
        observers.remove(observer);
    }

    protected void notifyObservers() {
        for (DialogueObserver obs : observers) {
            obs.onDialogueUpdated(manager);
        }
    }

    public void start(String dialogueId) {
        manager.start(dialogueId);
        notifyObservers();
    }

    public void chooseOption(int index) {
        manager.chooseOption(index);
        notifyObservers();
    }

    public abstract void onUserInput(Object input);
}