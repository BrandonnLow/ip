package pingpong.command;

import pingpong.task.TaskList;
import pingpong.task.Task;
import pingpong.ui.Ui;
import pingpong.storage.Storage;
import pingpong.PingpongException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/**
 * Command to find tasks that occur on a specific date.
 */
public class FindCommand extends Command {
    private LocalDate date;

    private String searchTerm;
    private boolean isDateSearch;
    private LocalDate targetDate;

    /**
     * Creates a new FindCommand for the specified date.
     *
     * @param searchTerm the searched term (either time or description)
     */
    public FindCommand(String searchTerm) {
        this.searchTerm = searchTerm;
        this.isDateSearch = false;

        try {
            this.targetDate = LocalDate.parse(searchTerm, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            this.isDateSearch = true;
        } catch (DateTimeParseException e) {
            this.isDateSearch = false;
        }
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
        ArrayList<Task> foundTasks;

        if (isDateSearch) {
            foundTasks = tasks.findTasksOnDate(targetDate);
            String formattedDate = targetDate.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
            ui.showFoundTasksByDate(foundTasks, formattedDate);
        } else {
            foundTasks = tasks.findTasksByKeyword(searchTerm);
            ui.showFoundTasksByKeyword(foundTasks, searchTerm);
        }
    }
}
