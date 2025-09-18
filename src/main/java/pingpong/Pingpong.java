package pingpong;

import pingpong.command.Command;
import pingpong.command.Parser;
import pingpong.storage.SampleDataLoader;
import pingpong.storage.Storage;
import pingpong.task.TaskList;
import pingpong.ui.Ui;

/**
 * Main class for the Pingpong task management application.
 * Coordinates the interaction between the UI, task list, storage, and command parsing.
 */
public class Pingpong {
    private static Ui ui;
    private TaskList tasks;
    private Storage storage;
    /**
     * Creates a new Pingpong application instance with the specified storage file path.
     * Initializes the UI, storage, and loads existing tasks from file.
     *
     * @param filePath the path to the file where tasks are stored
     */
    public Pingpong(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);

        // Check if this is the first run
        boolean isFirstRun = SampleDataLoader.isFirstRun(filePath);

        try {
            if (isFirstRun) {
                // First run - load sample data
                tasks = new TaskList();
                SampleDataLoader.loadSampleData(tasks);
                storage.save(tasks.getAllTasks());
                ui.showMessages(
                        "Welcome to Pingpong! Sample tasks have been loaded to help you get started.",
                        "Type 'help' to see all available commands.",
                        ""
                );
            } else {
                // Normal run - load existing data
                tasks = new TaskList(storage.load());
            }
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

    /**
     * Processes a single command using the provided UI without running the full interactive loop.
     * Used by the GUI to get responses for user input.
     *
     * @param fullCommand the command to process
     * @param ui the UI to use for output
     * @throws PingpongException if an error occurs during command execution
     */
    public void processCommand(String fullCommand, Ui ui) throws PingpongException {
        Command command = Parser.parse(fullCommand);
        command.execute(tasks, ui, storage);
    }

}
