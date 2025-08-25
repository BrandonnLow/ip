package pingpong.command;

import pingpong.PingpongException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Handles parsing of user commands and returns appropriate Command objects.
 * This class contains the main parsing logic for all supported commands in the Pingpong application.
 */
public class Parser {

    /**
     * Parses user input and returns the appropriate Command object for execution.
     * Supports commands: list, mark, unmark, todo, deadline, event, delete, find.
     *
     * @param input the raw user input string
     * @return the Command object corresponding to the user input
     * @throws PingpongException if the command format is invalid or unrecognized
     */
    public static Command parse(String input) throws PingpongException {
        if (input.trim().isEmpty()) {
            throw new PingpongException("Please enter a command.");
        }

        if (input.equals("list")) {
            return new ListCommand();
        } else if (input.equals("mark") || input.equals("mark ") || input.startsWith("mark ")) {
            return parseMarkCommand(input);
        } else if (input.equals("unmark") || input.equals("unmark ") || input.startsWith("unmark ")) {
            return parseUnmarkCommand(input);
        } else if (input.equals("todo") || input.equals("todo ")) {
            throw new PingpongException("The description of a todo cannot be empty.");
        } else if (input.startsWith("todo ")) {
            return parseTodoCommand(input);
        } else if (input.equals("deadline") || input.equals("deadline ")) {
            throw new PingpongException("The description of a deadline cannot be empty.");
        } else if (input.startsWith("deadline ")) {
            return parseDeadlineCommand(input);
        } else if (input.equals("event") || input.equals("event ")) {
            throw new PingpongException("The description of an event cannot be empty.");
        } else if (input.startsWith("event ")) {
            return parseEventCommand(input);
        } else if (input.equals("delete") || input.equals("delete ") || input.startsWith("delete ")) {
            return parseDeleteCommand(input);
        } else if (input.equals("find") || input.equals("find ")) {
            throw new PingpongException("Please specify a keyword or date (yyyy-MM-dd) to search for.");
        } else if (input.startsWith("find ")) {
            return parseFindCommand(input);
        } else {
            throw new PingpongException("I'm sorry, but I don't know what that means :-(");
        }
    }

    /**
     * Parses a mark command to extract the task number.
     * Expected format: "mark <task_number>"
     *
     * @param input the mark command string
     * @return a MarkCommand with the specified task number
     * @throws PingpongException if the task number is missing or invalid
     */
    private static Command parseMarkCommand(String input) throws PingpongException {
        String numberStr = "";
        if (input.length() > 4) {
            numberStr = input.substring(4).trim();
        }
        if (numberStr.isEmpty()) {
            throw new PingpongException("Please specify which task to mark.");
        }
        try {
            int taskNum = Integer.parseInt(numberStr);
            return new MarkCommand(taskNum);
        } catch (NumberFormatException e) {
            throw new PingpongException("Please provide a valid task number.");
        }
    }

    /**
     * Parses an unmark command to extract the task number.
     * Expected format: "unmark <task_number>"
     *
     * @param input the unmark command string
     * @return an UnmarkCommand with the specified task number
     * @throws PingpongException if the task number is missing or invalid
     */
    private static Command parseUnmarkCommand(String input) throws PingpongException {
        String numberStr = "";
        if (input.length() > 6) {
            numberStr = input.substring(6).trim();
        }
        if (numberStr.isEmpty()) {
            throw new PingpongException("Please specify which task to unmark.");
        }
        try {
            int taskNum = Integer.parseInt(numberStr);
            return new UnmarkCommand(taskNum);
        } catch (NumberFormatException e) {
            throw new PingpongException("Please provide a valid task number.");
        }
    }

    /**
     * Parses a todo command to extract the task description.
     * Expected format: "todo <description>"
     *
     * @param input the todo command string
     * @return an AddTodoCommand with the specified description
     * @throws PingpongException if the description is empty
     */
    private static Command parseTodoCommand(String input) throws PingpongException {
        String description = input.substring(5).trim();
        if (description.isEmpty()) {
            throw new PingpongException("The description of a todo cannot be empty.");
        }
        return new AddTodoCommand(description);
    }

    /**
     * Parses a deadline command to extract the description and deadline date.
     * Expected format: "deadline <description> /by <yyyy-MM-dd>"
     *
     * @param input the deadline command string
     * @return an AddDeadlineCommand with the specified description and date
     * @throws PingpongException if the format is invalid or dates cannot be parsed
     */
    private static Command parseDeadlineCommand(String input) throws PingpongException {
        String[] parts = input.substring(9).split(" /by ");
        if (parts.length != 2) {
            throw new PingpongException("Please use format: deadline <description> /by <yyyy-MM-dd>");
        }
        String description = parts[0].trim();
        String byStr = parts[1].trim();
        if (description.isEmpty()) {
            throw new PingpongException("The description of a deadline cannot be empty.");
        }
        if (byStr.isEmpty()) {
            throw new PingpongException("The deadline date cannot be empty.");
        }
        LocalDate by = parseDate(byStr);
        return new AddDeadlineCommand(description, by);
    }

