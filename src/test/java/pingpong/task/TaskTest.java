package pingpong.task;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    @Test
    public void constructor_validInput_success() {
        Task task = new Todo("Buy groceries");

        assertEquals("Buy groceries", task.getDescription());
        assertEquals(TaskType.TODO, task.getType());
        assertFalse(task.isDone());
    }

    @Test
    public void markAsDone_initiallyUndone_becomesMarked() {
        Task task = new Todo("Test task");

        assertFalse(task.isDone());

        task.markAsDone();

        assertTrue(task.isDone());
    }

    @Test
    public void markAsUndone_initiallyDone_becomesUnmarked() {
        Task task = new Todo("Test task");
        task.markAsDone(); // First mark as done

        assertTrue(task.isDone());

        task.markAsUndone();

        assertFalse(task.isDone());
    }

    @Test
    public void toString_unmarkedTask_correctFormat() {
        Task task = new Todo("Buy milk");

        String expected = "[T][ ] Buy milk";
        assertEquals(expected, task.toString());
    }

    @Test
    public void toString_markedTask_correctFormat() {
        Task task = new Todo("Buy milk");
        task.markAsDone();

        String expected = "[T][X] Buy milk";
        assertEquals(expected, task.toString());
    }

    @Test
    public void deadline_toString_correctFormat() {
        Deadline deadline = new Deadline("Submit report", java.time.LocalDate.of(2024, 12, 25));

        String result = deadline.toString();

        assertTrue(result.contains("[D][ ] Submit report"));
        assertTrue(result.contains("(by: Dec 25 2024)"));
    }

    @Test
    public void event_toString_correctFormat() {
        java.time.LocalDateTime start = java.time.LocalDateTime.of(2024, 12, 25, 14, 0);
        java.time.LocalDateTime end = java.time.LocalDateTime.of(2024, 12, 25, 16, 0);
        Event event = new Event("Team meeting", start, end);

        String result = event.toString();

        assertTrue(result.contains("[E][ ] Team meeting"));
        assertTrue(result.contains("(from: Dec 25 2024, 2:00pm"));
        assertTrue(result.contains("to: Dec 25 2024, 4:00pm)"));
    }
}