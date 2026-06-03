package interfaces;

import entity.Player;

/**
 * Interactable - implemented by NPCs and interactive objects.
 *
 * Created by: Lee Yun Zhan
 * Tested by: Nathanael
 */
public interface Interactable {
 void interact(Player player);
}
