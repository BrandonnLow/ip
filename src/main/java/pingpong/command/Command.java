package pingpong.command;

import pingpong.PingpongException;
import pingpong.storage.Storage;
import pingpong.task.TaskList;
import pingpong.ui.Ui;

/**
 * Represents an abstract command that can be executed by the Pingpong application.
 * All concrete commands should extend this class and implement the execute method.
 */
public abstract class Command {

    /**
     * Executes the command with the given task list, UI, and storage.
     *
     * @param tasks the task list to operate on
     * @param ui the UI for user interactions
     * @param storage the storage for saving/loading tasks
     * @throws PingpongException if an error occurs during command execution
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws PingpongException;

}
