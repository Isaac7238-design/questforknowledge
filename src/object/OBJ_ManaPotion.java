package object;

import entity.Entity;
import entity.Player;
import java.awt.Color;
import main.GamePanel;

/**
 * OBJ_ManaPotion - restores 3 life (1.5 hearts).
 * Stackable consumable.
 *
 * Created by: Habib
 * Tested by: Aezekiel
 * Desc: Consumable item that restores player health during gameplay.
 */
public class OBJ_ManaPotion extends Entity {

 public OBJ_ManaPotion(GamePanel gp) {
 super(gp);
 name = "Mana Potion";
 type = type_consumable;
 stackable = true;
 price = 15; // cost in Knowledge Points
 description = "Restores 3 HP.\nUseful after tough battles.";
 value = 3;

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
 p.life = Math.min(p.life + value, p.maxLife);
 gp.ui.addMessage("Restored " + value + " HP!");
 gp.playSE(2);
 return true;
 }
 return false;
 }

 @Override
 protected Color getFallbackColor() { return new Color(100, 255, 150); }
}
