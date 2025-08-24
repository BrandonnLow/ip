public class Deadline extends Task {

    private String by;

    public Deadline(String deadline, String by) {
        super(deadline, TaskType.DEADLINE);
        this.by = by;
    }

    public String getBy() {
        return this.by;
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + by + ")";
    }

}
