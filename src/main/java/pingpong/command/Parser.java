package pingpong.command;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import pingpong.PingpongException;

/**
 * Handles parsing of user commands and returns appropriate Command objects.
 * This class contains the main parsing logic for all supported commands in the Pingpong application.
 * Now supports varargs for batch operations on multiple tasks.
 */
public class Parser {
    // Command keywords
    private static final String LIST_COMMAND = "list";
    private static final String MARK_COMMAND = "mark";
    private static final String UNMARK_COMMAND = "unmark";
    private static final String TODO_COMMAND = "todo";
    private static final String DEADLINE_COMMAND = "deadline";
    private static final String EVENT_COMMAND = "event";
    private static final String DELETE_COMMAND = "delete";
    private static final String FIND_COMMAND = "find";
    private static final String ADD_MULTIPLE_COMMAND = "addmultiple";

    // Error messages
    private static final String EMPTY_COMMAND_ERROR = "Please enter a command.";
    private static final String UNKNOWN_COMMAND_ERROR = "I'm sorry, but I don't know what that means :-(";
    private static final String MARK_MISSING_ERROR = "Please specify which task(s) to mark.";
    private static final String UNMARK_MISSING_ERROR = "Please specify which task(s) to unmark.";
    private static final String DELETE_MISSING_ERROR = "Please specify which task(s) to delete.";
    private static final String TODO_EMPTY_ERROR = "The description of a todo cannot be empty.";
    private static final String DEADLINE_EMPTY_ERROR = "The description of a deadline cannot be empty.";
    private static final String EVENT_EMPTY_ERROR = "The description of an event cannot be empty.";
    private static final String FIND_EMPTY_ERROR = "Please specify a keyword or date (yyyy-MM-dd) to search for.";
    private static final String ADD_MULTIPLE_EMPTY_ERROR = "Please specify todo descriptions separated by semicolons.";
    private static final String INVALID_TASK_NUMBER_ERROR = "Please provide valid task number(s).";
    private static final String POSITIVE_NUMBER_ERROR = "Task numbers must be positive integers.";

