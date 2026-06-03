package object;

import entity.Entity;
import entity.Player;
import java.awt.Color;
import main.GamePanel;

/**
 * OBJ_MemoryCharm - gives bonus XP after the next battle.
 * Pickup-only item found in the world.
 *
 * Created by: Habib
 * Tested by: Aezekiel
 * Desc: Pickup item that grants bonus XP reward after the next quiz battle.
 */
public class OBJ_MemoryCharm extends Entity {

 public OBJ_MemoryCharm(GamePanel gp) {
 super(gp);
 name = "Memory Charm";
 type = type_pickupOnly;
 price = 25; // shop cost in KP
 description = "Grants bonus XP\nafter your next battle.";

 down1 = setup("/objects/boots", gp.tileSize, gp.tileSize);
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
 p.hasMemoryCharm = true;
 gp.ui.addMessage("Memory Charm! Bonus XP after next battle.");
 gp.playSE(3);
 return true;
 }
 return false;
 }

 @Override
 protected Color getFallbackColor() { return new Color(255, 150, 220); }
}
