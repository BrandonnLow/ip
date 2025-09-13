package pingpong.task;

import java.time.LocalDate;
import java.time.LocalDateTime;

import pingpong.PingpongException;

/**
 * Handles task update operations.
 */
public class TaskUpdater {

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
    public static Task createUpdatedTask(Task originalTask, String newDescription, LocalDate newDeadline,
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

    private static Task createUpdatedTodo(Task originalTask, String newDescription, LocalDate newDeadline,
                                          LocalDateTime newStart, LocalDateTime newEnd) throws PingpongException {
        assert originalTask != null : "Original task should not be null";
        assert originalTask.getType() == TaskType.TODO : "Task should be a Todo";

        if (newDeadline != null) {
            throw new PingpongException("Cannot set deadline for Todo tasks. "
                    + "Use 'deadline' command to create a Deadline task.");
        }
        if (newStart != null || newEnd != null) {
            throw new PingpongException("Cannot set times for Todo tasks. "
                    + "Use 'event' command to create an Event task.");
        }

        String description = newDescription != null ? newDescription : originalTask.getDescription();
        Task updatedTask = new Todo(description);

        if (originalTask.isDone()) {
            updatedTask.markAsDone();
        }

        return updatedTask;
    }

    private static Task createUpdatedDeadline(Task originalTask, String newDescription, LocalDate newDeadline,
                                              LocalDateTime newStart, LocalDateTime newEnd) throws PingpongException {
        assert originalTask != null : "Original task should not be null";
        assert originalTask.getType() == TaskType.DEADLINE : "Task should be a Deadline";
        assert originalTask instanceof Deadline : "Task should be instance of Deadline";

        if (newStart != null || newEnd != null) {
            throw new PingpongException("Cannot set start/end times for Deadline tasks."
                    + "Use 'event' command to create an Event task.");
        }

        Deadline originalDeadline = (Deadline) originalTask;
        String description = newDescription != null ? newDescription : originalTask.getDescription();
        LocalDate by = newDeadline != null ? newDeadline : originalDeadline.getBy();

        Task updatedTask = new Deadline(description, by);

        if (originalTask.isDone()) {
            updatedTask.markAsDone();
        }

        return updatedTask;
    }

    private static Task createUpdatedEvent(Task originalTask, String newDescription, LocalDate newDeadline,
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

        if (start.isAfter(end)) {
            throw new PingpongException("Event start time cannot be after end time.");
        }

        Task updatedTask = new Event(description, start, end);

        if (originalTask.isDone()) {
            updatedTask.markAsDone();
        }

        return updatedTask;
    }
}
