package pingpong.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pingpong.PingpongException;

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

    @Test
    public void addTodos_multipleTasks_success() {
        ArrayList<Task> addedTasks = taskList.addTodos("Buy groceries", "Read book", "Submit assignment");

        assertEquals(3, taskList.size());
        assertEquals(3, addedTasks.size());
        assertEquals("Buy groceries", addedTasks.get(0).getDescription());
        assertEquals("Read book", addedTasks.get(1).getDescription());
        assertEquals("Submit assignment", addedTasks.get(2).getDescription());

        for (Task task : addedTasks) {
            assertEquals(TaskType.TODO, task.getType());
            assertFalse(task.isDone());
        }
    }

    @Test
    public void addTodos_singleTask_success() {
        ArrayList<Task> addedTasks = taskList.addTodos("Single task");

        assertEquals(1, taskList.size());
        assertEquals(1, addedTasks.size());
        assertEquals("Single task", addedTasks.get(0).getDescription());
    }

    @Test
    public void addTodos_emptyVarargs_success() {
        ArrayList<Task> addedTasks = taskList.addTodos();

        assertEquals(0, taskList.size());
        assertEquals(0, addedTasks.size());
    }

    @Test
    public void markTasks_multipleTasks_success() throws PingpongException {
        taskList.addTodo("Task 1");
        taskList.addTodo("Task 2");
        taskList.addTodo("Task 3");
        taskList.addTodo("Task 4");

        ArrayList<Task> markedTasks = taskList.markTasks(0, 2, 3);

        assertEquals(3, markedTasks.size());
        assertTrue(taskList.getTask(0).isDone());
        assertFalse(taskList.getTask(1).isDone());
        assertTrue(taskList.getTask(2).isDone());
        assertTrue(taskList.getTask(3).isDone());
    }

    @Test
    public void markTasks_singleTask_success() throws PingpongException {
        taskList.addTodo("Task to mark");

        ArrayList<Task> markedTasks = taskList.markTasks(0);

        assertEquals(1, markedTasks.size());
        assertTrue(markedTasks.get(0).isDone());
        assertTrue(taskList.getTask(0).isDone());
    }

    @Test
    public void markTasks_invalidIndex_throwsException() {
        taskList.addTodo("Only task");

        assertThrows(PingpongException.class, () -> taskList.markTasks(0, 1));
        assertThrows(PingpongException.class, () -> taskList.markTasks(-1));
    }

    @Test
    public void unmarkTasks_multipleTasks_success() throws PingpongException {
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
    public void unmarkTasks_singleTask_success() throws PingpongException {
        taskList.addTodo("Task to unmark");
        taskList.markTask(0);

        ArrayList<Task> unmarkedTasks = taskList.unmarkTasks(0);

        assertEquals(1, unmarkedTasks.size());
        assertFalse(unmarkedTasks.get(0).isDone());
        assertFalse(taskList.getTask(0).isDone());
    }

    @Test
    public void unmarkTasks_invalidIndex_throwsException() {
        taskList.addTodo("Only task");

        assertThrows(PingpongException.class, () -> taskList.unmarkTasks(0, 1));
        assertThrows(PingpongException.class, () -> taskList.unmarkTasks(-1));
    }

    @Test
    public void findTasksByKeywords_multipleKeywords_returnsMatchingTasks() {
        taskList.addTodo("Buy book for reading");
        taskList.addTodo("Submit assignment");
        taskList.addTodo("Read newspaper");
        taskList.addTodo("Write essay");

        ArrayList<Task> foundTasks = taskList.findTasksByKeywords("book", "assignment");

        assertEquals(2, foundTasks.size());
        assertEquals("Buy book for reading", foundTasks.get(0).getDescription());
        assertEquals("Submit assignment", foundTasks.get(1).getDescription());
    }

    @Test
    public void findTasksByKeywords_singleKeyword_returnsMatchingTasks() {
        taskList.addTodo("Buy book");
        taskList.addTodo("Read book");
        taskList.addTodo("Submit assignment");

        ArrayList<Task> foundTasks = taskList.findTasksByKeywords("book");

        assertEquals(2, foundTasks.size());
        assertEquals("Buy book", foundTasks.get(0).getDescription());
        assertEquals("Read book", foundTasks.get(1).getDescription());
    }

    @Test
    public void findTasksByKeywords_overlappingMatches_noDuplicates() {
        taskList.addTodo("Buy book for assignment");
        taskList.addTodo("Read another book");
        taskList.addTodo("Submit assignment");

        ArrayList<Task> foundTasks = taskList.findTasksByKeywords("book", "assignment");

        assertEquals(3, foundTasks.size());
        assertEquals("Buy book for assignment", foundTasks.get(0).getDescription());
        assertEquals("Read another book", foundTasks.get(1).getDescription());
        assertEquals("Submit assignment", foundTasks.get(2).getDescription());
    }

    @Test
    public void findTasksByKeywords_noMatches_returnsEmpty() {
        taskList.addTodo("Buy groceries");
        taskList.addTodo("Submit assignment");

        ArrayList<Task> foundTasks = taskList.findTasksByKeywords("book", "reading");

        assertTrue(foundTasks.isEmpty());
    }

    @Test
    public void findTasksByKeywords_caseInsensitive_returnsMatchingTasks() {
        taskList.addTodo("Buy BOOK");
        taskList.addTodo("read Book");
        taskList.addTodo("SUBMIT assignment");

        ArrayList<Task> foundTasks = taskList.findTasksByKeywords("book", "ASSIGNMENT");

        assertEquals(3, foundTasks.size());
    }

    @Test
    public void findTasksByKeywords_emptyKeywords_returnsEmpty() {
        taskList.addTodo("Buy groceries");
        taskList.addTodo("Submit assignment");

        ArrayList<Task> foundTasks = taskList.findTasksByKeywords();

        assertTrue(foundTasks.isEmpty());
    }

    @Test
    public void markTasks_emptyIndices_returnsEmpty() throws PingpongException {
        taskList.addTodo("Task 1");
        taskList.addTodo("Task 2");

        ArrayList<Task> markedTasks = taskList.markTasks();

        assertEquals(0, markedTasks.size());
        assertFalse(taskList.getTask(0).isDone());
        assertFalse(taskList.getTask(1).isDone());
    }

    @Test
    public void unmarkTasks_emptyIndices_returnsEmpty() throws PingpongException {
        taskList.addTodo("Task 1");
        taskList.markTask(0);

        ArrayList<Task> unmarkedTasks = taskList.unmarkTasks();

        assertEquals(0, unmarkedTasks.size());
        assertTrue(taskList.getTask(0).isDone());
    }

    @Test
    public void markTasks_duplicateIndices_marksTaskOnlyOnce() throws PingpongException {
        taskList.addTodo("Task 1");
        taskList.addTodo("Task 2");

        ArrayList<Task> markedTasks = taskList.markTasks(0, 0, 1);

        assertEquals(3, markedTasks.size());
        assertTrue(taskList.getTask(0).isDone());
        assertTrue(taskList.getTask(1).isDone());
    }

    @Test
    public void addTodos_largeNumberOfTasks_success() {
        String[] descriptions = {
                "Task 1", "Task 2", "Task 3", "Task 4", "Task 5",
                "Task 6", "Task 7", "Task 8", "Task 9", "Task 10"
        };

        ArrayList<Task> addedTasks = taskList.addTodos(descriptions);

        assertEquals(10, taskList.size());
        assertEquals(10, addedTasks.size());

        for (int i = 0; i < descriptions.length; i++) {
            assertEquals(descriptions[i], addedTasks.get(i).getDescription());
            assertEquals(TaskType.TODO, addedTasks.get(i).getType());
        }
    }

    @Test
    public void markTasks_partiallyMarkedTasks_worksCorrectly() throws PingpongException {
        taskList.addTodo("Task 1");
        taskList.addTodo("Task 2");
        taskList.addTodo("Task 3");

        taskList.markTask(0);

        ArrayList<Task> markedTasks = taskList.markTasks(0, 1);

        assertEquals(2, markedTasks.size());
        assertTrue(taskList.getTask(0).isDone());
        assertTrue(taskList.getTask(1).isDone());
        assertFalse(taskList.getTask(2).isDone());
    }

    @Test
    public void findTasksByKeywords_withDifferentTaskTypes_returnsAllMatching() {
        taskList.addTodo("Read programming book");
        LocalDate deadline = LocalDate.of(2024, 12, 25);
        taskList.addDeadline("Submit programming assignment", deadline);
        LocalDateTime start = LocalDateTime.of(2024, 12, 25, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 12, 25, 12, 0);
        taskList.addEvent("Programming workshop", start, end);
        taskList.addTodo("Buy groceries");

        ArrayList<Task> foundTasks = taskList.findTasksByKeywords("programming", "book");

        assertEquals(3, foundTasks.size());
        assertEquals("Read programming book", foundTasks.get(0).getDescription());
        assertEquals("Submit programming assignment", foundTasks.get(1).getDescription());
        assertEquals("Programming workshop", foundTasks.get(2).getDescription());
    }

    @Test
    public void operations_afterMultipleOperations_maintainCorrectState() throws PingpongException {
        taskList.addTodos("Task 1", "Task 2", "Task 3", "Task 4");
        assertEquals(4, taskList.size());

        taskList.markTasks(0, 2);
        assertTrue(taskList.getTask(0).isDone());
        assertTrue(taskList.getTask(2).isDone());
        assertFalse(taskList.getTask(1).isDone());
        assertFalse(taskList.getTask(3).isDone());

        taskList.unmarkTasks(0);
        assertFalse(taskList.getTask(0).isDone());
        assertTrue(taskList.getTask(2).isDone());

        ArrayList<Task> foundTasks = taskList.findTasksByKeywords("Task");
        assertEquals(4, foundTasks.size());
    }
}
