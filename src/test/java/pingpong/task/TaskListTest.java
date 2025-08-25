package pingpong.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pingpong.PingpongException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TaskListTest {

    private TaskList taskList;

    @BeforeEach
    public void setUp() {
        taskList = new TaskList();
    }

    @Test
    public void addTodo_validDescription_success() {
        Task todo = taskList.addTodo("Buy groceries");

        assertEquals(1, taskList.size());
        assertEquals("Buy groceries", todo.getDescription());
        assertEquals(TaskType.TODO, todo.getType());
        assertFalse(todo.isDone());
    }

    @Test
    public void addDeadline_validInput_success() {
        LocalDate deadline = LocalDate.of(2024, 12, 25);
        Task deadlineTask = taskList.addDeadline("Submit assignment", deadline);

        assertEquals(1, taskList.size());
        assertEquals("Submit assignment", deadlineTask.getDescription());
        assertEquals(TaskType.DEADLINE, deadlineTask.getType());
        assertTrue(deadlineTask instanceof Deadline);

        Deadline d = (Deadline) deadlineTask;
        assertEquals(deadline, d.getBy());
    }

    @Test
    public void deleteTask_validIndex_success() throws PingpongException {
        taskList.addTodo("Task to delete");
        taskList.addTodo("Another task");

        Task deletedTask = taskList.deleteTask(0);

        assertEquals(1, taskList.size());
        assertEquals("Task to delete", deletedTask.getDescription());
    }

    @Test
    public void deleteTask_invalidIndex_throwsException() {
        taskList.addTodo("Only task");

        assertThrows(PingpongException.class, () -> taskList.deleteTask(1));
        assertThrows(PingpongException.class, () -> taskList.deleteTask(-1));
    }

    @Test
    public void markTask_validIndex_success() throws PingpongException {
        taskList.addTodo("Task to mark");

        Task markedTask = taskList.markTask(0);

        assertTrue(markedTask.isDone());
        assertEquals("Task to mark", markedTask.getDescription());
    }

    @Test
    public void findTasksOnDate_withDeadlines_returnsMatchingTasks() {
        LocalDate targetDate = LocalDate.of(2024, 12, 25);
        LocalDate otherDate = LocalDate.of(2024, 12, 26);

        taskList.addDeadline("Christmas task", targetDate);
        taskList.addDeadline("CNY task", otherDate);
        taskList.addTodo("Regular todo");

        ArrayList<Task> foundTasks = taskList.findTasksOnDate(targetDate);

        assertEquals(1, foundTasks.size());
        assertEquals("Christmas task", foundTasks.get(0).getDescription());
    }

    @Test
    public void findTasksOnDate_withEvents_returnsMatchingTasks() {
        LocalDate targetDate = LocalDate.of(2024, 12, 25);
        LocalDateTime start = targetDate.atTime(10, 0);
        LocalDateTime end = targetDate.atTime(12, 0);

        LocalDateTime otherStart = LocalDate.of(2024, 12, 26).atTime(10, 0);
        LocalDateTime otherEnd = LocalDate.of(2024, 12, 26).atTime(12, 0);

        taskList.addEvent("Christmas event", start, end);
        taskList.addEvent("CNY event", otherStart, otherEnd);

        ArrayList<Task> foundTasks = taskList.findTasksOnDate(targetDate);

        assertEquals(1, foundTasks.size());
        assertEquals("Christmas event", foundTasks.get(0).getDescription());
    }

    @Test
    public void findTasksOnDate_noMatchingTasks_returnsEmpty() {
        LocalDate targetDate = LocalDate.of(2024, 12, 25);
        LocalDate otherDate = LocalDate.of(2024, 12, 26);

        taskList.addDeadline("Other date task", otherDate);
        taskList.addTodo("Regular todo");

        ArrayList<Task> foundTasks = taskList.findTasksOnDate(targetDate);

        assertTrue(foundTasks.isEmpty());
    }

    @Test
    public void findTasksByKeyword_singleMatch_returnsMatchingTask() {
        taskList.addTodo("Buy groceries");
        taskList.addTodo("Read book");
        taskList.addTodo("Submit assignment");

        ArrayList<Task> foundTasks = taskList.findTasksByKeyword("book");

        assertEquals(1, foundTasks.size());
        assertEquals("Read book", foundTasks.get(0).getDescription());
    }

    @Test
    public void findTasksByKeyword_multipleMatches_returnsAllMatchingTasks() {
        taskList.addTodo("Buy book");
        taskList.addTodo("Read book review");
        taskList.addTodo("Return book");
        taskList.addTodo("Submit assignment");

        ArrayList<Task> foundTasks = taskList.findTasksByKeyword("book");

        assertEquals(3, foundTasks.size());
        assertEquals("Buy book", foundTasks.get(0).getDescription());
        assertEquals("Read book review", foundTasks.get(1).getDescription());
        assertEquals("Return book", foundTasks.get(2).getDescription());
    }

    @Test
    public void findTasksByKeyword_caseInsensitive_returnsMatchingTasks() {
        taskList.addTodo("Buy BOOK");
        taskList.addTodo("read Book");
        taskList.addTodo("submit assignment");

        ArrayList<Task> foundTasks = taskList.findTasksByKeyword("book");

        assertEquals(2, foundTasks.size());
        assertEquals("Buy BOOK", foundTasks.get(0).getDescription());
        assertEquals("read Book", foundTasks.get(1).getDescription());
    }

    @Test
    public void findTasksByKeyword_noMatches_returnsEmpty() {
        taskList.addTodo("Buy groceries");
        taskList.addTodo("Submit assignment");

        ArrayList<Task> foundTasks = taskList.findTasksByKeyword("book");

        assertTrue(foundTasks.isEmpty());
    }

    @Test
    public void findTasksByKeyword_partialMatch_returnsMatchingTasks() {
        taskList.addTodo("Bookstore visit");
        taskList.addTodo("Facebook update");
        taskList.addTodo("Submit assignment");

        ArrayList<Task> foundTasks = taskList.findTasksByKeyword("book");

        assertEquals(2, foundTasks.size());
        assertEquals("Bookstore visit", foundTasks.get(0).getDescription());
        assertEquals("Facebook update", foundTasks.get(1).getDescription());
    }

    @Test
    public void findTasksByKeyword_worksWithAllTaskTypes() {
        taskList.addTodo("Read book");
        LocalDate deadline = LocalDate.of(2024, 12, 25);
        taskList.addDeadline("Return book", deadline);
        LocalDateTime start = LocalDateTime.of(2024, 12, 25, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 12, 25, 12, 0);
        taskList.addEvent("Book club meeting", start, end);

        ArrayList<Task> foundTasks = taskList.findTasksByKeyword("book");

        assertEquals(3, foundTasks.size());
        assertEquals("Read book", foundTasks.get(0).getDescription());
        assertEquals("Return book", foundTasks.get(1).getDescription());
        assertEquals("Book club meeting", foundTasks.get(2).getDescription());
    }
}