package entity;

import java.awt.Color;
import main.GamePanel;
import progress.LockedAreaException;

/**
 * NPC_Lucious - castle guard. Blocks entry until player meets conditions.
 * When unlocked, walks away to the side clearing the door.
 *
 * Created by: Lee Yun Zhan
 * Tested by: Nathanael
 */
public class NPC_Lucious extends Entity {

 private boolean gateOpened = false;
 private int moveAwayCounter = 0;

 public NPC_Lucious(GamePanel gp) {
 super(gp);
 setDefaultValues();
 getImage();
 setDialogue();
 }
 public void setDefaultValues() {
 worldX = gp.tileSize * 12;
 worldY = gp.tileSize * 5;
 speed = 0;
 defaultSpeed = 0;
 direction = "down";
 name = "Lucious Francis";
 type = type_npc;
 solidArea.x = 8; solidArea.y = 16;
 solidArea.width = 32; solidArea.height = 32;
 solidAreaDefaultX = 8; solidAreaDefaultY = 16;
 }
 public void getImage() {
 up1 = setup("/npc/guard_down_1", gp.tileSize, gp.tileSize);
 up2 = setup("/npc/guard_down_2", gp.tileSize, gp.tileSize);
 down1 = setup("/npc/guard_down_1", gp.tileSize, gp.tileSize);
 down2 = setup("/npc/guard_down_2", gp.tileSize, gp.tileSize);
 left1 = setup("/npc/guard_down_1", gp.tileSize, gp.tileSize);
 left2 = setup("/npc/guard_down_2", gp.tileSize, gp.tileSize);
 right1 = setup("/npc/guard_down_1", gp.tileSize, gp.tileSize);
 right2 = setup("/npc/guard_down_2", gp.tileSize, gp.tileSize);
 }

 public void setDialogue() {
 dialogues[0][0] = "You are brave, but not ready.";
 dialogues[0][1] = "Learn more before entering the castle.";
 dialogues[0][2] = "Need: 70 Knowledge Points AND 7 Scrolls completed.";
 dialogues[1][0] = "Your knowledge shines. You may enter the castle!";
 dialogues[1][1] = "I shall step aside. Go forth, brave scholar.";
 }

 @Override
 public void setAction() {
 // After gate opens, walk to the right to clear the doorway
 if (gateOpened && moveAwayCounter < 120) {
 direction = "right";
 speed = 2;
 moveAwayCounter++;
 if (moveAwayCounter >= 120) {
 speed = 0; // Stop after moving away
 }
 }
 }

 @Override
 public void speak() {
 facePlayer();
 try {
 boolean unlocked = gp.player.knowledgePoints >= 70
 && gp.player.scrollsCompleted >= 7;
 if (!unlocked) {
 throw new LockedAreaException("Need 70 KP AND 7 scrolls. "
 + "Have: " + gp.player.knowledgePoints + " KP, "
 + gp.player.scrollsCompleted + " scrolls.");
 }
 // UNLOCK THE GATE - remove wall tiles to create door (3 wide x 3 tall)
 gateOpened = true;
 collisionOn = false;
 // Row 10, 11, 12 - open 3 tiles wide
 for (int r = 10; r <= 12; r++) {
 gp.tileM.mapTileNum[0][24][r] = 4;
 gp.tileM.mapTileNum[0][25][r] = 4;
 gp.tileM.mapTileNum[0][26][r] = 4;
 }
 gp.playSE(11); // door open sound
 gp.player.unlockBadge("Castle Scholar");
 startDialogue(this, 1);
 } catch (LockedAreaException e) {
 gp.ui.addMessage(e.getMessage());
 startDialogue(this, 0);
 }
 }

 @Override
 protected Color getFallbackColor() { return new Color(200, 50, 50); }
}
