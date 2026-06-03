package quiz;

// Author: Nathanael | Tester: Habib
/**
 * Thrown when a quiz answer is empty or null.
 *
 */
public class InvalidAnswerException extends Exception {
 public InvalidAnswerException(String message) { super(message); }
}
