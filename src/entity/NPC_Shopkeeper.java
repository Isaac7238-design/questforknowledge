package entity;

import java.awt.Color;
import main.GamePanel;

/**
 * NPC_Shopkeeper - opens the shop state when spoken to.
 *
 * Created by: Lee Yun Zhan
 * Tested by: Nathanael
 */
public class NPC_Shopkeeper extends Entity {

 public NPC_Shopkeeper(GamePanel gp) {
 super(gp);
 setDefaultValues();
 getImage();
 setDialogue();
 }
 public void setDefaultValues() {
 worldX = gp.tileSize * 10;
 worldY = gp.tileSize * 8;
 speed = 0;
 direction = "down";
 name = "Shopkeeper";
 type = type_npc;
 solidArea.x = 8; solidArea.y = 16;
 solidArea.width = 32; solidArea.height = 32;
 solidAreaDefaultX = 8; solidAreaDefaultY = 16;
 }
 public void getImage() {
 up1 = setup("/npc/merchant_down_1", gp.tileSize, gp.tileSize);
 up2 = setup("/npc/merchant_down_2", gp.tileSize, gp.tileSize);
 down1 = setup("/npc/merchant_down_1", gp.tileSize, gp.tileSize);
 down2 = setup("/npc/merchant_down_2", gp.tileSize, gp.tileSize);
 left1 = setup("/npc/merchant_down_1", gp.tileSize, gp.tileSize);
 left2 = setup("/npc/merchant_down_2", gp.tileSize, gp.tileSize);
 right1 = setup("/npc/merchant_down_1", gp.tileSize, gp.tileSize);
 right2 = setup("/npc/merchant_down_2", gp.tileSize, gp.tileSize);
 }

 public void setDialogue() {
 dialogues[0][0] = "Welcome! I sell items to help your journey!";
 dialogues[0][1] = "Use Knowledge Points to buy items.";
 }

 @Override
 public void speak() {
 facePlayer();
 gp.gameState = gp.shopState;
 gp.ui.commandNum = 0;
 }

 @Override
 protected Color getFallbackColor() { return new Color(100, 180, 100); }
}
