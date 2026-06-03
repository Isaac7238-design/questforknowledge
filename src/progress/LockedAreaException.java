package progress;

/**
 * Thrown when player tries to enter a locked area without enough progress.
 *
 * Created by: Habib
 * Tested by: Aezekiel
 * Purpose: Custom exception for gating progression (castle entry requires KP + scrolls).
 */
public class LockedAreaException extends Exception {
    public LockedAreaException(String message) { super(message); }
}
