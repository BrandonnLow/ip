public class MarkCommand extends Command {

    private int taskNumber;

    public MarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws PingpongException {
        Task markedTask = tasks.markTask(taskNumber - 1);
        ui.showTaskMarked(markedTask);
        storage.save(tasks.getAllTasks());
    }
}
