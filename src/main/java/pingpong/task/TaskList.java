package pingpong.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

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
        assert tasks != null : "Task list should be initialized";
        assert tasks.isEmpty() : "New task list should be empty";
    }

    /**
     * Creates a new TaskList with the provided list of tasks.
     *
     * @param tasks the initial list of tasks
     */
    public TaskList(ArrayList<Task> tasks) {
        assert tasks != null : "Input task list should not be null";
        this.tasks = tasks;
        assert this.tasks != null : "Task list should be initialized";
    }

    /**
     * Adds a task to the task list.
     *
     * @param task the task to add
     */
    public void addTask(Task task) {
        assert task != null : "Task to be added should not be null";
        assert tasks != null : "Task list should be initialized";

        int originalSize = tasks.size();
        tasks.add(task);

        assert tasks.size() == originalSize + 1 : "Task list size should increase by 1 after adding";
        assert tasks.contains(task) : "Task should be in the list after adding";
    }

    /**
     * Deletes a task from the task list at the specified index.
     *
     * @param index the 0-based index of the task to delete
     * @return the deleted task
     * @throws PingpongException if the index is invalid
     */
    public Task deleteTask(int index) throws PingpongException {
        assert tasks != null : "Task list should be initialized";

        validateTaskIndex(index);

        int originalSize = tasks.size();
        Task deletedTask = tasks.remove(index);

        assert deletedTask != null : "Deleted task should not be null";
        assert tasks.size() == originalSize - 1 : "Task list size should decrease by 1 after deletion";
        return deletedTask;
    }

    /**
     * Marks a task as completed at the specified index.
     *
     * @param index the 0-based index of the task to mark
     * @return the marked task
     * @throws PingpongException if the index is invalid
     */
    public Task markTask(int index) throws PingpongException {
        assert tasks != null : "Task list should be initialized";

        validateTaskIndex(index);

        Task task = tasks.get(index);
        assert task != null : "Retrieved task should not be null";

        task.markAsDone();

        assert task.isDone() : "Task should be marked as done after marking";
        return task;
    }

    /**
     * Marks multiple tasks as completed using varargs and streams.
     *
     * @param indices the 0-based indices of tasks to mark
     * @return a list of marked tasks
     * @throws PingpongException if any index is invalid
     */
    public ArrayList<Task> markTasks(int... indices) throws PingpongException {
        assert tasks != null : "Task list should be initialized";
        assert indices != null : "Indices array should not be null";

        if (indices.length == 0) {
            return new ArrayList<>();
        }

        ArrayList<Task> markedTasks = new ArrayList<>();
        for (int index : indices) {
            Task markedTask = markTask(index);
            markedTasks.add(markedTask);
        }

        assert markedTasks.size() == indices.length : "Number of marked tasks should match number of indices";
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
        assert tasks != null : "Task list should be initialized";

        validateTaskIndex(index);

        Task task = tasks.get(index);
        assert task != null : "Retrieved task should not be null";

        task.markAsUndone();

        assert !task.isDone() : "Task should be unmarked after unmarking";
        return task;
    }


    /**
     * Unmarks multiple tasks using varargs and streams.
     *
     * @param indices the 0-based indices of tasks to unmark
     * @return a list of unmarked tasks
     * @throws PingpongException if any index is invalid
     */
    public ArrayList<Task> unmarkTasks(int... indices) throws PingpongException {
        assert tasks != null : "Task list should be initialized";
        assert indices != null : "Indices array should not be null";

        if (indices.length == 0) {
            return new ArrayList<>();
        }

        ArrayList<Task> unmarkedTasks = new ArrayList<>();
        for (int index : indices) {
            Task unmarkedTask = unmarkTask(index);
            unmarkedTasks.add(unmarkedTask);
        }

        assert unmarkedTasks.size() == indices.length : "Number of unmarked tasks should match number of indices";
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
        assert tasks != null : "Task list should be initialized";
        assert index >= 0 : "Index should not be negative";

        validateTaskIndex(index);

        Task task = tasks.get(index);
        assert task != null : "Retrieved task should not be null";
        return task;
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
        assert tasks != null : "Task list should be initialized";
        return tasks;
    }

    /**
     * Gets the number of tasks in the task list.
     *
     * @return the size of the task list
     */
    public int size() {
        assert tasks != null : "Task list should be initialized";
        int size = tasks.size();
        assert size >= 0 : "Task list size should not be negative";
        return size;
    }

    /**
     * Creates and adds a new Todo task to the task list.
     *
     * @param description the description of the todo task
     * @return the created Todo task
     */
    public Task addTodo(String description) {
        assert description != null : "Todo description should not be null";
        assert !description.trim().isEmpty() : "Todo description should not be empty";
        assert tasks != null : "Task list should be initialized";

        Task task = new Todo(description);
        addTask(task);

        assert task != null : "Created todo should not be null";
        assert tasks.contains(task) : "Todo should be in task list after adding";
        return task;
    }

    /**
     * Creates and adds multiple Todo tasks to the task list using streams.
     *
     * @param descriptions the descriptions of the todo tasks
     * @return a list of created Todo tasks
     */
    public ArrayList<Task> addTodos(String... descriptions) {
        assert descriptions != null : "Descriptions array should not be null";
        assert tasks != null : "Task list should be initialized";

        if (descriptions.length == 0) {
            return new ArrayList<>();
        }

        ArrayList<Task> createdTasks = new ArrayList<>();
        for (String description : descriptions) {
            Task createdTask = addTodo(description);
            createdTasks.add(createdTask);
        }

        assert createdTasks.size() == descriptions.length : "Number of created tasks should match descriptions";
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
        assert description != null : "Deadline description should not be null";
        assert !description.trim().isEmpty() : "Deadline description should not be empty";
        assert by != null : "Deadline date should not be null";
        assert tasks != null : "Task list should be initialized";

        Task task = new Deadline(description, by);
        addTask(task);

        assert task != null : "Created deadline should not be null";
        assert tasks.contains(task) : "Deadline should be in task list after adding";
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
        assert description != null : "Event description should not be null";
        assert !description.trim().isEmpty() : "Event description should not be empty";
        assert start != null : "Event start time should not be null";
        assert end != null : "Event end time should not be null";
        assert !start.isAfter(end) : "Event start time should not be after end time";
        assert tasks != null : "Task list should be initialized";

        Task task = new Event(description, start, end);
        addTask(task);

        assert task != null : "Created event should not be null";
        assert tasks.contains(task) : "Event should be in task list after adding";
        return task;
    }

    /**
     * Finds all tasks that occur on the specified date using streams.
     * For Deadline tasks, matches if the deadline is on the target date.
     * For Event tasks, matches if the target date falls within the event period.
     * Todo tasks are never matched as they have no associated dates.
     *
     * @param targetDate the date to search for
     * @return a list of tasks occurring on the specified date
     */
    public ArrayList<Task> findTasksOnDate(LocalDate targetDate) {
        assert targetDate != null : "Target date should not be null";
        assert tasks != null : "Task list should be initialized";

        ArrayList<Task> matchingTasks = tasks.stream()
                .filter(task -> {
                    assert task != null : "Task in list should not be null";
                    return isTaskOnDate(task, targetDate);
                })
                .collect(Collectors.toCollection(ArrayList::new));

        assert matchingTasks != null : "Matching tasks list should not be null";
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
        assert deadline != null : "Deadline should not be null";
        assert deadline.getBy() != null : "Deadline date should not be null";
        assert targetDate != null : "Target date should not be null";

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
        assert event != null : "Event should not be null";
        assert event.getStart() != null : "Event start time should not be null";
        assert event.getEnd() != null : "Event end time should not be null";
        assert targetDate != null : "Target date should not be null";

        LocalDate startDate = event.getStart().toLocalDate();
        LocalDate endDate = event.getEnd().toLocalDate();
        return !targetDate.isBefore(startDate) && !targetDate.isAfter(endDate);
    }

    /**
     * Finds all tasks that contain the specified keyword in their description using streams.
     * The search is case-insensitive.
     *
     * @param keyword the keyword to search for in task descriptions
     * @return a list of tasks whose descriptions contain the keyword
     */
    public ArrayList<Task> findTasksByKeyword(String keyword) {
        assert keyword != null : "Keyword should not be null";
        assert !keyword.trim().isEmpty() : "Keyword should not be empty";
        assert tasks != null : "Task list should be initialized";

        String keywordLower = keyword.toLowerCase();

        ArrayList<Task> matchingTasks = tasks.stream()
                .filter(task -> {
                    assert task != null : "Task in list should not be null";
                    assert task.getDescription() != null : "Task description should not be null";
                    return containsKeyword(task, keywordLower);
                })
                .collect(Collectors.toCollection(ArrayList::new));

        assert matchingTasks != null : "Matching tasks list should not be null";
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
        assert task != null : "Task should not be null";
        assert task.getDescription() != null : "Task description should not be null";
        assert keywordLower != null : "Keyword should not be null";

        return task.getDescription().toLowerCase().contains(keywordLower);
    }

    /**
     * Finds all tasks that contain any of the specified keywords in their description using streams.
     * The search is case-insensitive.
     *
     * @param keywords the keywords to search for in task descriptions
     * @return a list of tasks whose descriptions contain any of the keywords
     */
    public ArrayList<Task> findTasksByKeywords(String... keywords) {
        assert keywords != null : "Keywords array should not be null";
        assert tasks != null : "Task list should be initialized";

        if (keywords.length == 0) {
            return new ArrayList<>();
        }

        ArrayList<Task> matchingTasks = tasks.stream()
                .filter(task -> {
                    assert task != null : "Task in list should not be null";
                    assert task.getDescription() != null : "Task description should not be null";
                    return taskMatchesAnyKeyword(task, keywords);
                })
                .collect(Collectors.toCollection(ArrayList::new));

        assert matchingTasks != null : "Matching tasks list should not be null";
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
        assert task != null : "Task should not be null";
        assert task.getDescription() != null : "Task description should not be null";
        assert keywords != null : "Keywords array should not be null";

        String descriptionLower = task.getDescription().toLowerCase();
        return Arrays.stream(keywords)
                .anyMatch(keyword -> descriptionLower.contains(keyword.toLowerCase()));
    }


    /**
     * Updates a task at the specified index with new field values.
     * Only non-null parameters will be updated.
     *
     * @param index the 0-based index of the task to update
     * @param newDescription the new description (null to keep current)
     * @param newDeadline the new deadline date (null to keep current, only for Deadline tasks)
     * @param newStart the new start time (null to keep current, only for Event tasks)
     * @param newEnd the new end time (null to keep current, only for Event tasks)
     * @return the updated task
     * @throws PingpongException if the index is invalid or update is not supported for the task type
     */
    public Task updateTask(int index, String newDescription, LocalDate newDeadline,
                           LocalDateTime newStart, LocalDateTime newEnd) throws PingpongException {
        assert tasks != null : "Task list should be initialized";

        validateTaskIndex(index);

        assert index >= 0 : "Index should not be negative";

        Task originalTask = tasks.get(index);
        assert originalTask != null : "Retrieved task should not be null";

        // Create new task based on the original task type with updated fields
        Task updatedTask = createUpdatedTask(originalTask, newDescription, newDeadline, newStart, newEnd);
        assert updatedTask != null : "Updated task should not be null";

        // Replace the task in the list
        tasks.set(index, updatedTask);

        assert tasks.get(index) == updatedTask : "Task should be replaced in the list";
        return updatedTask;
    }

    /**
     * Creates a new task with updated fields based on the original task.
     *
     * @param originalTask the original task to base the update on
     * @param newDescription the new description (null to keep current)
     * @param newDeadline the new deadline date (null to keep current)
     * @param newStart the new start time (null to keep current)
     * @param newEnd the new end time (null to keep current)
     * @return the updated task
     * @throws PingpongException if update is not supported for the task type
     */
    private Task createUpdatedTask(Task originalTask, String newDescription, LocalDate newDeadline,
                                   LocalDateTime newStart, LocalDateTime newEnd) throws PingpongException {
        assert originalTask != null : "Original task should not be null";

        switch (originalTask.getType()) {
        case TODO:
            return createUpdatedTodo(originalTask, newDescription, newDeadline, newStart, newEnd);
        case DEADLINE:
            return createUpdatedDeadline(originalTask, newDescription, newDeadline, newStart, newEnd);
        case Event:
            return createUpdatedEvent(originalTask, newDescription, newDeadline, newStart, newEnd);
        default:
            throw new PingpongException("Unknown task type cannot be updated.");
        }
    }

    /**
     * Creates an updated Todo task.
     *
     * @param originalTask the original Todo task
     * @param newDescription the new description (null to keep current)
     * @param newDeadline ignored for Todo tasks
     * @param newStart ignored for Todo tasks
     * @param newEnd ignored for Todo tasks
     * @return the updated Todo task
     * @throws PingpongException if deadline or time fields are specified for Todo
     */
    private Task createUpdatedTodo(Task originalTask, String newDescription, LocalDate newDeadline,
                                   LocalDateTime newStart, LocalDateTime newEnd) throws PingpongException {
        assert originalTask != null : "Original task should not be null";
        assert originalTask.getType() == TaskType.TODO : "Task should be a Todo";

        // Validate that time-related fields are not specified for Todo tasks
        if (newDeadline != null) {
            throw new PingpongException("Cannot set deadline for Todo tasks. "
                    + "Use 'deadline' command to create a Deadline task.");
        }
        if (newStart != null || newEnd != null) {
            throw new PingpongException("Cannot set times for Todo tasks. "
                    + "Use 'event' command to create an Event task.");
        }

        String description = newDescription != null ? newDescription : originalTask.getDescription();
        assert description != null : "Description should not be null";

        Task updatedTask = new Todo(description);

        if (originalTask.isDone()) {
            updatedTask.markAsDone();
        }

        assert updatedTask != null : "Updated todo should not be null";
        return updatedTask;
    }

    /**
     * Creates an updated Deadline task.
     *
     * @param originalTask the original Deadline task
     * @param newDescription the new description (null to keep current)
     * @param newDeadline the new deadline date (null to keep current)
     * @param newStart ignored for Deadline tasks
     * @param newEnd ignored for Deadline tasks
     * @return the updated Deadline task
     * @throws PingpongException if event time fields are specified for Deadline
     */
    private Task createUpdatedDeadline(Task originalTask, String newDescription, LocalDate newDeadline,
                                       LocalDateTime newStart, LocalDateTime newEnd) throws PingpongException {
        assert originalTask != null : "Original task should not be null";
        assert originalTask.getType() == TaskType.DEADLINE : "Task should be a Deadline";
        assert originalTask instanceof Deadline : "Task should be instance of Deadline";

        // Validate that event time fields are not specified for Deadline tasks
        if (newStart != null || newEnd != null) {
            throw new PingpongException("Cannot set start/end times for Deadline tasks."
                    + "Use 'event' command to create an Event task.");
        }

        Deadline originalDeadline = (Deadline) originalTask;
        String description = newDescription != null ? newDescription : originalTask.getDescription();
        LocalDate by = newDeadline != null ? newDeadline : originalDeadline.getBy();

        assert description != null : "Description should not be null";
        assert by != null : "Deadline date should not be null";

        Task updatedTask = new Deadline(description, by);

        if (originalTask.isDone()) {
            updatedTask.markAsDone();
        }

        assert updatedTask != null : "Updated deadline should not be null";
        return updatedTask;
    }

    /**
     * Creates an updated Event task.
     *
     * @param originalTask the original Event task
     * @param newDescription the new description (null to keep current)
     * @param newDeadline ignored for Event tasks
     * @param newStart the new start time (null to keep current)
     * @param newEnd the new end time (null to keep current)
     * @return the updated Event task
     * @throws PingpongException if deadline field is specified for Event
     */
    private Task createUpdatedEvent(Task originalTask, String newDescription, LocalDate newDeadline,
                                    LocalDateTime newStart, LocalDateTime newEnd) throws PingpongException {
        assert originalTask != null : "Original task should not be null";
        assert originalTask.getType() == TaskType.Event : "Task should be an Event";
        assert originalTask instanceof Event : "Task should be instance of Event";

        if (newDeadline != null) {
            throw new PingpongException("Cannot set deadline for Event tasks. "
                    + "Use 'deadline' command to create a Deadline task.");
        }

        Event originalEvent = (Event) originalTask;
        String description = newDescription != null ? newDescription : originalTask.getDescription();
        LocalDateTime start = newStart != null ? newStart : originalEvent.getStart();
        LocalDateTime end = newEnd != null ? newEnd : originalEvent.getEnd();

        assert description != null : "Description should not be null";
        assert start != null : "Start time should not be null";
        assert end != null : "End time should not be null";

        // Validate that start time is not after end time
        if (start.isAfter(end)) {
            throw new PingpongException("Event start time cannot be after end time.");
        }

        Task updatedTask = new Event(description, start, end);

        if (originalTask.isDone()) {
            updatedTask.markAsDone();
        }

        assert updatedTask != null : "Updated event should not be null";
        return updatedTask;
    }
}
