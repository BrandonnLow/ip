package pingpong.command;

import pingpong.PingpongException;
import pingpong.storage.Storage;
import pingpong.task.TaskList;
import pingpong.ui.Ui;

/**
 * Command to list all tasks in the task list.
 */
public class ListCommand extends Command {

    /**
     * Executes the command to display all tasks in the task list.
     *
     * @param tasks the task list to display
     * @param ui the UI to show the task list
     * @param storage the storage (not modified by this command)
     * @throws PingpongException if an error occurs during execution
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws PingpongException {
        ui.showTaskList(tasks.getAllTasks());
    }
}