    // Date format strings
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String DATETIME_FORMAT_HHMM = "yyyy-MM-dd HHmm";
    private static final String DATETIME_FORMAT_COLON = "yyyy-MM-dd HH:mm";
    private static final String DATE_REGEX = "\\d{4}-\\d{2}-\\d{2}";
    private static final String DATETIME_HHMM_REGEX = "\\d{4}-\\d{2}-\\d{2} \\d{4}";
    private static final String DATETIME_COLON_REGEX = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}";

    /**
     * Parses user input and returns the appropriate Command object for execution.
     * Supports commands: list, mark, unmark, todo, deadline, event, delete, find, addmultiple.
     * Also supports batch operations with multiple task numbers.
     *
     * @param input the raw user input string
     * @return the Command object corresponding to the user input
     * @throws PingpongException if the command format is invalid or unrecognized
     */
    public static Command parse(String input) throws PingpongException {
        validateInput(input);

        String command = extractCommand(input);

        switch (command) {
        case LIST_COMMAND:
            return new ListCommand();
        case MARK_COMMAND:
            return parseMarkCommand(input);
        case UNMARK_COMMAND:
            return parseUnmarkCommand(input);
        case TODO_COMMAND:
            return parseTodoCommand(input);
        case DEADLINE_COMMAND:
            return parseDeadlineCommand(input);
        case EVENT_COMMAND:
            return parseEventCommand(input);
        case DELETE_COMMAND:
            return parseDeleteCommand(input);
        case FIND_COMMAND:
            return parseFindCommand(input);
        case ADD_MULTIPLE_COMMAND:
            return parseAddMultipleCommand(input);
        default:
            throw new PingpongException(UNKNOWN_COMMAND_ERROR);
        }
    }

    /**
     * Validates that the input is not empty or blank.
     *
     * @param input the user input to validate
     * @throws PingpongException if input is empty or blank
     */
    private static void validateInput(String input) throws PingpongException {
        if (input.trim().isEmpty()) {
            throw new PingpongException(EMPTY_COMMAND_ERROR);
        }
    }

    /**
     * Extracts the command keyword from user input.
     *
     * @param input the user input
     * @return the command keyword
     */
    private static String extractCommand(String input) {
        return input.trim().split("\\s+")[0];
    }

    /**
     * Checks if the command has arguments after the command keyword.
     *
     * @param input the complete user input
     * @param command the command keyword
     * @return true if arguments exist, false otherwise
     */
    private static boolean hasArguments(String input, String command) {
        return input.length() > command.length() &&
                input.substring(command.length()).trim().length() > 0;
    }

    /**
     * Parses a mark command to extract task numbers (supports both single and multiple).
     * Expected format: "mark task_number" or "mark task_number1 task_number2 ..."
     *
     * @param input the mark command string
     * @return a MarkCommand or MarkMultipleCommand depending on number of tasks
     * @throws PingpongException if task numbers are missing or invalid
     */
    private static Command parseMarkCommand(String input) throws PingpongException {
        if (!hasArguments(input, MARK_COMMAND)) {
            throw new PingpongException(MARK_MISSING_ERROR);
        }

        String numbersStr = input.substring(MARK_COMMAND.length()).trim();
        String[] numberParts = numbersStr.split("\\s+");

        if (numberParts.length == 1) {
            int taskNum = parseTaskNumber(numberParts[0]);
            return new MarkCommand(taskNum);
        } else {
            int[] taskNumbers = parseTaskNumbers(numberParts);
            return new MarkMultipleCommand(taskNumbers);
        }
    }

    /**
     * Parses an unmark command to extract task numbers (supports both single and multiple).
     * Expected format: "unmark task_number" or "unmark task_number1 task_number2 ..."
     *
     * @param input the unmark command string
     * @return an UnmarkCommand or UnmarkMultipleCommand depending on number of tasks
     * @throws PingpongException if task numbers are missing or invalid
     */
    private static Command parseUnmarkCommand(String input) throws PingpongException {
        if (!hasArguments(input, UNMARK_COMMAND)) {
            throw new PingpongException(UNMARK_MISSING_ERROR);
        }

        String numbersStr = input.substring(UNMARK_COMMAND.length()).trim();
        String[] numberParts = numbersStr.split("\\s+");

        if (numberParts.length == 1) {
            int taskNum = parseTaskNumber(numberParts[0]);
            return new UnmarkCommand(taskNum);
        } else {
            int[] taskNumbers = parseTaskNumbers(numberParts);
            return new UnmarkMultipleCommand(taskNumbers);
        }
    }

    /**
     * Parses a delete command to extract task numbers (supports both single and multiple).
     * Expected format: "delete task_number" or "delete task_number1 task_number2 ..."
     *
     * @param input the delete command string
     * @return a DeleteCommand or DeleteMultipleCommand depending on number of tasks
     * @throws PingpongException if task numbers are missing or invalid
     */
    private static Command parseDeleteCommand(String input) throws PingpongException {
        if (!hasArguments(input, DELETE_COMMAND)) {
            throw new PingpongException(DELETE_MISSING_ERROR);
        }

        String numbersStr = input.substring(DELETE_COMMAND.length()).trim();
        String[] numberParts = numbersStr.split("\\s+");

        if (numberParts.length == 1) {
            int taskNum = parseTaskNumber(numberParts[0]);
            return new DeleteCommand(taskNum);
        } else {
            int[] taskNumbers = parseTaskNumbers(numberParts);
            return new DeleteMultipleCommand(taskNumbers);
        }
    }

    /**
     * Parses an addmultiple command to extract multiple todo descriptions.
     * Expected format: "addmultiple description1; description2; description3"
     *
     * @param input the addmultiple command string
     * @return an AddMultipleCommand with the specified descriptions
     * @throws PingpongException if descriptions are missing or invalid
     */
    private static Command parseAddMultipleCommand(String input) throws PingpongException {
        if (!hasArguments(input, ADD_MULTIPLE_COMMAND)) {
            throw new PingpongException(ADD_MULTIPLE_EMPTY_ERROR);
        }

        String descriptionsStr = input.substring(ADD_MULTIPLE_COMMAND.length()).trim();
        String[] descriptions = descriptionsStr.split(";");
        ArrayList<String> validDescriptions = extractValidDescriptions(descriptions);

        if (validDescriptions.isEmpty()) {
            throw new PingpongException("Please provide at least one valid todo description.");
        }

        return new AddMultipleCommand(validDescriptions.toArray(new String[0]));
    }

    /**
     * Filters out empty descriptions from the array of description strings.
     *
     * @param descriptions array of raw description strings
     * @return list of valid, non-empty descriptions
     */
    private static ArrayList<String> extractValidDescriptions(String[] descriptions) {
        ArrayList<String> validDescriptions = new ArrayList<>();
        for (String desc : descriptions) {
            String trimmed = desc.trim();
            if (!trimmed.isEmpty()) {
                validDescriptions.add(trimmed);
            }
        }
        return validDescriptions;
    }

    /**
     * Helper method to parse a single task number.
     *
     * @param numberStr string representation of task number
     * @return parsed task number
     * @throws PingpongException if task number is invalid
     */
    private static int parseTaskNumber(String numberStr) throws PingpongException {
        try {
            int taskNum = Integer.parseInt(numberStr);
            if (taskNum <= 0) {
                throw new PingpongException(POSITIVE_NUMBER_ERROR);
            }
            return taskNum;
        } catch (NumberFormatException e) {
            throw new PingpongException(INVALID_TASK_NUMBER_ERROR);
        }
    }

    /**
     * Helper method to parse multiple task numbers from string array using varargs concept.
     *
     * @param numberParts array of string representations of task numbers
     * @return array of parsed task numbers
     * @throws PingpongException if any task number is invalid
     */
    private static int[] parseTaskNumbers(String... numberParts) throws PingpongException {
        int[] taskNumbers = new int[numberParts.length];

        for (int i = 0; i < numberParts.length; i++) {
            taskNumbers[i] = parseTaskNumber(numberParts[i]);
        }
        return taskNumbers;
    }

    /**
     * Parses a todo command to extract the task description.
     * Expected format: "todo description"
     *
     * @param input the todo command string
     * @return an AddTodoCommand with the specified description
     * @throws PingpongException if the description is empty
     */
    private static Command parseTodoCommand(String input) throws PingpongException {
        if (!hasArguments(input, TODO_COMMAND)) {
            throw new PingpongException(TODO_EMPTY_ERROR);
        }

        String description = input.substring(TODO_COMMAND.length()).trim();
        return new AddTodoCommand(description);
    }

    /**
     * Parses a deadline command to extract the description and deadline date.
     * Expected format: "deadline description /by yyyy-MM-dd"
     *
     * @param input the deadline command string
     * @return an AddDeadlineCommand with the specified description and date
     * @throws PingpongException if the format is invalid or dates cannot be parsed
     */
    private static Command parseDeadlineCommand(String input) throws PingpongException {
        if (!hasArguments(input, DEADLINE_COMMAND)) {
            throw new PingpongException(DEADLINE_EMPTY_ERROR);
        }

        String[] parts = input.substring(DEADLINE_COMMAND.length()).split(" /by ");
        if (parts.length != 2) {
            throw new PingpongException("Please use format: deadline <description> /by <yyyy-MM-dd>");
        }

        String description = parts[0].trim();
        String byStr = parts[1].trim();

        validateDeadlineComponents(description, byStr);
        LocalDate by = parseDate(byStr);

        return new AddDeadlineCommand(description, by);
    }

    /**
     * Validates components of a deadline command.
     *
     * @param description the task description
     * @param byStr the deadline date string
     * @throws PingpongException if any component is invalid
     */
    private static void validateDeadlineComponents(String description, String byStr) throws PingpongException {
        if (description.isEmpty()) {
            throw new PingpongException(DEADLINE_EMPTY_ERROR);
        }
        if (byStr.isEmpty()) {
            throw new PingpongException("The deadline date cannot be empty.");
        }
    }

    /**
     * Parses an event command to extract the description, start time, and end time.
     * Expected format: "event description /from yyyy-MM-dd HHmm /to yyyy-MM-dd HHmm"
     *
     * @param input the event command string
     * @return an AddEventCommand with the specified description and time range
     * @throws PingpongException if the format is invalid, dates cannot be parsed, or start time is after end time
     */
    private static Command parseEventCommand(String input) throws PingpongException {
        if (!hasArguments(input, EVENT_COMMAND)) {
            throw new PingpongException(EVENT_EMPTY_ERROR);
        }

        String[] eventParts = parseEventParts(input);
        String description = eventParts[0];
        String fromStr = eventParts[1];
        String toStr = eventParts[2];

        validateEventComponents(description, fromStr, toStr);

        LocalDateTime from = parseDateTime(fromStr);
        LocalDateTime to = parseDateTime(toStr);

        validateEventTiming(from, to);

        return new AddEventCommand(description, from, to);
    }

    /**
     * Parses event command into its component parts.
     *
     * @param input the event command string
     * @return array containing [description, fromStr, toStr]
     * @throws PingpongException if format is invalid
     */
    private static String[] parseEventParts(String input) throws PingpongException {
        String remaining = input.substring(EVENT_COMMAND.length());
        String[] fromParts = remaining.split(" /from ");

        if (fromParts.length != 2) {
            throw new PingpongException("Please use format: event <description> "
                    + "/from <yyyy-MM-dd HHmm> /to <yyyy-MM-dd HHmm>");
        }

        String description = fromParts[0].trim();
        String[] toParts = fromParts[1].split(" /to ");

        if (toParts.length != 2) {
            throw new PingpongException("Please use format: event <description> "
                    + "/from <yyyy-MM-dd HHmm> /to <yyyy-MM-dd HHmm>");
        }

        return new String[]{description, toParts[0].trim(), toParts[1].trim()};
    }

    /**
     * Validates components of an event command.
     *
     * @param description the event description
     * @param fromStr the start time string
     * @param toStr the end time string
     * @throws PingpongException if any component is invalid
     */
    private static void validateEventComponents(String description, String fromStr, String toStr)
            throws PingpongException {
        if (description.isEmpty()) {
            throw new PingpongException(EVENT_EMPTY_ERROR);
        }
        if (fromStr.isEmpty()) {
            throw new PingpongException("The event start time cannot be empty.");
        }
        if (toStr.isEmpty()) {
            throw new PingpongException("The event end time cannot be empty.");
        }
    }

    /**
     * Validates that event start time is not after end time.
     *
     * @param from the start time
     * @param to the end time
     * @throws PingpongException if start time is after end time
     */
    private static void validateEventTiming(LocalDateTime from, LocalDateTime to) throws PingpongException {
        if (from.isAfter(to)) {
            throw new PingpongException("Event start time cannot be after end time.");
        }
    }

    /**
     * Parses a find command to extract the target date or keywords.
     * Expected format: "find yyyy-MM-dd" or "find keyword"
     *
     * @param input the find command string
     * @return a FindCommand with the specified date or keyword
     * @throws PingpongException if the date/keyword is missing or in invalid format
     */
    private static Command parseFindCommand(String input) throws PingpongException {
        if (!hasArguments(input, FIND_COMMAND)) {
            throw new PingpongException(FIND_EMPTY_ERROR);
        }

        String searchTerm = input.substring(FIND_COMMAND.length()).trim();
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
            return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(DATE_FORMAT));
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
            if (dateTimeStr.matches(DATETIME_HHMM_REGEX)) {
                return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(DATETIME_FORMAT_HHMM));
            } else if (dateTimeStr.matches(DATETIME_COLON_REGEX)) {
                return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(DATETIME_FORMAT_COLON));
            } else if (dateTimeStr.matches(DATE_REGEX)) {
                return LocalDate.parse(dateTimeStr, DateTimeFormatter.ofPattern(DATE_FORMAT)).atStartOfDay();
            } else {
                throw new DateTimeParseException("Unsupported format", dateTimeStr, 0);
            }
        } catch (DateTimeParseException e) {
            throw new PingpongException("Invalid datetime format."
                    + "Please use formats like: 2019-12-02 1800, 2019-12-02 18:00, or 2019-12-02");
        }
    }
}
