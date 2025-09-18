package pingpong.command;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import pingpong.PingpongException;
import pingpong.command.parser.DateTimeParser;
import pingpong.command.parser.TaskNumberParser;
import pingpong.command.parser.UpdateFieldParser;

/**
 * Handles parsing of user commands and returns appropriate Command objects.
 * This class contains the main parsing logic for all supported commands in the Pingpong application.
 * Supports varargs for batch operations on multiple tasks and help command.
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
    private static final String HELP_COMMAND = "help";

    // Error messages
    private static final String EMPTY_COMMAND_ERROR = "Please enter a command.\nType 'help' to see available commands.";
    private static final String UNKNOWN_COMMAND_ERROR = "I don't understand '%s'.\n\n"
            + "Available commands: todo, deadline, event, list, mark, unmark, delete, find, update, addmultiple, help, bye\n"
            + "Type 'help' for detailed usage information.";
    private static final String MARK_MISSING_ERROR = "Please specify which task(s) to mark.\n"
            + "Format: mark INDEX [INDEX2 INDEX3...]\n"
            + "Example: mark 1 OR mark 1 3 5";
    private static final String UNMARK_MISSING_ERROR = "Please specify which task(s) to unmark.\n"
            + "Format: unmark INDEX [INDEX2 INDEX3...]\n"
            + "Example: unmark 2 OR unmark 1 2 3";
    private static final String DELETE_MISSING_ERROR = "Please specify which task(s) to delete.\n"
            + "Format: delete INDEX [INDEX2 INDEX3...]\n"
            + "Example: delete 3 OR delete 1 2 4";
    private static final String TODO_EMPTY_ERROR = "The description of a todo cannot be empty.\n"
            + "Format: todo DESCRIPTION\n"
            + "Example: todo Buy groceries";
    private static final String DEADLINE_EMPTY_ERROR = "The description of a deadline cannot be empty.\n"
            + "Format: deadline DESCRIPTION /by DATE\n"
            + "Example: deadline Submit report /by 2025-09-15";
    private static final String EVENT_EMPTY_ERROR = "The description of an event cannot be empty.\n"
            + "Format: event DESCRIPTION /from DATETIME /to DATETIME\n"
            + "Example: event Meeting /from 2025-09-10 1400 /to 2025-09-10 1600";
    private static final String FIND_EMPTY_ERROR = "Please specify a keyword or date (yyyy-MM-dd) to search for.\n"
            + "Examples: find meeting OR find 2025-09-10";
    private static final String ADD_MULTIPLE_EMPTY_ERROR = "Please specify todo descriptions separated by semicolons.\n"
            + "Format: addmultiple DESC1; DESC2; DESC3\n"
            + "Example: addmultiple Buy milk; Call mom; Read book";
    private static final String UPDATE_MISSING_ERROR = "Please specify which task(s) to update.\n"
            + "Format: update INDEX [/desc DESC] [/by DATE] [/from DATETIME] [/to DATETIME]\n"
            + "Example: update 1 /desc New description";
    private static final String UPDATE_NO_FIELDS_ERROR = "Please specify what to update using "
            + "/desc, /by, /from, and/or /to.\n"
            + "Example: update 1 /desc New task description";

    /**
     * Parses user input and returns the appropriate Command object for execution.
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
        case HELP_COMMAND:
            return new HelpCommand();
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
            throw new PingpongException(String.format(UNKNOWN_COMMAND_ERROR, command));
        }
    }

    private static void validateInput(String input) throws PingpongException {
        if (input.trim().isEmpty()) {
            throw new PingpongException(EMPTY_COMMAND_ERROR);
        }
    }

    private static String extractCommand(String input) {
        assert input != null : "Input should not be null";
        assert !input.trim().isEmpty() : "Input should not be empty";

        return input.trim().split("\\s+")[0];
    }

    private static boolean hasArguments(String input, String command) {
        assert input != null : "Input should not be null";
        assert command != null : "Command should not be null";

        return input.length() > command.length()
                && input.substring(command.length()).trim().length() > 0;
    }

    private static Command parseMarkCommand(String input) throws PingpongException {
        assert input != null : "Input should not be null";
        assert input.startsWith("mark") : "Input should start with 'mark'";

        if (!hasArguments(input, MARK_COMMAND)) {
            throw new PingpongException(MARK_MISSING_ERROR);
        }

        String numbersStr = input.substring(MARK_COMMAND.length()).trim();
        String[] numberParts = numbersStr.split("\\s+");

        if (numberParts.length == 1) {
            int taskNum = TaskNumberParser.parseTaskNumber(numberParts[0]);
            return new MarkCommand(taskNum);
        } else {
            int[] taskNumbers = TaskNumberParser.parseTaskNumbers(numberParts);
            return new MarkMultipleCommand(taskNumbers);
        }
    }

    private static Command parseUnmarkCommand(String input) throws PingpongException {
        assert input != null : "Input should not be null";
        assert input.startsWith("unmark") : "Input should start with 'unmark'";

        if (!hasArguments(input, UNMARK_COMMAND)) {
            throw new PingpongException(UNMARK_MISSING_ERROR);
        }

        String numbersStr = input.substring(UNMARK_COMMAND.length()).trim();
        String[] numberParts = numbersStr.split("\\s+");

        if (numberParts.length == 1) {
            int taskNum = TaskNumberParser.parseTaskNumber(numberParts[0]);
            return new UnmarkCommand(taskNum);
        } else {
            int[] taskNumbers = TaskNumberParser.parseTaskNumbers(numberParts);
            return new UnmarkMultipleCommand(taskNumbers);
        }
    }

    private static Command parseDeleteCommand(String input) throws PingpongException {
        assert input != null : "Input should not be null";
        assert input.startsWith("delete") : "Input should start with 'delete'";

        if (!hasArguments(input, DELETE_COMMAND)) {
            throw new PingpongException(DELETE_MISSING_ERROR);
        }

        String numbersStr = input.substring(DELETE_COMMAND.length()).trim();
        String[] numberParts = numbersStr.split("\\s+");

        if (numberParts.length == 1) {
            int taskNum = TaskNumberParser.parseTaskNumber(numberParts[0]);
            return new DeleteCommand(taskNum);
        } else {
            int[] taskNumbers = TaskNumberParser.parseTaskNumbers(numberParts);
            return new DeleteMultipleCommand(taskNumbers);
        }
    }

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

    private static Command parseDeadlineCommand(String input) throws PingpongException {
        assert input != null : "Input should not be null";
        assert input.startsWith("deadline") : "Input should start with 'deadline'";

        if (!hasArguments(input, DEADLINE_COMMAND)) {
            throw new PingpongException(DEADLINE_EMPTY_ERROR);
        }

        String[] parts = input.substring(DEADLINE_COMMAND.length()).split(" /by ");
        if (parts.length != 2) {
            throw new PingpongException("Please use format: deadline <description> /by <yyyy-MM-dd>\n"
                    + "Example: deadline Submit report /by 2025-09-15");
        }

        String description = parts[0].trim();
        String byStr = parts[1].trim();

        validateDeadlineComponents(description, byStr);
        LocalDate by = DateTimeParser.parseDate(byStr);

        return new AddDeadlineCommand(description, by);
    }

    private static void validateDeadlineComponents(String description, String byStr) throws PingpongException {
        if (description.isEmpty()) {
            throw new PingpongException(DEADLINE_EMPTY_ERROR);
        }
        if (byStr.isEmpty()) {
            throw new PingpongException("The deadline date cannot be empty.\n"
                    + "Date format: yyyy-MM-dd (e.g., 2025-09-15)");
        }
    }

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

        LocalDateTime from = DateTimeParser.parseDateTime(fromStr);
        LocalDateTime to = DateTimeParser.parseDateTime(toStr);

        validateEventTiming(from, to);

        return new AddEventCommand(description, from, to);
    }

    private static String[] parseEventParts(String input) throws PingpongException {
        String remaining = input.substring(EVENT_COMMAND.length());
        String[] fromParts = remaining.split(" /from ");

        if (fromParts.length != 2) {
            throw new PingpongException("Please use format: event <description> "
                    + "/from <yyyy-MM-dd HHmm> /to <yyyy-MM-dd HHmm>\n"
                    + "Example: event Meeting /from 2025-09-10 1400 /to 2025-09-10 1600");
        }

        String description = fromParts[0].trim();
        String[] toParts = fromParts[1].split(" /to ");

        if (toParts.length != 2) {
            throw new PingpongException("Please use format: event <description> "
                    + "/from <yyyy-MM-dd HHmm> /to <yyyy-MM-dd HHmm>\n"
                    + "Example: event Meeting /from 2025-09-10 1400 /to 2025-09-10 1600");
        }

        return new String[]{description, toParts[0].trim(), toParts[1].trim()};
    }

    private static void validateEventComponents(String description, String fromStr, String toStr)
            throws PingpongException {
        if (description.isEmpty()) {
            throw new PingpongException(EVENT_EMPTY_ERROR);
        }
        if (fromStr.isEmpty()) {
            throw new PingpongException("The event start time cannot be empty.\n"
                    + "DateTime format: yyyy-MM-dd HHmm (e.g., 2025-09-10 1400)");
        }
        if (toStr.isEmpty()) {
            throw new PingpongException("The event end time cannot be empty.\n"
                    + "DateTime format: yyyy-MM-dd HHmm (e.g., 2025-09-10 1600)");
        }
    }

    private static void validateEventTiming(LocalDateTime from, LocalDateTime to) throws PingpongException {
        if (from.isAfter(to)) {
            throw new PingpongException("Event start time cannot be after end time.");
        }
    }

    private static Command parseFindCommand(String input) throws PingpongException {
        assert input != null : "Input should not be null";
        assert input.startsWith("find") : "Input should start with 'find'";

        if (!hasArguments(input, FIND_COMMAND)) {
            throw new PingpongException(FIND_EMPTY_ERROR);
        }

        String searchTerm = input.substring(FIND_COMMAND.length()).trim();
        return new FindCommand(searchTerm);
    }

    private static Command parseAddMultipleCommand(String input) throws PingpongException {
        assert input != null : "Input should not be null";
        assert input.startsWith("addmultiple") : "Input should start with 'addmultiple'";

        if (!hasArguments(input, ADD_MULTIPLE_COMMAND)) {
            throw new PingpongException(ADD_MULTIPLE_EMPTY_ERROR);
        }

        String descriptionsStr = input.substring(ADD_MULTIPLE_COMMAND.length()).trim();
        String[] descriptions = descriptionsStr.split(";");

        ArrayList<String> validDescriptions = extractValidDescriptions(descriptions);

        if (validDescriptions.isEmpty()) {
            throw new PingpongException("Please provide at least one valid todo description.\n"
                    + "Separate multiple descriptions with semicolons (;)");
        }

        return new AddMultipleCommand(validDescriptions.toArray(new String[0]));
    }

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

    private static Command parseUpdateCommand(String input) throws PingpongException {
        assert input != null : "Input should not be null";
        assert input.startsWith("update") : "Input should start with 'update'";

        if (!hasArguments(input, UPDATE_COMMAND)) {
            throw new PingpongException(UPDATE_MISSING_ERROR);
        }

        String remaining = input.substring(UPDATE_COMMAND.length()).trim();

        int firstFieldIndex = findFirstFieldIndicator(remaining);
        if (firstFieldIndex == -1) {
            throw new PingpongException(UPDATE_NO_FIELDS_ERROR);
        }

        String taskNumbersStr = remaining.substring(0, firstFieldIndex).trim();
        String fieldsStr = remaining.substring(firstFieldIndex).trim();

        String[] taskNumberParts = taskNumbersStr.split("\\s+");

        if (taskNumberParts.length == 1) {
            int taskNumber = TaskNumberParser.parseTaskNumber(taskNumberParts[0]);
            UpdateCommand command = new UpdateCommand(taskNumber);
            UpdateFieldParser.parseFields(command, fieldsStr);
            return command;
        } else {
            int[] taskNumbers = TaskNumberParser.parseTaskNumbers(taskNumberParts);
            UpdateMultipleCommand command = new UpdateMultipleCommand(taskNumbers);
            UpdateFieldParser.parseFields(command, fieldsStr);
            return command;
        }
    }

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
}
