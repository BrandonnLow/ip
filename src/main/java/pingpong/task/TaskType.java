package pingpong.task;

/**
 * Enumeration representing the different types of tasks in the Pingpong application.
 */
public enum TaskType {
    /** A todo task */
    TODO("T"),
    /** A task with a deadline */
    DEADLINE("D"),
    /** An event task with start and end times */
    Event("E");

    private final String symbol;

    /**
     * Creates a TaskType with the specified symbol.
     *
     * @param symbol the single-character symbol representing this task type
     */
    TaskType(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Gets the symbol for this task type.
     *
     * @return the single-character symbol
     */
    public String getSymbol() {
        return this.symbol;
    }
}
