package pingpong.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

import pingpong.task.Deadline;
import pingpong.task.Event;
import pingpong.task.Task;
import pingpong.task.Todo;

/**
 * Handles loading and saving of tasks to/from file storage.
 * Manages the persistence layer for the Pingpong application.
 */
public class Storage {
    // File format constants
    private static final String FIELD_SEPARATOR = " | ";
    private static final String TODO_TYPE = "T";
    private static final String DEADLINE_TYPE = "D";
    private static final String EVENT_TYPE = "E";
    private static final String DONE_MARKER = "1";
    private static final String NOT_DONE_MARKER = "0";
    private static final String DEFAULT_DIRECTORY = "./";

    // Error message constants
    private static final String LOAD_ERROR_PREFIX = "Error loading tasks from file: ";
    private static final String SAVE_ERROR_PREFIX = "Error saving tasks to file: ";
    private static final String CORRUPTED_DATA_WARNING = "Warning: Skipping corrupted task data: ";
    private static final String INVALID_DATE_WARNING = "Warning: Invalid date format in file for deadline: ";
    private static final String INVALID_DATETIME_WARNING = "Warning: Invalid datetime format in file for event: ";

    // Minimum number of parts required for different task types
    private static final int MIN_TASK_PARTS = 3;
    private static final int MIN_DEADLINE_PARTS = 4;
    private static final int MIN_EVENT_PARTS = 5;

    private final String filePath;
    private final String directoryPath;

    /**
     * Creates a new Storage instance with the specified file path.
     *
     * @param filePath the path to the file where tasks will be stored
     */
    public Storage(String filePath) {
        assert filePath != null : "File path should not be null";
        assert !filePath.trim().isEmpty() : "File path should not be empty";

        this.filePath = filePath;
        this.directoryPath = extractDirectoryPath(filePath);

        assert this.filePath != null : "File path should be set";
        assert this.directoryPath != null : "Directory path should be set";
    }

    /**
     * Extracts the directory path from the full file path.
     *
     * @param filePath the complete file path
     * @return the directory path portion
     */
    private String extractDirectoryPath(String filePath) {
        int lastSlashIndex = filePath.lastIndexOf('/');
        return lastSlashIndex != -1 ? filePath.substring(0, lastSlashIndex) : DEFAULT_DIRECTORY;
    }

    /**
     * Loads tasks from the storage file.
     * Creates the directory and file if they don't exist.
     * Handles corrupted or invalid task data gracefully by skipping them.
     *
     * @return a list of tasks loaded from the file, or empty list if file doesn't exist
     */
    public ArrayList<Task> load() {
        ArrayList<Task> tasks = new ArrayList<>();
        assert tasks != null : "Task list should be initialized";

        try {
            ensureDirectoryExists();
            File dataFile = new File(filePath);
            assert dataFile != null : "Data file object should not be null";

            if (!dataFile.exists()) {
                assert tasks.isEmpty() : "Task list should be empty when no file exists";
                return tasks;
            }

            tasks = loadTasksFromFile(dataFile);
        } catch (IOException e) {
            System.out.println(LOAD_ERROR_PREFIX + e.getMessage());
        }

        assert tasks != null : "Returned task list should not be null";
        return tasks;
    }

    /**
     * Creates the data directory if it doesn't exist.
     *
     * @throws IOException if directory creation fails
     */
    private void ensureDirectoryExists() throws IOException {
        File dataDir = new File(directoryPath);
        assert dataDir != null : "Data directory file object should not be null";

        if (!dataDir.exists()) {
            boolean created = dataDir.mkdirs();
            if (!created) {
                throw new IOException("Failed to create data directory: " + directoryPath);
            }
            assert created || dataDir.exists() : "Directory should exist after creation attempt";
        }
    }

    /**
     * Loads tasks from the specified file.
     *
     * @param dataFile the file to load from
     * @return list of tasks loaded from file
     * @throws IOException if file reading fails
     */
    private ArrayList<Task> loadTasksFromFile(File dataFile) throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();
        assert dataFile != null : "Data file should not be null";

