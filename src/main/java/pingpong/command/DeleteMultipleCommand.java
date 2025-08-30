package pingpong.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import pingpong.PingpongException;
import pingpong.storage.Storage;
import pingpong.task.Task;
import pingpong.task.TaskList;
import pingpong.ui.Ui;

/**
 * Command to delete multiple tasks from the task list using varargs.
 */
public class DeleteMultipleCommand extends Command {
    private int[] taskNumbers;

    /**
     * Creates a new DeleteMultipleCommand for the specified task numbers.
     *
     * @param taskNumbers the numbers of the tasks to delete (1-indexed, varargs)
     */
    public DeleteMultipleCommand(int... taskNumbers) {
        this.taskNumbers = taskNumbers;
    }

    /**
     * Executes the command to delete multiple specified tasks from the task list.
     * Deletes tasks in reverse order to maintain valid indices.
     *
     * @param tasks the task list to delete the tasks from
     * @param ui the UI to display feedback to the user
     * @param storage the storage to save the updated task list
     * @throws PingpongException if any task number is invalid
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws PingpongException {
        // Sort task numbers in descending order to delete from highest index first
        // This prevents index shifting issues when deleting multiple items
        Integer[] taskNumbersWrapper = new Integer[taskNumbers.length];
        for (int i = 0; i < taskNumbers.length; i++) {
            taskNumbersWrapper[i] = taskNumbers[i];
        }
        Arrays.sort(taskNumbersWrapper, Comparator.reverseOrder());

        ArrayList<Task> deletedTasks = new ArrayList<>();

        // Delete tasks in descending order of their indices
        for (int taskNumber : taskNumbersWrapper) {
            Task deletedTask = tasks.deleteTask(taskNumber - 1);
            deletedTasks.add(0, deletedTask);
        }

        ui.showTasksDeleted(deletedTasks, tasks.size());
        storage.save(tasks.getAllTasks());
    }
}
