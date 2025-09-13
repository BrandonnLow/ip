package pingpong.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pingpong.PingpongException;

/**
 * Tests for basic TaskList operations.
 */
public class TaskListBasicTest {
    private TaskList taskList;

    @BeforeEach
    public void setUp() {
        taskList = new TaskList();
    }

    @Test
    public void addTodo_validDescription_success() {
        Task task = taskList.addTodo("Test todo");

        assertNotNull(task);
        assertEquals("Test todo", task.getDescription());
        assertEquals(TaskType.TODO, task.getType());
        assertEquals(1, taskList.size());
    }

    @Test
    public void addDeadline_validInputs_success() {
        LocalDate deadline = LocalDate.of(2024, 12, 31);
        Task task = taskList.addDeadline("Test deadline", deadline);

        assertNotNull(task);
        assertEquals("Test deadline", task.getDescription());
        assertEquals(TaskType.DEADLINE, task.getType());
        assertTrue(task instanceof Deadline);
        assertEquals(deadline, ((Deadline) task).getBy());
        assertEquals(1, taskList.size());
    }

    @Test
    public void addEvent_validInputs_success() {
        LocalDateTime start = LocalDateTime.of(2024, 12, 25, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 12, 25, 12, 0);
        Task task = taskList.addEvent("Test event", start, end);

        assertNotNull(task);
        assertEquals("Test event", task.getDescription());
        assertEquals(TaskType.Event, task.getType());
        assertTrue(task instanceof Event);
        assertEquals(start, ((Event) task).getStart());
        assertEquals(end, ((Event) task).getEnd());
        assertEquals(1, taskList.size());
    }

    @Test
    public void deleteTask_validIndex_success() throws PingpongException {
        Task addedTask = taskList.addTodo("Task to delete");
        assertEquals(1, taskList.size());

        Task deletedTask = taskList.deleteTask(0);

        assertEquals(addedTask, deletedTask);
        assertEquals(0, taskList.size());
    }

    @Test
    public void deleteTask_invalidIndex_throwsException() {
        assertThrows(PingpongException.class, () -> taskList.deleteTask(0));
        assertThrows(PingpongException.class, () -> taskList.deleteTask(-1));
        assertThrows(PingpongException.class, () -> taskList.deleteTask(5));
    }

    @Test
    public void markTask_validIndex_success() throws PingpongException {
        Task task = taskList.addTodo("Task to mark");
        assertFalse(task.isDone());

        Task markedTask = taskList.markTask(0);

        assertEquals(task, markedTask);
        assertTrue(markedTask.isDone());
    }

    @Test
    public void unmarkTask_validIndex_success() throws PingpongException {
        Task task = taskList.addTodo("Task to unmark");
        taskList.markTask(0);
        assertTrue(task.isDone());

        Task unmarkedTask = taskList.unmarkTask(0);

        assertEquals(task, unmarkedTask);
        assertFalse(unmarkedTask.isDone());
    }

    @Test
    public void getTask_validIndex_success() throws PingpongException {
        Task addedTask = taskList.addTodo("Test task");

        Task retrievedTask = taskList.getTask(0);

        assertEquals(addedTask, retrievedTask);
    }

    @Test
    public void getTask_invalidIndex_throwsException() {
        assertThrows(PingpongException.class, () -> taskList.getTask(0));
        assertThrows(PingpongException.class, () -> taskList.getTask(-1));
    }

    @Test
    public void size_emptyList_returnsZero() {
        assertEquals(0, taskList.size());
    }

    @Test
    public void size_afterAddingTasks_returnsCorrectSize() {
        taskList.addTodo("Task 1");
        assertEquals(1, taskList.size());

        taskList.addTodo("Task 2");
        assertEquals(2, taskList.size());

        taskList.addTodo("Task 3");
        assertEquals(3, taskList.size());
    }

    @Test
    public void getAllTasks_returnsAllTasks() {
        Task task1 = taskList.addTodo("Task 1");
        Task task2 = taskList.addTodo("Task 2");
        Task task3 = taskList.addTodo("Task 3");

        var allTasks = taskList.getAllTasks();

        assertEquals(3, allTasks.size());
        assertTrue(allTasks.contains(task1));
        assertTrue(allTasks.contains(task2));
        assertTrue(allTasks.contains(task3));
    }
}
