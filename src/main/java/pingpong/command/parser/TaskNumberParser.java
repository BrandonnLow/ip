package pingpong.command.parser;

import pingpong.PingpongException;

/**
 * Handles parsing of task numbers from user input.
 */
public class TaskNumberParser {
    private static final String INVALID_TASK_NUMBER_ERROR = "Please provide valid task number(s).";
    private static final String POSITIVE_NUMBER_ERROR = "Task numbers must be positive integers.";

    /**
     * Parses a single task number from string.
     *
     * @param numberStr string representation of task number
     * @return parsed task number
     * @throws PingpongException if task number is invalid
     */
    public static int parseTaskNumber(String numberStr) throws PingpongException {
        assert numberStr != null : "Number string should not be null";

        try {
            int taskNum = Integer.parseInt(numberStr);
            if (taskNum <= 0) {
                throw new PingpongException(POSITIVE_NUMBER_ERROR);
            }
            assert taskNum > 0 : "Parsed task number should be positive";
            return taskNum;
        } catch (NumberFormatException e) {
            throw new PingpongException(INVALID_TASK_NUMBER_ERROR);
        }
    }

    /**
     * Parses multiple task numbers from string array.
     *
     * @param numberParts array of string representations of task numbers
     * @return array of parsed task numbers
     * @throws PingpongException if any task number is invalid
     */
    public static int[] parseTaskNumbers(String... numberParts) throws PingpongException {
        assert numberParts != null : "Number parts array should not be null";
        assert numberParts.length > 0 : "Should have at least one number part";

        int[] taskNumbers = new int[numberParts.length];

        for (int i = 0; i < numberParts.length; i++) {
            assert numberParts[i] != null : "Each number part should not be null";
            taskNumbers[i] = parseTaskNumber(numberParts[i]);
        }

        assert taskNumbers.length == numberParts.length : "All numbers should be parsed";
        return taskNumbers;
    }
}
