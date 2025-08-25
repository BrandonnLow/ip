package pingpong.command;

import pingpong.task.TaskList;
import pingpong.task.Task;
import pingpong.ui.Ui;
import pingpong.storage.Storage;
import pingpong.PingpongException;

/**
 * Command to add a new Todo task to the task list.
 */
public class AddTodoCommand extends Command {
    private String description;

    /**
     * Creates a new AddTodoCommand with the specified description.
     *
     * @param description the description of the todo task
     */
    public AddTodoCommand(String description) {
        this.description = description;
    }

    /**
     * Executes the command to add a todo task to the task list.
     *
     * @param tasks the task list to add the todo to
     * @param ui the UI to display feedback to the user
     * @param storage the storage to save the updated task list
     * @throws PingpongException if an error occurs during execution
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws PingpongException {
        Task todo = tasks.addTodo(description);
        ui.showTaskAdded(todo, tasks.size());
        storage.save(tasks.getAllTasks());
    }
}
