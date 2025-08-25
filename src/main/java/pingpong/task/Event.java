package pingpong.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an event task that occurs over a specific time period.
 */
public class Event extends Task {
    private LocalDateTime start;
    private LocalDateTime end;

    /**
     * Creates a new Event task with the specified description, start time, and end time.
     *
     * @param event the description of the event
     * @param start the start date and time of the event
     * @param end the end date and time of the event
     */
    public Event(String event, LocalDateTime start, LocalDateTime end) {
        super(event, TaskType.Event);
        this.start = start;
        this.end = end;
    }

    /**
     * Gets the start date and time of this event.
     *
     * @return the start datetime
     */
    public LocalDateTime getStart() {
        return this.start;
    }

    /**
     * Gets the end date and time of this event.
     *
     * @return the end datetime
     */
    public LocalDateTime getEnd() {
        return this.end;
    }

    /**
     * Gets a human-readable string representation of the start time.
     *
     * @return the start time formatted as "MMM d yyyy, h:mma"
     */
    private String getStartString() {
        return start.format(DateTimeFormatter.ofPattern("MMM d yyyy, h:mma"));
    }

    /**
     * Gets a human-readable string representation of the end time.
     *
     * @return the end time formatted as "MMM d yyyy, h:mma"
     */
    private String getEndString() {
        return end.format(DateTimeFormatter.ofPattern("MMM d yyyy, h:mma"));
    }

    /**
     * Gets the start time formatted for file storage.
     *
     * @return the start time in ISO format for file storage
     */
    public String getStartForFile() {
        return start.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    /**
     * Gets the end time formatted for file storage.
     *
     * @return the end time in ISO format for file storage
     */
    public String getEndForFile() {
        return end.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    /**
     * Returns a string representation of this event task.
     *
     * @return a formatted string showing the task details and time period
     */
    @Override
    public String toString() {
        return super.toString() + " (from: " + this.getStartString() + " to: " + this.getEndString() + ")";
    }
}
