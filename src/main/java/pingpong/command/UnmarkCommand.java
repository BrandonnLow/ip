package pingpong.command;

import pingpong.task.TaskList;
import pingpong.task.Task;
import pingpong.ui.Ui;
import pingpong.storage.Storage;
import pingpong.PingpongException;

/**
 * Command to unmark a task (mark it as not completed).
 */
public class UnmarkCommand extends Command {
    private int taskNumber;

    /**
     * Creates a new UnmarkCommand for the specified task number.
     *
     * @param taskNumber the number of the task to unmark (1-indexed)
     */
    public UnmarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    /**
     * Executes the command to unmark the specified task.
     *
     * @param tasks the task list containing the task to unmark
     * @param ui the UI to display feedback to the user
     * @param storage the storage to save the updated task list
     * @throws PingpongException if the task number is invalid
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws PingpongException {
        Task unmarkedTask = tasks.unmarkTask(taskNumber - 1);
        ui.showTaskUnmarked(unmarkedTask);
        storage.save(tasks.getAllTasks());
    }
}