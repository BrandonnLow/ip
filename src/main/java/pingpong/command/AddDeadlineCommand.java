package pingpong.command;

import pingpong.task.TaskList;
import pingpong.task.Task;
import pingpong.ui.Ui;
import pingpong.storage.Storage;

import java.time.LocalDate;

public class AddDeadlineCommand extends Command {
    private String description;
    private LocalDate by;

    public AddDeadlineCommand(String description, LocalDate by) {
        this.description = description;
        this.by = by;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        Task deadline = tasks.addDeadline(description, by);
        ui.showTaskAdded(deadline, tasks.size());
        storage.save(tasks.getAllTasks());
    }
}
