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
        assert description != null : "Task description should not be null";
        assert !description.trim().isEmpty() : "Task description should not be empty";
        assert type != null : "Task type should not be null";

        this.description = description;
        this.isDone = false;
        this.type = type;

        assert this.description != null : "Task description should be set";
        assert !this.isDone : "New task should not be marked as done";
        assert this.type != null : "Task type should be set";
    }

    /**
     * Gets the status symbol for this task.
     *
     * @return "X" if the task is done, " " (space) if not done
     */
    private String getStatus() {
        String status = (isDone ? "X" : " ");
        assert status != null : "Status should not be null";
        assert status.equals("X") || status.equals(" ") : "Status should be either 'X' or ' '";
        return status;
    }

    /**
     * Marks this task as completed.
     */
    public void markAsDone() {
        this.isDone = true;
        assert this.isDone : "Task should be marked as done after calling markAsDone()";
    }

    /**
     * Marks this task as not completed.
     */
    public void markAsUndone() {
        this.isDone = false;
        assert !this.isDone : "Task should be marked as undone after calling markAsUndone()";
    }

    /**
     * Gets the description of this task.
     *
     * @return the task description
     */
    public String getDescription() {
        assert this.description != null : "Task description should not be null";
        return this.description;
    }

    /**
     * Gets the type of this task.
     *
     * @return the task type
     */
    public TaskType getType() {
        assert this.type != null : "Task type should not be null";
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
        assert type != null : "Task type should not be null";
        assert type.getSymbol() != null : "Task type symbol should not be null";
        assert description != null : "Task description should not be null";

        String result = "[" + type.getSymbol() + "][" + getStatus() + "] " + getDescription();

        assert result != null : "String representation should not be null";
        assert result.contains(type.getSymbol()) : "String should contain task type symbol";
        assert result.contains(description) : "String should contain task description";

        return result;
    }
}
