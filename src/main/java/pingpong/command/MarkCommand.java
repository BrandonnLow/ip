package pingpong.command;

import pingpong.PingpongException;
import pingpong.storage.Storage;
import pingpong.task.Task;
import pingpong.task.TaskList;
import pingpong.ui.Ui;

/**
 * Command to mark a task as completed.
 */
public class MarkCommand extends Command {
    private int taskNumber;

    /**
     * Creates a new MarkCommand for the specified task number.
     *
     * @param taskNumber the number of the task to mark (1-indexed)
     */
    public MarkCommand(int taskNumber) {
        assert taskNumber > 0 : "Task number should be positive (1-indexed)";

        this.taskNumber = taskNumber;

        assert this.taskNumber > 0 : "Task number should be set and positive";
    }

    /**
     * Executes the command to mark the specified task as completed.
     *
     * @param tasks the task list containing the task to mark
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

        int zeroIndexedTaskNumber = taskNumber - 1;
        assert zeroIndexedTaskNumber >= 0 : "Zero-indexed task number should not be negative";

        Task markedTask = tasks.markTask(zeroIndexedTaskNumber);

        assert markedTask != null : "Marked task should not be null";
        assert markedTask.isDone() : "Task should be marked as done after execution";

        ui.showTaskMarked(markedTask);
        storage.save(tasks.getAllTasks());
    }
}
