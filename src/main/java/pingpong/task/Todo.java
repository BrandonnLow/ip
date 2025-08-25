package pingpong.task;

/**
 * Represents a simple todo task without any time constraints.
 */
public class Todo extends Task {

    /**
     * Creates a new Todo task with the specified description.
     *
     * @param todo the description of the todo task
     */
    public Todo(String todo) {
        super(todo, TaskType.TODO);
    }
}
