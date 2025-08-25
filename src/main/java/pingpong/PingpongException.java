package pingpong;

/**
 * Custom exception class for the Pingpong application.
 * Used to handle application-specific errors and provide meaningful error messages to users.
 */
public class PingpongException extends Exception {

    /**
     * Creates a new PingpongException with the specified error message.
     *
     * @param message the error message describing what went wrong
     */
    public PingpongException(String message) {
        super(message);
    }
}