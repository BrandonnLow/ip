package pingpong.command;

import pingpong.task.TaskList;
import pingpong.task.Task;
import pingpong.ui.Ui;
import pingpong.storage.Storage;
import pingpong.PingpongException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Command to find tasks that occur on a specific date.
 */
public class FindCommand extends Command {
    private LocalDate date;

    /**
     * Creates a new FindCommand for the specified date.
     *
     * @param date the date to search for tasks
     */
    public FindCommand(LocalDate date) {
        this.date = date;
    }

    /**
     * Executes the command to find and display tasks occurring on the specified date.
     *
     * @param tasks the task list to search through
     * @param ui the UI to display the found tasks
     * @param storage the storage (not modified by this command)
     * @throws PingpongException if an error occurs during execution
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws PingpongException {
        ArrayList<Task> foundTasks = tasks.findTasksOnDate(date);
        String formattedDate = date.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
        ui.showFoundTasks(foundTasks, formattedDate);
    }
}
