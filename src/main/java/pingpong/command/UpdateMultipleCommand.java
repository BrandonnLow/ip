package pingpong.command;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import pingpong.PingpongException;
import pingpong.storage.Storage;
import pingpong.task.Task;
import pingpong.task.TaskList;
import pingpong.ui.Ui;

/**
 * Command to update multiple tasks with the same modifications using varargs.
 */
public class UpdateMultipleCommand extends Command {
    private int[] taskNumbers;
    private String newDescription;
    private LocalDate newDeadline;
    private LocalDateTime newStart;
    private LocalDateTime newEnd;

    /**
     * Creates a new UpdateMultipleCommand for the specified task numbers.
     *
     * @param taskNumbers the numbers of the tasks to update (1-indexed, varargs)
     */
    public UpdateMultipleCommand(int... taskNumbers) {
        assert taskNumbers != null : "Task numbers should not be null";
        assert taskNumbers.length > 0 : "Should have at least one task number";

        this.taskNumbers = taskNumbers;
        this.newDescription = null;
        this.newDeadline = null;
        this.newStart = null;
        this.newEnd = null;

        assert this.taskNumbers != null : "Task numbers should be set";
        assert this.taskNumbers.length > 0 : "Should have task numbers";
    }

    /**
     * Sets the new description for all tasks.
     *
     * @param newDescription the new description
     * @return this UpdateMultipleCommand for method chaining
     */
    public UpdateMultipleCommand withDescription(String newDescription) {
        assert newDescription != null : "New description should not be null";
        assert !newDescription.trim().isEmpty() : "New description should not be empty";

        this.newDescription = newDescription;
        return this;
    }

    /**
     * Sets the new deadline for all deadline tasks.
     *
     * @param newDeadline the new deadline date
     * @return this UpdateMultipleCommand for method chaining
     */
    public UpdateMultipleCommand withDeadline(LocalDate newDeadline) {
        assert newDeadline != null : "New deadline should not be null";

        this.newDeadline = newDeadline;
        return this;
    }

    /**
     * Sets the new start time for all event tasks.
     *
     * @param newStart the new start time
     * @return this UpdateMultipleCommand for method chaining
     */
    public UpdateMultipleCommand withStart(LocalDateTime newStart) {
        assert newStart != null : "New start time should not be null";

        this.newStart = newStart;
        return this;
    }

    /**
     * Sets the new end time for all event tasks.
     *
     * @param newEnd the new end time
     * @return this UpdateMultipleCommand for method chaining
     */
    public UpdateMultipleCommand withEnd(LocalDateTime newEnd) {
        assert newEnd != null : "New end time should not be null";

        this.newEnd = newEnd;
        return this;
    }

    /**
     * Executes the command to update multiple specified tasks.
     *
     * @param tasks the task list containing the tasks to update
     * @param ui the UI to display feedback to the user
     * @param storage the storage to save the updated task list
     * @throws PingpongException if any task number is invalid or update fails
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws PingpongException {
        assert tasks != null : "Task list should not be null";
        assert ui != null : "UI should not be null";
        assert storage != null : "Storage should not be null";
        assert taskNumbers != null : "Task numbers should not be null";
        assert taskNumbers.length > 0 : "Should have task numbers";

        // Validate that at least one field is being updated
        if (newDescription == null && newDeadline == null && newStart == null && newEnd == null) {
            throw new PingpongException("Please specify what to update (description, deadline, or event times).");
        }

        Map<Task, Task> originalToUpdated = new HashMap<>();

        // Update all tasks
        for (int taskNumber : taskNumbers) {
            int zeroIndexedTaskNumber = taskNumber - 1;
            assert zeroIndexedTaskNumber >= 0 : "Zero-indexed task number should not be negative";

            Task originalTask = tasks.getTask(zeroIndexedTaskNumber);
            assert originalTask != null : "Original task should not be null";

            Task updatedTask = tasks.updateTask(zeroIndexedTaskNumber, newDescription,
                    newDeadline, newStart, newEnd);

            assert updatedTask != null : "Updated task should not be null";
            originalToUpdated.put(originalTask, updatedTask);
        }

        // Convert to arrays for UI display
        Task[] originalTasks = new Task[originalToUpdated.size()];
        Task[] updatedTasks = new Task[originalToUpdated.size()];

        int index = 0;
        for (Map.Entry<Task, Task> entry : originalToUpdated.entrySet()) {
            originalTasks[index] = entry.getKey();
            updatedTasks[index] = entry.getValue();
            index++;
        }

        ui.showTasksUpdated(originalTasks, updatedTasks);
        storage.save(tasks.getAllTasks());
    }
}
