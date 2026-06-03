package main;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * UtilityTool - helper for scaling images.
 *
 * Created by: Aezekiel
 * Tested by: Habib
 */
public class UtilityTool {

 /** Scale a BufferedImage to the given dimensions with smooth interpolation. */
 public BufferedImage scaleImage(BufferedImage original, int width, int height) {
 BufferedImage scaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
 Graphics2D g2 = scaled.createGraphics();
 g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
 g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
 g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
 g2.drawImage(original, 0, 0, width, height, null);
 g2.dispose();
 return scaled;
 }
}
