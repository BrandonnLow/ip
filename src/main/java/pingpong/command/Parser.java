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
    private static final String UPDATE_COMMAND = "update";

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
    private static final String UPDATE_MISSING_ERROR = "Please specify which task(s) to update.";
    private static final String UPDATE_NO_FIELDS_ERROR = "Please specify what to update using "
            + "/desc, /by, /from, and/or /to.";

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
        assert input != null : "Input should not be null";

        validateInput(input);

        String command = extractCommand(input);
        assert command != null : "Extracted command should not be null";

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
        case UPDATE_COMMAND:
            return parseUpdateCommand(input);
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
        assert input != null : "Input should not be null";
        assert !input.trim().isEmpty() : "Input should not be empty";

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
        assert input != null : "Input should not be null";
        assert command != null : "Command should not be null";

        return input.length() > command.length()
                && input.substring(command.length()).trim().length() > 0;
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
        assert input != null : "Input should not be null";
        assert input.startsWith("mark") : "Input should start with 'mark'";

        if (!hasArguments(input, MARK_COMMAND)) {
            throw new PingpongException(MARK_MISSING_ERROR);
        }

        String numbersStr = input.substring(MARK_COMMAND.length()).trim();
        assert !numbersStr.isEmpty() : "Numbers string should not be empty after validation";

        String[] numberParts = numbersStr.split("\\s+");
        assert numberParts != null : "Number parts array should not be null";
        assert numberParts.length > 0 : "Should have at least one number part";

        if (numberParts.length == 1) {
            int taskNum = parseTaskNumber(numberParts[0]);
            assert taskNum > 0 : "Task number should be positive";
            return new MarkCommand(taskNum);
        } else {
            int[] taskNumbers = parseTaskNumbers(numberParts);
            assert taskNumbers != null : "Parsed task numbers should not be null";
            assert taskNumbers.length > 1 : "Should have multiple task numbers";
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
        assert input != null : "Input should not be null";
        assert input.startsWith("unmark") : "Input should start with 'unmark'";

        if (!hasArguments(input, UNMARK_COMMAND)) {
            throw new PingpongException(UNMARK_MISSING_ERROR);
        }

        String numbersStr = input.substring(UNMARK_COMMAND.length()).trim();
        assert !numbersStr.isEmpty() : "Numbers string should not be empty after validation";

        String[] numberParts = numbersStr.split("\\s+");
        assert numberParts != null : "Number parts array should not be null";
        assert numberParts.length > 0 : "Should have at least one number part";

        if (numberParts.length == 1) {
            int taskNum = parseTaskNumber(numberParts[0]);
            assert taskNum > 0 : "Task number should be positive";
            return new UnmarkCommand(taskNum);
        } else {
            int[] taskNumbers = parseTaskNumbers(numberParts);
            assert taskNumbers != null : "Parsed task numbers should not be null";
            assert taskNumbers.length > 1 : "Should have multiple task numbers";
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
        assert input != null : "Input should not be null";
        assert input.startsWith("delete") : "Input should start with 'delete'";

        if (!hasArguments(input, DELETE_COMMAND)) {
            throw new PingpongException(DELETE_MISSING_ERROR);
        }

        String numbersStr = input.substring(DELETE_COMMAND.length()).trim();
        assert !numbersStr.isEmpty() : "Numbers string should not be empty after validation";

        String[] numberParts = numbersStr.split("\\s+");
        assert numberParts != null : "Number parts array should not be null";
        assert numberParts.length > 0 : "Should have at least one number part";

        if (numberParts.length == 1) {
            int taskNum = parseTaskNumber(numberParts[0]);
            assert taskNum > 0 : "Task number should be positive";
            return new DeleteCommand(taskNum);
        } else {
            int[] taskNumbers = parseTaskNumbers(numberParts);
            assert taskNumbers != null : "Parsed task numbers should not be null";
            assert taskNumbers.length > 1 : "Should have multiple task numbers";
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
        assert input != null : "Input should not be null";
        assert input.startsWith("addmultiple") : "Input should start with 'addmultiple'";

        if (!hasArguments(input, ADD_MULTIPLE_COMMAND)) {
            throw new PingpongException(ADD_MULTIPLE_EMPTY_ERROR);
        }

        String descriptionsStr = input.substring(ADD_MULTIPLE_COMMAND.length()).trim();
        assert !descriptionsStr.isEmpty() : "Descriptions string should not be empty after validation";

        String[] descriptions = descriptionsStr.split(";");
        assert descriptions != null : "Descriptions array should not be null";

        ArrayList<String> validDescriptions = extractValidDescriptions(descriptions);

        if (validDescriptions.isEmpty()) {
            throw new PingpongException("Please provide at least one valid todo description.");
        }

        assert !validDescriptions.isEmpty() : "Should have at least one valid description";
        return new AddMultipleCommand(validDescriptions.toArray(new String[0]));
    }

    /**
     * Filters out empty descriptions from the array of description strings.
     *
     * @param descriptions array of raw description strings
     * @return list of valid, non-empty descriptions
     */
    private static ArrayList<String> extractValidDescriptions(String[] descriptions) {
        assert descriptions != null : "Descriptions array should not be null";

        ArrayList<String> validDescriptions = new ArrayList<>();
        for (String desc : descriptions) {
            assert desc != null : "Each description should not be null";
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
        assert numberStr != null : "Number string should not be null";

        try {
            int taskNum = Integer.parseInt(numberStr);
            if (taskNum <= 0) {
                throw new PingpongException(POSITIVE_NUMBER_ERROR);
            }
            assert taskNum > 0 : "Parsed task number should be positive";
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
        assert numberParts != null : "Number parts array should not be null";
        assert numberParts.length > 0 : "Should have at least one number part";

        int[] taskNumbers = new int[numberParts.length];

        for (int i = 0; i < numberParts.length; i++) {
            assert numberParts[i] != null : "Each number part should not be null";
            taskNumbers[i] = parseTaskNumber(numberParts[i]);
        }

        assert taskNumbers.length == numberParts.length : "All numbers should be parsed";
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
        assert input != null : "Input should not be null";
        assert input.startsWith("todo") : "Input should start with 'todo'";

        if (!hasArguments(input, TODO_COMMAND)) {
            throw new PingpongException(TODO_EMPTY_ERROR);
        }

        String description = input.substring(TODO_COMMAND.length()).trim();
        assert !description.isEmpty() : "Description should not be empty after validation";

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
        assert input != null : "Input should not be null";
        assert input.startsWith("deadline") : "Input should start with 'deadline'";

        if (!hasArguments(input, DEADLINE_COMMAND)) {
            throw new PingpongException(DEADLINE_EMPTY_ERROR);
        }

        String[] parts = input.substring(DEADLINE_COMMAND.length()).split(" /by ");
        if (parts.length != 2) {
            throw new PingpongException("Please use format: deadline <description> /by <yyyy-MM-dd>");
        }

        assert parts.length == 2 : "Should have exactly 2 parts after splitting";

        String description = parts[0].trim();
        String byStr = parts[1].trim();

        validateDeadlineComponents(description, byStr);
        LocalDate by = parseDate(byStr);
        assert by != null : "Parsed date should not be null";

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

        assert !description.isEmpty() : "Description should not be empty after validation";
        assert !byStr.isEmpty() : "Date string should not be empty after validation";
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
        assert input != null : "Input should not be null";
        assert input.startsWith("event") : "Input should start with 'event'";

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

        assert from != null : "Parsed start time should not be null";
        assert to != null : "Parsed end time should not be null";

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

        assert fromParts.length == 2 : "Should have exactly 2 parts after splitting by /from";

        String description = fromParts[0].trim();
        String[] toParts = fromParts[1].split(" /to ");

        if (toParts.length != 2) {
            throw new PingpongException("Please use format: event <description> "
                    + "/from <yyyy-MM-dd HHmm> /to <yyyy-MM-dd HHmm>");
        }

        assert toParts.length == 2 : "Should have exactly 2 parts after splitting by /to";

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

        assert !description.isEmpty() : "Description should not be empty after validation";
        assert !fromStr.isEmpty() : "From string should not be empty after validation";
        assert !toStr.isEmpty() : "To string should not be empty after validation";
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

        assert !from.isAfter(to) : "Start time should not be after end time";
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
        assert input != null : "Input should not be null";
        assert input.startsWith("find") : "Input should start with 'find'";

        if (!hasArguments(input, FIND_COMMAND)) {
            throw new PingpongException(FIND_EMPTY_ERROR);
        }

        String searchTerm = input.substring(FIND_COMMAND.length()).trim();
        assert !searchTerm.isEmpty() : "Search term should not be empty after validation";

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
        assert dateStr != null : "Date string should not be null";
        assert !dateStr.trim().isEmpty() : "Date string should not be empty";

        try {
            LocalDate parsed = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(DATE_FORMAT));
            assert parsed != null : "Parsed date should not be null";
            return parsed;
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
        assert dateTimeStr != null : "DateTime string should not be null";
        assert !dateTimeStr.trim().isEmpty() : "DateTime string should not be empty";

        try {
            LocalDateTime parsed;
            if (dateTimeStr.matches(DATETIME_HHMM_REGEX)) {
                parsed = LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(DATETIME_FORMAT_HHMM));
            } else if (dateTimeStr.matches(DATETIME_COLON_REGEX)) {
                parsed = LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(DATETIME_FORMAT_COLON));
            } else if (dateTimeStr.matches(DATE_REGEX)) {
                parsed = LocalDate.parse(dateTimeStr, DateTimeFormatter.ofPattern(DATE_FORMAT)).atStartOfDay();
            } else {
                throw new DateTimeParseException("Unsupported format", dateTimeStr, 0);
            }

            assert parsed != null : "Parsed datetime should not be null";
            return parsed;
        } catch (DateTimeParseException e) {
            throw new PingpongException("Invalid datetime format."
                    + "Please use formats like: 2019-12-02 1800, 2019-12-02 18:00, or 2019-12-02");
        }
    }


    /**
     * Parses an update command to extract task numbers and update fields.
     * Expected formats:
     * - "update 1 /desc new description"
     * - "update 1 /by 2025-09-05"
     * - "update 1 /from 2025-09-05 1400 /to 2025-09-05 1600"
     * - "update 1 2 3 /desc new description" (multiple tasks)
     * - "update 1 /desc new description /by 2025-09-05" (multiple fields)
     *
     * @param input the update command string
     * @return an UpdateCommand or UpdateMultipleCommand
     * @throws PingpongException if the format is invalid
     */
    private static Command parseUpdateCommand(String input) throws PingpongException {
        assert input != null : "Input should not be null";
        assert input.startsWith("update") : "Input should start with 'update'";

        if (!hasArguments(input, UPDATE_COMMAND)) {
            throw new PingpongException(UPDATE_MISSING_ERROR);
        }

        String remaining = input.substring(UPDATE_COMMAND.length()).trim();
        assert !remaining.isEmpty() : "Remaining string should not be empty after validation";

        // Find the first field indicator to separate task numbers from update fields
        int firstFieldIndex = findFirstFieldIndicator(remaining);
        if (firstFieldIndex == -1) {
            throw new PingpongException(UPDATE_NO_FIELDS_ERROR);
        }

        String taskNumbersStr = remaining.substring(0, firstFieldIndex).trim();
        String fieldsStr = remaining.substring(firstFieldIndex).trim();

        // Parse task numbers
        String[] taskNumberParts = taskNumbersStr.split("\\s+");
        assert taskNumberParts != null : "Task number parts should not be null";
        assert taskNumberParts.length > 0 : "Should have at least one task number";

        if (taskNumberParts.length == 1) {
            int taskNumber = parseTaskNumber(taskNumberParts[0]);
            UpdateCommand command = new UpdateCommand(taskNumber);
            parseUpdateFields(command, fieldsStr);
            return command;
        } else {
            int[] taskNumbers = parseTaskNumbers(taskNumberParts);
            UpdateMultipleCommand command = new UpdateMultipleCommand(taskNumbers);
            parseUpdateFields(command, fieldsStr);
            return command;
        }
    }

    /**
     * Finds the index of the first field indicator (/desc, /by, /from, /to) in the string.
     *
     * @param str the string to search
     * @return the index of the first field indicator, or -1 if none found
     */
    private static int findFirstFieldIndicator(String str) {
        assert str != null : "String should not be null";

        String[] indicators = {"/desc", "/by", "/from", "/to"};
        int earliest = Integer.MAX_VALUE;

        for (String indicator : indicators) {
            int index = str.indexOf(indicator);
            if (index != -1 && index < earliest) {
                earliest = index;
            }
        }

        return earliest == Integer.MAX_VALUE ? -1 : earliest;
    }

    /**
     * Parses update fields and applies them to an UpdateCommand.
     *
     * @param command the UpdateCommand to configure
     * @param fieldsStr the string containing field specifications
     * @throws PingpongException if field parsing fails
     */
    private static void parseUpdateFields(UpdateCommand command, String fieldsStr) throws PingpongException {
        assert command != null : "Command should not be null";
        assert fieldsStr != null : "Fields string should not be null";

        // Parse /desc field
        String description = parseUpdateField(fieldsStr, "/desc");
        if (description != null) {
            command.withDescription(description);
        }

        // Parse /by field
        String byStr = parseUpdateField(fieldsStr, "/by");
        if (byStr != null) {
            LocalDate by = parseDate(byStr);
            command.withDeadline(by);
        }

        // Parse /from field
        String fromStr = parseUpdateField(fieldsStr, "/from");
        if (fromStr != null) {
            LocalDateTime from = parseDateTime(fromStr);
            command.withStart(from);
        }

        // Parse /to field
        String toStr = parseUpdateField(fieldsStr, "/to");
        if (toStr != null) {
            LocalDateTime to = parseDateTime(toStr);
            command.withEnd(to);
        }
    }

    /**
     * Parses update fields and applies them to an UpdateMultipleCommand.
     *
     * @param command the UpdateMultipleCommand to configure
     * @param fieldsStr the string containing field specifications
     * @throws PingpongException if field parsing fails
     */
    private static void parseUpdateFields(UpdateMultipleCommand command, String fieldsStr) throws PingpongException {
        assert command != null : "Command should not be null";
        assert fieldsStr != null : "Fields string should not be null";

        // Parse /desc field
        String description = parseUpdateField(fieldsStr, "/desc");
        if (description != null) {
            command.withDescription(description);
        }

        // Parse /by field
        String byStr = parseUpdateField(fieldsStr, "/by");
        if (byStr != null) {
            LocalDate by = parseDate(byStr);
            command.withDeadline(by);
        }

        // Parse /from field
        String fromStr = parseUpdateField(fieldsStr, "/from");
        if (fromStr != null) {
            LocalDateTime from = parseDateTime(fromStr);
            command.withStart(from);
        }

        // Parse /to field
        String toStr = parseUpdateField(fieldsStr, "/to");
        if (toStr != null) {
            LocalDateTime to = parseDateTime(toStr);
            command.withEnd(to);
        }
    }

    /**
     * Parses a specific field from the fields string.
     *
     * @param fieldsStr the complete fields string
     * @param fieldIndicator the field indicator to look for (e.g., "/desc")
     * @return the field value, or null if field not found
     */
    private static String parseUpdateField(String fieldsStr, String fieldIndicator) {
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

        String value = fieldsStr.substring(startIndex, endIndex).trim();
        return value.isEmpty() ? null : value;
    }
}
