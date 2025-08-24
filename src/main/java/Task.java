public class Task {
    private String description;
    private boolean isDone;
    private TaskType type;


    public Task(String description, TaskType type) {
        this.description = description;
        this.isDone = false;
        this.type = type;
    }


    private String getStatus() {
        return (isDone ? "X" : " ");
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void markAsUndone() {
        this.isDone = false;
    }

    public String getDescription() {
        return this.description;
    }

    public TaskType getType() {
        return this.type;
    }

    public boolean isDone() {
        return this.isDone;
    }

    @Override
    public String toString() {
        return  "[" + type.getSymbol() + "][" + getStatus() + "] " + getDescription();
    }
}