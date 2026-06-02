package entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.UtilityTool;

/**
 * Entity - base class for Player, NPCs, Monsters, and Objects.
 * EXACT structure from RyiSnow Blue Boy Adventure.
 * Demonstrates: abstraction, inheritance
 */
public class Entity {

    public GamePanel gp;

    // ── Sprites ──────────────────────────────────────────────────
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2;
    public BufferedImage attackLeft1, attackLeft2, attackRight1, attackRight2;
    public BufferedImage guardUp, guardDown, guardLeft, guardRight;
    public BufferedImage image, image2, image3;

    // ── Collision ────────────────────────────────────────────────
    public Rectangle solidArea    = new Rectangle(0, 0, 48, 48);
    public Rectangle attackArea   = new Rectangle(0, 0, 0, 0);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collision = false;

    // ── Dialogue (2D array: [set][line] – RyiSnow style) ─────────
    public String dialogues[][] = new String[20][20];
    public Entity  attacker;
    public Entity  linkedEntity;
    public boolean temp = false;

    // ── State ────────────────────────────────────────────────────
    public int     worldX, worldY;
    public String  direction       = "down";
    public int     spriteNum       = 1;
    public int     dialogueSet     = 0;
    public int     dialogueIndex   = 0;
    public boolean collisionOn     = false;
    public boolean invincible      = false;
    public boolean attacking       = false;
    public boolean alive           = true;
    public boolean dying           = false;
    public boolean hpBarOn         = false;
    public boolean onPath          = false;
    public boolean knockBack       = false;
    public String  knockBackDirection;
    public boolean guarding        = false;
    public boolean transparent     = false;
    public boolean offBalance      = false;
    public Entity  loot;
    public boolean opened          = false;
    public boolean inRage          = false;
    public boolean sleep           = false;
    public boolean drawing         = true;

    // ── Counters ─────────────────────────────────────────────────
    public int spriteCounter        = 0;
    public int actionLockCounter    = 0;
    public int invincibleCounter    = 0;
    public int shotAvailableCounter = 0;
    int        dyingCounter         = 0;
    public int hpBarCounter         = 0;
    int        knockBackCounter     = 0;
    public int guardCounter         = 0;
    int        offBalanceCounter    = 0;

    // ── Character attributes ─────────────────────────────────────
    public String name;
    public int    defaultSpeed;
    public int    speed;
    public int    maxLife;
    public int    life;
    public int    maxMana;
    public int    mana;
    public int    ammo;
    public int    level;
    public int    strength;
    public int    dexterity;
    public int    attack;
    public int    defense;
    public int    exp;
    public int    nextLevelExp;
    public int    coin;
    public int    motion1_duration;
    public int    motion2_duration;
    public Entity currentWeapon;
    public Entity currentShield;
    public Entity currentLight;
    public boolean boss;

    // ── Inventory ────────────────────────────────────────────────
    public ArrayList<Entity> inventory   = new ArrayList<>();
    public final int maxInventorySize    = 20;
    public int    attackValue;
    public int    defenseValue;
    public String description            = "";
    public int    useCost;
    public int    value;
    public int    price;
    public int    knockBackPower;
    public boolean stackable             = false;
    public int    amount                 = 1;
    public int    lightRadius;

    // ── Type constants ───────────────────────────────────────────
    public int type;
    public final int type_player     = 0;
    public final int type_npc        = 1;
    public final int type_monster    = 2;
    public final int type_sword      = 3;
    public final int type_axe        = 4;
    public final int type_shield     = 5;
    public final int type_consumable = 6;
    public final int type_pickupOnly = 7;
    public final int type_obstacle   = 8;
    public final int type_light      = 9;
    public final int type_pickaxe    = 10;

    public Entity(GamePanel gp) {
        this.gp = gp;
    }

    // ── Position helpers ─────────────────────────────────────────

    public int getScreenX()  { return worldX - gp.player.worldX + gp.player.screenX; }
    public int getScreenY()  { return worldY - gp.player.worldY + gp.player.screenY; }
    public int getLeftX()    { return worldX + solidArea.x; }
    public int getRightX()   { return worldX + solidArea.x + solidArea.width; }
    public int getTopY()     { return worldY + solidArea.y; }
    public int getBottomY()  { return worldY + solidArea.y + solidArea.height; }
    public int getCol()      { return (worldX + solidArea.x) / gp.tileSize; }
    public int getRow()      { return (worldY + solidArea.y) / gp.tileSize; }

