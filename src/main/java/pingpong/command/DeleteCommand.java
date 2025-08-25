package pingpong.command;

import pingpong.task.TaskList;
import pingpong.task.Task;
import pingpong.ui.Ui;
import pingpong.storage.Storage;
import pingpong.PingpongException;

/**
 * Command to delete a task from the task list.
 */
public class DeleteCommand extends Command {
    private int taskNumber;

    /**
     * Creates a new DeleteCommand for the specified task number.
     *
     * @param taskNumber the number of the task to delete (1-indexed)
     */
    public DeleteCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    /**
     * Executes the command to delete the specified task from the task list.
     *
     * @param tasks the task list to delete the task from
     * @param ui the UI to display feedback to the user
     * @param storage the storage to save the updated task list
     * @throws PingpongException if the task number is invalid
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws PingpongException {
        Task deletedTask = tasks.deleteTask(taskNumber - 1);
        ui.showTaskDeleted(deletedTask, tasks.size());
        storage.save(tasks.getAllTasks());
    }
}
