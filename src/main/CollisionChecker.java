package main;

import entity.Entity;

/**
 * CollisionChecker - tile, object, and entity collision detection.
 * EXACT structure from RyiSnow Blue Boy Adventure.
 *
 * Created by: Aezekiel
 * Tested by: Habib
 * Purpose: Detect collisions between entities, tiles, and objects for movement logic.
 */
public class CollisionChecker {

    GamePanel gp;

    public CollisionChecker(GamePanel gp) { this.gp = gp; }

    public void checkTile(Entity entity) {
        int elx = entity.worldX + entity.solidArea.x;
        int erx = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int ety = entity.worldY + entity.solidArea.y;
        int eby = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int elCol = elx / gp.tileSize;
        int erCol = erx / gp.tileSize;
        int etRow = ety / gp.tileSize;
        int ebRow = eby / gp.tileSize;

        int t1, t2;
        String dir = entity.knockBack ? entity.knockBackDirection : entity.direction;

        switch (dir) {
            case "up":
                etRow = (ety - entity.speed) / gp.tileSize;
                t1 = safe(elCol, etRow); t2 = safe(erCol, etRow);
                if (gp.tileM.tile[t1].collision || gp.tileM.tile[t2].collision) entity.collisionOn = true;
                break;
            case "down":
                ebRow = (eby + entity.speed) / gp.tileSize;
                t1 = safe(elCol, ebRow); t2 = safe(erCol, ebRow);
                if (gp.tileM.tile[t1].collision || gp.tileM.tile[t2].collision) entity.collisionOn = true;
                break;
            case "left":
                elCol = (elx - entity.speed) / gp.tileSize;
                t1 = safe(elCol, etRow); t2 = safe(elCol, ebRow);
                if (gp.tileM.tile[t1].collision || gp.tileM.tile[t2].collision) entity.collisionOn = true;
                break;
            case "right":
                erCol = (erx + entity.speed) / gp.tileSize;
                t1 = safe(erCol, etRow); t2 = safe(erCol, ebRow);
                if (gp.tileM.tile[t1].collision || gp.tileM.tile[t2].collision) entity.collisionOn = true;
                break;
        }
    }

    private int safe(int col, int row) {
        col = Math.max(0, Math.min(col, gp.maxWorldCol - 1));
        row = Math.max(0, Math.min(row, gp.maxWorldRow - 1));
        int num = gp.tileM.mapTileNum[gp.currentMap][col][row];
        if (num < 0 || num >= gp.tileM.tile.length) return 0;
        return num;
    }

    public int checkObject(Entity entity, boolean player) {
        int index = 999;
        String dir = entity.knockBack ? entity.knockBackDirection : entity.direction;

        for (int i = 0; i < gp.obj[0].length; i++) {
            if (gp.obj[gp.currentMap][i] == null) continue;

            entity.solidArea.x += entity.worldX;
            entity.solidArea.y += entity.worldY;
            gp.obj[gp.currentMap][i].solidArea.x += gp.obj[gp.currentMap][i].worldX;
            gp.obj[gp.currentMap][i].solidArea.y += gp.obj[gp.currentMap][i].worldY;

            switch (dir) {
                case "up":    entity.solidArea.y -= entity.speed; break;
                case "down":  entity.solidArea.y += entity.speed; break;
                case "left":  entity.solidArea.x -= entity.speed; break;
                case "right": entity.solidArea.x += entity.speed; break;
            }

            if (entity.solidArea.intersects(gp.obj[gp.currentMap][i].solidArea)) {
                if (gp.obj[gp.currentMap][i].collision) entity.collisionOn = true;
                if (player) index = i;
            }

            entity.solidArea.x = entity.solidAreaDefaultX;
            entity.solidArea.y = entity.solidAreaDefaultY;
            gp.obj[gp.currentMap][i].solidArea.x = gp.obj[gp.currentMap][i].solidAreaDefaultX;
            gp.obj[gp.currentMap][i].solidArea.y = gp.obj[gp.currentMap][i].solidAreaDefaultY;
        }
        return index;
    }

    public int checkEntity(Entity entity, Entity[][] target) {
        int index = 999;
        String dir = entity.knockBack ? entity.knockBackDirection : entity.direction;

        for (int i = 0; i < target[0].length; i++) {
            if (target[gp.currentMap][i] == null) continue;

            entity.solidArea.x += entity.worldX;
            entity.solidArea.y += entity.worldY;
            target[gp.currentMap][i].solidArea.x += target[gp.currentMap][i].worldX;
            target[gp.currentMap][i].solidArea.y += target[gp.currentMap][i].worldY;

            switch (dir) {
                case "up":    entity.solidArea.y -= entity.speed; break;
                case "down":  entity.solidArea.y += entity.speed; break;
                case "left":  entity.solidArea.x -= entity.speed; break;
                case "right": entity.solidArea.x += entity.speed; break;
            }

            if (entity.solidArea.intersects(target[gp.currentMap][i].solidArea)) {
                if (target[gp.currentMap][i] != entity) {
                    entity.collisionOn = true;
                    index = i;
                }
            }

            entity.solidArea.x = entity.solidAreaDefaultX;
            entity.solidArea.y = entity.solidAreaDefaultY;
            target[gp.currentMap][i].solidArea.x = target[gp.currentMap][i].solidAreaDefaultX;
            target[gp.currentMap][i].solidArea.y = target[gp.currentMap][i].solidAreaDefaultY;
        }
        return index;
    }

    public boolean checkPlayer(Entity entity) {
        boolean contact = false;

        entity.solidArea.x    += entity.worldX;
        entity.solidArea.y    += entity.worldY;
        gp.player.solidArea.x += gp.player.worldX;
        gp.player.solidArea.y += gp.player.worldY;

        switch (entity.direction) {
            case "up":    entity.solidArea.y -= entity.speed; break;
            case "down":  entity.solidArea.y += entity.speed; break;
            case "left":  entity.solidArea.x -= entity.speed; break;
            case "right": entity.solidArea.x += entity.speed; break;
        }

        if (entity.solidArea.intersects(gp.player.solidArea)) {
            entity.collisionOn = true;
            contact = true;
        }

        entity.solidArea.x    = entity.solidAreaDefaultX;
        entity.solidArea.y    = entity.solidAreaDefaultY;
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;

        return contact;
    }
}
