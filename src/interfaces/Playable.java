package interfaces;

/**
 * Playable - core actions a playable character must support.
 * Implemented by Player.
 *
 * Created by: Aezekiel
 * Tested by: Habib
 */
public interface Playable {
 void move(String direction);
 void gainXP(int amount);
 void levelUp();
}
