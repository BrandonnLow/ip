package pingpong.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pingpong.PingpongException;

/**
 * Tests for batch operations in TaskList.
 */
public class TaskListBatchTest {
    private TaskList taskList;

    @BeforeEach
    public void setUp() {
        taskList = new TaskList();
    }

    @Test
    public void addTodos_multipleDescriptions_success() {
        ArrayList<Task> tasks = taskList.addTodos("Task 1", "Task 2", "Task 3");

        assertEquals(3, tasks.size());
        assertEquals(3, taskList.size());
        assertEquals("Task 1", tasks.get(0).getDescription());
        assertEquals("Task 2", tasks.get(1).getDescription());
        assertEquals("Task 3", tasks.get(2).getDescription());
    }

    @Test
    public void addTodos_emptyArray_returnsEmptyList() {
        ArrayList<Task> tasks = taskList.addTodos();

        assertEquals(0, tasks.size());
        assertEquals(0, taskList.size());
    }

    @Test
    public void markTasks_multipleIndices_success() throws PingpongException {
        taskList.addTodo("Task 1");
        taskList.addTodo("Task 2");
        taskList.addTodo("Task 3");

        ArrayList<Task> markedTasks = taskList.markTasks(0, 2);

        assertEquals(2, markedTasks.size());
        assertTrue(taskList.getTask(0).isDone());
        assertFalse(taskList.getTask(1).isDone());
        assertTrue(taskList.getTask(2).isDone());
    }

    @Test
    public void markTasks_emptyIndices_returnsEmptyList() throws PingpongException {
        taskList.addTodo("Task 1");

        ArrayList<Task> markedTasks = taskList.markTasks();

        assertEquals(0, markedTasks.size());
        assertFalse(taskList.getTask(0).isDone());
    }

    @Test
    public void unmarkTasks_multipleIndices_success() throws PingpongException {
        taskList.addTodo("Task 1");
        taskList.addTodo("Task 2");
        taskList.addTodo("Task 3");
        taskList.markTasks(0, 1, 2);

        ArrayList<Task> unmarkedTasks = taskList.unmarkTasks(0, 2);

        assertEquals(2, unmarkedTasks.size());
        assertFalse(taskList.getTask(0).isDone());
        assertTrue(taskList.getTask(1).isDone());
        assertFalse(taskList.getTask(2).isDone());
    }

    @Test
    public void unmarkTasks_emptyIndices_returnsEmptyList() throws PingpongException {
        taskList.addTodo("Task 1");
        taskList.markTask(0);

        ArrayList<Task> unmarkedTasks = taskList.unmarkTasks();

        assertEquals(0, unmarkedTasks.size());
        assertTrue(taskList.getTask(0).isDone());
    }
}
