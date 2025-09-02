package pingpong.command;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pingpong.MockUi;
import pingpong.PingpongException;
import pingpong.storage.Storage;
import pingpong.task.TaskList;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateCommandTest {

    private TaskList taskList;
    private MockUi mockUi;
    private Storage storage;

    @BeforeEach
    public void setUp() {
        taskList = new TaskList();
        mockUi = new MockUi();
        storage = new Storage("./data/test_update.txt");
    }

    @Test
    public void constructor_validTaskNumber_success() {
        UpdateCommand command = new UpdateCommand(1);

        // Constructor should not throw exception
        assertNotNull(command);
    }

    @Test
    public void withDescription_valid_success() {
        UpdateCommand command = new UpdateCommand(1);
        UpdateCommand result = command.withDescription("new description");

        assertSame(command, result); // Should return same instance for method chaining
    }

    @Test
    public void withDeadline_valid_success() {
        UpdateCommand command = new UpdateCommand(1);
        LocalDate deadline = LocalDate.of(2025, 9, 15);
        UpdateCommand result = command.withDeadline(deadline);

        assertSame(command, result); // Should return same instance for method chaining
    }

    @Test
    public void withStart_valid_success() {
        UpdateCommand command = new UpdateCommand(1);
        LocalDateTime start = LocalDateTime.of(2025, 9, 10, 14, 0);
        UpdateCommand result = command.withStart(start);

        assertSame(command, result); // Should return same instance for method chaining
    }

    @Test
    public void withEnd_valid_success() {
        UpdateCommand command = new UpdateCommand(1);
        LocalDateTime end = LocalDateTime.of(2025, 9, 10, 16, 0);
        UpdateCommand result = command.withEnd(end);

        assertSame(command, result); // Should return same instance for method chaining
    }

    @Test
    public void execute_todoDescription_success() throws PingpongException {
        taskList.addTodo("Original description");

        UpdateCommand command = new UpdateCommand(1).withDescription("Updated description");
        command.execute(taskList, mockUi, storage);

        assertEquals("Updated description", taskList.getTask(0).getDescription());
        String output = mockUi.getOutput();
        assertTrue(output.contains("Got it. I've updated this task:"));
        assertTrue(output.contains("From: [T][ ] Original description"));
        assertTrue(output.contains("To:   [T][ ] Updated description"));
    }

    @Test
    public void execute_deadlineDate_success() throws PingpongException {
        LocalDate originalDate = LocalDate.of(2025, 9, 10);
        LocalDate newDate = LocalDate.of(2025, 9, 15);
        taskList.addDeadline("Task", originalDate);

        UpdateCommand command = new UpdateCommand(1).withDeadline(newDate);
        command.execute(taskList, mockUi, storage);

        String output = mockUi.getOutput();
        assertTrue(output.contains("Got it. I've updated this task:"));
        assertTrue(output.contains("Sep 10 2025")); // Original date
        assertTrue(output.contains("Sep 15 2025")); // New date
    }

    @Test
    public void execute_eventTimes_success() throws PingpongException {
        LocalDateTime originalStart = LocalDateTime.of(2025, 9, 10, 14, 0);
        LocalDateTime originalEnd = LocalDateTime.of(2025, 9, 10, 16, 0);
        LocalDateTime newStart = LocalDateTime.of(2025, 9, 10, 15, 0);
        LocalDateTime newEnd = LocalDateTime.of(2025, 9, 10, 17, 0);

        taskList.addEvent("Meeting", originalStart, originalEnd);

        UpdateCommand command = new UpdateCommand(1).withStart(newStart).withEnd(newEnd);
        command.execute(taskList, mockUi, storage);

        String output = mockUi.getOutput();
        assertTrue(output.contains("Got it. I've updated this task:"));
        assertTrue(output.contains("2:00pm")); // Original time
        assertTrue(output.contains("3:00pm")); // New start time
        assertTrue(output.contains("5:00pm")); // New end time
    }

    @Test
    public void execute_multipleFields_success() throws PingpongException {
        LocalDate originalDate = LocalDate.of(2025, 9, 10);
        LocalDate newDate = LocalDate.of(2025, 9, 15);
        taskList.addDeadline("Original task", originalDate);

        UpdateCommand command = new UpdateCommand(1)
                .withDescription("Updated task")
                .withDeadline(newDate);
        command.execute(taskList, mockUi, storage);

        assertEquals("Updated task", taskList.getTask(0).getDescription());
        String output = mockUi.getOutput();
        assertTrue(output.contains("Got it. I've updated this task:"));
        assertTrue(output.contains("Original task"));
        assertTrue(output.contains("Updated task"));
    }

    @Test
    public void execute_invalidTaskNumber_throwsException() {
        taskList.addTodo("Only task");

        UpdateCommand command = new UpdateCommand(2).withDescription("New description");

        assertThrows(PingpongException.class, () -> command.execute(taskList, mockUi, storage));
    }

    @Test
    public void execute_noFields_throwsException() {
        taskList.addTodo("Task");

        UpdateCommand command = new UpdateCommand(1);

        assertThrows(PingpongException.class, () -> command.execute(taskList, mockUi, storage));
    }

    @Test
    public void execute_preservesStatus_success() throws PingpongException {
        taskList.addTodo("Task to update");
        taskList.markTask(0);

        UpdateCommand command = new UpdateCommand(1).withDescription("Updated task");
        command.execute(taskList, mockUi, storage);

        assertTrue(taskList.getTask(0).isDone());
        assertEquals("Updated task", taskList.getTask(0).getDescription());
    }

    @Test
    public void methodChaining_all_success() {
        LocalDate deadline = LocalDate.of(2025, 9, 15);
        LocalDateTime start = LocalDateTime.of(2025, 9, 10, 14, 0);
        LocalDateTime end = LocalDateTime.of(2025, 9, 10, 16, 0);

        UpdateCommand command = new UpdateCommand(1)
                .withDescription("Chained description")
                .withDeadline(deadline)
                .withStart(start)
                .withEnd(end);

        assertNotNull(command);
    }
}
