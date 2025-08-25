package pingpong.ui;

import pingpong.task.Task;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles interactions with the user, including input/output operations.
 * Manages the command line interface for the Pingpong application.
 */
public class Ui {
    private Scanner scanner;

    /**
     * Creates a new Ui instance and initializes the scanner for user input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the welcome message to the user when the application starts.
     */
    public void showWelcome() {
        showLine();
        System.out.println(" Hello! I'm Pingpong");
        System.out.println(" What can I do for you?");
        showLine();
    }

    /**
     * Displays the goodbye message when the application exits.
     */
    public void showGoodbye() {
        showLine();
        System.out.println(" Bye. Hope to see you again soon!");
        showLine();
    }

    /**
     * Displays a horizontal line separator for better visual formatting.
     */
    public void showLine() {
        System.out.println("____________________________________________________________");
    }

    /**
     * Reads a command from the user input.
     *
     * @return the command string entered by the user
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays an error message to the user.
     *
     * @param message the error message to display
     */
    public void showError(String message) {
        System.out.println(" OOPS!!! " + message);
    }

    /**
     * Displays a confirmation message when a task has been added.
     *
     * @param task the task that was added
     * @param totalTasks the total number of tasks in the list after adding
     */
    public void showTaskAdded(Task task, int totalTasks) {
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + totalTasks + " tasks in the list.");
    }

    /**
     * Displays the complete list of tasks.
     *
     * @param tasks the list of tasks to display
     */
    public void showTaskList(ArrayList<Task> tasks) {
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Displays a confirmation message when a task has been marked as done.
     *
     * @param task the task that was marked
     */
    public void showTaskMarked(Task task) {
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("  " + task);
    }

    /**
     * Displays a confirmation message when a task has been unmarked.
     *
     * @param task the task that was unmarked
     */
    public void showTaskUnmarked(Task task) {
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
    }

    /**
     * Displays a confirmation message when a task has been deleted.
     *
     * @param task the task that was deleted
     * @param totalTasks the total number of tasks remaining after deletion
     */
    public void showTaskDeleted(Task task, int totalTasks) {
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + totalTasks + " tasks in the list.");
    }

    /**
     * Displays the tasks found for a specific date.
     *
     * @param matchingTasks the list of tasks found for the date
     * @param dateStr the formatted date string for display
     */
    public void showFoundTasks(ArrayList<Task> matchingTasks, String dateStr) {
        if (matchingTasks.isEmpty()) {
            System.out.println(" No tasks found on " + dateStr);
        } else {
            System.out.println(" Here are the tasks on " + dateStr + ":");
            for (int i = 0; i < matchingTasks.size(); i++) {
                System.out.println(" " + (i + 1) + "." + matchingTasks.get(i));
            }
        }
    }

    /**
     * Closes the scanner and releases resources.
     */
    public void close() {
        scanner.close();
    }
}