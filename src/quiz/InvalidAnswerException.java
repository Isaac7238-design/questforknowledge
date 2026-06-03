package quiz;

/**
 * Thrown when a quiz answer is empty or null.
 *
 * Created by: Nathanael
 * Tested by: Habib
 * Purpose: Custom exception for invalid quiz answer input validation.
 */
public class InvalidAnswerException extends Exception {
    public InvalidAnswerException(String message) { super(message); }
}
