package entity;

import java.awt.Color;
import main.GamePanel;

/**
 * NPC_SheenaMemory - Shona's sister, hidden ghost NPC.
 * Finding her enables the SECRET_ENDING path.
 */
public class NPC_SheenaMemory extends Entity {

    private boolean activated = false;

    public NPC_SheenaMemory(GamePanel gp) {
        super(gp);
        setDefaultValues();
        getImage();
        setDialogue();
    }
    public void setDefaultValues() {
        worldX    = gp.tileSize * 5;
        worldY    = gp.tileSize * 8;
        speed     = 0;
        direction = "down";
        name      = "Sheena Memory";
        type      = type_npc;
        solidArea.x = 8; solidArea.y = 16;
        solidArea.width = 32; solidArea.height = 32;
        solidAreaDefaultX = 8; solidAreaDefaultY = 16;
    }
    public void getImage() {
        up1    = setup("/npc/sheena_down_1",    gp.tileSize, gp.tileSize);
        up2    = setup("/npc/sheena_down_2",    gp.tileSize, gp.tileSize);
        down1  = setup("/npc/sheena_down_1",  gp.tileSize, gp.tileSize);
        down2  = setup("/npc/sheena_down_2",  gp.tileSize, gp.tileSize);
        left1  = setup("/npc/sheena_down_1",  gp.tileSize, gp.tileSize);
        left2  = setup("/npc/sheena_down_2",  gp.tileSize, gp.tileSize);
        right1 = setup("/npc/sheena_down_1", gp.tileSize, gp.tileSize);
        right2 = setup("/npc/sheena_down_2", gp.tileSize, gp.tileSize);
    }

    public void setDialogue() {
        dialogues[0][0] = "...You found me. I am Sheena, Shona's sister.";
        dialogues[0][1] = "My sister feared that Lucienne would lose knowledge again.";
        dialogues[0][2] = "She tried to protect knowledge, but fear turned into control.";
        dialogues[0][3] = "The Knowledge Crystal was never meant to trap - only preserve.";
        dialogues[0][4] = "If you understand her pain, perhaps you can forgive her.";
        dialogues[0][5] = "Remember this when you face Shona. Empathy is also power.";
    }

    @Override
    public void speak() {
        facePlayer();
        if (!activated) {
            activated = true;
            gp.player.hasFoundSheenaMemory = true;
            gp.ui.addMessage("You found Sheena's hidden memory!");
        }
        startDialogue(this, 0);
    }

    @Override
    protected Color getFallbackColor() { return new Color(200, 180, 255); }
}
