import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Main class for the Pingpong task management app
 */
public class Pingpong {
    private TaskList tasks;
    private Storage storage;
    private static Ui ui;

    public Pingpong(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (Exception e) {
            ui.showError("Error loading tasks from file. Starting with empty task list.");
            tasks = new TaskList();
        }
    }

    public static void main(String[] args) {
        new Pingpong("./data/pingpong.txt").run();
    }

    public void run() {

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

    private void executeCommand(ParsedCommand command) throws PingpongException {
        switch (command.getType()) {
            case LIST:
                ui.showTaskList(tasks.getAllTasks());
                break;

            case MARK:
                Task markedTask = tasks.markTask(command.getTaskNumber() - 1);
                ui.showTaskMarked(markedTask);
                saveTasksToStorage();
                break;

            case UNMARK:
                Task unmarkedTest = tasks.unmarkTask(command.getTaskNumber() - 1);
                ui.showTaskUnmarked(unmarkedTest);
                saveTasksToStorage();
                break;

            case TODO:
                Task todo = tasks.addTodo(command.getDescription());
                ui.showTaskAdded(todo, tasks.size());
                saveTasksToStorage();
                break;

            case DEADLINE:
                Task deadline = tasks.addDeadline(command.getDescription(), command.getDate());
                ui.showTaskAdded(deadline, tasks.size());
                saveTasksToStorage();
                break;

            case EVENT:
                Task event = tasks.addEvent(command.getDescription(), command.getStartDateTime(), command.getEndDateTime());
                ui.showTaskAdded(event, tasks.size());
                saveTasksToStorage();
                break;

            case DELETE:
                Task deletedTask = tasks.deleteTask(command.getTaskNumber() - 1);
                ui.showTaskDeleted(deletedTask, tasks.size());
                saveTasksToStorage();
                break;

            case FIND:
                ArrayList<Task> foundTasks = tasks.findTasksOnDate(command.getDate());
                String formattedDate = command.getDate().format(DateTimeFormatter.ofPattern("MMM d yyyy"));
                ui.showFoundTasks(foundTasks, formattedDate);
                break;
        }
    }

    private void saveTasksToStorage() {
        try {
            storage.save(tasks.getAllTasks());
        } catch (Exception e) {
            ui.showError("Error saving tasks to file: " + e.getMessage());
        }
    }
}