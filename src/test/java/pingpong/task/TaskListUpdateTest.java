package pingpong.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pingpong.PingpongException;

/**
 * Tests for update operations in TaskList.
 */
public class TaskListUpdateTest {
    private TaskList taskList;

    @BeforeEach
    public void setUp() {
        taskList = new TaskList();
    }

    @Test
    public void updateTask_todoDescription_success() throws PingpongException {
        taskList.addTodo("Original todo");

        Task updatedTask = taskList.updateTask(0, "Updated todo", null, null, null);

        assertEquals("Updated todo", updatedTask.getDescription());
        assertEquals(TaskType.TODO, updatedTask.getType());
        assertEquals(1, taskList.size());
    }

    @Test
    public void updateTask_todoWithDeadline_throwsException() {
        taskList.addTodo("Todo task");

        assertThrows(PingpongException.class, () ->
                taskList.updateTask(0, null, LocalDate.of(2024, 12, 31), null, null)
        );
    }

    @Test
    public void updateTask_todoWithTimes_throwsException() {
        taskList.addTodo("Todo task");
        LocalDateTime time = LocalDateTime.of(2024, 12, 25, 10, 0);

        assertThrows(PingpongException.class, () ->
                taskList.updateTask(0, null, null, time, null)
        );

        assertThrows(PingpongException.class, () ->
                taskList.updateTask(0, null, null, null, time)
        );
    }

    @Test
    public void updateTask_deadlineDescription_success() throws PingpongException {
        taskList.addDeadline("Original deadline", LocalDate.of(2024, 12, 31));

        Task updatedTask = taskList.updateTask(0, "Updated deadline", null, null, null);

        assertEquals("Updated deadline", updatedTask.getDescription());
        assertEquals(TaskType.DEADLINE, updatedTask.getType());
        assertTrue(updatedTask instanceof Deadline);
        assertEquals(LocalDate.of(2024, 12, 31), ((Deadline) updatedTask).getBy());
    }

    @Test
    public void updateTask_deadlineDate_success() throws PingpongException {
        taskList.addDeadline("Deadline task", LocalDate.of(2024, 12, 31));

        Task updatedTask = taskList.updateTask(0, null, LocalDate.of(2025, 1, 15), null, null);

        assertEquals("Deadline task", updatedTask.getDescription());
        assertTrue(updatedTask instanceof Deadline);
        assertEquals(LocalDate.of(2025, 1, 15), ((Deadline) updatedTask).getBy());
    }

    @Test
    public void updateTask_deadlineWithTimes_throwsException() {
        taskList.addDeadline("Deadline task", LocalDate.of(2024, 12, 31));
        LocalDateTime time = LocalDateTime.of(2024, 12, 25, 10, 0);

        assertThrows(PingpongException.class, () ->
                taskList.updateTask(0, null, null, time, null)
        );
    }

    @Test
    public void updateTask_eventDescription_success() throws PingpongException {
        LocalDateTime start = LocalDateTime.of(2024, 12, 25, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 12, 25, 12, 0);
        taskList.addEvent("Original event", start, end);

        Task updatedTask = taskList.updateTask(0, "Updated event", null, null, null);

        assertEquals("Updated event", updatedTask.getDescription());
        assertEquals(TaskType.Event, updatedTask.getType());
        assertTrue(updatedTask instanceof Event);
        assertEquals(start, ((Event) updatedTask).getStart());
        assertEquals(end, ((Event) updatedTask).getEnd());
    }

    @Test
    public void updateTask_eventTimes_success() throws PingpongException {
        LocalDateTime oldStart = LocalDateTime.of(2024, 12, 25, 10, 0);
        LocalDateTime oldEnd = LocalDateTime.of(2024, 12, 25, 12, 0);
        LocalDateTime newStart = LocalDateTime.of(2024, 12, 26, 14, 0);
        LocalDateTime newEnd = LocalDateTime.of(2024, 12, 26, 16, 0);

        taskList.addEvent("Event", oldStart, oldEnd);

        Task updatedTask = taskList.updateTask(0, null, null, newStart, newEnd);

        assertEquals("Event", updatedTask.getDescription());
        assertTrue(updatedTask instanceof Event);
        assertEquals(newStart, ((Event) updatedTask).getStart());
        assertEquals(newEnd, ((Event) updatedTask).getEnd());
    }

    @Test
    public void updateTask_eventWithDeadline_throwsException() {
        LocalDateTime start = LocalDateTime.of(2024, 12, 25, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 12, 25, 12, 0);
        taskList.addEvent("Event", start, end);

        assertThrows(PingpongException.class, () ->
                taskList.updateTask(0, null, LocalDate.of(2024, 12, 31), null, null)
        );
    }

    @Test
    public void updateTask_eventInvalidTimeRange_throwsException() {
        LocalDateTime start = LocalDateTime.of(2024, 12, 25, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 12, 25, 12, 0);
        taskList.addEvent("Event", start, end);

        LocalDateTime newStart = LocalDateTime.of(2024, 12, 26, 16, 0);
        LocalDateTime newEnd = LocalDateTime.of(2024, 12, 26, 14, 0);

        assertThrows(PingpongException.class, () ->
                taskList.updateTask(0, null, null, newStart, newEnd)
        );
    }

    @Test
    public void updateTask_preservesDoneStatus_whenMarked() throws PingpongException {
        taskList.addTodo("Todo task");
        taskList.markTask(0);

        Task updatedTask = taskList.updateTask(0, "Updated todo", null, null, null);

        assertTrue(updatedTask.isDone());
    }

    @Test
    public void updateTask_preservesDoneStatus_whenUnmarked() throws PingpongException {
        taskList.addTodo("Todo task");

        Task updatedTask = taskList.updateTask(0, "Updated todo", null, null, null);

        assertFalse(updatedTask.isDone());
    }

    @Test
    public void updateTask_invalidIndex_throwsException() {
        assertThrows(PingpongException.class, () ->
                taskList.updateTask(0, "New description", null, null, null)
        );

        assertThrows(PingpongException.class, () ->
                taskList.updateTask(-1, "New description", null, null, null)
        );
    }
}
