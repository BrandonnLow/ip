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
        this.filePath = filePath;
        this.directoryPath = extractDirectoryPath(filePath);
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

        try {
            ensureDirectoryExists();
            File dataFile = new File(filePath);

            if (!dataFile.exists()) {
                return tasks;
            }

            tasks = loadTasksFromFile(dataFile);
        } catch (IOException e) {
            System.out.println(LOAD_ERROR_PREFIX + e.getMessage());
        }

        return tasks;
    }

    /**
     * Creates the data directory if it doesn't exist.
     *
     * @throws IOException if directory creation fails
     */
    private void ensureDirectoryExists() throws IOException {
        File dataDir = new File(directoryPath);
        if (!dataDir.exists()) {
            boolean created = dataDir.mkdirs();
            if (!created) {
                throw new IOException("Failed to create data directory: " + directoryPath);
            }
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

        try (Scanner fileScanner = new Scanner(dataFile)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (!line.isEmpty()) {
                    Task task = parseTaskFromFile(line);
                    if (task != null) {
                        tasks.add(task);
                    }
                }
            }
        }

        return tasks;
    }

    /**
     * Saves the provided list of tasks to the storage file.
     * Creates the directory if it doesn't exist.
     *
     * @param tasks the list of tasks to save
     */
    public void save(ArrayList<Task> tasks) {
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
            for (Task task : tasks) {
                String line = formatTaskForFile(task);
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
        try {
            String[] parts = line.split("\\" + FIELD_SEPARATOR);

            if (!isValidTaskFormat(parts)) {
                return null;
            }

            String type = parts[0].trim();
            boolean isDone = parts[1].trim().equals(DONE_MARKER);
            String description = parts[2].trim();

            Task task = createTaskByType(type, description, parts);

            if (task != null && isDone) {
                task.markAsDone();
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
        String isDoneStr = task.isDone() ? DONE_MARKER : NOT_DONE_MARKER;
        String type = task.getType().getSymbol();
        String description = task.getDescription();

        switch (task.getType()) {
        case TODO:
            return formatTodoForFile(type, isDoneStr, description);
        case DEADLINE:
            return formatDeadlineForFile(type, isDoneStr, description, (Deadline) task);
        case Event:
            return formatEventForFile(type, isDoneStr, description, (Event) task);
        default:
            return formatTodoForFile(type, isDoneStr, description);
        }
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
        return String.join(FIELD_SEPARATOR, type, isDoneStr, description,
                event.getStartForFile(), event.getEndForFile());
    }
}
