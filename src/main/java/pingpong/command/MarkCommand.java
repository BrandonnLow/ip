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
        this.taskNumber = taskNumber;
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
        Task markedTask = tasks.markTask(taskNumber - 1);
        ui.showTaskMarked(markedTask);
        storage.save(tasks.getAllTasks());
    }
}
