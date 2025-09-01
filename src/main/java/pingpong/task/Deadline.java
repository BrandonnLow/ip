package pingpong.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task with a deadline - a task that needs to be completed by a certain date.
 */
public class Deadline extends Task {
    private LocalDate by;

    /**
     * Creates a new Deadline task with the specified description and deadline date.
     *
     * @param deadline the description of the deadline task
     * @param by the date by which the task should be completed
     */
    public Deadline(String deadline, LocalDate by) {
        super(deadline, TaskType.DEADLINE);

        assert by != null : "Deadline date should not be null";
        assert !by.isBefore(LocalDate.of(1900, 1, 1)) : "Deadline date should be reasonable (after 1900)";

        this.by = by;

        assert this.by != null : "Deadline date should be set";
        assert this.getType() == TaskType.DEADLINE : "Task type should be DEADLINE";
    }

    /**
     * Gets the deadline date of this task.
     *
     * @return the deadline date
     */
    public LocalDate getBy() {
        assert this.by != null : "Deadline date should not be null";
        return this.by;
    }

    /**
     * Gets a human-readable string representation of the deadline date.
     *
     * @return the deadline date formatted as "MMM d yyyy"
     */
    private String getByString() {
        assert by != null : "Deadline date should not be null";

        String formatted = by.format(DateTimeFormatter.ofPattern("MMM d yyyy"));

        assert formatted != null : "Formatted date string should not be null";
        assert !formatted.trim().isEmpty() : "Formatted date string should not be empty";

        return formatted;
    }

    /**
     * Gets the deadline date formatted for file storage.
     *
     * @return the deadline date in ISO format for file storage
     */
    public String getByForFile() {
        assert by != null : "Deadline date should not be null";

        String formatted = by.format(DateTimeFormatter.ISO_LOCAL_DATE);

        assert formatted != null : "Formatted date string should not be null";
        assert !formatted.trim().isEmpty() : "Formatted date string should not be empty";
        assert formatted.matches("\\d{4}-\\d{2}-\\d{2}") : "Date should be in ISO format";

        return formatted;
    }

    /**
     * Returns a string representation of this deadline task.
     *
     * @return a formatted string showing the task details and deadline
     */
    @Override
    public String toString() {
        assert this.by != null : "Deadline date should not be null";

        String baseString = super.toString();
        String byString = this.getByString();

        assert baseString != null : "Base task string should not be null";
        assert byString != null : "Deadline date string should not be null";

        String result = baseString + " (by: " + byString + ")";

        assert result != null : "Result string should not be null";
        assert result.contains("by:") : "Result should contain deadline indicator";
        assert result.contains(byString) : "Result should contain formatted deadline date";

        return result;
    }
}
