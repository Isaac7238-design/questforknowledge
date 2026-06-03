package interfaces;

import entity.Player;

/**
 * Interactable - implemented by NPCs and interactive objects.
 *
 * Created by: Lee Yun Zhan
 * Tested by: Nathanael
 * Purpose: Define the interaction contract for NPCs and interactive world objects.
 */
public interface Interactable {
    void interact(Player player);
}
