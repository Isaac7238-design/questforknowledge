package interfaces;

/**
 * Playable - core actions a playable character must support.
 * Implemented by Player.
 */
public interface Playable {
    void move(String direction);
    void gainXP(int amount);
    void levelUp();
}
