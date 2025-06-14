package dialogue.logic;

import java.util.ArrayList;
import java.util.List;
import dialogue.service.DialogueService;
import dialogue.service.DialogueObserver;

public abstract class AbstractDialogueController {
    protected DialogueService service;
    private final List<DialogueObserver> observers = new ArrayList<>();

    public DialogueService getService() {
        return service;
    }

    public AbstractDialogueController(DialogueService service) {
        this.service = service;
    }

    public void addObserver(DialogueObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(DialogueObserver observer) {
        observers.remove(observer);
    }

    protected void notifyObservers() {
        for (DialogueObserver obs : observers) {
            obs.onDialogueUpdated(service);
        }
    }

    public void start(String dialogueId) {
        service.startDialogue(dialogueId);
        notifyObservers();
    }

    public void chooseOption(int index) {
        service.chooseOption(index);
        notifyObservers();
    }

    public abstract void onUserInput(Object input);
}