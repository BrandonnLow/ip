package pingpong.command;

import java.util.ArrayList;

import pingpong.PingpongException;
import pingpong.storage.Storage;
import pingpong.task.Task;
import pingpong.task.TaskList;
import pingpong.ui.Ui;

/**
 * Command to add multiple todo tasks to the task list using varargs.
 */
public class AddMultipleCommand extends Command {
    private String[] descriptions;

    /**
     * Creates a new AddMultipleCommand with the specified descriptions.
     *
     * @param descriptions the descriptions of the todo tasks (varargs)
     */
    public AddMultipleCommand(String... descriptions) {
        this.descriptions = descriptions;
    }

    /**
     * Executes the command to add multiple todo tasks to the task list.
     *
     * @param tasks the task list to add the todos to
     * @param ui the UI to display feedback to the user
     * @param storage the storage to save the updated task list
     * @throws PingpongException if an error occurs during execution
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws PingpongException {
        ArrayList<Task> addedTasks = tasks.addTodos(descriptions);
        ui.showTasksAdded(addedTasks, tasks.size());
        storage.save(tasks.getAllTasks());
    }
}