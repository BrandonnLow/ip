package pingpong.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import pingpong.PingpongException;

/**
 * Contains the task list and operations to add/delete/modify tasks.
 * Acts as the main container for all tasks in the Pingpong application.
 */
public class TaskList {
    private static final String TASK_NOT_EXISTS_ERROR = "Task number %d does not exist.";

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
        validateTaskIndex(index);
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
        validateTaskIndex(index);
        Task task = tasks.get(index);
        task.markAsDone();
        return task;
    }

    /**
     * Marks multiple tasks as completed using varargs.
     *
     * @param indices the 0-based indices of tasks to mark
     * @return a list of marked tasks
     * @throws PingpongException if any index is invalid
     */
    public ArrayList<Task> markTasks(int... indices) throws PingpongException {
        ArrayList<Task> markedTasks = new ArrayList<>();
        for (int index : indices) {
            markedTasks.add(markTask(index));
        }
        return markedTasks;
    }

    /**
     * Unmarks a task (marks as not completed) at the specified index.
     *
     * @param index the 0-based index of the task to unmark
     * @return the unmarked task
     * @throws PingpongException if the index is invalid
     */
    public Task unmarkTask(int index) throws PingpongException {
        validateTaskIndex(index);
        Task task = tasks.get(index);
        task.markAsUndone();
        return task;
    }

    /**
     * Unmarks multiple tasks using varargs.
     *
     * @param indices the 0-based indices of tasks to unmark
     * @return a list of unmarked tasks
     * @throws PingpongException if any index is invalid
     */
    public ArrayList<Task> unmarkTasks(int... indices) throws PingpongException {
        ArrayList<Task> unmarkedTasks = new ArrayList<>();
        for (int index : indices) {
            unmarkedTasks.add(unmarkTask(index));
        }
        return unmarkedTasks;
    }

    /**
     * Gets the task at the specified index.
     *
     * @param index the 0-based index of the task to retrieve
     * @return the task at the specified index
     * @throws PingpongException if the index is invalid
     */
    public Task getTask(int index) throws PingpongException {
        validateTaskIndex(index);
        return tasks.get(index);
    }

    /**
     * Validates that the given index is within valid range.
     *
     * @param index the index to validate
     * @throws PingpongException if index is out of bounds
     */
    private void validateTaskIndex(int index) throws PingpongException {
        if (index < 0 || index >= tasks.size()) {
            throw new PingpongException(String.format(TASK_NOT_EXISTS_ERROR, index + 1));
        }
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
     * Creates and adds multiple Todo tasks to the task list using varargs.
     *
     * @param descriptions the descriptions of the todo tasks
     * @return a list of created Todo tasks
     */
    public ArrayList<Task> addTodos(String... descriptions) {
        ArrayList<Task> createdTasks = new ArrayList<>();
        for (String description : descriptions) {
            Task task = addTodo(description);
            createdTasks.add(task);
        }
        return createdTasks;
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
            if (isTaskOnDate(task, targetDate)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }

    /**
     * Checks if a task occurs on the specified date.
     *
     * @param task the task to check
     * @param targetDate the target date
     * @return true if task occurs on the date, false otherwise
     */
    private boolean isTaskOnDate(Task task, LocalDate targetDate) {
        if (task instanceof Deadline) {
            return isDeadlineOnDate((Deadline) task, targetDate);
        } else if (task instanceof Event) {
            return isEventOnDate((Event) task, targetDate);
        }
        return false;
    }

    /**
     * Checks if a deadline task occurs on the specified date.
     *
     * @param deadline the deadline task
     * @param targetDate the target date
     * @return true if deadline is on the date
     */
    private boolean isDeadlineOnDate(Deadline deadline, LocalDate targetDate) {
        return deadline.getBy().equals(targetDate);
    }

    /**
     * Checks if an event task occurs on the specified date.
     *
     * @param event the event task
     * @param targetDate the target date
     * @return true if date falls within event period
     */
    private boolean isEventOnDate(Event event, LocalDate targetDate) {
        LocalDate startDate = event.getStart().toLocalDate();
        LocalDate endDate = event.getEnd().toLocalDate();
        return !targetDate.isBefore(startDate) && !targetDate.isAfter(endDate);
    }

    /**
     * Finds all tasks that contain the specified keyword in their description.
     * The search is case-insensitive.
     *
     * @param keyword the keyword to search for in task descriptions
     * @return a list of tasks whose descriptions contain the keyword
     */
    public ArrayList<Task> findTasksByKeyword(String keyword) {
        ArrayList<Task> matchingTasks = new ArrayList<>();
        String keywordLower = keyword.toLowerCase();

        for (Task task : tasks) {
            if (containsKeyword(task, keywordLower)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }

    /**
     * Checks if a task's description contains the specified keyword.
     *
     * @param task the task to check
     * @param keywordLower the keyword in lowercase
     * @return true if description contains keyword
     */
    private boolean containsKeyword(Task task, String keywordLower) {
        return task.getDescription().toLowerCase().contains(keywordLower);
    }

    /**
     * Finds all tasks that contain any of the specified keywords in their description using varargs.
     * The search is case-insensitive.
     *
     * @param keywords the keywords to search for in task descriptions
     * @return a list of tasks whose descriptions contain any of the keywords
     */
    public ArrayList<Task> findTasksByKeywords(String... keywords) {
        ArrayList<Task> matchingTasks = new ArrayList<>();

        for (Task task : tasks) {
            if (taskMatchesAnyKeyword(task, keywords)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }

    /**
     * Checks if a task matches any of the provided keywords.
     *
     * @param task the task to check
     * @param keywords the keywords to match against
     * @return true if task matches any keyword
     */
    private boolean taskMatchesAnyKeyword(Task task, String... keywords) {
        String descriptionLower = task.getDescription().toLowerCase();
        for (String keyword : keywords) {
            if (descriptionLower.contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