    public int getCenterX() {
        return (down1 != null) ? worldX + down1.getWidth() / 2 : worldX + gp.tileSize / 2;
    }
    public int getCenterY() {
        return (down1 != null) ? worldY + down1.getHeight() / 2 : worldY + gp.tileSize / 2;
    }
    public int getXdistance(Entity target) { return Math.abs(getCenterX() - target.getCenterX()); }
    public int getYdistance(Entity target) { return Math.abs(getCenterY() - target.getCenterY()); }
    public int getTileDistance(Entity target) {
        return (getXdistance(target) + getYdistance(target)) / gp.tileSize;
    }
    public int getGoalCol(Entity target) { return (target.worldX + target.solidArea.x) / gp.tileSize; }
    public int getGoalRow(Entity target) { return (target.worldY + target.solidArea.y) / gp.tileSize; }

    // ── Reset ────────────────────────────────────────────────────

    public void resetCounter() {
        spriteCounter        = 0;
        actionLockCounter    = 0;
        invincibleCounter    = 0;
        shotAvailableCounter = 0;
        dyingCounter         = 0;
        hpBarCounter         = 0;
        knockBackCounter     = 0;
        guardCounter         = 0;
        offBalanceCounter    = 0;
    }

    // ── Overrideable methods ──────────────────────────────────────

    public void setDialogue()   {}
    public void setLoot(Entity loot) {}
    public void setAction()     {}
    public void move(String direction) {}
    public void damageReaction() {}

    public void speak() {
        if (dialogues[dialogueSet][dialogueIndex] == null) {
            dialogueIndex = 0;
        }
        gp.ui.currentDialogue = dialogues[dialogueSet][dialogueIndex];
        dialogueIndex++;
    }

    public void facePlayer() {
        switch (gp.player.direction) {
            case "up":    direction = "down";  break;
            case "down":  direction = "up";    break;
            case "left":  direction = "right"; break;
            case "right": direction = "left";  break;
        }
    }

    /**
     * RyiSnow startDialogue: sets game to dialogue state, assigns npc reference.
     */
    public void startDialogue(Entity entity, int setNum) {
        gp.gameState    = gp.dialogueState;
        gp.ui.npc       = entity;
        dialogueSet     = setNum;
        dialogueIndex   = 0;
    }

    public void interact() {}

    public boolean use(Entity entity) {
        return false;
    }

    public void checkDrop() {}

    public void dropItem(Entity droppedItem) {
        for (int i = 0; i < gp.obj[1].length; i++) {
            if (gp.obj[gp.currentMap][i] == null) {
                gp.obj[gp.currentMap][i]        = droppedItem;
                gp.obj[gp.currentMap][i].worldX = worldX;
                gp.obj[gp.currentMap][i].worldY = worldY;
                break;
            }
        }
    }

    // ── Particle colour (override in monsters) ───────────────────

    public Color getParticleColor()  { return null; }
    public int   getParticleSize()   { return 0; }
    public int   getParticleSpeed()  { return 0; }
    public int   getParticleMaxLife(){ return 0; }

    // ── Collision check helper ───────────────────────────────────

    public void checkCollision() {
        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this, false);
        gp.cChecker.checkEntity(this, gp.npc);
        gp.cChecker.checkEntity(this, gp.monster);

