package interfaces;

import entity.Player;

/**
 * Interactable - implemented by NPCs and interactive objects.
 */
public interface Interactable {
    void interact(Player player);
}
