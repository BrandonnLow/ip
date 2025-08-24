import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {

    private LocalDate by;


    public Deadline(String deadline, LocalDate by) {
        super(deadline, TaskType.DEADLINE);
        this.by = by;
    }

    public LocalDate getBy() {
        return this.by;
    }

    private String getByString() {
        return by.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
    }

    public String getByForFile() {
        return by.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + this.getByString() + ")";
    }

}
