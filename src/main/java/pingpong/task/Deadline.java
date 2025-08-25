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
        this.by = by;
    }

    /**
     * Gets the deadline date of this task.
     *
     * @return the deadline date
     */
    public LocalDate getBy() {
        return this.by;
    }

    /**
     * Gets a human-readable string representation of the deadline date.
     *
     * @return the deadline date formatted as "MMM d yyyy"
     */
    private String getByString() {
        return by.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
    }

    /**
     * Gets the deadline date formatted for file storage.
     *
     * @return the deadline date in ISO format for file storage
     */
    public String getByForFile() {
        return by.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     * Returns a string representation of this deadline task.
     *
     * @return a formatted string showing the task details and deadline
     */
    @Override
    public String toString() {
        return super.toString() + " (by: " + this.getByString() + ")";
    }
}
