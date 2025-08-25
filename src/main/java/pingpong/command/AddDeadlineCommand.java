package pingpong.command;

import pingpong.task.TaskList;
import pingpong.task.Task;
import pingpong.ui.Ui;
import pingpong.storage.Storage;

import java.time.LocalDate;

/**
 * Command to add a new Deadline task to the task list.
 */
public class AddDeadlineCommand extends Command {
    private String description;
    private LocalDate by;

    /**
     * Creates a new AddDeadlineCommand with the specified description and deadline.
     *
     * @param description the description of the deadline task
     * @param by the date by which the task should be completed
     */
    public AddDeadlineCommand(String description, LocalDate by) {
        this.description = description;
        this.by = by;
    }

    /**
     * Executes the command to add a deadline task to the task list.
     *
     * @param tasks the task list to add the deadline to
     * @param ui the UI to display feedback to the user
     * @param storage the storage to save the updated task list
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        Task deadline = tasks.addDeadline(description, by);
        ui.showTaskAdded(deadline, tasks.size());
        storage.save(tasks.getAllTasks());
    }
}
