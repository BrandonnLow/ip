package pingpong.task;

/**
 * Represents a task with a description, completion status, and task type.
 * This is the base class for all types of tasks in the Pingpong application.
 */
public class Task {
    private String description;
    private boolean isDone;
    private TaskType type;

    /**
     * Creates a new Task with the specified description and type.
     * The task is initially marked as not completed.
     *
     * @param description the description of the task
     * @param type the type of the task
     */
    public Task(String description, TaskType type) {
        this.description = description;
        this.isDone = false;
        this.type = type;
    }

    /**
     * Gets the status symbol for this task.
     *
     * @return "X" if the task is done, " " (space) if not done
     */
    private String getStatus() {
        return (isDone ? "X" : " ");
    }

    /**
     * Marks this task as completed.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks this task as not completed.
     */
    public void markAsUndone() {
        this.isDone = false;
    }

    /**
     * Gets the description of this task.
     *
     * @return the task description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Gets the type of this task.
     *
     * @return the task type
     */
    public TaskType getType() {
        return this.type;
    }

    /**
     * Checks if this task is completed.
     *
     * @return true if the task is done, false otherwise
     */
    public boolean isDone() {
        return this.isDone;
    }

    /**
     * Returns a string representation of this task.
     *
     * @return a formatted string showing the task type, status, and description
     */
    @Override
    public String toString() {
        return "[" + type.getSymbol() + "][" + getStatus() + "] " + getDescription();
    }
}
