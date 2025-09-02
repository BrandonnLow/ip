package pingpong;

import java.util.ArrayList;

import pingpong.task.Task;
import pingpong.ui.Ui;

/**
 * Mock UI class to capture output from Pingpong operations for GUI display.
 */
public class MockUi extends Ui {
    private StringBuilder output = new StringBuilder();

    public MockUi() {
        super(); // This will initialize the Scanner, but we won't use it
    }

    public String getOutput() {
        String result = output.toString();
        output.setLength(0); // Clear the buffer for next use
        return result;
    }

    @Override
    public void showError(String message) {
        output.append(" OOPS!!! ").append(message);
    }

    @Override
    public void showErrors(String... messages) {
        for (String message : messages) {
            output.append(" OOPS!!! ").append(message).append("\n");
        }
    }

    @Override
    public void showMessages(String... messages) {
        for (String message : messages) {
            output.append(" ").append(message).append("\n");
        }
    }

    @Override
    public void showTaskAdded(Task task, int totalTasks) {
        output.append(" Got it. I've added this task:\n")
                .append("   ").append(task).append("\n")
                .append(" Now you have ").append(totalTasks).append(" tasks in the list.");
    }

    @Override
    public void showTasksAdded(ArrayList<Task> tasks, int totalTasks) {
        if (tasks.size() == 1) {
            showTaskAdded(tasks.get(0), totalTasks);
        } else {
            output.append(" Got it. I've added these ").append(tasks.size()).append(" tasks:\n");
            for (int i = 0; i < tasks.size(); i++) {
                output.append("   ").append(i + 1).append(". ").append(tasks.get(i)).append("\n");
            }
            output.append(" Now you have ").append(totalTasks).append(" tasks in the list.");
        }
    }

    @Override
    public void showTaskList(ArrayList<Task> tasks) {
        output.append(" Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            output.append(" ").append(i + 1).append(".").append(tasks.get(i)).append("\n");
        }
    }

    @Override
    public void showTaskMarked(Task task) {
        output.append(" Nice! I've marked this task as done:\n")
                .append("  ").append(task);
    }

    @Override
    public void showTasksMarked(Task... tasks) {
        if (tasks.length == 1) {
            showTaskMarked(tasks[0]);
        } else {
            output.append(" Nice! I've marked these ").append(tasks.length).append(" tasks as done:\n");
            for (int i = 0; i < tasks.length; i++) {
                output.append("  ").append(i + 1).append(". ").append(tasks[i]).append("\n");
            }
        }
    }

    @Override
    public void showTaskUnmarked(Task task) {
        output.append(" OK, I've marked this task as not done yet:\n")
                .append("  ").append(task);
    }

    @Override
    public void showTasksUnmarked(Task... tasks) {
        if (tasks.length == 1) {
            showTaskUnmarked(tasks[0]);
        } else {
            output.append(" OK, I've marked these ").append(tasks.length).append(" tasks as not done yet:\n");
            for (int i = 0; i < tasks.length; i++) {
                output.append("  ").append(i + 1).append(". ").append(tasks[i]).append("\n");
            }
        }
    }

    @Override
    public void showTaskDeleted(Task task, int totalTasks) {
        output.append(" Noted. I've removed this task:\n")
                .append("   ").append(task).append("\n")
                .append(" Now you have ").append(totalTasks).append(" tasks in the list.");
    }

    @Override
    public void showTasksDeleted(ArrayList<Task> tasks, int totalTasks) {
        if (tasks.size() == 1) {
            showTaskDeleted(tasks.get(0), totalTasks);
        } else {
            output.append(" Noted. I've removed these ").append(tasks.size()).append(" tasks:\n");
            for (int i = 0; i < tasks.size(); i++) {
                output.append("   ").append(i + 1).append(". ").append(tasks.get(i)).append("\n");
            }
            output.append(" Now you have ").append(totalTasks).append(" tasks in the list.");
        }
    }

    @Override
    public void showFoundTasksByKeyword(ArrayList<Task> matchingTasks, String keyword) {
        if (matchingTasks.isEmpty()) {
            output.append(" No matching tasks found.");
        } else {
            output.append(" Here are the matching tasks in your list:\n");
            for (int i = 0; i < matchingTasks.size(); i++) {
                output.append(" ").append(i + 1).append(".").append(matchingTasks.get(i)).append("\n");
            }
        }
    }

    @Override
    public void showFoundTasksByKeywords(ArrayList<Task> matchingTasks, String... keywords) {
        if (matchingTasks.isEmpty()) {
            output.append(" No matching tasks found for any of the keywords.");
        } else {
            output.append(" Here are the matching tasks for keywords: ");
            for (int i = 0; i < keywords.length; i++) {
                output.append(keywords[i]);
                if (i < keywords.length - 1) {
                    output.append(", ");
                }
            }
            output.append("\n");
            for (int i = 0; i < matchingTasks.size(); i++) {
                output.append(" ").append(i + 1).append(".").append(matchingTasks.get(i)).append("\n");
            }
        }
    }

    @Override
    public void showFoundTasksByDate(ArrayList<Task> matchingTasks, String dateStr) {
        if (matchingTasks.isEmpty()) {
            output.append(" No tasks found on ").append(dateStr);
        } else {
            output.append(" Here are the tasks on ").append(dateStr).append(":\n");
            for (int i = 0; i < matchingTasks.size(); i++) {
                output.append(" ").append(i + 1).append(".").append(matchingTasks.get(i)).append("\n");
            }
        }
    }

    @Override
    public void showTaskUpdated(Task originalTask, Task updatedTask) {
        output.append(" Got it. I've updated this task:\n")
                .append("   From: ").append(originalTask).append("\n")
                .append("   To:   ").append(updatedTask);
    }

    @Override
    public void showTasksUpdated(Task[] originalTasks, Task[] updatedTasks) {
        if (originalTasks.length == 1) {
            showTaskUpdated(originalTasks[0], updatedTasks[0]);
        } else {
            output.append(" Got it. I've updated these ").append(originalTasks.length).append(" tasks:\n");
            for (int i = 0; i < originalTasks.length; i++) {
                output.append("   ").append(i + 1).append(". From: ").append(originalTasks[i]).append("\n");
                output.append("      To:   ").append(updatedTasks[i]).append("\n");
            }
        }
    }

    // Override unused methods from parent class
    @Override
    public String readCommand() {
        return "";
    }

    @Override
    public void showWelcome() {
    }

    @Override
    public void showGoodbye() {
    }

    @Override
    public void showLine() {
        // Not used in GUI
    }

    @Override
    public void close() {
        // Not needed for GUI
    }
}
