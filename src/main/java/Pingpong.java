import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

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
                return null; // Invalid format
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
                        String by = parts[3].trim();
                        task = new Deadline(description, by);
                    }
                    break;
                case "E":
                    if (parts.length >= 4) {
                        String[] timeRange = parts[3].trim().split(" to ");
                        if (timeRange.length == 2) {
                            task = new Event(description, timeRange[0].trim(), timeRange[1].trim());
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
            // Ensure data directory exists
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
                return String.format("%s | %s | %s | %s", type, isDone, description, deadline.getBy());
            case Event:
                Event event = (Event) task;
                return String.format("%s | %s | %s | %s to %s", type, isDone, description,
                        event.getStart(), event.getEnd());
            default:
                return String.format("%s | %s | %s", type, isDone, description);
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
            throw new PingpongException("Please use format: deadline <description> /by <time>");
        }

        String description = parts[0].trim();
        String by = parts[1].trim();

        if (description.isEmpty()) {
            throw new PingpongException("The description of a deadline cannot be empty.");
        }
        if (by.isEmpty()) {
            throw new PingpongException("The deadline time cannot be empty.");
        }

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
            throw new PingpongException("Please use format: event <description> /from <start> /to <end>");
        }

        String description = fromParts[0].trim();
        String[] toParts = fromParts[1].split(" /to ");
        if (toParts.length != 2) {
            throw new PingpongException("Please use format: event <description> /from <start> /to <end>");
        }

        String from = toParts[0].trim();
        String to = toParts[1].trim();

        if (description.isEmpty()) {
            throw new PingpongException("The description of an event cannot be empty.");
        }
        if (from.isEmpty()) {
            throw new PingpongException("The event start time cannot be empty.");
        }
        if (to.isEmpty()) {
            throw new PingpongException("The event end time cannot be empty.");
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