package entity;

import java.awt.*;
import main.GamePanel;

/**
 * MON_MemoryFragment - quiz enemy. Touching triggers a quiz battle.
 * Demonstrates: inheritance, polymorphism, OOP
 */
public class MON_MemoryFragment extends Entity {

    public MON_MemoryFragment(GamePanel gp) {
        super(gp);
        setDefaultValues();
        getImage();
    }
    public void setDefaultValues() {
        speed        = 1;
        defaultSpeed = 1;
        direction    = "down";
        name         = "Memory Fragment";
        type         = type_monster;
        maxLife      = 3;
        life         = 3;
        attack       = 1;
        defense      = 0;
        exp          = 2;
        knockBackPower = 2;

        solidArea.x = 3; solidArea.y = 18;
        solidArea.width = 42; solidArea.height = 30;
        solidAreaDefaultX = 3; solidAreaDefaultY = 18;
    }
    public void getImage() {
        up1    = setup("/monster/greenslime_down_1", gp.tileSize, gp.tileSize);
        up2    = setup("/monster/greenslime_down_2", gp.tileSize, gp.tileSize);
        down1  = setup("/monster/greenslime_down_1", gp.tileSize, gp.tileSize);
        down2  = setup("/monster/greenslime_down_2", gp.tileSize, gp.tileSize);
        left1  = setup("/monster/greenslime_down_1", gp.tileSize, gp.tileSize);
        left2  = setup("/monster/greenslime_down_2", gp.tileSize, gp.tileSize);
        right1 = setup("/monster/greenslime_down_1", gp.tileSize, gp.tileSize);
        right2 = setup("/monster/greenslime_down_2", gp.tileSize, gp.tileSize);
    }

    @Override
    public void setAction() {
        // Wander randomly - do NOT chase player
        // Enemies are passive; player initiates battle with Enter key
        getRandomDirection(180);
    }

    @Override
    public void damageReaction() {
        // No chase reaction needed
    }

    @Override
    public void checkDrop() {
        // Drop a Knowledge Scroll or nothing
        int rand = new java.util.Random().nextInt(4);
        if (rand == 0) {
            // Drop a small KP bonus (handled via UI message)
            gp.ui.addMessage("+5 KP bonus drop!");
            gp.player.knowledgePoints += 5;
        }
    }

    @Override
    public Color getParticleColor()  { return new Color(200, 50, 50); }
    @Override
    public int   getParticleSize()   { return 10; }
    @Override
    public int   getParticleSpeed()  { return 1; }
    @Override
    public int   getParticleMaxLife(){ return 20; }

    @Override
    protected Color getFallbackColor() { return new Color(180, 40, 40); }

    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);
        // HP bar above enemy
        if (!inCamera()) return;
        int sx = getScreenX();
        int sy = getScreenY();
        double scale = (double) gp.tileSize / maxLife;
        int barW = (int)(scale * Math.max(0, life));
        g2.setColor(new Color(35,35,35));
        g2.fillRect(sx-1, sy-16, gp.tileSize+2, 12);
        g2.setColor(new Color(255, 0, 30));
        g2.fillRect(sx, sy-15, barW, 10);
    }
}
