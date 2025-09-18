package pingpong.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pingpong.MockUi;
import pingpong.PingpongException;
import pingpong.storage.Storage;
import pingpong.task.TaskList;

import static org.junit.jupiter.api.Assertions.*;

public class HelpCommandTest {

    private TaskList taskList;
    private MockUi mockUi;
    private Storage storage;

    @BeforeEach
    public void setUp() {
        taskList = new TaskList();
        mockUi = new MockUi();
        storage = new Storage("./data/test_help.txt");
    }

    @Test
    public void execute_helpCommand_displaysHelp() throws PingpongException {
        HelpCommand command = new HelpCommand();

        command.execute(taskList, mockUi, storage);

        String output = mockUi.getOutput();

        // Check for key parts of the help message
        assertTrue(output.contains("Here are the available commands:"));
        assertTrue(output.contains("todo"));
        assertTrue(output.contains("deadline"));
        assertTrue(output.contains("event"));
        assertTrue(output.contains("list"));
        assertTrue(output.contains("mark"));
        assertTrue(output.contains("unmark"));
        assertTrue(output.contains("delete"));
        assertTrue(output.contains("find"));
        assertTrue(output.contains("update"));
        assertTrue(output.contains("addmultiple"));
        assertTrue(output.contains("help"));
        assertTrue(output.contains("bye"));

        // Check for examples
        assertTrue(output.contains("Example:"));

        // Check for tips
        assertTrue(output.contains("Tips:"));
    }

    @Test
    public void parse_helpCommand_returnsHelpCommand() throws PingpongException {
        Command command = Parser.parse("help");

        assertTrue(command instanceof HelpCommand);
    }

    @Test
    public void parse_helpWithExtraText_stillWorks() throws PingpongException {
        // Parser should handle "help" as the command even with extra text
        Command command = Parser.parse("help me");

        assertTrue(command instanceof HelpCommand);
    }

    @Test
    public void execute_doesNotModifyTaskList() throws PingpongException {
        taskList.addTodo("Existing task");
        int originalSize = taskList.size();

        HelpCommand command = new HelpCommand();
        command.execute(taskList, mockUi, storage);

        assertEquals(originalSize, taskList.size());
        assertEquals("Existing task", taskList.getTask(0).getDescription());
    }
}
