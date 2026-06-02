package entity;

import java.awt.*;
import main.GamePanel;

/**
 * BOSS_Shona - final boss. Extends MON_MemoryFragment.
 * Defeating her triggers the ending choice.
 */
public class BOSS_Shona extends MON_MemoryFragment {

    public BOSS_Shona(GamePanel gp) {
        super(gp);
        speed = 1; defaultSpeed = 1;
        direction = "down"; name = "Miss Shona";
        type = type_monster;
        maxLife = 8; life = 8;
        attack = 2; defense = 1; exp = 10;
        knockBackPower = 3; boss = true; sleep = true;
        solidArea.x=3; solidArea.y=18; solidArea.width=42; solidArea.height=30;
        solidAreaDefaultX=3; solidAreaDefaultY=18;
        getImage();
    }

    public void getImage() {
        up1=setup("/monster/orc_up_1",gp.tileSize,gp.tileSize);
        up2=setup("/monster/orc_up_2",gp.tileSize,gp.tileSize);
        down1=setup("/monster/orc_down_1",gp.tileSize,gp.tileSize);
        down2=setup("/monster/orc_down_2",gp.tileSize,gp.tileSize);
        left1=setup("/monster/orc_left_1",gp.tileSize,gp.tileSize);
        left2=setup("/monster/orc_left_2",gp.tileSize,gp.tileSize);
        right1=setup("/monster/orc_right_1",gp.tileSize,gp.tileSize);
        right2=setup("/monster/orc_right_2",gp.tileSize,gp.tileSize);
    }

    public void setAction() {
        if (sleep && getTileDistance(gp.player) < 8) {
            sleep = false;
            gp.ui.addMessage("Miss Shona awakens!");
        }
        if (!sleep) moveTowardPlayer(30);
    }

    public void checkDrop() {
        gp.player.hasDefeatedShona = true;
        gp.player.unlockBadge("SDG Hero");
        gp.ui.addMessage("You defeated Miss Shona!");
        gp.gameState = gp.endingChoiceState;
        gp.ui.commandNum = 0;
    }

    protected Color getFallbackColor() { return new Color(120,0,160); }
}
