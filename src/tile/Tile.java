package tile;

import java.awt.image.BufferedImage;

/**
 * Tile - holds image and collision flag. RyiSnow exact structure.
 *
 * Created by: Aezekiel
 * Tested by: Habib
 * Purpose: Data class for a single tile type (image + collision property).
 */
public class Tile {
    public BufferedImage image;
    public boolean       collision = false;
}
