package pingpong.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pingpong.PingpongException;

/**
 * Integration tests for TaskList that verify multiple operations working together.
 * These tests ensure that different operations maintain consistency when combined.
 */
public class TaskListIntegrationTest {
    private TaskList taskList;

    @BeforeEach
    public void setUp() {
        taskList = new TaskList();
    }

    @Test
    public void operations_afterMultipleOperations_maintainCorrectState() throws PingpongException {
        // Add multiple tasks using batch operation
        taskList.addTodos("Task 1", "Task 2", "Task 3", "Task 4");
        assertEquals(4, taskList.size());

        // Mark some tasks using batch operation
        taskList.markTasks(0, 2);
        assertTrue(taskList.getTask(0).isDone());
        assertTrue(taskList.getTask(2).isDone());
        assertFalse(taskList.getTask(1).isDone());
        assertFalse(taskList.getTask(3).isDone());

        // Unmark one of the marked tasks
        taskList.unmarkTasks(0);
        assertFalse(taskList.getTask(0).isDone());
        assertTrue(taskList.getTask(2).isDone());

        // Search should still work correctly
        ArrayList<Task> foundTasks = taskList.findTasksByKeywords("Task");
        assertEquals(4, foundTasks.size());
    }

    @Test
    public void combinedOperations_differentTaskTypes_maintainIntegrity() throws PingpongException {
        // Add different types of tasks
        taskList.addTodo("Todo task");
        LocalDate deadline = LocalDate.of(2025, 9, 10);
        taskList.addDeadline("Deadline task", deadline);
        LocalDateTime start = LocalDateTime.of(2025, 9, 10, 14, 0);
        LocalDateTime end = LocalDateTime.of(2025, 9, 10, 16, 0);
        taskList.addEvent("Event task", start, end);

        assertEquals(3, taskList.size());

        // Update each type
        taskList.updateTask(0, "Updated todo", null, null, null);
        taskList.updateTask(1, "Updated deadline", null, null, null);
        taskList.updateTask(2, "Updated event", null, null, null);

        // Verify all updates
        assertEquals("Updated todo", taskList.getTask(0).getDescription());
        assertEquals("Updated deadline", taskList.getTask(1).getDescription());
        assertEquals("Updated event", taskList.getTask(2).getDescription());
        assertEquals(3, taskList.size());

        // Mark all tasks
        taskList.markTasks(0, 1, 2);
        assertTrue(taskList.getTask(0).isDone());
        assertTrue(taskList.getTask(1).isDone());
        assertTrue(taskList.getTask(2).isDone());
    }

    @Test
    public void updateAndSearch_combinedOperations_workCorrectly() throws PingpongException {
        // Add tasks
        taskList.addTodo("Buy programming book");
        LocalDate deadline = LocalDate.of(2024, 12, 25);
        taskList.addDeadline("Submit programming assignment", deadline);
        LocalDateTime start = LocalDateTime.of(2024, 12, 25, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 12, 25, 12, 0);
        taskList.addEvent("Programming workshop", start, end);
        taskList.addTodo("Buy groceries");

        // Search before update
        ArrayList<Task> foundTasks = taskList.findTasksByKeywords("programming", "book");
        assertEquals(3, foundTasks.size());

        // Update a task to remove keyword
        taskList.updateTask(0, "Buy Java book", null, null, null);

        // Search after update - should still find the updated task
        foundTasks = taskList.findTasksByKeywords("book");
        assertEquals(1, foundTasks.size());
        assertEquals("Buy Java book", foundTasks.get(0).getDescription());
    }

    @Test
    public void markUnmarkAndUpdate_preservesState() throws PingpongException {
        // Add tasks
        taskList.addTodo("Task 1");
        taskList.addTodo("Task 2");
        taskList.addTodo("Task 3");

        // Mark task 2
        taskList.markTask(1);
        assertTrue(taskList.getTask(1).isDone());

        // Update marked task - should preserve marked state
        taskList.updateTask(1, "Updated Task 2", null, null, null);
        assertTrue(taskList.getTask(1).isDone());
        assertEquals("Updated Task 2", taskList.getTask(1).getDescription());

        // Verify other tasks are unaffected
        assertFalse(taskList.getTask(0).isDone());
        assertFalse(taskList.getTask(2).isDone());
        assertEquals("Task 1", taskList.getTask(0).getDescription());
        assertEquals("Task 3", taskList.getTask(2).getDescription());
    }

    @Test
    public void batchOperationsAndSearch_integration() throws PingpongException {
        // Add multiple tasks
        taskList.addTodos("Read book", "Buy book", "Return book", "Submit assignment");

        // Mark some tasks
        taskList.markTasks(0, 2);

        // Search for keyword
        ArrayList<Task> bookTasks = taskList.findTasksByKeyword("book");
        assertEquals(3, bookTasks.size());

        // Verify marked status is preserved in search results
        boolean hasMarkedTask = bookTasks.stream().anyMatch(Task::isDone);
        assertTrue(hasMarkedTask);
    }

