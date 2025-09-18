package pingpong.command;

import pingpong.PingpongException;
import pingpong.storage.Storage;
import pingpong.task.TaskList;
import pingpong.ui.Ui;

/**
 * Command to display help information about all available commands.
 */
public class HelpCommand extends Command {

    private static final String HELP_MESSAGE = """
        ============================================================
         Here are the available commands:
        ============================================================
        
        1. todo DESCRIPTION
           - Adds a simple todo task
           - Example: todo Buy groceries
        
        2. deadline DESCRIPTION /by DATE
           - Adds a task with a deadline
           - Date format: yyyy-MM-dd
           - Example: deadline Submit report /by 2025-09-15
        
        3. event DESCRIPTION /from DATETIME /to DATETIME
           - Adds an event with start and end times
           - DateTime formats: yyyy-MM-dd HHmm OR yyyy-MM-dd HH:mm
           - Example: event Meeting /from 2025-09-10 1400 /to 2025-09-10 1600
        
        4. list
           - Shows all tasks in your list
        
        5. mark INDEX [INDEX2 INDEX3...]
           - Marks task(s) as completed
           - Example: mark 1 OR mark 1 3 5
        
        6. unmark INDEX [INDEX2 INDEX3...]
           - Marks task(s) as not completed
           - Example: unmark 2 OR unmark 1 2 3
        
        7. delete INDEX [INDEX2 INDEX3...]
           - Deletes task(s) from the list
           - Example: delete 3 OR delete 1 2 4
        
        8. find KEYWORD/DATE
           - Finds tasks by keyword or date
           - Example: find meeting OR find 2025-09-10
        
        9. update INDEX [/desc DESC] [/by DATE] [/from DATETIME] [/to DATETIME]
           - Updates an existing task's details
           - Example: update 1 /desc New description
           - Example: update 2 /by 2025-09-20
        
        10. addmultiple DESC1; DESC2; DESC3
            - Adds multiple todo tasks at once
            - Example: addmultiple Buy milk; Call mom; Read book
        
        11. help
            - Shows this help message
        
        12. bye
            - Exits the application
        
        ============================================================
         Tips:
        ============================================================
        - Task indices start from 1
        - Dates use format: yyyy-MM-dd (e.g., 2025-09-15)
        - Times use 24-hour format: HHmm or HH:mm (e.g., 1400 or 14:00)
        - Commands are case-sensitive
        - You can operate on multiple tasks at once for mark, unmark, and delete
        ============================================================
        """;

    /**
     * Executes the help command to display available commands and usage information.
     *
     * @param tasks the task list (not modified by this command)
     * @param ui the UI to display the help message
     * @param storage the storage (not modified by this command)
     * @throws PingpongException if an error occurs during execution
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws PingpongException {
        ui.showMessages(HELP_MESSAGE.split("\n"));
    }
}
