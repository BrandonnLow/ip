package pingpong.ui;

import java.util.ArrayList;
import java.util.Scanner;

import pingpong.task.Task;

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
     * Displays multiple error messages to the user using varargs.
     *
     * @param messages the error messages to display
     */
    public void showErrors(String... messages) {
        for (String message : messages) {
            showError(message);
        }
    }

    /**
     * Displays multiple custom messages using varargs.
     *
     * @param messages the messages to display
     */
    public void showMessages(String... messages) {
        for (String message : messages) {
            System.out.println(" " + message);
        }
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
     * Displays confirmation messages when multiple tasks have been added using varargs.
     *
     * @param tasks the tasks that were added
     * @param totalTasks the total number of tasks in the list after adding
     */
    public void showTasksAdded(ArrayList<Task> tasks, int totalTasks) {
        if (tasks.size() == 1) {
            showTaskAdded(tasks.get(0), totalTasks);
        } else {
            System.out.println(" Got it. I've added these " + tasks.size() + " tasks:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println("   " + (i + 1) + ". " + tasks.get(i));
            }
            System.out.println(" Now you have " + totalTasks + " tasks in the list.");
        }
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
     * Displays multiple task lists with headers using varargs.
     *
     * @param listsWithHeaders pairs of header strings and task lists
     */
    public void showTaskLists(Object... listsWithHeaders) {
        for (int i = 0; i < listsWithHeaders.length; i += 2) {
            if (i + 1 < listsWithHeaders.length) {
                String header = (String) listsWithHeaders[i];
                @SuppressWarnings("unchecked")
                ArrayList<Task> tasks = (ArrayList<Task>) listsWithHeaders[i + 1];

                System.out.println(" " + header);
                for (int j = 0; j < tasks.size(); j++) {
                    System.out.println(" " + (j + 1) + "." + tasks.get(j));
                }
                System.out.println();
            }
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
     * Displays confirmation messages when multiple tasks have been marked using varargs.
     *
     * @param tasks the tasks that were marked
     */
    public void showTasksMarked(Task... tasks) {
        if (tasks.length == 1) {
            showTaskMarked(tasks[0]);
        } else {
            System.out.println(" Nice! I've marked these " + tasks.length + " tasks as done:");
            for (int i = 0; i < tasks.length; i++) {
                System.out.println("  " + (i + 1) + ". " + tasks[i]);
            }
        }
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
     * Displays confirmation messages when multiple tasks have been unmarked using varargs.
     *
     * @param tasks the tasks that were unmarked
     */
    public void showTasksUnmarked(Task... tasks) {
        if (tasks.length == 1) {
            showTaskUnmarked(tasks[0]);
        } else {
            System.out.println(" OK, I've marked these " + tasks.length + " tasks as not done yet:");
            for (int i = 0; i < tasks.length; i++) {
                System.out.println("  " + (i + 1) + ". " + tasks[i]);
            }
        }
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
     * Displays confirmation messages when multiple tasks have been deleted using varargs.
     *
     * @param tasks the tasks that were deleted
     * @param totalTasks the total number of tasks remaining after deletion
     */
    public void showTasksDeleted(ArrayList<Task> tasks, int totalTasks) {
        if (tasks.size() == 1) {
            showTaskDeleted(tasks.get(0), totalTasks);
        } else {
            System.out.println(" Noted. I've removed these " + tasks.size() + " tasks:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println("   " + (i + 1) + ". " + tasks.get(i));
            }
            System.out.println(" Now you have " + totalTasks + " tasks in the list.");
        }
    }

    /**
     * Displays the tasks found for a specific date.
     *
     * @param matchingTasks the list of tasks found for the date
     * @param keyword the search term
     */
    public void showFoundTasksByKeyword(ArrayList<Task> matchingTasks, String keyword) {
        if (matchingTasks.isEmpty()) {
            System.out.println(" No matching tasks found.");
        } else {
            System.out.println(" Here are the matching tasks in your list:");
            for (int i = 0; i < matchingTasks.size(); i++) {
                System.out.println(" " + (i + 1) + "." + matchingTasks.get(i));
            }
        }
    }

    /**
     * Displays the tasks found for multiple keywords using varargs.
     *
     * @param matchingTasks the list of tasks found for the keywords
     * @param keywords the search terms
     */
    public void showFoundTasksByKeywords(ArrayList<Task> matchingTasks, String... keywords) {
        if (matchingTasks.isEmpty()) {
            System.out.println(" No matching tasks found for any of the keywords.");
        } else {
            System.out.print(" Here are the matching tasks for keywords: ");
            for (int i = 0; i < keywords.length; i++) {
                System.out.print(keywords[i]);
                if (i < keywords.length - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println();
            for (int i = 0; i < matchingTasks.size(); i++) {
                System.out.println(" " + (i + 1) + "." + matchingTasks.get(i));
            }
        }
    }

    /**
     * Displays the tasks found for a specific date.
     * Shows either a "no tasks found" message if the list is empty,
     * or displays all matching tasks in a numbered list.
     *
     * @param matchingTasks the list of tasks found for the date
     * @param dateStr the formatted date string for display
     */
    public void showFoundTasksByDate(ArrayList<Task> matchingTasks, String dateStr) {
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
