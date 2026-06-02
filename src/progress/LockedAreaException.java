package progress;

/** Thrown when player tries to enter a locked area without enough progress. */
public class LockedAreaException extends Exception {
    public LockedAreaException(String message) { super(message); }
}
