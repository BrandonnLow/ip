package pingpong.command;

import pingpong.PingpongException;
import pingpong.storage.Storage;
import pingpong.task.Task;
import pingpong.task.TaskList;
import pingpong.ui.Ui;

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
        assert description != null : "Todo description should not be null";
        assert !description.trim().isEmpty() : "Todo description should not be empty";

        this.description = description;

        assert this.description != null : "Todo description should be set";
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
        assert tasks != null : "Task list should not be null";
        assert ui != null : "UI should not be null";
        assert storage != null : "Storage should not be null";
        assert description != null : "Description should not be null";

        int originalSize = tasks.size();
        Task todo = tasks.addTodo(description);

        assert todo != null : "Created todo should not be null";
        assert tasks.size() == originalSize + 1 : "Task list size should increase by 1";
        assert tasks.getAllTasks().contains(todo) : "Todo should be in task list";

        ui.showTaskAdded(todo, tasks.size());
        storage.save(tasks.getAllTasks());
    }
}