    @Test
    public void complexEventUpdate_withSearch_integration() throws PingpongException {
        // Add events on different dates
        LocalDateTime start1 = LocalDateTime.of(2024, 12, 25, 10, 0);
        LocalDateTime end1 = LocalDateTime.of(2024, 12, 25, 12, 0);
        taskList.addEvent("Christmas meeting", start1, end1);

        LocalDateTime start2 = LocalDateTime.of(2024, 12, 26, 14, 0);
        LocalDateTime end2 = LocalDateTime.of(2024, 12, 26, 16, 0);
        taskList.addEvent("Boxing day meeting", start2, end2);

        // Search by date
        ArrayList<Task> christmas = taskList.findTasksOnDate(LocalDate.of(2024, 12, 25));
        assertEquals(1, christmas.size());

        // Update event to different time
        LocalDateTime newStart = LocalDateTime.of(2024, 12, 26, 10, 0);
        LocalDateTime newEnd = LocalDateTime.of(2024, 12, 26, 12, 0);
        taskList.updateTask(0, null, null, newStart, newEnd);

        // Search should reflect the update
        christmas = taskList.findTasksOnDate(LocalDate.of(2024, 12, 25));
        assertEquals(0, christmas.size());

        ArrayList<Task> boxingDay = taskList.findTasksOnDate(LocalDate.of(2024, 12, 26));
        assertEquals(2, boxingDay.size());
    }

    @Test
    public void batchAddMarkUnmark_maintainsConsistency() throws PingpongException {
        // Add multiple tasks in batch
        ArrayList<Task> addedTasks = taskList.addTodos(
                "Task 1", "Task 2", "Task 3", "Task 4", "Task 5"
        );
        assertEquals(5, addedTasks.size());
        assertEquals(5, taskList.size());

        // Mark tasks in batch
        taskList.markTasks(0, 2, 4);
        assertTrue(taskList.getTask(0).isDone());
        assertFalse(taskList.getTask(1).isDone());
        assertTrue(taskList.getTask(2).isDone());
        assertFalse(taskList.getTask(3).isDone());
        assertTrue(taskList.getTask(4).isDone());

        // Unmark some tasks
        taskList.unmarkTasks(0, 4);
        assertFalse(taskList.getTask(0).isDone());
        assertTrue(taskList.getTask(2).isDone());
        assertFalse(taskList.getTask(4).isDone());

        // Size should remain consistent
        assertEquals(5, taskList.size());
    }

    @Test
    public void updateMultipleFields_withStatusPreservation() throws PingpongException {
        // Add deadline task
        LocalDate originalDate = LocalDate.of(2025, 9, 10);
        taskList.addDeadline("Original task", originalDate);

        // Mark it as done
        taskList.markTask(0);
        assertTrue(taskList.getTask(0).isDone());

        // Update both description and deadline
        LocalDate newDate = LocalDate.of(2025, 9, 15);
        Task updatedTask = taskList.updateTask(0, "Updated task", newDate, null, null);

        // Verify both fields updated and status preserved
        assertEquals("Updated task", updatedTask.getDescription());
        assertTrue(updatedTask instanceof Deadline);
        assertEquals(newDate, ((Deadline) updatedTask).getBy());
        assertTrue(updatedTask.isDone());
    }

    @Test
    public void searchAfterDelete_maintainsAccuracy() throws PingpongException {
        // Add tasks
        taskList.addTodo("Buy book");
        taskList.addTodo("Read book");
        taskList.addTodo("Return book");
        taskList.addTodo("Submit assignment");

        // Initial search
        ArrayList<Task> bookTasks = taskList.findTasksByKeyword("book");
        assertEquals(3, bookTasks.size());

        // Delete one book task
        taskList.deleteTask(0);
        assertEquals(3, taskList.size());

        // Search should reflect deletion
        bookTasks = taskList.findTasksByKeyword("book");
        assertEquals(2, bookTasks.size());
        assertEquals("Read book", bookTasks.get(0).getDescription());
        assertEquals("Return book", bookTasks.get(1).getDescription());
    }

    @Test
    public void complexScenario_allOperations() throws PingpongException {
        // Add various tasks
        taskList.addTodos("Task 1", "Task 2", "Task 3");
        taskList.addDeadline("Deadline project", LocalDate.of(2025, 9, 10));
        LocalDateTime start = LocalDateTime.of(2025, 9, 10, 14, 0);
        LocalDateTime end = LocalDateTime.of(2025, 9, 10, 16, 0);
        taskList.addEvent("Event meeting", start, end);

        assertEquals(5, taskList.size());

        // Mark some tasks
        taskList.markTasks(0, 3);
        assertTrue(taskList.getTask(0).isDone());
        assertTrue(taskList.getTask(3).isDone());

        // Update a task
        taskList.updateTask(1, "Updated Task 2", null, null, null);
        assertEquals("Updated Task 2", taskList.getTask(1).getDescription());

        // Delete a task
        taskList.deleteTask(2);
        assertEquals(4, taskList.size());

        // Search by keyword
        ArrayList<Task> taskResults = taskList.findTasksByKeyword("task");
        assertEquals(2, taskResults.size()); // Task 1, Updated Task 2

        // Search by date
        ArrayList<Task> dateResults = taskList.findTasksOnDate(LocalDate.of(2025, 9, 10));
        assertEquals(2, dateResults.size()); // Deadline project and Event meeting

        // Verify state consistency
        assertTrue(taskList.getTask(0).isDone()); // Task 1 still marked
        assertFalse(taskList.getTask(1).isDone()); // Updated Task 2 not marked
        assertTrue(taskList.getTask(2).isDone()); // Deadline project was marked at index 3, now at 2
        assertFalse(taskList.getTask(3).isDone()); // Event meeting not marked
    }
}
