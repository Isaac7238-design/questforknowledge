package object;

import entity.Entity;
import java.awt.*;
import main.GamePanel;

/**
 * OBJ_KnowledgeScroll - opens a learning page when interacted with.
 * Demonstrates: inheritance (Entity), OOP
 */
public class OBJ_KnowledgeScroll extends Entity {

    private int scrollId;
    private boolean used = false;

    public OBJ_KnowledgeScroll(GamePanel gp, int scrollId) {
        super(gp);
        this.scrollId = scrollId;
        name      = "Knowledge Scroll";
        type      = type_obstacle;
        collision = false;
        description = "A scroll containing SDG 4 wisdom.";

        solidArea.x = 0; solidArea.y = 0;
        solidArea.width = 48; solidArea.height = 48;
        solidAreaDefaultX = 0; solidAreaDefaultY = 0;

        down1 = setup("/objects/chest", gp.tileSize, gp.tileSize);
        up1   = down1; left1 = down1; right1 = down1;
        up2   = down1; down2 = down1; left2 = down1; right2 = down1;
    }

    @Override
    public void interact() {
        if (!used) {
            used = true;
            gp.currentScrollIndex = scrollId;
            gp.keyH.textInputMode = false;
            gp.gameState = gp.learningState;
        } else {
            gp.ui.addMessage("You have already read this scroll.");
        }
    }

    @Override
    protected Color getFallbackColor() { return new Color(255, 220, 100); }
}
