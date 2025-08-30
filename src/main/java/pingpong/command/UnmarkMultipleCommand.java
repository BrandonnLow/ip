package pingpong.command;

import java.util.ArrayList;

import pingpong.PingpongException;
import pingpong.storage.Storage;
import pingpong.task.Task;
import pingpong.task.TaskList;
import pingpong.ui.Ui;

/**
 * Command to unmark multiple tasks (mark them as not completed) using varargs.
 */
public class UnmarkMultipleCommand extends Command {
    private int[] taskNumbers;

    /**
     * Creates a new UnmarkMultipleCommand for the specified task numbers.
     *
     * @param taskNumbers the numbers of the tasks to unmark (1-indexed, varargs)
     */
    public UnmarkMultipleCommand(int... taskNumbers) {
        this.taskNumbers = taskNumbers;
    }

    /**
     * Executes the command to unmark multiple specified tasks.
     *
     * @param tasks the task list containing the tasks to unmark
     * @param ui the UI to display feedback to the user
     * @param storage the storage to save the updated task list
     * @throws PingpongException if any task number is invalid
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws PingpongException {
        int[] indices = new int[taskNumbers.length];
        for (int i = 0; i < taskNumbers.length; i++) {
            indices[i] = taskNumbers[i] - 1;
        }

        ArrayList<Task> unmarkedTasks = tasks.unmarkTasks(indices);

        Task[] unmarkedTasksArray = unmarkedTasks.toArray(new Task[0]);
        ui.showTasksUnmarked(unmarkedTasksArray);

        storage.save(tasks.getAllTasks());
    }
}
