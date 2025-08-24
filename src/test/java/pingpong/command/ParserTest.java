package pingpong.command;

import org.junit.jupiter.api.Test;
import pingpong.PingpongException;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    @Test
    public void parse_listCommand_success() throws PingpongException {
        Command command = Parser.parse("list");

        assertTrue(command instanceof ListCommand);
    }

    @Test
    public void parse_todoCommand_success() throws PingpongException {
        Command command = Parser.parse("todo Buy groceries");

        assertTrue(command instanceof AddTodoCommand);
    }

    @Test
    public void parse_emptyTodoDescription_throwsException() {
        assertThrows(PingpongException.class, () -> Parser.parse("todo"));
        assertThrows(PingpongException.class, () -> Parser.parse("todo "));
    }

    @Test
    public void parse_deadlineCommand_success() throws PingpongException {
        Command command = Parser.parse("deadline Submit assignment /by 2024-12-25");

        assertTrue(command instanceof AddDeadlineCommand);
    }

    @Test
    public void parse_deadlineInvalidFormat_throwsException() {
        // Missing /by
        assertThrows(PingpongException.class, () -> Parser.parse("deadline Submit assignment 2024-12-25"));

        // Invalid date format
        assertThrows(PingpongException.class, () -> Parser.parse("deadline Submit assignment /by 25-12-2024"));

        // Empty description
        assertThrows(PingpongException.class, () -> Parser.parse("deadline /by 2024-12-25"));

        // Empty date
        assertThrows(PingpongException.class, () -> Parser.parse("deadline Submit assignment /by"));
    }

    @Test
    public void parse_eventCommand_success() throws PingpongException {
        Command command = Parser.parse("event Team meeting /from 2024-12-25 1400 /to 2024-12-25 1600");

        assertTrue(command instanceof AddEventCommand);
    }

    @Test
    public void parse_eventInvalidFormat_throwsException() {
        // Missing /from
        assertThrows(PingpongException.class, () -> Parser.parse("event Team meeting 2024-12-25 1400 /to 2024-12-25 1600"));

        // Missing /to
        assertThrows(PingpongException.class, () -> Parser.parse("event Team meeting /from 2024-12-25 1400 2024-12-25 1600"));

        // Invalid date format
        assertThrows(PingpongException.class, () -> Parser.parse("event Team meeting /from 25-12-2024 1400 /to 25-12-2024 1600"));

        // Start time after end time
        assertThrows(PingpongException.class, () -> Parser.parse("event Team meeting /from 2024-12-25 1600 /to 2024-12-25 1400"));
    }

    @Test
    public void parse_markCommand_success() throws PingpongException {
        Command command = Parser.parse("mark 1");

        assertTrue(command instanceof MarkCommand);
    }

    @Test
    public void parse_markInvalidNumber_throwsException() {
        assertThrows(PingpongException.class, () -> Parser.parse("mark"));
        assertThrows(PingpongException.class, () -> Parser.parse("mark abc"));
        assertThrows(PingpongException.class, () -> Parser.parse("mark "));
    }

    @Test
    public void parse_unmarkCommand_success() throws PingpongException {
        Command command = Parser.parse("unmark 1");

        assertTrue(command instanceof UnmarkCommand);
    }

    @Test
    public void parse_deleteCommand_success() throws PingpongException {
        Command command = Parser.parse("delete 1");

        assertTrue(command instanceof DeleteCommand);
    }

    @Test
    public void parse_deleteInvalidNumber_throwsException() {
        assertThrows(PingpongException.class, () -> Parser.parse("delete"));
        assertThrows(PingpongException.class, () -> Parser.parse("delete xyz"));
    }

    @Test
    public void parse_findCommand_success() throws PingpongException {
        Command command = Parser.parse("find 2024-12-25");

        assertTrue(command instanceof FindCommand);
    }

    @Test
    public void parse_findInvalidDate_throwsException() {
        assertThrows(PingpongException.class, () -> Parser.parse("find"));
        assertThrows(PingpongException.class, () -> Parser.parse("find invalid-date"));
        assertThrows(PingpongException.class, () -> Parser.parse("find 25-12-2024"));
    }

    @Test
    public void parse_emptyInput_throwsException() {
        assertThrows(PingpongException.class, () -> Parser.parse(""));
        assertThrows(PingpongException.class, () -> Parser.parse("   "));
    }

    @Test
    public void parse_unknownCommand_throwsException() {
        assertThrows(PingpongException.class, () -> Parser.parse("unknown command"));
        assertThrows(PingpongException.class, () -> Parser.parse("invalid"));
    }

    @Test
    public void parse_eventDifferentTimeFormats_success() throws PingpongException {
        // Test HHmm format
        Command command1 = Parser.parse("event Meeting /from 2024-12-25 1400 /to 2024-12-25 1600");
        assertTrue(command1 instanceof AddEventCommand);

        // Test HH:mm format
        Command command2 = Parser.parse("event Meeting /from 2024-12-25 14:00 /to 2024-12-25 16:00");
        assertTrue(command2 instanceof AddEventCommand);

        // Test date only format (should default to start of day)
        Command command3 = Parser.parse("event Meeting /from 2024-12-25 /to 2024-12-26");
        assertTrue(command3 instanceof AddEventCommand);
    }
}