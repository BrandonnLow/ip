package pingpong.command.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;

import pingpong.PingpongException;
import pingpong.command.UpdateCommand;
import pingpong.command.UpdateMultipleCommand;

/**
 * Handles parsing of update command fields.
 */
public class UpdateFieldParser {

    /**
     * Parses update fields and applies them to an UpdateCommand.
     *
     * @param command the UpdateCommand to configure
     * @param fieldsStr the string containing field specifications
     * @throws PingpongException if field parsing fails or validation errors occur
     */
    public static void parseFields(UpdateCommand command, String fieldsStr) throws PingpongException {
        assert command != null : "Command should not be null";
        assert fieldsStr != null : "Fields string should not be null";

        // Parse and apply each field
        String description = parseDescriptionField(fieldsStr);
        if (description != null) {
            command.withDescription(description);
        }

        LocalDate deadline = parseDeadlineField(fieldsStr);
        if (deadline != null) {
            command.withDeadline(deadline);
        }

        LocalDateTime startTime = parseStartTimeField(fieldsStr);
        if (startTime != null) {
            command.withStart(startTime);
        }

        LocalDateTime endTime = parseEndTimeField(fieldsStr);
        if (endTime != null) {
            command.withEnd(endTime);
        }

        validateTimeRange(startTime, endTime);
    }

    /**
     * Parses update fields and applies them to an UpdateMultipleCommand.
     *
     * @param command the UpdateMultipleCommand to configure
     * @param fieldsStr the string containing field specifications
     * @throws PingpongException if field parsing fails or validation errors occur
     */
    public static void parseFields(UpdateMultipleCommand command, String fieldsStr) throws PingpongException {
        assert command != null : "Command should not be null";
        assert fieldsStr != null : "Fields string should not be null";

        // Parse and apply each field
        String description = parseDescriptionField(fieldsStr);
        if (description != null) {
            command.withDescription(description);
        }

        LocalDate deadline = parseDeadlineField(fieldsStr);
        if (deadline != null) {
            command.withDeadline(deadline);
        }

        LocalDateTime startTime = parseStartTimeField(fieldsStr);
        if (startTime != null) {
            command.withStart(startTime);
        }

        LocalDateTime endTime = parseEndTimeField(fieldsStr);
        if (endTime != null) {
            command.withEnd(endTime);
        }

        validateTimeRange(startTime, endTime);
    }

    /**
     * Parses a specific field from the fields string.
     *
     * @param fieldsStr the complete fields string
     * @param fieldIndicator the field indicator to look for (e.g., "/desc")
     * @return the field value, or null if field not found
     */
    public static String parseUpdateField(String fieldsStr, String fieldIndicator) {
        assert fieldsStr != null : "Fields string should not be null";
        assert fieldIndicator != null : "Field indicator should not be null";

        int startIndex = fieldsStr.indexOf(fieldIndicator);
        if (startIndex == -1) {
            return null;
        }

        startIndex += fieldIndicator.length();

        // Find the end of this field (next field indicator or end of string)
        String[] nextIndicators = {"/desc", "/by", "/from", "/to"};
        int endIndex = fieldsStr.length();

        for (String nextIndicator : nextIndicators) {
            if (!nextIndicator.equals(fieldIndicator)) {
                int nextIndex = fieldsStr.indexOf(nextIndicator, startIndex);
                if (nextIndex != -1 && nextIndex < endIndex) {
                    endIndex = nextIndex;
                }
            }
        }

        return fieldsStr.substring(startIndex, endIndex).trim();
    }

    private static String parseDescriptionField(String fieldsStr) throws PingpongException {
        String description = parseUpdateField(fieldsStr, "/desc");
        if (description != null && description.trim().isEmpty()) {
            throw new PingpongException("Description cannot be empty.");
        }
        return description;
    }

    private static LocalDate parseDeadlineField(String fieldsStr) throws PingpongException {
        String byStr = parseUpdateField(fieldsStr, "/by");
        if (byStr != null) {
            if (byStr.trim().isEmpty()) {
                throw new PingpongException("Deadline date cannot be empty.");
            }
            return DateTimeParser.parseDate(byStr);
        }
        return null;
    }

    private static LocalDateTime parseStartTimeField(String fieldsStr) throws PingpongException {
        String fromStr = parseUpdateField(fieldsStr, "/from");
        if (fromStr != null) {
            if (fromStr.trim().isEmpty()) {
                throw new PingpongException("Start time cannot be empty.");
            }
            return DateTimeParser.parseDateTime(fromStr);
        }
        return null;
    }

    private static LocalDateTime parseEndTimeField(String fieldsStr) throws PingpongException {
        String toStr = parseUpdateField(fieldsStr, "/to");
        if (toStr != null) {
            if (toStr.trim().isEmpty()) {
                throw new PingpongException("End time cannot be empty.");
            }
            return DateTimeParser.parseDateTime(toStr);
        }
        return null;
    }

    private static void validateTimeRange(LocalDateTime startTime, LocalDateTime endTime) throws PingpongException {
        if (startTime != null && endTime != null && startTime.isAfter(endTime)) {
            throw new PingpongException("Event start time cannot be after end time.");
        }
    }
}
