package pingpong.command;

import pingpong.task.TaskList;
import pingpong.ui.Ui;
import pingpong.storage.Storage;
import pingpong.PingpongException;

public class ListCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws PingpongException {
        ui.showTaskList(tasks.getAllTasks());
    }
}
