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
        int lastSlash = filePath.lastIndexOf('/');
        if (lastSlash != -1) {
            this.directoryPath = filePath.substring(0, lastSlash);
        } else {
            this.directoryPath = "./";
        }

        assert this.filePath != null : "File path should be set";
        assert this.directoryPath != null : "Directory path should be set";
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
            File dataDir = new File(directoryPath);
            assert dataDir != null : "Data directory file object should not be null";

            if (!dataDir.exists()) {
                boolean created = dataDir.mkdirs();
                assert created || dataDir.exists() : "Directory should exist after creation attempt";
            }

            File dataFile = new File(filePath);
            assert dataFile != null : "Data file object should not be null";

            if (!dataFile.exists()) {
                assert tasks.isEmpty() : "Task list should be empty when no file exists";
                return tasks;
            }

            Scanner fileScanner = new Scanner(dataFile);
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
            fileScanner.close();
        } catch (IOException e) {
            System.out.println("Error loading tasks from file: " + e.getMessage());
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
            File dataDir = new File(directoryPath);
            assert dataDir != null : "Data directory file object should not be null";

            if (!dataDir.exists()) {
                boolean created = dataDir.mkdirs();
                assert created || dataDir.exists() : "Directory should exist after creation attempt";
            }

            FileWriter fileWriter = new FileWriter(filePath);
            assert fileWriter != null : "File writer should not be null";

            PrintWriter printWriter = new PrintWriter(fileWriter);
            assert printWriter != null : "Print writer should not be null";

            for (Task task : tasks) {
                assert task != null : "Each task should not be null";
                String line = formatTaskForFile(task);
                assert line != null : "Formatted task string should not be null";
                assert !line.trim().isEmpty() : "Formatted task string should not be empty";
                printWriter.println(line);
            }

            printWriter.close();
        } catch (IOException e) {
            System.out.println("Error saving tasks to file: " + e.getMessage());
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
            String[] parts = line.split(" \\| ");
            if (parts.length < 3) {
                return null;
            }

            assert parts.length >= 3 : "Should have at least 3 parts for valid task";

            String type = parts[0].trim();
            boolean isDone = parts[1].trim().equals("1");
            String description = parts[2].trim();

            assert type != null : "Task type should not be null";
            assert description != null : "Task description should not be null";
            assert !description.isEmpty() : "Task description should not be empty";

            Task task = null;
            switch (type) {
            case "T":
                task = new Todo(description);
                break;
            case "D":
                if (parts.length >= 4) {
                    try {
                        LocalDate by = LocalDate.parse(parts[3].trim(), DateTimeFormatter.ISO_LOCAL_DATE);
                        assert by != null : "Parsed deadline date should not be null";
                        task = new Deadline(description, by);
                    } catch (DateTimeParseException e) {
                        System.out.println("Warning: Invalid date format in file for deadline: " + line);
                        return null;
                    }
                }
                break;
            case "E":
                if (parts.length >= 5) {
                    try {
                        LocalDateTime start = LocalDateTime.parse(parts[3].trim(),
                                DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                        LocalDateTime end = LocalDateTime.parse(parts[4].trim(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                        assert start != null : "Parsed start time should not be null";
                        assert end != null : "Parsed end time should not be null";
                        assert !start.isAfter(end) : "Start time should not be after end time";
                        task = new Event(description, start, end);
                    } catch (DateTimeParseException e) {
                        System.out.println("Warning: Invalid datetime format in file for event: " + line);
                        return null;
                    }
                }
                break;
            default:
                break;
            }

            if (task != null && isDone) {
                task.markAsDone();
                assert task.isDone() : "Task should be marked as done if loaded as done";
            }

            return task;
        } catch (Exception e) {
            System.out.println("Warning: Skipping corrupted task data: " + line);
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

        String isDone = task.isDone() ? "1" : "0";
        String type = task.getType().getSymbol();
        String description = task.getDescription();

        assert type != null : "Task type symbol should not be null";
        assert !type.isEmpty() : "Task type symbol should not be empty";
        assert description != null : "Task description should not be null";

        String formattedString;
        switch (task.getType()) {
        case TODO:
            formattedString = String.format("%s | %s | %s", type, isDone, description);
            break;
        case DEADLINE:
            Deadline deadline = (Deadline) task;
            assert deadline.getByForFile() != null : "Deadline date string should not be null";
            formattedString = String.format("%s | %s | %s | %s", type, isDone, description, deadline.getByForFile());
            break;
        case Event:
            Event event = (Event) task;
            assert event.getStartForFile() != null : "Event start string should not be null";
            assert event.getEndForFile() != null : "Event end string should not be null";
            formattedString = String.format("%s | %s | %s | %s | %s", type, isDone, description,
                    event.getStartForFile(), event.getEndForFile());
            break;
        default:
            formattedString = String.format("%s | %s | %s", type, isDone, description);
            break;
        }

        assert formattedString != null : "Formatted string should not be null";
        assert !formattedString.trim().isEmpty() : "Formatted string should not be empty";
        return formattedString;
    }
}
