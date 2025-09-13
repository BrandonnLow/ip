package pingpong.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for search operations in TaskList.
 */
public class TaskListSearchTest {
    private TaskList taskList;

    @BeforeEach
    public void setUp() {
        taskList = new TaskList();
    }

    @Test
    public void findTasksByKeyword_matchingTasks_returnsMatchingTasks() {
        Task task1 = taskList.addTodo("Buy groceries");
        taskList.addTodo("Read book");
        Task task3 = taskList.addTodo("Buy milk");

        ArrayList<Task> foundTasks = taskList.findTasksByKeyword("buy");

        assertEquals(2, foundTasks.size());
        assertTrue(foundTasks.contains(task1));
        assertTrue(foundTasks.contains(task3));
    }

    @Test
    public void findTasksByKeyword_noMatches_returnsEmptyList() {
        taskList.addTodo("Buy groceries");
        taskList.addTodo("Read book");

        ArrayList<Task> foundTasks = taskList.findTasksByKeyword("xyz");

        assertEquals(0, foundTasks.size());
    }

    @Test
    public void findTasksByKeyword_caseInsensitive_findsMatches() {
        Task task1 = taskList.addTodo("BUY groceries");
        Task task2 = taskList.addTodo("buy milk");

        ArrayList<Task> foundTasks = taskList.findTasksByKeyword("Buy");

        assertEquals(2, foundTasks.size());
        assertTrue(foundTasks.contains(task1));
        assertTrue(foundTasks.contains(task2));
    }

    @Test
    public void findTasksByKeywords_multipleKeywords_returnsAllMatches() {
        Task task1 = taskList.addTodo("Buy groceries");
        Task task2 = taskList.addTodo("Read book");
        Task task3 = taskList.addTodo("Write code");

        ArrayList<Task> foundTasks = taskList.findTasksByKeywords("buy", "write");

        assertEquals(2, foundTasks.size());
        assertTrue(foundTasks.contains(task1));
        assertTrue(foundTasks.contains(task3));
    }

    @Test
    public void findTasksByKeywords_emptyKeywords_returnsEmptyList() {
        taskList.addTodo("Task 1");

        ArrayList<Task> foundTasks = taskList.findTasksByKeywords();

        assertEquals(0, foundTasks.size());
    }

    @Test
    public void findTasksOnDate_matchingDeadlines_returnsMatchingTasks() {
        LocalDate targetDate = LocalDate.of(2024, 12, 25);
        Task task1 = taskList.addDeadline("Christmas shopping", targetDate);
        taskList.addDeadline("New Year prep", LocalDate.of(2024, 12, 31));

        ArrayList<Task> foundTasks = taskList.findTasksOnDate(targetDate);

        assertEquals(1, foundTasks.size());
        assertTrue(foundTasks.contains(task1));
    }

    @Test
    public void findTasksOnDate_matchingEvents_returnsMatchingTasks() {
        LocalDate targetDate = LocalDate.of(2024, 12, 25);
        LocalDateTime eventStart = targetDate.atTime(10, 0);
        LocalDateTime eventEnd = targetDate.atTime(14, 0);

        Task event = taskList.addEvent("Christmas party", eventStart, eventEnd);
        taskList.addTodo("Buy gifts");

        ArrayList<Task> foundTasks = taskList.findTasksOnDate(targetDate);

        assertEquals(1, foundTasks.size());
        assertTrue(foundTasks.contains(event));
    }

    @Test
    public void findTasksOnDate_multiDayEvent_returnsForAllDays() {
        LocalDateTime start = LocalDateTime.of(2024, 12, 24, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 12, 26, 18, 0);
        Task event = taskList.addEvent("Christmas celebration", start, end);

        ArrayList<Task> day1Tasks = taskList.findTasksOnDate(LocalDate.of(2024, 12, 24));
        ArrayList<Task> day2Tasks = taskList.findTasksOnDate(LocalDate.of(2024, 12, 25));
        ArrayList<Task> day3Tasks = taskList.findTasksOnDate(LocalDate.of(2024, 12, 26));
        ArrayList<Task> day4Tasks = taskList.findTasksOnDate(LocalDate.of(2024, 12, 27));

        assertEquals(1, day1Tasks.size());
        assertEquals(1, day2Tasks.size());
        assertEquals(1, day3Tasks.size());
        assertEquals(0, day4Tasks.size());
        assertTrue(day1Tasks.contains(event));
        assertTrue(day2Tasks.contains(event));
        assertTrue(day3Tasks.contains(event));
    }

    @Test
    public void findTasksOnDate_todosNeverMatch_returnsEmptyList() {
        taskList.addTodo("Todo task");

        ArrayList<Task> foundTasks = taskList.findTasksOnDate(LocalDate.of(2024, 12, 25));

        assertEquals(0, foundTasks.size());
    }
}
