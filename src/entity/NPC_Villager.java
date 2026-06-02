package entity;

import java.awt.Color;
import main.GamePanel;

/**
 * NPC_Villager - teaches SDG 4 ideas. Multiple instances with different dialogue.
 */
public class NPC_Villager extends Entity {

    private int id;

    public NPC_Villager(GamePanel gp, int id) {
        super(gp);
        this.id = id;
        setDefaultValues();
        getImage();
        setDialogue();
    }
    public void setDefaultValues() {
        worldX    = gp.tileSize * (7 + id * 2);
        worldY    = gp.tileSize * 9;
        speed     = 1;
        defaultSpeed = 1;
        direction = "down";
        name      = "Villager";
        type      = type_npc;
        solidArea.x = 8; solidArea.y = 16;
        solidArea.width = 32; solidArea.height = 32;
        solidAreaDefaultX = 8; solidAreaDefaultY = 16;
    }
    public void getImage() {
        up1    = setup("/npc/oldman_up_1",    gp.tileSize, gp.tileSize);
        up2    = setup("/npc/oldman_up_2",    gp.tileSize, gp.tileSize);
        down1  = setup("/npc/oldman_down_1",  gp.tileSize, gp.tileSize);
        down2  = setup("/npc/oldman_down_2",  gp.tileSize, gp.tileSize);
        left1  = setup("/npc/oldman_left_1",  gp.tileSize, gp.tileSize);
        left2  = setup("/npc/oldman_left_2",  gp.tileSize, gp.tileSize);
        right1 = setup("/npc/oldman_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("/npc/oldman_right_2", gp.tileSize, gp.tileSize);
    }

    public void setDialogue() {
        if (id == 0) {
            dialogues[0][0] = "Education gives people the power to improve their future.";
            dialogues[0][1] = "SDG 4 believes every person deserves quality learning.";
            dialogues[0][2] = "Schools should be open to all, everywhere.";
        } else if (id == 1) {
            dialogues[0][0] = "Every child deserves a chance to learn.";
            dialogues[0][1] = "Boys and girls should have equal access to education.";
            dialogues[0][2] = "Inclusive schools welcome students of all backgrounds.";
        } else {
            dialogues[0][0] = "A safe school helps students grow.";
            dialogues[0][1] = "Technology can open new doors to learning.";
            dialogues[0][2] = "Lifelong learning helps people adapt throughout life.";
        }
    }

    @Override
    public void setAction() {
        getRandomDirection(120);
    }

    @Override
    public void speak() {
        facePlayer();
        startDialogue(this, 0);
        // Reward player for talking to villagers (+5 XP)
        gp.player.exp += 5;
        gp.ui.addMessage("+5 XP (talking to villager)");
    }

    @Override
    protected Color getFallbackColor() { return new Color(200, 160, 90); }
}
