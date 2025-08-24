import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Pingpong {
    private static ArrayList<Task> tasks = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static final String DATA_DIRECTORY = "./data";
    private static final String DATA_FILE_PATH = "./data/pingpong.txt";

    public static void main(String[] args) {
        loadTasks();

        System.out.println("____________________________________________________________");
        System.out.println(" Hello! I'm Pingpong");
        System.out.println(" What can I do for you?");
        System.out.println("____________________________________________________________");

        String input;
        while (!(input = scanner.nextLine()).equals("bye")) {
            System.out.println("____________________________________________________________");

            try {
                processCommand(input);
            } catch (PingpongException e) {
                System.out.println(" OOPS!!! " + e.getMessage());
            }

            System.out.println("____________________________________________________________");
        }

        System.out.println("____________________________________________________________");
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println("____________________________________________________________");

        scanner.close();
    }

    private static void loadTasks() {
        try {
            File dataDir = new File(DATA_DIRECTORY);
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }

            File dataFile = new File(DATA_FILE_PATH);
            if (!dataFile.exists()) {
                return;
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
    }

    private static Task parseTaskFromFile(String line) {
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

    private static void saveTasks() {
        try {
            File dataDir = new File(DATA_DIRECTORY);
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }

            FileWriter fileWriter = new FileWriter(DATA_FILE_PATH);
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

    private static String formatTaskForFile(Task task) {
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

    private static LocalDate parseDate(String dateStr) throws PingpongException {
        try {
            return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            throw new PingpongException("Invalid date format. Please use yyyy-MM-dd format (e.g., 2019-12-02)");
        }
    }

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

    private static void processCommand(String input) throws PingpongException {
        if (input.trim().isEmpty()) {
            throw new PingpongException("Please enter a command.");
        }

        if (input.equals("list")) {
            listTasks();
        } else if (input.equals("mark") || input.equals("mark ") || input.startsWith("mark ")) {
            handleMark(input);
        } else if (input.equals("unmark") || input.equals("unmark ") || input.startsWith("unmark ")) {
            handleUnmark(input);
        } else if (input.equals("todo") || input.equals("todo ")) {
            throw new PingpongException("The description of a todo cannot be empty.");
        } else if (input.startsWith("todo ")) {
            handleTodo(input);
        } else if (input.equals("deadline") || input.equals("deadline ")) {
            throw new PingpongException("The description of a deadline cannot be empty.");
        } else if (input.startsWith("deadline ")) {
            handleDeadline(input);
        } else if (input.equals("event") || input.equals("event ")) {
            throw new PingpongException("The description of an event cannot be empty.");
        } else if (input.startsWith("event ")) {
            handleEvent(input);
        } else if (input.equals("delete") || input.equals("delete ") || input.startsWith("delete ")) {
            handleDelete(input);
        } else if (input.equals("find") || input.equals("find ")) {
            throw new PingpongException("Please specify a date to find tasks. Format: find yyyy-MM-dd");
        } else if (input.startsWith("find ")) {
            handleFind(input);
        } else {
            throw new PingpongException("I'm sorry, but I don't know what that means :-(");
        }
    }

    private static void listTasks() {
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
    }

    private static void handleFind(String input) throws PingpongException {
        String dateStr = input.substring(5).trim();
        if (dateStr.isEmpty()) {
            throw new PingpongException("Please specify a date to find tasks. Format: find yyyy-MM-dd");
        }

        LocalDate targetDate = parseDate(dateStr);
        ArrayList<Task> matchingTasks = new ArrayList<>();

        for (Task task : tasks) {
            boolean matches = false;

            if (task instanceof Deadline) {
                Deadline deadline = (Deadline) task;
                if (deadline.getBy().equals(targetDate)) {
                    matches = true;
                }
            } else if (task instanceof Event) {
                Event event = (Event) task;
                LocalDate startDate = event.getStart().toLocalDate();
                LocalDate endDate = event.getEnd().toLocalDate();
                if (!targetDate.isBefore(startDate) && !targetDate.isAfter(endDate)) {
                    matches = true;
                }
            }

            if (matches) {
                matchingTasks.add(task);
            }
        }

        if (matchingTasks.isEmpty()) {
            System.out.println(" No tasks found on " + targetDate.format(DateTimeFormatter.ofPattern("MMM d yyyy")));
        } else {
            System.out.println(" Here are the tasks on " + targetDate.format(DateTimeFormatter.ofPattern("MMM d yyyy")) + ":");
            for (int i = 0; i < matchingTasks.size(); i++) {
                System.out.println(" " + (i + 1) + "." + matchingTasks.get(i));
            }
        }
    }

    private static void handleMark(String input) throws PingpongException {
        String numberStr = "";
        if (input.length() > 4) {
            numberStr = input.substring(4).trim();
        }

        if (numberStr.isEmpty()) {
            throw new PingpongException("Please specify which task to mark.");
        }

        try {
            int taskNum = Integer.parseInt(numberStr) - 1;
            if (taskNum < 0 || taskNum >= tasks.size()) {
                throw new PingpongException("Task number " + (taskNum + 1) + " does not exist.");
            }

            Task task = tasks.get(taskNum);
            task.markAsDone();
            System.out.println(" Nice! I've marked this task as done:");
            System.out.println("  " + task);
            saveTasks();
        } catch (NumberFormatException e) {
            throw new PingpongException("Please provide a valid task number.");
        }
    }

    private static void handleUnmark(String input) throws PingpongException {
        String numberStr = "";
        if (input.length() > 6) {
            numberStr = input.substring(6).trim();
        }

        if (numberStr.isEmpty()) {
            throw new PingpongException("Please specify which task to unmark.");
        }

        try {
            int taskNum = Integer.parseInt(numberStr) - 1;
            if (taskNum < 0 || taskNum >= tasks.size()) {
                throw new PingpongException("Task number " + (taskNum + 1) + " does not exist.");
            }

            Task task = tasks.get(taskNum);
            task.markAsUndone();
            System.out.println(" OK, I've marked this task as not done yet:");
            System.out.println("  " + task);
            saveTasks();
        } catch (NumberFormatException e) {
            throw new PingpongException("Please provide a valid task number.");
        }
    }

    private static void handleTodo(String input) throws PingpongException {
        String description = input.substring(5).trim();
        if (description.isEmpty()) {
            throw new PingpongException("The description of a todo cannot be empty.");
        }

        Task newTask = new Todo(description);
        tasks.add(newTask);
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + newTask);
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
        saveTasks();
    }

    private static void handleDeadline(String input) throws PingpongException {
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
        Task newTask = new Deadline(description, by);
        tasks.add(newTask);
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + newTask);
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
        saveTasks();
    }

    private static void handleEvent(String input) throws PingpongException {
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

        Task newTask = new Event(description, from, to);
        tasks.add(newTask);
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + newTask);
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
        saveTasks();
    }

    private static void handleDelete(String input) throws PingpongException {
        String numberStr = "";
        if (input.length() > 6) {
            numberStr = input.substring(6).trim();
        }

        if (numberStr.isEmpty()) {
            throw new PingpongException("Please specify which task to delete.");
        }

        try {
            int taskNum = Integer.parseInt(numberStr) - 1;
            if (taskNum < 0 || taskNum >= tasks.size()) {
                throw new PingpongException("Task number " + (taskNum + 1) + " does not exist.");
            }

            Task task = tasks.get(taskNum);
            tasks.remove(taskNum);
            System.out.println(" Noted. I've removed this task:");
            System.out.println("   " + task);
            System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
            saveTasks();
        } catch (NumberFormatException e) {
            throw new PingpongException("Please provide a valid task number.");
        }
    }
}