        boolean contactPlayer = gp.cChecker.checkPlayer(this);
        // No auto-damage: battles are Enter-key initiated only
    }

    // ── Update (RyiSnow exact pattern) ───────────────────────────

    public void update() {
        if (!sleep) {
            if (knockBack) {
                checkCollision();
                if (collisionOn) {
                    knockBackCounter = 0;
                    knockBack = false;
                    speed = defaultSpeed;
                } else {
                    switch (knockBackDirection) {
                        case "up":    worldY -= speed; break;
                        case "down":  worldY += speed; break;
                        case "left":  worldX -= speed; break;
                        case "right": worldX += speed; break;
                    }
                }
                knockBackCounter++;
                if (knockBackCounter == 10) {
                    knockBackCounter = 0;
                    knockBack = false;
                    speed = defaultSpeed;
                }
            } else if (attacking) {
                attacking();
            } else {
                setAction();
                checkCollision();
                if (!collisionOn) {
                    switch (direction) {
                        case "up":    worldY -= speed; break;
                        case "down":  worldY += speed; break;
                        case "left":  worldX -= speed; break;
                        case "right": worldX += speed; break;
                    }
                }
                spriteCounter++;
                if (spriteCounter > 24) {
                    spriteNum     = (spriteNum == 1) ? 2 : 1;
                    spriteCounter = 0;
                }
            }

            if (invincible) {
                invincibleCounter++;
                if (invincibleCounter > 40) {
                    invincible        = false;
                    invincibleCounter = 0;
                }
            }
            if (shotAvailableCounter < 30) shotAvailableCounter++;
            if (offBalance) {
                offBalanceCounter++;
                if (offBalanceCounter > 60) {
                    offBalance        = false;
                    offBalanceCounter = 0;
                }
            }
        }
    }

    // ── Attack helpers ───────────────────────────────────────────

    public void checkAttackOrNot(int rate, int straight, int horizontal) {
        boolean inRange = false;
        int xDis = getXdistance(gp.player);
        int yDis = getYdistance(gp.player);
        switch (direction) {
            case "up":    if (gp.player.getCenterY() < getCenterY()  && yDis < straight && xDis < horizontal) inRange = true; break;
            case "down":  if (gp.player.getCenterY() > getCenterY()  && yDis < straight && xDis < horizontal) inRange = true; break;
            case "left":  if (gp.player.getCenterX() < getCenterX()  && xDis < straight && yDis < horizontal) inRange = true; break;
            case "right": if (gp.player.getCenterX() > getCenterX()  && xDis < straight && yDis < horizontal) inRange = true; break;
        }
        if (inRange && new Random().nextInt(rate) == 0) {
            attacking     = true;
            spriteNum     = 1;
            spriteCounter = 0;
            shotAvailableCounter = 0;
        }
    }

    public void getRandomDirection(int interval) {
        actionLockCounter++;
        if (actionLockCounter > interval) {
            int i = new Random().nextInt(100) + 1;
            if (i <= 25)             direction = "up";
            else if (i <= 50)        direction = "down";
            else if (i <= 75)        direction = "left";
            else                     direction = "right";
            actionLockCounter = 0;
        }
    }

    public void moveTowardPlayer(int interval) {
        actionLockCounter++;
        if (actionLockCounter > interval) {
            if (getXdistance(gp.player) > getYdistance(gp.player))
                direction = (gp.player.getCenterX() < getCenterX()) ? "left" : "right";
            else
                direction = (gp.player.getCenterY() < getCenterY()) ? "up" : "down";
            actionLockCounter = 0;
        }
    }

    public String getOppositeDirection(String d) {
        switch (d) {
            case "up":    return "down";
            case "down":  return "up";
            case "left":  return "right";
            case "right": return "left";
        }
        return d;
    }

    public void attacking() {
        spriteCounter++;
        if (spriteCounter <= motion1_duration) {
            spriteNum = 1;
        }
        if (spriteCounter > motion1_duration && spriteCounter <= motion2_duration) {
            spriteNum = 2;

            int curWorldX        = worldX;
            int curWorldY        = worldY;
            int solidAreaWidth   = solidArea.width;
            int solidAreaHeight  = solidArea.height;

            switch (direction) {
                case "up":    worldY -= attackArea.height; break;
                case "down":  worldY += gp.tileSize;       break;
                case "left":  worldX -= attackArea.width;  break;
                case "right": worldX += gp.tileSize;       break;
            }
            solidArea.width  = attackArea.width;
            solidArea.height = attackArea.height;

            if (type == type_monster) {
                // Monsters don't auto-attack; quizzes handle combat
            } else {
                int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
                gp.player.damageMonster(monsterIndex, this, attack, knockBackPower);
            }

            worldX = curWorldX;
            worldY = curWorldY;
            solidArea.width  = solidAreaWidth;
            solidArea.height = solidAreaHeight;
        }
        if (spriteCounter > motion2_duration) {
            spriteNum     = 1;
            spriteCounter = 0;
            attacking     = false;
        }
    }

    public void damagePlayer(int atk) {
        if (!gp.player.invincible) {
            int damage = atk - gp.player.defense;
            if (damage < 1) damage = 1;
            gp.playSE(6);
            gp.player.life       -= damage;
            gp.player.invincible  = true;
            gp.player.transparent = true;
        }
    }

    public void setKnockBack(Entity target, Entity attacker, int power) {
        this.attacker          = attacker;
        target.knockBackDirection = attacker.direction;
        target.speed           += power;
        target.knockBack        = true;
    }

    // ── Camera culling ───────────────────────────────────────────

    public boolean inCamera() {
        return worldX + gp.tileSize * 5 > gp.player.worldX - gp.player.screenX
            && worldX - gp.tileSize     < gp.player.worldX + gp.player.screenX
            && worldY + gp.tileSize * 5 > gp.player.worldY - gp.player.screenY
            && worldY - gp.tileSize     < gp.player.worldY + gp.player.screenY;
    }

    // ── Draw (RyiSnow exact pattern) ─────────────────────────────

    public void draw(Graphics2D g2) {
        if (!inCamera()) return;

        BufferedImage image   = null;
        int tempScreenX       = getScreenX();
        int tempScreenY       = getScreenY();

        switch (direction) {
            case "up":
                image = (attacking && attackUp1 != null)
                      ? (spriteNum == 1 ? attackUp1 : attackUp2)
                      : (spriteNum == 1 ? up1 : up2);
                if (attacking && attackUp1 != null) tempScreenY = getScreenY() - gp.tileSize;
                break;
            case "down":
                image = (attacking && attackDown1 != null)
                      ? (spriteNum == 1 ? attackDown1 : attackDown2)
                      : (spriteNum == 1 ? down1 : down2);
                break;
            case "left":
                image = (attacking && attackLeft1 != null)
                      ? (spriteNum == 1 ? attackLeft1 : attackLeft2)
                      : (spriteNum == 1 ? left1 : left2);
                if (attacking && attackLeft1 != null) tempScreenX = getScreenX() - gp.tileSize;
                break;
            case "right":
                image = (attacking && attackRight1 != null)
                      ? (spriteNum == 1 ? attackRight1 : attackRight2)
                      : (spriteNum == 1 ? right1 : right2);
                break;
        }

        if (invincible) {
            hpBarOn      = true;
            hpBarCounter = 0;
            changeAlpha(g2, 0.4f);
        }
        if (dying) dyingAnimation(g2);

        if (image != null) {
            g2.drawImage(image, tempScreenX, tempScreenY, null);
        } else {
            // Fallback coloured rectangle
            g2.setColor(getFallbackColor());
            g2.fillRoundRect(tempScreenX + 4, tempScreenY + 4,
                             gp.tileSize - 8, gp.tileSize - 8, 10, 10);
            if (name != null) {
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 9));
                g2.drawString(name.length() > 5 ? name.substring(0,5) : name,
                              tempScreenX + 6, tempScreenY + gp.tileSize / 2 + 4);
            }
        }
        changeAlpha(g2, 1f);
    }

    public void dyingAnimation(Graphics2D g2) {
        dyingCounter++;
        int i = 5;
        if      (dyingCounter <= i)     changeAlpha(g2, 0f);
        else if (dyingCounter <= i * 2) changeAlpha(g2, 1f);
        else if (dyingCounter <= i * 3) changeAlpha(g2, 0f);
        else if (dyingCounter <= i * 4) changeAlpha(g2, 1f);
        else if (dyingCounter <= i * 5) changeAlpha(g2, 0f);
        else if (dyingCounter <= i * 6) changeAlpha(g2, 1f);
        else if (dyingCounter <= i * 7) changeAlpha(g2, 0f);
        else if (dyingCounter <= i * 8) changeAlpha(g2, 1f);
        else alive = false;
    }

    public void changeAlpha(Graphics2D g2, float alpha) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
    }

    /** RyiSnow setup: load PNG from classpath, scale to size. Falls back to colored rect. */
    public BufferedImage setup(String imagePath, int width, int height) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            if (image != null)
                image = uTool.scaleImage(image, width, height);
        } catch (IOException | IllegalArgumentException e) {
            // Image not found - return null, draw() will use fallback
        }
        return image;
    }

    protected Color getFallbackColor() { return new Color(200, 100, 200); }

    public int getDetected(Entity user, Entity[][] target, String targetName) {
        int index = 999;
        int nextWorldX = user.getLeftX();
        int nextWorldY = user.getTopY();
        switch (user.direction) {
            case "up":    nextWorldY = user.getTopY()    - gp.player.speed; break;
            case "down":  nextWorldY = user.getBottomY() + gp.player.speed; break;
            case "left":  nextWorldX = user.getLeftX()   - gp.player.speed; break;
            case "right": nextWorldX = user.getRightX()  + gp.player.speed; break;
        }
        int col = nextWorldX / gp.tileSize;
        int row = nextWorldY / gp.tileSize;
        for (int i = 0; i < target[1].length; i++) {
            if (target[gp.currentMap][i] != null
             && target[gp.currentMap][i].getCol() == col
             && target[gp.currentMap][i].getRow() == row
             && target[gp.currentMap][i].name.equals(targetName)) {
                index = i;
                break;
            }
        }
        return index;
    }
}
