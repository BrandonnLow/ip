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
        assertThrows(PingpongException.class, () -> Parser.parse("event Team meeting "
                + "2024-12-25 1400 /to 2024-12-25 1600"));

        // Missing /to
        assertThrows(PingpongException.class, () -> Parser.parse("event Team meeting "
                + "/from 2024-12-25 1400 2024-12-25 1600"));

        // Invalid date format
        assertThrows(PingpongException.class, () -> Parser.parse("event Team meeting "
                + "/from 25-12-2024 1400 /to 25-12-2024 1600"));

        // Start time after end time
        assertThrows(PingpongException.class, () -> Parser.parse("event Team meeting "
                + "/from 2024-12-25 1600 /to 2024-12-25 1400"));
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
    public void parse_findByDateCommand_success() throws PingpongException {
        Command command = Parser.parse("find 2024-12-25");

        assertTrue(command instanceof FindCommand);
    }

    @Test
    public void parse_findByKeywordCommand_success() throws PingpongException {
        Command command = Parser.parse("find book");

        assertTrue(command instanceof FindCommand);
    }

    @Test
    public void parse_findInvalidDateAsKeyword_success() throws PingpongException {
        Command command1 = Parser.parse("find invalid-date");
        Command command2 = Parser.parse("find 25-12-2024");

        assertTrue(command1 instanceof FindCommand);
        assertTrue(command2 instanceof FindCommand);
    }

    @Test
    public void parse_findEmpty_throwsException() {
        assertThrows(PingpongException.class, () -> Parser.parse("find"));
        assertThrows(PingpongException.class, () -> Parser.parse("find "));
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

    @Test
    public void parse_addMultipleCommand_success() throws PingpongException {
        Command command = Parser.parse("addmultiple Buy groceries; Read book; Submit assignment");

        assertTrue(command instanceof AddMultipleCommand);
    }

    @Test
    public void parse_addMultipleCommandSingleTask_success() throws PingpongException {
        Command command = Parser.parse("addmultiple Buy groceries");

        assertTrue(command instanceof AddMultipleCommand);
    }

    @Test
    public void parse_addMultipleCommandWithSpaces_success() throws PingpongException {
        Command command = Parser.parse("addmultiple  Buy groceries ; Read book ; Submit assignment  ");

        assertTrue(command instanceof AddMultipleCommand);
    }

    @Test
    public void parse_addMultipleEmpty_throwsException() {
        assertThrows(PingpongException.class, () -> Parser.parse("addmultiple"));
        assertThrows(PingpongException.class, () -> Parser.parse("addmultiple "));
    }

    @Test
    public void parse_addMultipleOnlySemicolons_throwsException() {
        assertThrows(PingpongException.class, () -> Parser.parse("addmultiple ;;;"));
        assertThrows(PingpongException.class, () -> Parser.parse("addmultiple ; ; ;"));
    }

    @Test
    public void parse_markMultipleCommand_success() throws PingpongException {
        Command command = Parser.parse("mark 1 2 3");

        assertTrue(command instanceof MarkMultipleCommand);
    }

    @Test
    public void parse_markMultipleCommandTwoTasks_success() throws PingpongException {
        Command command = Parser.parse("mark 1 5");

        assertTrue(command instanceof MarkMultipleCommand);
    }

    @Test
    public void parse_markMultipleInvalidNumbers_throwsException() {
        assertThrows(PingpongException.class, () -> Parser.parse("mark 1 abc 3"));
        assertThrows(PingpongException.class, () -> Parser.parse("mark 1 2 -1"));
        assertThrows(PingpongException.class, () -> Parser.parse("mark 1 2 0"));
    }

    @Test
    public void parse_unmarkMultipleCommand_success() throws PingpongException {
        Command command = Parser.parse("unmark 1 2 3");

        assertTrue(command instanceof UnmarkMultipleCommand);
    }

    @Test
    public void parse_unmarkMultipleCommandTwoTasks_success() throws PingpongException {
        Command command = Parser.parse("unmark 2 4");

        assertTrue(command instanceof UnmarkMultipleCommand);
    }

    @Test
    public void parse_unmarkMultipleInvalidNumbers_throwsException() {
        assertThrows(PingpongException.class, () -> Parser.parse("unmark 1 xyz"));
        assertThrows(PingpongException.class, () -> Parser.parse("unmark -5 2"));
    }

    @Test
    public void parse_deleteMultipleCommand_success() throws PingpongException {
        Command command = Parser.parse("delete 1 2 3");

        assertTrue(command instanceof DeleteMultipleCommand);
    }

    @Test
    public void parse_deleteMultipleCommandTwoTasks_success() throws PingpongException {
        Command command = Parser.parse("delete 3 1");

        assertTrue(command instanceof DeleteMultipleCommand);
    }

    @Test
    public void parse_deleteMultipleInvalidNumbers_throwsException() {
        assertThrows(PingpongException.class, () -> Parser.parse("delete 1 notanumber"));
        assertThrows(PingpongException.class, () -> Parser.parse("delete 0 1"));
    }

    @Test
    public void parse_markSingleVsMultiple_returnsCorrectCommandType() throws PingpongException {
        Command singleCommand = Parser.parse("mark 1");
        Command multipleCommand = Parser.parse("mark 1 2");

        assertTrue(singleCommand instanceof MarkCommand);
        assertTrue(multipleCommand instanceof MarkMultipleCommand);
    }

    @Test
    public void parse_unmarkSingleVsMultiple_returnsCorrectCommandType() throws PingpongException {
        Command singleCommand = Parser.parse("unmark 3");
        Command multipleCommand = Parser.parse("unmark 1 3 5");

        assertTrue(singleCommand instanceof UnmarkCommand);
        assertTrue(multipleCommand instanceof UnmarkMultipleCommand);
    }

    @Test
    public void parse_deleteSingleVsMultiple_returnsCorrectCommandType() throws PingpongException {
        Command singleCommand = Parser.parse("delete 2");
        Command multipleCommand = Parser.parse("delete 1 2 3 4");

        assertTrue(singleCommand instanceof DeleteCommand);
        assertTrue(multipleCommand instanceof DeleteMultipleCommand);
    }

    @Test
    public void parse_addMultipleWithEmptyDescriptions_throwsException() {
        assertThrows(PingpongException.class, () -> Parser.parse("addmultiple ; ; "));
        assertThrows(PingpongException.class, () -> Parser.parse("addmultiple   ;   ;   "));
    }

    @Test
    public void parse_addMultipleWithSomeEmptyDescriptions_filtersEmpty() throws PingpongException {
        // Should work - empty descriptions should be filtered out
        Command command = Parser.parse("addmultiple Buy groceries; ; Read book");

        assertTrue(command instanceof AddMultipleCommand);
    }
}
