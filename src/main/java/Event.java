public class Event extends Task {

    private String start;
    private String end;

    public Event(String event, String start, String end) {
        super(event, TaskType.Event);
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return super.toString() + " (from: " + start + " to: " + end + ")";
    }

}
