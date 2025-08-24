import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a parsed command with its type and arguments
 */
public class ParsedCommand {
    private CommandType type;
    private String description;
    private int taskNumber;
    private LocalDate date;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public ParsedCommand(CommandType type) {
        this.type = type;
    }

    public ParsedCommand(CommandType type, int taskNumber) {
        this.type = type;
        this.taskNumber = taskNumber;
    }

    public ParsedCommand(CommandType type, String description) {
        this.type = type;
        this.description = description;
    }

    public ParsedCommand(CommandType type, String description, LocalDate date) {
        this.type = type;
        this.description = description;
        this.date = date;
    }

    public ParsedCommand(CommandType type, String description, LocalDateTime start, LocalDateTime end) {
        this.type = type;
        this.description = description;
        this.startDateTime = start;
        this.endDateTime = end;
    }

    public ParsedCommand(CommandType type, LocalDate date) {
        this.type = type;
        this.date = date;
    }

    public CommandType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public int getTaskNumber() {
        return taskNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }
}