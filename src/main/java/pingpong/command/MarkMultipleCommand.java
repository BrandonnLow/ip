package pingpong.command;

import java.util.ArrayList;

import pingpong.PingpongException;
import pingpong.storage.Storage;
import pingpong.task.Task;
import pingpong.task.TaskList;
import pingpong.ui.Ui;

/**
 * Command to mark multiple tasks as completed using varargs.
 */
public class MarkMultipleCommand extends Command {
    private int[] taskNumbers;

    /**
     * Creates a new MarkMultipleCommand for the specified task numbers.
     *
     * @param taskNumbers the numbers of the tasks to mark (1-indexed, varargs)
     */
    public MarkMultipleCommand(int... taskNumbers) {
        this.taskNumbers = taskNumbers;
    }

    /**
     * Executes the command to mark multiple specified tasks as completed.
     *
     * @param tasks the task list containing the tasks to mark
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

        ArrayList<Task> markedTasks = tasks.markTasks(indices);

        Task[] markedTasksArray = markedTasks.toArray(new Task[0]);
        ui.showTasksMarked(markedTasksArray);

        storage.save(tasks.getAllTasks());
    }
}
