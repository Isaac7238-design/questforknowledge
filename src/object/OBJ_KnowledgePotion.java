package object;

import entity.Entity;
import entity.Player;
import java.awt.Color;
import main.GamePanel;

/**
 * OBJ_KnowledgePotion - gives a quiz hint (reveals correct answer once).
 * Stackable consumable item.
 */
public class OBJ_KnowledgePotion extends Entity {

    public OBJ_KnowledgePotion(GamePanel gp) {
        super(gp);
        name        = "Knowledge Potion";
        type        = type_consumable;
        stackable   = true;
        price       = 20;   // cost in Knowledge Points
        description = "Gives a quiz hint.\nReveals the correct answer once.";
        value       = 1;

        down1 = setup("/objects/potion_red", gp.tileSize, gp.tileSize);
        up1 = down1; left1 = down1; right1 = down1;
        up2 = down1; down2 = down1; left2 = down1; right2 = down1;

        solidArea.x = 0; solidArea.y = 0;
        solidArea.width = 48; solidArea.height = 48;
        solidAreaDefaultX = 0; solidAreaDefaultY = 0;
    }

    @Override
    public boolean use(Entity entity) {
        if (entity instanceof Player) {
            Player p = (Player) entity;
            p.hintCount++;
            String hint = gp.quizManager.getCurrentQuestion() != null
                ? "Hint: Answer is [" + gp.quizManager.getCurrentQuestion().getCorrectAnswer() + "]"
                : "No active quiz question.";
            gp.ui.addMessage(hint);
            gp.playSE(2);
            return true;
        }
        return false;
    }

    @Override
    protected Color getFallbackColor() { return new Color(100, 180, 255); }
}
