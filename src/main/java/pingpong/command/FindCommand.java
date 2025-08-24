package pingpong.command;

import pingpong.task.TaskList;
import pingpong.task.Task;
import pingpong.ui.Ui;
import pingpong.storage.Storage;
import pingpong.PingpongException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FindCommand extends Command {

    private LocalDate date;

    public FindCommand(LocalDate date) {
        this.date = date;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws PingpongException {
        ArrayList<Task> foundTasks = tasks.findTasksOnDate(date);
        String formattedDate = date.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
        ui.showFoundTasks(foundTasks, formattedDate);
    }

}
