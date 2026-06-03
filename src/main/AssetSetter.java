package main;

import entity.*;
import object.*;

/**
 * AssetSetter - places entities. NO world items (potions only from shop).
 *
 * Created by: Aezekiel
 * Tested by: Habib
 * Purpose: Place all NPCs, monsters, and objects at their world positions.
 */
public class AssetSetter {

    GamePanel gp;
    public AssetSetter(GamePanel gp) { this.gp = gp; }

    public void setObject() {
        // Only Knowledge Scrolls in the garden (Y:15-29, X:4-15)
        int[][] scrollPos = {
            {5,16}, {8,18}, {12,20}, {6,22}, {10,24},
            {14,17}, {7,26}, {4,28}, {11,19}, {13,25}
        };
        for (int i = 0; i < scrollPos.length; i++) {
            gp.obj[0][i] = new OBJ_KnowledgeScroll(gp, i);
            gp.obj[0][i].worldX = scrollPos[i][0] * gp.tileSize;
            gp.obj[0][i].worldY = scrollPos[i][1] * gp.tileSize;
        }
        // NO world potions - buy from shop only
    }

    public void setNPC() {
        // PIERCEHARDT in Safe Area (row 34, col 24) - right next to Jeff's spawn
        gp.npc[0][0] = new NPC_Piercehardt(gp);
        gp.npc[0][0].worldX = gp.tileSize * 24;
        gp.npc[0][0].worldY = gp.tileSize * 34;

        // SHOPKEEPER next to shop building (row 26, col 31)
        gp.npc[0][4] = new NPC_Shopkeeper(gp);
        gp.npc[0][4].worldX = gp.tileSize * 32;
        gp.npc[0][4].worldY = gp.tileSize * 28;

        // VILLAGERS inside village area
        gp.npc[0][1] = new NPC_Villager(gp, 0);
        gp.npc[0][1].worldX = gp.tileSize * 23;
        gp.npc[0][1].worldY = gp.tileSize * 22;

        gp.npc[0][2] = new NPC_Villager(gp, 1);
        gp.npc[0][2].worldX = gp.tileSize * 27;
        gp.npc[0][2].worldY = gp.tileSize * 26;

        gp.npc[0][3] = new NPC_Villager(gp, 2);
        gp.npc[0][3].worldX = gp.tileSize * 30;
        gp.npc[0][3].worldY = gp.tileSize * 22;

        // LUCIOUS at castle gate (row 13, col 25) - in front of the wall
        gp.npc[0][5] = new NPC_Lucious(gp);
        gp.npc[0][5].worldX = gp.tileSize * 25;
        gp.npc[0][5].worldY = gp.tileSize * 13;

        // KING LUIN inside castle (row 5, col 25)
        gp.npc[0][6] = new NPC_KingLuin(gp);
        gp.npc[0][6].worldX = gp.tileSize * 25;
        gp.npc[0][6].worldY = gp.tileSize * 4;

        // SHEENA hidden at dead-end corner of the forest maze (row 9, col 11)
        gp.npc[0][7] = new NPC_SheenaMemory(gp);
        gp.npc[0][7].worldX = gp.tileSize * 11;
        gp.npc[0][7].worldY = gp.tileSize * 9;
    }

    public void setMonster() {
        // Enemies in Battleground (Y:12-22, X:34-47)
        int[][] enemyPos = {{37,15},{41,18},{45,14},{39,20},{43,22}};
        for (int i = 0; i < enemyPos.length; i++) {
            if (gp.monster[0][i] == null || !gp.monster[0][i].alive) {
                gp.monster[0][i] = new MON_MemoryFragment(gp);
                gp.monster[0][i].worldX = enemyPos[i][0] * gp.tileSize;
                gp.monster[0][i].worldY = enemyPos[i][1] * gp.tileSize;
            }
        }
        // BOSS SHONA inside castle (row 5, col 25) - always present until all endings achieved
        if (gp.monster[0][5] == null || !gp.monster[0][5].alive) {
            gp.monster[0][5] = new BOSS_Shona(gp);
            gp.monster[0][5].worldX = gp.tileSize * 25;
            gp.monster[0][5].worldY = gp.tileSize * 5;
        }
    }
}
