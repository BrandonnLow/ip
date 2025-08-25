package pingpong;

import pingpong.task.TaskList;
import pingpong.storage.Storage;
import pingpong.ui.Ui;
import pingpong.command.Parser;
import pingpong.command.Command;

/**
 * Main class for the Pingpong task management application.
 * Coordinates the interaction between the UI, task list, storage, and command parsing.
 */
public class Pingpong {
    private TaskList tasks;
    private Storage storage;
    private static Ui ui;

    /**
     * Creates a new Pingpong application instance with the specified storage file path.
     * Initializes the UI, storage, and loads existing tasks from file.
     *
     * @param filePath the path to the file where tasks are stored
     */
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

    /**
     * The main entry point for the Pingpong application.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        new Pingpong("./data/pingpong.txt").run();
    }

    /**
     * Starts the main application loop.
     * Handles user input, command parsing, and execution until the user exits.
     */
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
