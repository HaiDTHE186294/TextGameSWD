package quest.model;

public class QuestProgress {
    private boolean started;
    private boolean completed;
    private int currentAmount; // số lượng đã thu thập/giết
    private boolean talked; // cho quest nói chuyện

    public QuestProgress() {
        this.started = false;
        this.completed = false;
        this.currentAmount = 0;
        this.talked = false;
    }

    public boolean isStarted() { return started; }
    public void setStarted(boolean started) { this.started = started; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    public int getCurrentAmount() { return currentAmount; }
    public void setCurrentAmount(int currentAmount) { this.currentAmount = currentAmount; }
    public boolean isTalked() { return talked; }
    public void setTalked(boolean talked) { this.talked = talked; }
}