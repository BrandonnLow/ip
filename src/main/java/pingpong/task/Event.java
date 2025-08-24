package pingpong.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event extends Task {

    private LocalDateTime start;
    private LocalDateTime end;

    public Event(String event, LocalDateTime start, LocalDateTime end) {
        super(event, TaskType.Event);
        this.start = start;
        this.end = end;
    }

    public LocalDateTime getStart() {
        return this.start;
    }

    public LocalDateTime getEnd() {
        return this.end;
    }

    private String getStartString() {
        return start.format(DateTimeFormatter.ofPattern("MMM d yyyy, h:mma"));
    }

    private String getEndString() {
        return end.format(DateTimeFormatter.ofPattern("MMM d yyyy, h:mma"));
    }

    public String getStartForFile() {
        return start.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public String getEndForFile() {
        return end.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    @Override
    public String toString() {
        return super.toString() + " (from: " + this.getStartString() + " to: " + this.getEndString() + ")";
    }

}
