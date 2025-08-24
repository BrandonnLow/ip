public class DeleteCommand extends Command {

    private int taskNumber;

    public DeleteCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws PingpongException {
        Task deletedTask = tasks.deleteTask(taskNumber - 1);
        ui.showTaskDeleted(deletedTask, tasks.size());
        storage.save(tasks.getAllTasks());
    }
}
