package pingpong.command;

import pingpong.task.TaskList;
import pingpong.task.Task;
import pingpong.ui.Ui;
import pingpong.storage.Storage;

import java.time.LocalDateTime;

/**
 * Command to add a new Event task to the task list.
 */
public class AddEventCommand extends Command {
    private String description;
    private LocalDateTime start;
    private LocalDateTime end;

    /**
     * Creates a new AddEventCommand with the specified description, start time, and end time.
     *
     * @param description the description of the event
     * @param start the start date and time of the event
     * @param end the end date and time of the event
     */
    public AddEventCommand(String description, LocalDateTime start, LocalDateTime end) {
        this.description = description;
        this.start = start;
        this.end = end;
    }

    /**
     * Executes the command to add an event task to the task list.
     *
     * @param tasks the task list to add the event to
     * @param ui the UI to display feedback to the user
     * @param storage the storage to save the updated task list
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        Task event = tasks.addEvent(description, start, end);
        ui.showTaskAdded(event, tasks.size());
        storage.save(tasks.getAllTasks());
    }
}
