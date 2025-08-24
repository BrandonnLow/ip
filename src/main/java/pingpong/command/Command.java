package pingpong.command;

import pingpong.task.TaskList;
import pingpong.ui.Ui;
import pingpong.storage.Storage;
import pingpong.PingpongException;

public abstract class Command {

    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws PingpongException;

    public boolean isExit() {
        return false;
    }
}
