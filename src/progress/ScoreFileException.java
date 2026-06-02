package progress;

/** Custom exception for score file read/write errors. */
public class ScoreFileException extends Exception {
    public ScoreFileException(String message) { super(message); }
    public ScoreFileException(String message, Throwable cause) { super(message, cause); }
}
