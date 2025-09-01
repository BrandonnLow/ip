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

        assert start != null : "Event start time should not be null";
        assert end != null : "Event end time should not be null";
        assert !start.isAfter(end) : "Event start time should not be after end time";
        assert !start.isBefore(LocalDateTime.of(1900, 1, 1, 0, 0)) : "Event start should be reasonable (after 1900)";

        this.start = start;
        this.end = end;

        assert this.start != null : "Event start time should be set";
        assert this.end != null : "Event end time should be set";
        assert this.getType() == TaskType.Event : "Task type should be Event";
    }

    /**
     * Gets the start date and time of this event.
     *
     * @return the start datetime
     */
    public LocalDateTime getStart() {
        assert this.start != null : "Event start time should not be null";
        return this.start;
    }

    /**
     * Gets the end date and time of this event.
     *
     * @return the end datetime
     */
    public LocalDateTime getEnd() {
        assert this.end != null : "Event end time should not be null";
        return this.end;
    }

    /**
     * Gets a human-readable string representation of the start time.
     *
     * @return the start time formatted as "MMM d yyyy, h:mma"
     */
    private String getStartString() {
        assert start != null : "Event start time should not be null";

        String formatted = start.format(DateTimeFormatter.ofPattern("MMM d yyyy, h:mma"));

        assert formatted != null : "Formatted start string should not be null";
        assert !formatted.trim().isEmpty() : "Formatted start string should not be empty";

        return formatted;
    }

    /**
     * Gets a human-readable string representation of the end time.
     *
     * @return the end time formatted as "MMM d yyyy, h:mma"
     */
    private String getEndString() {
        assert end != null : "Event end time should not be null";

        String formatted = end.format(DateTimeFormatter.ofPattern("MMM d yyyy, h:mma"));

        assert formatted != null : "Formatted end string should not be null";
        assert !formatted.trim().isEmpty() : "Formatted end string should not be empty";

        return formatted;
    }

    /**
     * Gets the start time formatted for file storage.
     *
     * @return the start time in ISO format for file storage
     */
    public String getStartForFile() {
        assert start != null : "Event start time should not be null";

        String formatted = start.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        assert formatted != null : "Formatted start string should not be null";
        assert !formatted.trim().isEmpty() : "Formatted start string should not be empty";
        assert formatted.contains("T") : "ISO datetime should contain 'T' separator";

        return formatted;
    }

    /**
     * Gets the end time formatted for file storage.
     *
     * @return the end time in ISO format for file storage
     */
    public String getEndForFile() {
        assert end != null : "Event end time should not be null";

        String formatted = end.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        assert formatted != null : "Formatted end string should not be null";
        assert !formatted.trim().isEmpty() : "Formatted end string should not be empty";
        assert formatted.contains("T") : "ISO datetime should contain 'T' separator";

        return formatted;
    }

    /**
     * Returns a string representation of this event task.
     *
     * @return a formatted string showing the task details and time period
     */
    @Override
    public String toString() {
        assert this.start != null : "Event start time should not be null";
        assert this.end != null : "Event end time should not be null";

        String baseString = super.toString();
        String startString = this.getStartString();
        String endString = this.getEndString();

        assert baseString != null : "Base task string should not be null";
        assert startString != null : "Start time string should not be null";
        assert endString != null : "End time string should not be null";

        String result = baseString + " (from: " + startString + " to: " + endString + ")";

        assert result != null : "Result string should not be null";
        assert result.contains("from:") : "Result should contain 'from:' indicator";
        assert result.contains("to:") : "Result should contain 'to:' indicator";
        assert result.contains(startString) : "Result should contain start time";
        assert result.contains(endString) : "Result should contain end time";

        return result;
    }
}
