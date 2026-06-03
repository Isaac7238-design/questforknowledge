package progress;

/**
 * Custom exception for score file read/write errors.
 *
 * Created by: Habib
 * Tested by: Aezekiel
 * Desc: Handle errors during score file I/O operations.
 */
public class ScoreFileException extends Exception {
 public ScoreFileException(String message) { super(message); }
 public ScoreFileException(String message, Throwable cause) { super(message, cause); }
}
