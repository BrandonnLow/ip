public class UnmarkCommand extends Command {

    private int taskNumber;

    public UnmarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws PingpongException {
        Task unmarkedTask = tasks.unmarkTask(taskNumber - 1);
        ui.showTaskUnmarked(unmarkedTask);
        storage.save(tasks.getAllTasks());
    }
}
