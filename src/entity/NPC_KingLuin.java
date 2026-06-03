package entity;

import java.awt.Color;
import main.GamePanel;

/**
 * NPC_KingLuin - King of Lucienne. Appears near the castle.
 *
 * Created by: Lee Yun Zhan
 * Tested by: Nathanael
 */
public class NPC_KingLuin extends Entity {

 public NPC_KingLuin(GamePanel gp) {
 super(gp);
 setDefaultValues();
 getImage();
 setDialogue();
 }
 public void setDefaultValues() {
 worldX = gp.tileSize * 14;
 worldY = gp.tileSize * 6;
 speed = 0;
 direction = "down";
 name = "King Luin";
 type = type_npc;
 solidArea.x = 8; solidArea.y = 16;
 solidArea.width = 32; solidArea.height = 32;
 solidAreaDefaultX = 8; solidAreaDefaultY = 16;
 }
 public void getImage() {
 up1 = setup("/npc/king_down_1", gp.tileSize, gp.tileSize);
 up2 = setup("/npc/king_down_2", gp.tileSize, gp.tileSize);
 down1 = setup("/npc/king_down_1", gp.tileSize, gp.tileSize);
 down2 = setup("/npc/king_down_2", gp.tileSize, gp.tileSize);
 left1 = setup("/npc/king_down_1", gp.tileSize, gp.tileSize);
 left2 = setup("/npc/king_down_2", gp.tileSize, gp.tileSize);
 right1 = setup("/npc/king_down_1", gp.tileSize, gp.tileSize);
 right2 = setup("/npc/king_down_2", gp.tileSize, gp.tileSize);
 }

 public void setDialogue() {
 dialogues[0][0] = "Brave traveller, welcome to Lucienne.";
 dialogues[0][1] = "Lucienne's wisdom can return only when knowledge is shared.";
 dialogues[0][2] = "Defeat the Evil Memory Fragments.";
 dialogues[0][3] = "Find the Knowledge Scrolls hidden across the land.";
 dialogues[0][4] = "Face Miss Shona and restore our kingdom's light.";
 }

 @Override
 public void speak() {
 facePlayer();
 startDialogue(this, 0);
 }

 @Override
 protected Color getFallbackColor() { return new Color(218, 165, 32); }
}