    /**
     * Parses an event command to extract the description, start time, and end time.
     * Expected format: "event <description> /from <yyyy-MM-dd HHmm> /to <yyyy-MM-dd HHmm>"
     *
     * @param input the event command string
     * @return an AddEventCommand with the specified description and time range
     * @throws PingpongException if the format is invalid, dates cannot be parsed, or start time is after end time
     */
    private static Command parseEventCommand(String input) throws PingpongException {
        String remaining = input.substring(6);
        String[] fromParts = remaining.split(" /from ");
        if (fromParts.length != 2) {
            throw new PingpongException("Please use format: event <description> /from <yyyy-MM-dd HHmm> /to <yyyy-MM-dd HHmm>");
        }
        String description = fromParts[0].trim();
        String[] toParts = fromParts[1].split(" /to ");
        if (toParts.length != 2) {
            throw new PingpongException("Please use format: event <description> /from <yyyy-MM-dd HHmm> /to <yyyy-MM-dd HHmm>");
        }
        String fromStr = toParts[0].trim();
        String toStr = toParts[1].trim();
        if (description.isEmpty()) {
            throw new PingpongException("The description of an event cannot be empty.");
        }
        if (fromStr.isEmpty()) {
            throw new PingpongException("The event start time cannot be empty.");
        }
        if (toStr.isEmpty()) {
            throw new PingpongException("The event end time cannot be empty.");
        }
        LocalDateTime from = parseDateTime(fromStr);
        LocalDateTime to = parseDateTime(toStr);
        if (from.isAfter(to)) {
            throw new PingpongException("Event start time cannot be after end time.");
        }
        return new AddEventCommand(description, from, to);
    }

    /**
     * Parses a delete command to extract the task number.
     * Expected format: "delete <task_number>"
     *
     * @param input the delete command string
     * @return a DeleteCommand with the specified task number
     * @throws PingpongException if the task number is missing or invalid
     */
    private static Command parseDeleteCommand(String input) throws PingpongException {
        String numberStr = "";
        if (input.length() > 6) {
            numberStr = input.substring(6).trim();
        }
        if (numberStr.isEmpty()) {
            throw new PingpongException("Please specify which task to delete.");
        }
        try {
            int taskNum = Integer.parseInt(numberStr);
            return new DeleteCommand(taskNum);
        } catch (NumberFormatException e) {
            throw new PingpongException("Please provide a valid task number.");
        }
    }

    /**
     * Parses a find command to extract the target date.
     * Expected format: "find <yyyy-MM-dd>"
     *
     * @param input the find command string
     * @return a FindCommand with the specified date
     * @throws PingpongException if the date is missing or in invalid format
     */
    private static Command parseFindCommand(String input) throws PingpongException {
        String searchTerm = input.substring(5).trim();
        if (searchTerm.isEmpty()) {
            throw new PingpongException("Please specify a keyword or date (yyyy-MM-dd) to search for.");
        }
        return new FindCommand(searchTerm);
    }

    /**
     * Parses a date string in yyyy-MM-dd format into a LocalDate object.
     *
     * @param dateStr the date string to parse
     * @return the parsed LocalDate
     * @throws PingpongException if the date format is invalid
     */
    private static LocalDate parseDate(String dateStr) throws PingpongException {
        try {
            return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            throw new PingpongException("Invalid date format. Please use yyyy-MM-dd format (e.g., 2019-12-02)");
        }
    }

    /**
     * Parses a date-time string into a LocalDateTime object.
     * Supports multiple formats:
     * - "yyyy-MM-dd HHmm" (e.g., "2019-12-02 1800")
     * - "yyyy-MM-dd HH:mm" (e.g., "2019-12-02 18:00")
     * - "yyyy-MM-dd" (treated as start of day)
     *
     * @param dateTimeStr the date-time string to parse
     * @return the parsed LocalDateTime
     * @throws PingpongException if the date-time format is invalid
     */
    private static LocalDateTime parseDateTime(String dateTimeStr) throws PingpongException {
        try {
            if (dateTimeStr.matches("\\d{4}-\\d{2}-\\d{2} \\d{4}")) {
                return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
            }
            else if (dateTimeStr.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}")) {
                return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            }
            else if (dateTimeStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
                return LocalDate.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
            }
            else {
                throw new DateTimeParseException("Unsupported format", dateTimeStr, 0);
            }
        } catch (DateTimeParseException e) {
            throw new PingpongException("Invalid datetime format. Please use formats like: 2019-12-02 1800, 2019-12-02 18:00, or 2019-12-02");
        }
    }
}