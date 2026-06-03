package interfaces;

/**
 * Playable - core actions a playable character must support.
 * Implemented by Player.
 *
 * Created by: Aezekiel
 * Tested by: Habib
 * Purpose: Define the contract for playable character actions (move, gainXP, levelUp).
 */
public interface Playable {
    void move(String direction);
    void gainXP(int amount);
    void levelUp();
}
