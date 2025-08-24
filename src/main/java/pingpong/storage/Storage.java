package pingpong.storage;

import pingpong.task.Task;
import pingpong.task.Todo;
import pingpong.task.Deadline;
import pingpong.task.Event;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles loading and saving of tasks to/from file
 */
public class Storage {
    private final String filePath;
    private final String directoryPath;

    public Storage(String filePath) {
        this.filePath = filePath;
        int lastSlash = filePath.lastIndexOf('/');
        if (lastSlash != -1) {
            this.directoryPath = filePath.substring(0, lastSlash);
        } else {
            this.directoryPath = "./";
        }
    }

    public ArrayList<Task> load() {
        ArrayList<Task> tasks = new ArrayList<>();

        try {
            File dataDir = new File(directoryPath);
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }

            File dataFile = new File(filePath);
            if (!dataFile.exists()) {
                return tasks;
            }

            Scanner fileScanner = new Scanner(dataFile);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (!line.isEmpty()) {
                    Task task = parseTaskFromFile(line);
                    if (task != null) {
                        tasks.add(task);
                    }
                }
            }
            fileScanner.close();
        } catch (IOException e) {
            System.out.println("Error loading tasks from file: " + e.getMessage());
        }

        return tasks;
    }

    public void save(ArrayList<Task> tasks) {
        try {
            File dataDir = new File(directoryPath);
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }

            FileWriter fileWriter = new FileWriter(filePath);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            for (Task task : tasks) {
                String line = formatTaskForFile(task);
                printWriter.println(line);
            }

            printWriter.close();
        } catch (IOException e) {
            System.out.println("Error saving tasks to file: " + e.getMessage());
        }
    }

    private Task parseTaskFromFile(String line) {
        try {
            String[] parts = line.split(" \\| ");
            if (parts.length < 3) {
                return null;
            }

            String type = parts[0].trim();
            boolean isDone = parts[1].trim().equals("1");
            String description = parts[2].trim();

            Task task = null;
            switch (type) {
                case "T":
                    task = new Todo(description);
                    break;
                case "D":
                    if (parts.length >= 4) {
                        try {
                            LocalDate by = LocalDate.parse(parts[3].trim(), DateTimeFormatter.ISO_LOCAL_DATE);
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
                            LocalDateTime start = LocalDateTime.parse(parts[3].trim(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                            LocalDateTime end = LocalDateTime.parse(parts[4].trim(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                            task = new Event(description, start, end);
                        } catch (DateTimeParseException e) {
                            System.out.println("Warning: Invalid datetime format in file for event: " + line);
                            return null;
                        }
                    }
                    break;
            }

            if (task != null && isDone) {
                task.markAsDone();
            }

            return task;
        } catch (Exception e) {
            System.out.println("Warning: Skipping corrupted task data: " + line);
            return null;
        }
    }

    private String formatTaskForFile(Task task) {
        String isDone = task.isDone() ? "1" : "0";
        String type = task.getType().getSymbol();
        String description = task.getDescription();

        switch (task.getType()) {
            case TODO:
                return String.format("%s | %s | %s", type, isDone, description);
            case DEADLINE:
                Deadline deadline = (Deadline) task;
                return String.format("%s | %s | %s | %s", type, isDone, description, deadline.getByForFile());
            case Event:
                Event event = (Event) task;
                return String.format("%s | %s | %s | %s | %s", type, isDone, description,
                        event.getStartForFile(), event.getEndForFile());
            default:
                return String.format("%s | %s | %s", type, isDone, description);
        }
    }
}