package pingpong.storage;

import java.time.LocalDate;
import java.time.LocalDateTime;

import pingpong.task.TaskList;

/**
 * Loads sample data into the task list for first-time users.
 * Provides a set of example tasks to help users understand the application.
 */
public class SampleDataLoader {

    /**
     * Checks if this is the first run (no existing data file).
     *
     * @param filePath the path to the data file
     * @return true if this appears to be the first run
     */
    public static boolean isFirstRun(String filePath) {
        java.io.File file = new java.io.File(filePath);
        return !file.exists();
    }

    /**
     * Loads sample tasks into the task list.
     * Creates a variety of task types to demonstrate functionality.
     *
     * @param tasks the task list to populate with sample data
     */
    public static void loadSampleData(TaskList tasks) {
        try {
            // Add sample todos
            tasks.addTodo("Read user guide (type 'help' for commands)");
            tasks.addTodo("Explore Pingpong features");

            // Add sample deadline
            LocalDate nextWeek = LocalDate.now().plusDays(7);
            tasks.addDeadline("Complete project proposal", nextWeek);

            // Add sample event
            LocalDateTime meetingStart = LocalDateTime.now().plusDays(2).withHour(14).withMinute(0);
            LocalDateTime meetingEnd = meetingStart.plusHours(2);
            tasks.addEvent("Team meeting", meetingStart, meetingEnd);

            // Mark first task as done to show the feature
            tasks.markTask(0);

            // Add a few more todos
            tasks.addTodo("Buy groceries");
            tasks.addTodo("Call dentist for appointment");

            // Add another deadline
            LocalDate monthEnd = LocalDate.now().withDayOfMonth(
                    LocalDate.now().lengthOfMonth()
            );
            tasks.addDeadline("Pay monthly bills", monthEnd);

        } catch (Exception e) {
            // If sample data loading fails, just continue with empty list
            System.err.println("Could not load sample data: " + e.getMessage());
        }
    }
}
