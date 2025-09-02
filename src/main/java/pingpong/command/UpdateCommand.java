package pingpong.command;

import java.time.LocalDate;
import java.time.LocalDateTime;

import pingpong.PingpongException;
import pingpong.storage.Storage;
import pingpong.task.Task;
import pingpong.task.TaskList;
import pingpong.ui.Ui;

/**
 * Command to update an existing task's details.
 * Supports updating description, deadline dates, and event times.
 */
public class UpdateCommand extends Command {
    private int taskNumber;
    private String newDescription;
    private LocalDate newDeadline;
    private LocalDateTime newStart;
    private LocalDateTime newEnd;

    /**
     * Creates a new UpdateCommand for the specified task number.
     *
     * @param taskNumber the number of the task to update (1-indexed)
     */
    public UpdateCommand(int taskNumber) {
        assert taskNumber > 0 : "Task number should be positive (1-indexed)";

        this.taskNumber = taskNumber;
        this.newDescription = null;
        this.newDeadline = null;
        this.newStart = null;
        this.newEnd = null;

        assert this.taskNumber > 0 : "Task number should be set and positive";
    }

    /**
     * Sets the new description for the task.
     *
     * @param newDescription the new description
     * @return this UpdateCommand for method chaining
     */
    public UpdateCommand withDescription(String newDescription) {
        assert newDescription != null : "New description should not be null";
        assert !newDescription.trim().isEmpty() : "New description should not be empty";

        this.newDescription = newDescription;
        return this;
    }

    /**
     * Sets the new deadline for deadline tasks.
     *
     * @param newDeadline the new deadline date
     * @return this UpdateCommand for method chaining
     */
    public UpdateCommand withDeadline(LocalDate newDeadline) {
        assert newDeadline != null : "New deadline should not be null";

        this.newDeadline = newDeadline;
        return this;
    }

    /**
     * Sets the new start time for event tasks.
     *
     * @param newStart the new start time
     * @return this UpdateCommand for method chaining
     */
    public UpdateCommand withStart(LocalDateTime newStart) {
        assert newStart != null : "New start time should not be null";

        this.newStart = newStart;
        return this;
    }

    /**
     * Sets the new end time for event tasks.
     *
     * @param newEnd the new end time
     * @return this UpdateCommand for method chaining
     */
    public UpdateCommand withEnd(LocalDateTime newEnd) {
        assert newEnd != null : "New end time should not be null";

        this.newEnd = newEnd;
        return this;
    }

    /**
     * Executes the command to update the specified task.
     *
     * @param tasks the task list containing the task to update
     * @param ui the UI to display feedback to the user
     * @param storage the storage to save the updated task list
     * @throws PingpongException if the task number is invalid or update fails
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws PingpongException {
        assert tasks != null : "Task list should not be null";
        assert ui != null : "UI should not be null";
        assert storage != null : "Storage should not be null";
        assert taskNumber > 0 : "Task number should be positive";

        // Validate that at least one field is being updated
        if (newDescription == null && newDeadline == null && newStart == null && newEnd == null) {
            throw new PingpongException("Please specify what to update (description, deadline, or event times).");
        }

        int zeroIndexedTaskNumber = taskNumber - 1;
        assert zeroIndexedTaskNumber >= 0 : "Zero-indexed task number should not be negative";

        Task originalTask = tasks.getTask(zeroIndexedTaskNumber);
        assert originalTask != null : "Original task should not be null";

        Task updatedTask = tasks.updateTask(zeroIndexedTaskNumber, newDescription,
                newDeadline, newStart, newEnd);

        assert updatedTask != null : "Updated task should not be null";

        ui.showTaskUpdated(originalTask, updatedTask);
        storage.save(tasks.getAllTasks());
    }
}
