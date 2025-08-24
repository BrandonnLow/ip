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
        boolean isExit = false;

        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();

                if (fullCommand.equals("bye")) {
                    isExit = true;
                    break;
                }

                ui.showLine();
                Command command = Parser.parse(fullCommand);
                command.execute(tasks, ui, storage);
                ui.showLine();

            } catch (PingpongException e) {
                ui.showError(e.getMessage());
                ui.showLine();
            }
        }

        ui.showGoodbye();
        ui.close();
    }


}