package entity;

import java.awt.Color;
import main.GamePanel;

/**
 * NPC_Piercehardt - wise mage, tutorial NPC in Safe Area.
 * Explains the mission to Jeff Lionhardt.
 *
 * Created by: Lee Yun Zhan
 * Tested by: Nathanael
 */
public class NPC_Piercehardt extends Entity {

 public NPC_Piercehardt(GamePanel gp) {
 super(gp);
 setDefaultValues();
 getImage();
 setDialogue();
 }
 public void setDefaultValues() {
 worldX = gp.tileSize * 5;
 worldY = gp.tileSize * 3;
 speed = 0;
 direction = "down";
 name = "Piercehardt";
 type = type_npc;
 solidArea.x = 8; solidArea.y = 16;
 solidArea.width = 32; solidArea.height = 32;
 solidAreaDefaultX = 8; solidAreaDefaultY = 16;
 }
 public void getImage() {
 int size = (int)(gp.tileSize * 1.5);
 up1 = setup("/npc/piercehardt_down_1", size, size);
 up2 = setup("/npc/piercehardt_down_2", size, size);
 down1 = setup("/npc/piercehardt_down_1", size, size);
 down2 = setup("/npc/piercehardt_down_2", size, size);
 left1 = setup("/npc/piercehardt_down_1", size, size);
 left2 = setup("/npc/piercehardt_down_2", size, size);
 right1 = setup("/npc/piercehardt_down_1", size, size);
 right2 = setup("/npc/piercehardt_down_2", size, size);
 }

 public void setDialogue() {
 dialogues[0][0] = "Welcome to Lucienne, Jeff!";
 dialogues[0][1] = "This kingdom was once full of wisdom.";
 dialogues[0][2] = "But Miss Shona created the Knowledge Crystal...";
 dialogues[0][3] = "Instead of protecting knowledge, it trapped it.";
 dialogues[0][4] = "Evil Memory Fragments now roam the kingdom.";
 dialogues[0][5] = "They represent forgotten knowledge.";
 dialogues[0][6] = "Collect Knowledge Scrolls and defeat the fragments.";
 dialogues[0][7] = "Gain enough Knowledge Points to unlock the castle.";
 dialogues[0][8] = "Face Miss Shona and restore Lucienne's wisdom!";
 dialogues[0][9] = "Knowledge is your strongest magic, Jeff!";

 dialogues[1][0] = "Remember: answer quiz questions to defeat enemies.";
 dialogues[1][1] = "Correct = enemy takes damage. Wrong = you lose HP.";
 dialogues[1][2] = "Visit the Shopkeeper to buy helpful items!";
 }

 @Override
 public void speak() {
 facePlayer();
 startDialogue(this, dialogueSet);
 }

 @Override
 protected Color getFallbackColor() { return new Color(150, 80, 200); }
}
