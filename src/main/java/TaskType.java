public enum TaskType {

    TODO("T"), DEADLINE("D"), Event("E");

    private final String symbol;

    TaskType(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return this.symbol;
    }
}
