package tile;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

/**
 * TileManager - loads tile data and draws the world map.
 * RyiSnow Blue Boy Adventure exact structure.
 * Reads tile names + collision from /maps/tiledata.txt.
 * Falls back to coloured rectangles when PNG images are missing.
 */
public class TileManager {

    GamePanel gp;
    public Tile[] tile;
    public int    mapTileNum[][][]; // [mapIndex][col][row]

    ArrayList<String>  fileNames       = new ArrayList<>();
    ArrayList<String>  collisionStatus = new ArrayList<>();

    // Fallback colours indexed by tile name keyword
    private static final java.util.Map<String,Color> FALLBACK_COLORS = new java.util.LinkedHashMap<>();
    static {
        FALLBACK_COLORS.put("grass",  new Color(34, 139, 34));
        FALLBACK_COLORS.put("wall",   new Color(80,  80,  80));
        FALLBACK_COLORS.put("water",  new Color(30, 100, 200));
        FALLBACK_COLORS.put("road",   new Color(180,150, 100));
        FALLBACK_COLORS.put("floor",  new Color(200,200, 180));
        FALLBACK_COLORS.put("tree",   new Color(0,  100,   0));
        FALLBACK_COLORS.put("earth",  new Color(139, 90,  43));
        FALLBACK_COLORS.put("hut",    new Color(160,110,  60));
        FALLBACK_COLORS.put("table",  new Color(120, 80,  40));
        FALLBACK_COLORS.put("sand",   new Color(210,190, 100));
        FALLBACK_COLORS.put("door",   new Color(139, 69,  19));
        FALLBACK_COLORS.put("chest",  new Color(180,130,  50));
    }

    public TileManager(GamePanel gp) {
        this.gp = gp;

        // ── Read tiledata.txt ──────────────────────────────────────
        InputStream is = getClass().getResourceAsStream("/maps/tiledata.txt");
        if (is != null) {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            try {
                String line;
                while ((line = br.readLine()) != null) {
                    fileNames.add(line.trim());
                    String col = br.readLine();
                    collisionStatus.add(col != null ? col.trim() : "false");
                }
                br.close();
            } catch (IOException e) { e.printStackTrace(); }
        }

        // Ensure at least one tile entry so array is not empty
        if (fileNames.isEmpty()) {
            String[] defaults = {"grass00","wall","water00","road00","floor01",
                                 "tree","earth","road01","sand","door"};
            boolean[] cols    = {false, true, true, false, false,
                                 true, false, false, false, false};
            for (int i = 0; i < defaults.length; i++) {
                fileNames.add(defaults[i]);
                collisionStatus.add(String.valueOf(cols[i]));
            }
        }

        tile = new Tile[fileNames.size()];
        getTileImage();

        // ── Determine world size from map 0 ───────────────────────
        InputStream is2 = getClass().getResourceAsStream("/maps/worldmap.txt");
        if (is2 != null) {
            try {
                BufferedReader br2 = new BufferedReader(new InputStreamReader(is2));
                String firstLine = br2.readLine();
                if (firstLine != null) {
                    String[] cols2 = firstLine.trim().split(" ");
                    gp.maxWorldCol = cols2.length;
                }
                int rowCount = 1;
                while (br2.readLine() != null) rowCount++;
                gp.maxWorldRow = rowCount;
                br2.close();
            } catch (IOException e) { e.printStackTrace(); }
        }
        if (gp.maxWorldCol == 0) gp.maxWorldCol = 50;
        if (gp.maxWorldRow == 0) gp.maxWorldRow = 50;

        mapTileNum = new int[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];

        loadMap("/maps/worldmap.txt",  0);
        loadMap("/maps/indoor01.txt",  1);
        loadMap("/maps/dungeon01.txt", 2);
        loadMap("/maps/dungeon02.txt", 3);
    }

    private void getTileImage() {
        for (int i = 0; i < fileNames.size(); i++) {
            boolean col = "true".equalsIgnoreCase(collisionStatus.get(i));
            setup(i, fileNames.get(i), col);
        }
    }

    public void setup(int index, String imageName, boolean collision) {
        UtilityTool uTool = new UtilityTool();
        tile[index]           = new Tile();
        tile[index].collision = collision;

        try {
            InputStream is = getClass().getResourceAsStream("/tiles/" + imageName + ".png");
            if (is == null) is = getClass().getResourceAsStream("/tiles/" + imageName);
            if (is != null) {
                BufferedImage img = ImageIO.read(is);
                tile[index].image = uTool.scaleImage(img, gp.tileSize, gp.tileSize);
                return;
            }
        } catch (IOException e) { /* fall through */ }

        // Programmatic fallback coloured tile
        tile[index].image = makeFallbackTile(imageName, collision);
    }

    private BufferedImage makeFallbackTile(String name, boolean isBlocked) {
        int ts = gp.tileSize;
        BufferedImage img = new BufferedImage(ts, ts, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = img.createGraphics();

        Color c = new Color(34, 139, 34); // default green
        String lower = name.toLowerCase();
        for (java.util.Map.Entry<String,Color> e : FALLBACK_COLORS.entrySet()) {
            if (lower.contains(e.getKey())) { c = e.getValue(); break; }
        }

        g2.setColor(c);
        g2.fillRect(0, 0, ts, ts);
        g2.setColor(c.darker());
        g2.drawRect(0, 0, ts-1, ts-1);

        // X mark for collision tiles
        if (isBlocked) {
            g2.setColor(new Color(0,0,0,60));
            g2.drawLine(0, 0, ts-1, ts-1);
            g2.drawLine(ts-1, 0, 0, ts-1);
        }
        g2.dispose();
        return img;
    }

    public void loadMap(String filePath, int map) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            if (is == null) return;
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            int col = 0, row = 0;
            while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
                String line = br.readLine();
                if (line == null) break;
                String[] nums = line.trim().split(" ");
                while (col < gp.maxWorldCol && col < nums.length) {
                    try {
                        int num = Integer.parseInt(nums[col].trim());
                        // clamp to valid tile range
                        if (num < 0 || num >= tile.length) num = 0;
                        mapTileNum[map][col][row] = num;
                    } catch (NumberFormatException e) {
                        mapTileNum[map][col][row] = 0;
                    }
                    col++;
                }
                if (col >= gp.maxWorldCol) { col = 0; row++; }
            }
            br.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void draw(Graphics2D g2) {
        int worldCol = 0, worldRow = 0;
        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
            int tileNum = mapTileNum[gp.currentMap][worldCol][worldRow];
            int worldX  = worldCol * gp.tileSize;
            int worldY  = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
                g2.drawImage(tile[tileNum].image, screenX, screenY, null);
            }
            worldCol++;
            if (worldCol == gp.maxWorldCol) { worldCol = 0; worldRow++; }
        }
    }
}
