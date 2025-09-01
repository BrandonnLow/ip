package pingpong.command;

import pingpong.PingpongException;
import pingpong.storage.Storage;
import pingpong.task.Task;
import pingpong.task.TaskList;
import pingpong.ui.Ui;

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
        assert taskNumber > 0 : "Task number should be positive (1-indexed)";

        this.taskNumber = taskNumber;

        assert this.taskNumber > 0 : "Task number should be set and positive";
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
        assert tasks != null : "Task list should not be null";
        assert ui != null : "UI should not be null";
        assert storage != null : "Storage should not be null";
        assert taskNumber > 0 : "Task number should be positive";

        int originalSize = tasks.size();
        assert originalSize >= 0 : "Original task list size should not be negative";

        int zeroIndexedTaskNumber = taskNumber - 1;
        assert zeroIndexedTaskNumber >= 0 : "Zero-indexed task number should not be negative";

        Task deletedTask = tasks.deleteTask(zeroIndexedTaskNumber);

        assert deletedTask != null : "Deleted task should not be null";
        assert tasks.size() == originalSize - 1 : "Task list size should decrease by 1";

        ui.showTaskDeleted(deletedTask, tasks.size());
        storage.save(tasks.getAllTasks());
    }
}
