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

        if (input.trim().isEmpty()) {
            throw new PingpongException("Please enter a command.");
        }

        String trimmedInput = input.trim();
        assert !trimmedInput.isEmpty() : "Trimmed input should not be empty after validation";

        if (trimmedInput.equals("list")) {
            return new ListCommand();
        } else if (trimmedInput.equals("mark") || trimmedInput.equals("mark ")) {
            throw new PingpongException("Please specify which task(s) to mark.");
        } else if (trimmedInput.startsWith("mark ")) {
            return parseMarkCommand(trimmedInput);
        } else if (trimmedInput.equals("unmark") || trimmedInput.equals("unmark ")) {
            throw new PingpongException("Please specify which task(s) to unmark.");
        } else if (trimmedInput.startsWith("unmark ")) {
            return parseUnmarkCommand(trimmedInput);
        } else if (trimmedInput.equals("todo") || trimmedInput.equals("todo ")) {
            throw new PingpongException("The description of a todo cannot be empty.");
        } else if (trimmedInput.startsWith("todo ")) {
            return parseTodoCommand(trimmedInput);
        } else if (trimmedInput.equals("deadline") || trimmedInput.equals("deadline ")) {
            throw new PingpongException("The description of a deadline cannot be empty.");
        } else if (trimmedInput.startsWith("deadline ")) {
            return parseDeadlineCommand(trimmedInput);
        } else if (trimmedInput.equals("event") || trimmedInput.equals("event ")) {
            throw new PingpongException("The description of an event cannot be empty.");
        } else if (trimmedInput.startsWith("event ")) {
            return parseEventCommand(trimmedInput);
        } else if (trimmedInput.equals("delete") || trimmedInput.equals("delete ")) {
            throw new PingpongException("Please specify which task(s) to delete.");
        } else if (trimmedInput.startsWith("delete ")) {
            return parseDeleteCommand(trimmedInput);
        } else if (trimmedInput.equals("find") || trimmedInput.equals("find ")) {
            throw new PingpongException("Please specify a keyword or date (yyyy-MM-dd) to search for.");
        } else if (trimmedInput.startsWith("find ")) {
            return parseFindCommand(trimmedInput);
        } else if (trimmedInput.equals("addmultiple") || trimmedInput.equals("addmultiple ")) {
            throw new PingpongException("Please specify todo descriptions separated by semicolons.");
        } else if (trimmedInput.startsWith("addmultiple ")) {
            return parseAddMultipleCommand(trimmedInput);
        } else {
            throw new PingpongException("I'm sorry, but I don't know what that means :-(");
        }
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
        assert input.startsWith("mark ") : "Input should start with 'mark '";

        String numbersStr = input.substring(4).trim();
        assert !numbersStr.isEmpty() : "Numbers string should not be empty after validation";

        String[] numberParts = numbersStr.split("\\s+");
        assert numberParts != null : "Number parts array should not be null";
        assert numberParts.length > 0 : "Should have at least one number part";

        if (numberParts.length == 1) {
            // Single task - use original MarkCommand
            try {
                int taskNum = Integer.parseInt(numberParts[0]);
                assert taskNum > 0 : "Task number should be positive";
                return new MarkCommand(taskNum);
            } catch (NumberFormatException e) {
                throw new PingpongException("Please provide valid task number(s).");
            }
        } else {
            // Multiple tasks - use new MarkMultipleCommand with varargs
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
        assert input.startsWith("unmark ") : "Input should start with 'unmark '";

        String numbersStr = input.substring(6).trim();
        assert !numbersStr.isEmpty() : "Numbers string should not be empty after validation";

        String[] numberParts = numbersStr.split("\\s+");
        assert numberParts != null : "Number parts array should not be null";
        assert numberParts.length > 0 : "Should have at least one number part";

        if (numberParts.length == 1) {
            // Single task - use original UnmarkCommand
            try {
                int taskNum = Integer.parseInt(numberParts[0]);
                assert taskNum > 0 : "Task number should be positive";
                return new UnmarkCommand(taskNum);
            } catch (NumberFormatException e) {
                throw new PingpongException("Please provide valid task number(s).");
            }
        } else {
            // Multiple tasks - use new UnmarkMultipleCommand with varargs
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
        assert input.startsWith("delete ") : "Input should start with 'delete '";

        String numbersStr = input.substring(6).trim();
        assert !numbersStr.isEmpty() : "Numbers string should not be empty after validation";

        String[] numberParts = numbersStr.split("\\s+");
        assert numberParts != null : "Number parts array should not be null";
        assert numberParts.length > 0 : "Should have at least one number part";

        if (numberParts.length == 1) {
            // Single task - use original DeleteCommand
            try {
                int taskNum = Integer.parseInt(numberParts[0]);
                assert taskNum > 0 : "Task number should be positive";
                return new DeleteCommand(taskNum);
            } catch (NumberFormatException e) {
                throw new PingpongException("Please provide valid task number(s).");
            }
        } else {
            // Multiple tasks - use new DeleteMultipleCommand with varargs
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
        assert input.startsWith("addmultiple ") : "Input should start with 'addmultiple '";

        String descriptionsStr = input.substring(11).trim();
        assert !descriptionsStr.isEmpty() : "Descriptions string should not be empty after validation";

        String[] descriptions = descriptionsStr.split(";");
        assert descriptions != null : "Descriptions array should not be null";

        ArrayList<String> validDescriptions = new ArrayList<>();

        for (String desc : descriptions) {
            assert desc != null : "Each description should not be null";
            String trimmed = desc.trim();
            if (!trimmed.isEmpty()) {
                validDescriptions.add(trimmed);
            }
        }

        if (validDescriptions.isEmpty()) {
            throw new PingpongException("Please provide at least one valid todo description.");
        }

        assert !validDescriptions.isEmpty() : "Should have at least one valid description";
        return new AddMultipleCommand(validDescriptions.toArray(new String[0]));
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

        try {
            for (int i = 0; i < numberParts.length; i++) {
                assert numberParts[i] != null : "Each number part should not be null";
                taskNumbers[i] = Integer.parseInt(numberParts[i]);
                if (taskNumbers[i] <= 0) {
                    throw new PingpongException("Task numbers must be positive integers.");
                }
                assert taskNumbers[i] > 0 : "Parsed task number should be positive";
            }

            assert taskNumbers.length == numberParts.length : "All numbers should be parsed";
            return taskNumbers;
        } catch (NumberFormatException e) {
            throw new PingpongException("Please provide valid task number(s).");
        }
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
        assert input.startsWith("todo ") : "Input should start with 'todo '";

        String description = input.substring(5).trim();
        if (description.isEmpty()) {
            throw new PingpongException("The description of a todo cannot be empty.");
        }

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
        assert input.startsWith("deadline ") : "Input should start with 'deadline '";

        String[] parts = input.substring(9).split(" /by ");
        if (parts.length != 2) {
            throw new PingpongException("Please use format: deadline <description> /by <yyyy-MM-dd>");
        }

        assert parts.length == 2 : "Should have exactly 2 parts after splitting";

        String description = parts[0].trim();
        String byStr = parts[1].trim();

        if (description.isEmpty()) {
            throw new PingpongException("The description of a deadline cannot be empty.");
        }
        if (byStr.isEmpty()) {
            throw new PingpongException("The deadline date cannot be empty.");
        }

        assert !description.isEmpty() : "Description should not be empty after validation";
        assert !byStr.isEmpty() : "Date string should not be empty after validation";

        LocalDate by = parseDate(byStr);
        assert by != null : "Parsed date should not be null";

        return new AddDeadlineCommand(description, by);
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
        assert input.startsWith("event ") : "Input should start with 'event '";

        String remaining = input.substring(6);
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

        assert !description.isEmpty() : "Description should not be empty after validation";
        assert !fromStr.isEmpty() : "From string should not be empty after validation";
        assert !toStr.isEmpty() : "To string should not be empty after validation";

        LocalDateTime from = parseDateTime(fromStr);
        LocalDateTime to = parseDateTime(toStr);

        assert from != null : "Parsed start time should not be null";
        assert to != null : "Parsed end time should not be null";

        if (from.isAfter(to)) {
            throw new PingpongException("Event start time cannot be after end time.");
        }

        assert !from.isAfter(to) : "Start time should not be after end time";
        return new AddEventCommand(description, from, to);
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
        assert input.startsWith("find ") : "Input should start with 'find '";

        String searchTerm = input.substring(5).trim();
        if (searchTerm.isEmpty()) {
            throw new PingpongException("Please specify a keyword or date (yyyy-MM-dd) to search for.");
        }

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
            LocalDate parsed = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
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
            if (dateTimeStr.matches("\\d{4}-\\d{2}-\\d{2} \\d{4}")) {
                parsed = LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
            } else if (dateTimeStr.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}")) {
                parsed = LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            } else if (dateTimeStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
                parsed = LocalDate.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
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
}