package entity;

import main.GamePanel;
import progress.LockedAreaException;
import java.awt.Color;

/**
 * NPC_Lucious - castle guard. Blocks entry until player meets conditions.
 * Demonstrates: exception handling (LockedAreaException)
 */
public class NPC_Lucious extends Entity {

    public NPC_Lucious(GamePanel gp) {
        super(gp);
        setDefaultValues();
        getImage();
        setDialogue();
    }
    public void setDefaultValues() {
        worldX    = gp.tileSize * 12;
        worldY    = gp.tileSize * 5;
        speed     = 0;
        direction = "down";
        name      = "Lucious Francis";
        type      = type_npc;
        solidArea.x = 8; solidArea.y = 16;
        solidArea.width = 32; solidArea.height = 32;
        solidAreaDefaultX = 8; solidAreaDefaultY = 16;
    }
    public void getImage() {
        up1    = setup("/npc/guard_down_1",    gp.tileSize, gp.tileSize);
        up2    = setup("/npc/guard_down_2",    gp.tileSize, gp.tileSize);
        down1  = setup("/npc/guard_down_1",  gp.tileSize, gp.tileSize);
        down2  = setup("/npc/guard_down_2",  gp.tileSize, gp.tileSize);
        left1  = setup("/npc/guard_down_1",  gp.tileSize, gp.tileSize);
        left2  = setup("/npc/guard_down_2",  gp.tileSize, gp.tileSize);
        right1 = setup("/npc/guard_down_1", gp.tileSize, gp.tileSize);
        right2 = setup("/npc/guard_down_2", gp.tileSize, gp.tileSize);
    }

    public void setDialogue() {
        dialogues[0][0] = "You are brave, but not ready.";
        dialogues[0][1] = "Learn more before entering the castle.";
        dialogues[0][2] = "Need: 70 Knowledge Points OR 7 Scrolls completed.";
        dialogues[1][0] = "Your knowledge shines. You may enter the castle!";
        dialogues[1][1] = "Go forth and face Miss Shona, brave scholar.";
    }

    @Override
    public void speak() {
        facePlayer();
        try {
            boolean unlocked = gp.player.knowledgePoints >= 70
                            || gp.player.scrollsCompleted >= 7;
            if (!unlocked) {
                throw new LockedAreaException("Need 70 KP or 7 scrolls. "
                    + "Have: " + gp.player.knowledgePoints + " KP, "
                    + gp.player.scrollsCompleted + " scrolls.");
            }
            // UNLOCK THE GATE - change map tile from wall to floor
            gp.tileM.mapTileNum[0][25][10] = 4; // open the gate tile
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
