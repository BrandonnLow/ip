package pingpong.task;

import pingpong.PingpongException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Contains the task list and operations to add/delete/modify tasks.
 * Acts as the main container for all tasks in the Pingpong application.
 */
public class TaskList {
    private ArrayList<Task> tasks;

    /**
     * Creates a new empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a new TaskList with the provided list of tasks.
     *
     * @param tasks the initial list of tasks
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a task to the task list.
     *
     * @param task the task to add
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Deletes a task from the task list at the specified index.
     *
     * @param index the 0-based index of the task to delete
     * @return the deleted task
     * @throws PingpongException if the index is invalid
     */
    public Task deleteTask(int index) throws PingpongException {
        if (index < 0 || index >= tasks.size()) {
            throw new PingpongException("Task number " + (index + 1) + " does not exist.");
        }
        return tasks.remove(index);
    }

    /**
     * Marks a task as completed at the specified index.
     *
     * @param index the 0-based index of the task to mark
     * @return the marked task
     * @throws PingpongException if the index is invalid
     */
    public Task markTask(int index) throws PingpongException {
        if (index < 0 || index >= tasks.size()) {
            throw new PingpongException("Task number " + (index + 1) + " does not exist.");
        }
        Task task = tasks.get(index);
        task.markAsDone();
        return task;
    }

    /**
     * Unmarks a task (marks as not completed) at the specified index.
     *
     * @param index the 0-based index of the task to unmark
     * @return the unmarked task
     * @throws PingpongException if the index is invalid
     */
    public Task unmarkTask(int index) throws PingpongException {
        if (index < 0 || index >= tasks.size()) {
            throw new PingpongException("Task number " + (index + 1) + " does not exist.");
        }
        Task task = tasks.get(index);
        task.markAsUndone();
        return task;
    }

    /**
     * Gets the task at the specified index.
     *
     * @param index the 0-based index of the task to retrieve
     * @return the task at the specified index
     * @throws PingpongException if the index is invalid
     */
    public Task getTask(int index) throws PingpongException {
        if (index < 0 || index >= tasks.size()) {
            throw new PingpongException("Task number " + (index + 1) + " does not exist.");
        }
        return tasks.get(index);
    }

    /**
     * Gets all tasks in the task list.
     *
     * @return the complete list of tasks
     */
    public ArrayList<Task> getAllTasks() {
        return tasks;
    }

    /**
     * Gets the number of tasks in the task list.
     *
     * @return the size of the task list
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Creates and adds a new Todo task to the task list.
     *
     * @param description the description of the todo task
     * @return the created Todo task
     */
    public Task addTodo(String description) {
        Task task = new Todo(description);
        addTask(task);
        return task;
    }

    /**
     * Creates and adds a new Deadline task to the task list.
     *
     * @param description the description of the deadline task
     * @param by the deadline date
     * @return the created Deadline task
     */
    public Task addDeadline(String description, LocalDate by) {
        Task task = new Deadline(description, by);
        addTask(task);
        return task;
    }

    /**
     * Creates and adds a new Event task to the task list.
     *
     * @param description the description of the event
     * @param start the start date and time of the event
     * @param end the end date and time of the event
     * @return the created Event task
     */
    public Task addEvent(String description, LocalDateTime start, LocalDateTime end) {
        Task task = new Event(description, start, end);
        addTask(task);
        return task;
    }

    /**
     * Finds all tasks that occur on the specified date.
     * For Deadline tasks, matches if the deadline is on the target date.
     * For Event tasks, matches if the target date falls within the event period.
     * Todo tasks are never matched as they have no associated dates.
     *
     * @param targetDate the date to search for
     * @return a list of tasks occurring on the specified date
     */
    public ArrayList<Task> findTasksOnDate(LocalDate targetDate) {
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
        return matchingTasks;
    }

    public ArrayList<Task> findTasksByKeyword(String keyword) {
        ArrayList<Task> matchingTasks = new ArrayList<>();
        String keywordLower = keyword.toLowerCase();

        for (Task task : tasks) {
            if (task.getDescription().toLowerCase().contains(keywordLower)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }
}