        try (Scanner fileScanner = new Scanner(dataFile)) {
            assert fileScanner != null : "File scanner should not be null";

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (!line.isEmpty()) {
                    Task task = parseTaskFromFile(line);
                    if (task != null) {
                        assert task.getDescription() != null : "Loaded task should have description";
                        tasks.add(task);
                    }
                }
            }
        }

        assert tasks != null : "Returned task list should not be null";
        return tasks;
    }

    /**
     * Saves the provided list of tasks to the storage file.
     * Creates the directory if it doesn't exist.
     *
     * @param tasks the list of tasks to save
     */
    public void save(ArrayList<Task> tasks) {
        assert tasks != null : "Tasks list should not be null";

        try {
            ensureDirectoryExists();
            saveTasksToFile(tasks);
        } catch (IOException e) {
            System.out.println(SAVE_ERROR_PREFIX + e.getMessage());
        }
    }

    /**
     * Writes tasks to the storage file.
     *
     * @param tasks the list of tasks to write
     * @throws IOException if file writing fails
     */
    private void saveTasksToFile(ArrayList<Task> tasks) throws IOException {
        try (PrintWriter printWriter = new PrintWriter(new FileWriter(filePath))) {
            assert printWriter != null : "Print writer should not be null";

            for (Task task : tasks) {
                assert task != null : "Each task should not be null";
                String line = formatTaskForFile(task);
                assert line != null : "Formatted task string should not be null";
                assert !line.trim().isEmpty() : "Formatted task string should not be empty";
                printWriter.println(line);
            }
        }
    }

    /**
     * Parses a single line from the storage file into a Task object.
     * Handles different task types and validates date/time formats.
     *
     * @param line the line from the file to parse
     * @return the parsed Task object, or null if parsing fails
     */
    private Task parseTaskFromFile(String line) {
        assert line != null : "Line should not be null";
        assert !line.trim().isEmpty() : "Line should not be empty";

        try {
            String[] parts = line.split(Pattern.quote(FIELD_SEPARATOR));

            if (!isValidTaskFormat(parts)) {
                return null;
            }

            assert parts.length >= MIN_TASK_PARTS : "Should have at least 3 parts for valid task";

            String type = parts[0].trim();
            boolean isDone = parts[1].trim().equals(DONE_MARKER);
            String description = parts[2].trim();

            assert type != null : "Task type should not be null";
            assert description != null : "Task description should not be null";
            assert !description.isEmpty() : "Task description should not be empty";

            Task task = createTaskByType(type, description, parts);

            if (task != null && isDone) {
                task.markAsDone();
                assert task.isDone() : "Task should be marked as done if loaded as done";
            }

            return task;
        } catch (Exception e) {
            System.out.println(CORRUPTED_DATA_WARNING + line);
            return null;
        }
    }

    /**
     * Validates that the parsed parts array has minimum required elements.
     *
     * @param parts the split line parts
     * @return true if format is valid, false otherwise
     */
    private boolean isValidTaskFormat(String[] parts) {
        return parts.length >= MIN_TASK_PARTS;
    }

    /**
     * Creates a task object based on the type indicator.
     *
     * @param type the task type indicator
     * @param description the task description
     * @param parts the complete parsed line parts
     * @return the created Task object, or null if creation fails
     */
    private Task createTaskByType(String type, String description, String[] parts) {
        assert type != null : "Task type should not be null";
        assert description != null : "Task description should not be null";
        assert parts != null : "Parts array should not be null";

        switch (type) {
        case TODO_TYPE:
            return new Todo(description);
        case DEADLINE_TYPE:
            return createDeadlineTask(description, parts);
        case EVENT_TYPE:
            return createEventTask(description, parts);
        default:
            return null;
        }
    }

    /**
     * Creates a Deadline task from file data.
     *
     * @param description the task description
     * @param parts the complete parsed line parts
     * @return Deadline task or null if creation fails
     */
    private Task createDeadlineTask(String description, String[] parts) {
        if (parts.length < MIN_DEADLINE_PARTS) {
            return null;
        }

        try {
            LocalDate by = LocalDate.parse(parts[3].trim(), DateTimeFormatter.ISO_LOCAL_DATE);
            assert by != null : "Parsed deadline date should not be null";
            return new Deadline(description, by);
        } catch (DateTimeParseException e) {
            System.out.println(INVALID_DATE_WARNING + String.join(FIELD_SEPARATOR, parts));
            return null;
        }
    }

    /**
     * Creates an Event task from file data.
     *
     * @param description the task description
     * @param parts the complete parsed line parts
     * @return Event task or null if creation fails
     */
    private Task createEventTask(String description, String[] parts) {
        if (parts.length < MIN_EVENT_PARTS) {
            return null;
        }

        try {
            LocalDateTime start = LocalDateTime.parse(parts[3].trim(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            LocalDateTime end = LocalDateTime.parse(parts[4].trim(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            assert start != null : "Parsed start time should not be null";
            assert end != null : "Parsed end time should not be null";
            assert !start.isAfter(end) : "Start time should not be after end time";
            return new Event(description, start, end);
        } catch (DateTimeParseException e) {
            System.out.println(INVALID_DATETIME_WARNING + String.join(FIELD_SEPARATOR, parts));
            return null;
        }
    }

    /**
     * Formats a Task object into a string suitable for file storage.
     *
     * @param task the task to format
     * @return the formatted string for file storage
     */
    private String formatTaskForFile(Task task) {
        assert task != null : "Task should not be null";
        assert task.getType() != null : "Task type should not be null";
        assert task.getDescription() != null : "Task description should not be null";

        String isDoneStr = task.isDone() ? DONE_MARKER : NOT_DONE_MARKER;
        String type = task.getType().getSymbol();
        String description = task.getDescription();

        assert type != null : "Task type symbol should not be null";
        assert !type.isEmpty() : "Task type symbol should not be empty";
        assert description != null : "Task description should not be null";

        String formattedString;
        switch (task.getType()) {
        case TODO:
            formattedString = formatTodoForFile(type, isDoneStr, description);
            break;
        case DEADLINE:
            formattedString = formatDeadlineForFile(type, isDoneStr, description, (Deadline) task);
            break;
        case Event:
            formattedString = formatEventForFile(type, isDoneStr, description, (Event) task);
            break;
        default:
            formattedString = formatTodoForFile(type, isDoneStr, description);
            break;
        }

        assert formattedString != null : "Formatted string should not be null";
        assert !formattedString.trim().isEmpty() : "Formatted string should not be empty";
        return formattedString;
    }

    /**
     * Formats a Todo task for file storage.
     *
     * @param type the task type symbol
     * @param isDoneStr the completion status string
     * @param description the task description
     * @return formatted string
     */
    private String formatTodoForFile(String type, String isDoneStr, String description) {
        return String.join(FIELD_SEPARATOR, type, isDoneStr, description);
    }

    /**
     * Formats a Deadline task for file storage.
     *
     * @param type the task type symbol
     * @param isDoneStr the completion status string
     * @param description the task description
     * @param deadline the deadline task
     * @return formatted string
     */
    private String formatDeadlineForFile(String type, String isDoneStr, String description, Deadline deadline) {
        assert deadline != null : "Deadline should not be null";
        assert deadline.getByForFile() != null : "Deadline date string should not be null";

        return String.join(FIELD_SEPARATOR, type, isDoneStr, description, deadline.getByForFile());
    }

    /**
     * Formats an Event task for file storage.
     *
     * @param type the task type symbol
     * @param isDoneStr the completion status string
     * @param description the task description
     * @param event the event task
     * @return formatted string
     */
    private String formatEventForFile(String type, String isDoneStr, String description, Event event) {
        assert event != null : "Event should not be null";
        assert event.getStartForFile() != null : "Event start string should not be null";
        assert event.getEndForFile() != null : "Event end string should not be null";

        return String.join(FIELD_SEPARATOR, type, isDoneStr, description,
                event.getStartForFile(), event.getEndForFile());
    }
}
