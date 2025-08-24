import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Pingpong {
    private static ArrayList<Task> tasks = new ArrayList<>();
    private static Storage storage = new Storage("./data/pingpong.txt");
    private static Ui ui = new Ui();

    public static void main(String[] args) {
        tasks = storage.load();

        ui.showWelcome();

        String input;
        while (!(input = ui.readCommand()).equals("bye")) {
            ui.showLine();

            try {
                ParsedCommand command = Parser.parse(input);
                executeCommand(command);
            } catch (PingpongException e) {
                ui.showError(e.getMessage());
            }

            ui.showLine();
        }

        ui.showGoodbye();
        ui.close();
    }

    private static void executeCommand(ParsedCommand command) throws PingpongException {
        switch (command.getType()) {
            case LIST:
                ui.showTaskList(tasks);
                break;

            case MARK:
                handleMark(command.getTaskNumber());
                break;

            case UNMARK:
                handleUnmark(command.getTaskNumber());
                break;

            case TODO:
                handleTodo(command.getDescription());
                break;

            case DEADLINE:
                handleDeadline(command.getDescription(), command.getDate());
                break;

            case EVENT:
                handleEvent(command.getDescription(), command.getStartDateTime(), command.getEndDateTime());
                break;

            case DELETE:
                handleDelete(command.getTaskNumber());
                break;

            case FIND:
                handleFind(command.getDate());
                break;
        }
    }

    private static void handleFind(LocalDate targetDate) throws PingpongException {
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

        String formattedDate = targetDate.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
        ui.showFoundTasks(matchingTasks, formattedDate);
    }

    private static void handleMark(int taskNum) throws PingpongException {
        int index = taskNum - 1;
        if (index < 0 || index >= tasks.size()) {
            throw new PingpongException("Task number " + taskNum + " does not exist.");
        }

        Task task = tasks.get(index);
        task.markAsDone();
        ui.showTaskMarked(task);
        storage.save(tasks);
    }

    private static void handleUnmark(int taskNum) throws PingpongException {
        int index = taskNum - 1;
        if (index < 0 || index >= tasks.size()) {
            throw new PingpongException("Task number " + taskNum + " does not exist.");
        }

        Task task = tasks.get(index);
        task.markAsUndone();
        ui.showTaskUnmarked(task);
        storage.save(tasks);
    }

    private static void handleTodo(String description) throws PingpongException {
        Task newTask = new Todo(description);
        tasks.add(newTask);
        ui.showTaskAdded(newTask, tasks.size());
        storage.save(tasks);
    }

    private static void handleDeadline(String description, LocalDate by) throws PingpongException {
        Task newTask = new Deadline(description, by);
        tasks.add(newTask);
        ui.showTaskAdded(newTask, tasks.size());
        storage.save(tasks);
    }

    private static void handleEvent(String description, LocalDateTime from, LocalDateTime to) throws PingpongException {
        Task newTask = new Event(description, from, to);
        tasks.add(newTask);
        ui.showTaskAdded(newTask, tasks.size());
        storage.save(tasks);
    }

    private static void handleDelete(int taskNum) throws PingpongException {
        int index = taskNum - 1;
        if (index < 0 || index >= tasks.size()) {
            throw new PingpongException("Task number " + taskNum + " does not exist.");
        }

        Task task = tasks.get(index);
        tasks.remove(index);
        ui.showTaskDeleted(task, tasks.size());
        storage.save(tasks);
    }
}