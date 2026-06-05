package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import progress.*;

/**
 * UI - draws all screens: title, HUD, dialogue, quiz, shop, endings, scores.
 *
 * Created by: Aezekiel
 * Tested by: Habib
 */
public class UI {

 GamePanel gp;
 public Graphics2D g2;
 Font maruMonica;
 Font titleFont;

 // Dialogue
 public String currentDialogue = "";
 public entity.Entity npc;

 // Menu navigation
 public int commandNum = 0;
 public int titleScreenState = 0; // 0=main, 1=howtoplay

 // Messages (floating)
 ArrayList<String> message = new ArrayList<>();
 ArrayList<Integer> messageCounter = new ArrayList<>();

 // Screen shake
 public int screenShakeCounter = 0;

 // Level up flash
 public int levelUpFlashCounter = 0;

 // Center toast notification
 public String toastMessage = "";
 public int toastCounter = 0;
 private ArrayList<String> toastQueue = new ArrayList<>();

 // Transition counter
 int counter = 0;

 // Prologue cutscene
 public int prologueLine = 0;
 public int prologueTimer = 0;
 private final String[] PROLOGUE = {
 "Another long day has ended.",
 "Jeff slowly closes his eyes...",
 "As sleep takes over, he finds himself",
 "pulled into a strange world.",
 "",
 "...",
 "",
 "A bright light appears.",
 "Jeff wakes up in an unfamiliar kingdom.",
 "",
 "The kingdom is called Lucienne.",
 "",
 "The environment looks beautiful",
 "but strangely quiet.",
 "Many buildings appear abandoned.",
 "Knowledge seems to have disappeared",
 "from the people.",
 "",
 "Jeff must find answers.",
 "",
 "[Press Enter to continue]"
 };

 public int getPrologueLineCount() { return PROLOGUE.length; }


 // Shop
 private final String[] SHOP_ITEMS = {"Knowledge Potion","Mana Potion","Memory Charm"};
 private final int[] SHOP_PRICES = {20, 15, 25};
 private final String[] SHOP_DESCS = {"Reveals quiz hint","Restores 3 HP","Bonus XP after battle"};
 private String shopMsg = "";
 private int shopMsgTimer = 0;

 // Ending choice
 private final String[] ENDING_CHOICES = {"SHARE","KEEP","FORGIVE"};
 private final String[] ENDING_DESCS = {
 "Share knowledge with everyone",
 "Keep the Knowledge Crystal safe",
 "Try to understand and forgive Shona"
 };

 public String[] getAvailableEndingChoices() {
 if (gp.player.hasFoundSheenaMemory) return ENDING_CHOICES;
 return new String[]{"SHARE", "KEEP"};
 }
 public String[] getAvailableEndingDescs() {
 if (gp.player.hasFoundSheenaMemory) return ENDING_DESCS;
 return new String[]{"Share knowledge with everyone", "Keep the Knowledge Crystal safe"};
 }

 // Score / ending helpers
 ScoreStorage scoreStorage = new ScoreStorage();

 // Title screen wizard image
 BufferedImage titleWizardImage;
 EndingManager endingManager = new EndingManager();
 String scoreText = "Loading...";
 String currentEnding = "NORMAL_ENDING";

 public UI(GamePanel gp) {
 this.gp = gp;
 maruMonica = new Font("Arial", Font.PLAIN, 16);
 titleFont = new Font("Arial", Font.BOLD, 22);
 try {
 java.io.InputStream is = getClass().getResourceAsStream("/font/x12y16pxMaruMonica.ttf");
 if (is != null) {
 maruMonica = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(16f);
 titleFont = maruMonica.deriveFont(Font.BOLD, 22f);
 }
 } catch (Exception e) { /* use default Arial */ }

 // Load wizard image for title screen
 try {
 java.io.InputStream wizIs = getClass().getResourceAsStream("/player_title.png");
 if (wizIs != null) titleWizardImage = ImageIO.read(wizIs);
 } catch (Exception e) { /* fallback to player sprite */ }
 }

 // Main draw dispatcher - pattern
 public void draw(Graphics2D g2) {
 this.g2 = g2;
 g2.setFont(maruMonica);
 g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
 RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
 g2.setColor(Color.white);

 if (gp.gameState == gp.titleState) { drawTitleScreen(); return; }
 if (gp.gameState == gp.prologueState) { drawPrologueScreen(); return; }

 if (gp.gameState == gp.playState) { drawPlayerLife(); drawMessage(); drawEffects(); }
 if (gp.gameState == gp.pauseState) { drawPlayerLife(); drawPauseScreen(); }
 if (gp.gameState == gp.dialogueState) { drawDialogueScreen(); }
 if (gp.gameState == gp.optionsState) { drawOptionsScreen(); }
 if (gp.gameState == gp.gameOverState) { drawGameOverScreen(); }
 if (gp.gameState == gp.transitionState) { drawTransition(); }
 if (gp.gameState == gp.quizState) { drawQuizScreen(); }
 if (gp.gameState == gp.learningState) { drawLearningScreen(); }
 if (gp.gameState == gp.shopState) { drawShopScreen(); }
 if (gp.gameState == gp.endingChoiceState) { drawEndingChoiceScreen(); }
 if (gp.gameState == gp.endingState) { drawEndingScreen(); }
 if (gp.gameState == gp.scoreState) { drawScoreScreen(); }
 }

 // Title Screen
 public void drawTitleScreen() {
 g2.setColor(new Color(0, 0, 0));
 g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

 if (titleScreenState == 1) { drawHowToPlay(); return; }

 // Stars
 g2.setColor(Color.WHITE);
 for (int i = 0; i < 50; i++)
 g2.fillOval((i*53+7)%gp.screenWidth, (i*37+11)%(gp.screenHeight/2), 2, 2);

 // Title
 g2.setFont(titleFont.deriveFont(Font.BOLD, 20f));
 g2.setColor(new Color(255, 215, 0));
 String title = "Lucienne: Quest for Quality Education";
 g2.drawString(title, getXforCenteredText(title), 65);
 g2.setFont(maruMonica.deriveFont(13f));
 g2.setColor(new Color(180, 220, 255));
 String sub = "SDG 4: Quality Education for All";
 g2.drawString(sub, getXforCenteredText(sub), 90);

 // Player image - show wizard on title
 if (titleWizardImage != null) {
 // Scale wizard to fit nicely - about 120px tall
 int wizH = 120;
 int wizW = (int)(titleWizardImage.getWidth() * (120.0 / titleWizardImage.getHeight()));
 int ix = gp.screenWidth / 2 - wizW / 2;
 g2.drawImage(titleWizardImage, ix, 100, wizW, wizH, null);
 } else if (gp.player.down1 != null) {
 int ix = gp.screenWidth/2 - gp.tileSize;
 g2.drawImage(gp.player.down1, ix, 105, gp.tileSize*2, gp.tileSize*2, null);
 }

 // Menu items
 g2.setFont(maruMonica.deriveFont(Font.BOLD, 18f));
 String[] opts = {"NEW GAME","CONTINUE","HOW TO PLAY","VIEW SCORES","QUIT"};
 int startY = 230;
 for (int i = 0; i < opts.length; i++) {
 g2.setColor(commandNum == i ? new Color(255,215,0) : Color.white);
 g2.drawString(opts[i], getXforCenteredText(opts[i]), startY + i*35);
 if (commandNum == i) g2.drawString(">", getXforCenteredText(opts[i]) - 30, startY + i*35);
 }

 // High score at bottom
 g2.setFont(maruMonica.deriveFont(13f));
 g2.setColor(new Color(150, 200, 150));
 String hs = scoreStorage.displayHighScore();
 g2.drawString(hs, getXforCenteredText(hs), gp.screenHeight - 30);
 }

 private void drawHowToPlay() {
 drawSubWindow(20, 20, gp.screenWidth-40, gp.screenHeight-40);
 g2.setFont(titleFont.deriveFont(Font.BOLD, 16f));
 g2.setColor(new Color(255,215,0));
 g2.drawString("HOW TO PLAY", getXforCenteredText("HOW TO PLAY"), 55);
 g2.setFont(maruMonica.deriveFont(14f));
 g2.setColor(Color.WHITE);
 String[] lines = {
 "WASD / Arrow Keys = Move",
 "Enter = Interact / Attack",
 "Space = Guard",
 "P = Pause | Esc = Options",
 "",
 "Talk to NPCs to learn about SDG 4.",
 "Collect Knowledge Scrolls (yellow objects).",
 "Battle quiz enemies - answer correctly!",
 "Correct = +15 XP, +10 KP, enemy damaged",
 "Wrong = -1 HP",
 "Earn 70 KP or 7 Scrolls to unlock castle.",
 "Defeat Miss Shona to unlock the ending.",
 "",
 "Press Enter or Esc to return"
 };
 int y = 90;
 for (String l : lines) { g2.drawString(l, getXforCenteredText(l), y); y += 22; }
 }

 public void drawPrologueScreen() {
 g2.setColor(new Color(0, 0, 0));
 g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
 g2.setColor(new Color(255, 255, 255, 80));
 for (int i = 0; i < 30; i++) {
 int sx = (i * 47 + prologueTimer * 2 + 13) % gp.screenWidth;
 int sy = (i * 31 + 7) % gp.screenHeight;
 g2.fillOval(sx, sy, 2, 2);
 }
 prologueTimer++;
 g2.setFont(titleFont.deriveFont(Font.BOLD, 16f));
 g2.setColor(new Color(100, 150, 255));
 String title = "Lucienne: Quest for Quality Education";
 g2.drawString(title, getXforCenteredText(title), 50);
 g2.setFont(maruMonica.deriveFont(15f));
 int startLine = Math.max(0, prologueLine - 8);
 int y = 100;
 for (int i = startLine; i <= prologueLine && i < PROLOGUE.length; i++) {
 int age = prologueLine - i;
 int alpha = Math.max(60, 255 - age * 30);
 g2.setColor(i == prologueLine ? new Color(255, 255, 255, alpha) : new Color(180, 180, 200, alpha));
 String line = PROLOGUE[i];
 g2.drawString(line, getXforCenteredText(line), y);
 y += 30;
 }
 g2.setFont(maruMonica.deriveFont(12f));
 g2.setColor(new Color(150, 150, 150));
 String footer = "Enter/Space = next | Esc = skip";
 g2.drawString(footer, getXforCenteredText(footer), gp.screenHeight - 30);
 }
 public void drawPlayerLife() {
 int x = 8, y = 8;
 // Draw hearts
 for (int i = 0; i < gp.player.maxLife/2; i++) {
 g2.setColor(new Color(100,0,0)); g2.fillRect(x+i*18, y, 14,12);
 g2.setColor(Color.RED); g2.drawRect(x+i*18, y, 14,12);
 }
 for (int i = 0; i < gp.player.life/2; i++) {
 g2.setColor(Color.RED); g2.fillRect(x+i*18, y, 14,12);
 }
 if (gp.player.life % 2 == 1) {
 int i = gp.player.life/2;
 g2.setColor(Color.RED); g2.fillRect(x+i*18, y, 7,12);
 }
 // Stats text
 int sy = y + 16;
 g2.setFont(maruMonica.deriveFont(11f));
 g2.setColor(new Color(0,0,0,180));
 g2.fillRoundRect(x-2, sy, 180, 14, 4, 4);
 g2.setColor(new Color(255,220,100));
 g2.drawString("Lv:" + gp.player.level + " XP:" + gp.player.exp
 + " KP:" + gp.player.knowledgePoints + " Sc:" + gp.player.scrollsCompleted, x+2, sy+11);

 // KP Progress bar (compact)
 int barY = sy + 18;
 int barW = 120, barH = 6;
 int kpProgress = Math.min(gp.player.knowledgePoints, 70);
 int filledW = (int)((kpProgress / 70.0) * barW);
 g2.setColor(new Color(0,0,0,160));
 g2.fillRoundRect(x, barY, barW, barH, 3, 3);
 g2.setColor(kpProgress >= 70 ? new Color(100,255,100) : new Color(80,180,255));
 g2.fillRoundRect(x, barY, filledW, barH, 3, 3);
 g2.setFont(maruMonica.deriveFont(9f));
 g2.setColor(new Color(200,200,200));
 g2.drawString(kpProgress + "/70", x + barW + 4, barY + 6);

 // Scroll dots (compact)
 int scrollY = barY + 10;
 g2.setFont(maruMonica.deriveFont(9f));
 g2.setColor(new Color(255,220,100));
 StringBuilder sb = new StringBuilder();
 for (int i = 0; i < 10; i++) sb.append(i < gp.player.scrollsCompleted ? "\u25CF" : "\u25CB");
 g2.drawString(sb.toString(), x, scrollY + 8);
 }

 // draws the floating message log on the left
    public void drawMessage() {
 int mx = 10, my = gp.screenHeight / 2 - 60;
 g2.setFont(maruMonica.deriveFont(Font.BOLD, 15f));
 for (int i = 0; i < message.size(); i++) {
 if (message.get(i) != null) {
 int counter = messageCounter.get(i);
 float alpha = Math.max(0, 1f - (counter / 480f));
 int floatOffset = counter / 12;

 int textW = g2.getFontMetrics().stringWidth(message.get(i));
 g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha * 0.7f));
 g2.setColor(new Color(0, 0, 0));
 g2.fillRoundRect(mx - 4, my - floatOffset - 14, textW + 12, 20, 6, 6);

 g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
 g2.setColor(new Color(255, 255, 100));
 g2.drawString(message.get(i), mx + 2, my - floatOffset);

 g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
 messageCounter.set(i, counter + 1);
 my += 26;
 if (counter > 480) { message.remove(i); messageCounter.remove(i); i--; }
 }
 }
 }

 /** Draw screen shake and level-up flash effects */
 public void drawEffects() {
 // Level-up flash (white overlay that fades)
 if (levelUpFlashCounter > 0) {
 int alpha = Math.min(180, levelUpFlashCounter * 12);
 g2.setColor(new Color(255, 255, 255, alpha));
 g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
 levelUpFlashCounter--;
 }
 // Center toast notification
 if (toastCounter > 0) {
 float alpha = Math.min(1f, toastCounter / 30f); // fade in first 0.5s
 if (toastCounter < 60) alpha = toastCounter / 60f; // fade out last 1s
 g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha * 0.85f));
 g2.setFont(maruMonica.deriveFont(Font.BOLD, 16f));
 int tw = g2.getFontMetrics().stringWidth(toastMessage);
 int tx = gp.screenWidth / 2 - tw / 2;
 int ty = gp.screenHeight / 2;
 g2.setColor(new Color(0, 0, 0));
 g2.fillRoundRect(tx - 15, ty - 20, tw + 30, 32, 12, 12);
 g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
 g2.setColor(new Color(255, 215, 0));
 g2.drawString(toastMessage, tx, ty);
 g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
 toastCounter--;
 if (toastCounter <= 0 && !toastQueue.isEmpty()) {
 toastMessage = toastQueue.remove(0);
 toastCounter = 240;
 }
 }
 }

 public void showToast(String msg) {
 if (toastCounter > 0) {
 toastQueue.add(msg);
 } else {
 toastMessage = msg;
 toastCounter = 240;
 }
 }

 public void addMessage(String text) {
 message.add(text);
 messageCounter.add(0);
 }

 // Pause
 public void drawPauseScreen() {
 drawSubWindow(gp.screenWidth/2-110, 70, 220, 200);
 g2.setFont(titleFont.deriveFont(20f));
 g2.setColor(Color.WHITE);
 g2.drawString("PAUSED", getXforCenteredText("PAUSED"), 110);
 g2.setFont(maruMonica.deriveFont(14f));
 g2.setColor(new Color(200,200,200));
 g2.drawString("Press P to resume", getXforCenteredText("Press P to resume"), 150);
 g2.drawString("Press Esc for options", getXforCenteredText("Press Esc for options"), 175);
 }

 // Dialogue - pattern
 public void drawDialogueScreen() {
 int x = gp.tileSize, y = gp.tileSize*7;
 int w = gp.screenWidth - gp.tileSize*2;
 int h = gp.tileSize*3;
 drawSubWindow(x, y, w, h);

 g2.setFont(maruMonica.deriveFont(14f));
 g2.setColor(Color.white);

 // Get current dialogue line from NPC's 2D array
 if (npc != null && npc.dialogues[npc.dialogueSet][npc.dialogueIndex] != null) {
 String line = npc.dialogues[npc.dialogueSet][npc.dialogueIndex];
 for (String l : line.split("\n")) {
 g2.drawString(l, x+20, y+40);
 y += 20;
 }
 } else if (!currentDialogue.isEmpty()) {
 g2.drawString(currentDialogue, x+20, y+40);
 }

 g2.setFont(maruMonica.deriveFont(11f));
 g2.setColor(new Color(180,180,180));
 g2.drawString("Enter/Space - next | Esc - close",
 x+10, gp.tileSize*7 + h - 10);
 }

 // Options
 public void drawOptionsScreen() {
 drawSubWindow(gp.screenWidth/2-120, 50, 240, 280);
 g2.setFont(titleFont.deriveFont(18f)); g2.setColor(Color.white);
 g2.drawString("Options", getXforCenteredText("Options"), 85);
 g2.setFont(maruMonica.deriveFont(15f));
 String[] opts = {"Music Vol +","Music Vol -","SE Vol +","SE Vol -","End Game"};
 for (int i = 0; i < opts.length; i++) {
 g2.setColor(commandNum == i ? new Color(255,215,0) : Color.white);
 g2.drawString(opts[i], gp.screenWidth/2-80, 130+i*40);
 if (commandNum == i) g2.drawString(">", gp.screenWidth/2-105, 130+i*40);
 }
 g2.setFont(maruMonica.deriveFont(12f)); g2.setColor(new Color(180,180,180));
 g2.drawString("Esc - back to game", getXforCenteredText("Esc - back to game"), 345);
 }

 // Game Over
 public void drawGameOverScreen() {
 g2.setColor(new Color(0,0,0,150)); g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);
 g2.setFont(titleFont.deriveFont(Font.BOLD, 60f));
 g2.setColor(Color.BLACK); g2.drawString("Game Over", getXforCenteredText("Game Over"), 200);
 g2.setColor(Color.WHITE); g2.drawString("Game Over", getXforCenteredText("Game Over")-4, 196);
 g2.setFont(maruMonica.deriveFont(24f));
 g2.setColor(commandNum==0 ? new Color(255,215,0) : Color.white);
 g2.drawString("Retry", getXforCenteredText("Retry"), 280);
 if (commandNum==0) g2.drawString(">", getXforCenteredText("Retry")-30, 280);
 g2.setColor(commandNum==1 ? new Color(255,215,0) : Color.white);
 g2.drawString("Quit", getXforCenteredText("Quit"), 330);
 if (commandNum==1) g2.drawString(">", getXforCenteredText("Quit")-30, 330);
 }

 // Transition - map change fade
 public void drawTransition() {
 counter++;
 g2.setColor(new Color(0,0,0, Math.min(255, counter*5)));
 g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);
 if (counter == 50) {
 counter = 0;
 gp.gameState = gp.playState;
 gp.player.worldX = gp.tileSize * gp.eHandler.tempCol;
 gp.player.worldY = gp.tileSize * gp.eHandler.tempRow;
 gp.currentMap = gp.eHandler.tempMap;
 }
 }

 // Quiz Screen
 public void drawQuizScreen() {
 gp.quizManager.drawQuiz(g2, gp.screenWidth, gp.screenHeight,
 titleFont, maruMonica, "");
 }

 // Learning Screen
 public void drawLearningScreen() {
 g2.setColor(new Color(10,30,20)); g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);
 g2.setColor(new Color(80,180,80)); g2.setStroke(new BasicStroke(3));
 g2.drawRoundRect(10,10,gp.screenWidth-20,gp.screenHeight-20,16,16);
 g2.setStroke(new BasicStroke(1));

 learning.LearningPage page = gp.learningManager.getPage(gp.currentScrollIndex);
 if (page == null) { gp.gameState = gp.playState; return; }

 g2.setFont(titleFont.deriveFont(Font.BOLD,16f)); g2.setColor(new Color(100,255,100));
 String hdr = "Knowledge Scroll " + (gp.currentScrollIndex+1) + "/10";
 g2.drawString(hdr, getXforCenteredText(hdr), 42);

 g2.setFont(maruMonica.deriveFont(Font.BOLD,15f)); g2.setColor(new Color(255,220,100));
 g2.drawString(page.getTitle(), getXforCenteredText(page.getTitle()), 68);

 g2.setFont(maruMonica.deriveFont(14f)); g2.setColor(Color.WHITE);
 int y = 100;
 for (String line : page.getContent().split("\n")) {
 g2.drawString(line.trim(), getXforCenteredText(line.trim()), y); y += 25;
 }

 int comp = gp.learningManager.getCompletedCount();
 g2.setFont(maruMonica.deriveFont(13f)); g2.setColor(new Color(100,200,100));
 String cstr = "Scrolls: " + comp + "/10";
 g2.drawString(cstr, getXforCenteredText(cstr), gp.screenHeight-68);

 g2.setColor(new Color(180,180,180));
 String done = page.isCompleted() ? "[Already completed]"
 : "Enter/Space = complete (+10 KP)";
 g2.drawString(done, getXforCenteredText(done), gp.screenHeight-45);
 g2.drawString("Esc = close", getXforCenteredText("Esc = close"), gp.screenHeight-22);
 }

 // Shop Screen
 public void drawShopScreen() {
 drawSubWindow(20, 30, gp.screenWidth-40, gp.screenHeight-60);
 g2.setFont(titleFont.deriveFont(Font.BOLD,18f)); g2.setColor(new Color(255,215,0));
 g2.drawString("Lucienne Shop", getXforCenteredText("Lucienne Shop"), 65);
 g2.setFont(maruMonica.deriveFont(14f)); g2.setColor(new Color(100,220,255));
 String kpStr = "Knowledge Points: " + gp.player.knowledgePoints;
 g2.drawString(kpStr, getXforCenteredText(kpStr), 95);

 int sy = 130;
 for (int i = 0; i < SHOP_ITEMS.length; i++) {
 String label = SHOP_ITEMS[i] + " [" + SHOP_PRICES[i] + " KP]";
 g2.setColor(commandNum == i ? new Color(255,215,0) : Color.white);
 g2.setFont(commandNum == i ? maruMonica.deriveFont(Font.BOLD,15f) : maruMonica.deriveFont(15f));
 g2.drawString((commandNum==i?"> ":" ") + label, 40, sy + i*45);
 // Show description for selected item
 if (commandNum == i) { g2.setFont(maruMonica.deriveFont(12f)); g2.setColor(new Color(200,200,100)); g2.drawString(" -> " + SHOP_DESCS[i], 50, sy + i*45 + 16); }
 }

 if (shopMsgTimer > 0) {
 g2.setFont(maruMonica.deriveFont(13f)); g2.setColor(new Color(100,255,100));
 g2.drawString(shopMsg, getXforCenteredText(shopMsg), gp.screenHeight-85);
 shopMsgTimer--;
 }
 g2.setFont(maruMonica.deriveFont(12f)); g2.setColor(new Color(180,180,180));
 g2.drawString("W/S=navigate Enter=buy Esc=exit",
 getXforCenteredText("W/S=navigate Enter=buy Esc=exit"), gp.screenHeight-50);
 }

 public void buyShopItem() {
 int price = SHOP_PRICES[commandNum];
 if (gp.player.knowledgePoints < price) {
 shopMsg = "Not enough Knowledge Points!"; shopMsgTimer = 120; return;
 }
 gp.player.knowledgePoints -= price;
 switch (commandNum) {
 case 0: gp.player.hintCount++; addMessage("Hint ready! Use during quiz."); gp.playSE(3); break;
 case 1: gp.player.life = Math.min(gp.player.life+3, gp.player.maxLife);
 addMessage("+3 HP restored!"); gp.playSE(2); break;
 case 2: gp.player.hasMemoryCharm = true;
 addMessage("Memory Charm active! Bonus XP after battle."); gp.playSE(3); break;
 }
 shopMsg = "Bought: " + SHOP_ITEMS[commandNum]; shopMsgTimer = 120;
 }

 // Ending Choice
 public void drawEndingChoiceScreen() {
 g2.setColor(new Color(10,10,30)); g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);
 g2.setFont(titleFont.deriveFont(Font.BOLD,18f)); g2.setColor(new Color(255,215,0));
 g2.drawString("The Knowledge Crystal awaits...",
 getXforCenteredText("The Knowledge Crystal awaits..."), 65);
 g2.setFont(maruMonica.deriveFont(14f)); g2.setColor(new Color(200,200,255));
 g2.drawString("What will you do with it?", getXforCenteredText("What will you do with it?"), 92);

 String[] choices = getAvailableEndingChoices();
 String[] descs = getAvailableEndingDescs();
 int maxCmd = choices.length - 1;
 if (commandNum > maxCmd) commandNum = maxCmd;

 int sy = 140;
 for (int i = 0; i < choices.length; i++) {
 g2.setColor(commandNum==i ? new Color(255,215,0) : Color.white);
 g2.setFont(commandNum==i ? maruMonica.deriveFont(Font.BOLD,16f) : maruMonica.deriveFont(15f));
 g2.drawString((commandNum==i?"> ":" ") + choices[i], getXforCenteredText(choices[i])-10, sy+i*50);
 g2.setFont(maruMonica.deriveFont(12f)); g2.setColor(new Color(180,180,180));
 g2.drawString(descs[i], getXforCenteredText(descs[i]), sy+i*50+18);
 }
 g2.setFont(maruMonica.deriveFont(12f)); g2.setColor(new Color(150,150,150));
 g2.drawString("W/S=choose Enter=confirm", getXforCenteredText("W/S=choose Enter=confirm"), gp.screenHeight-30);
 }

 // Ending Screen
 public void drawEndingScreen() {
 GameProgress progress = new GameProgress();
 progress.syncFromPlayer(gp.player);
 progress.setEndingType(gp.currentEnding);
 int score = calcFinalScore();
 progress.setFinalScore(score);
 endingManager.drawEnding(g2, progress, gp.screenWidth, gp.screenHeight, titleFont, maruMonica);
 }

 public int calcFinalScore() {
 int s = gp.player.knowledgePoints
 + gp.player.scrollsCompleted * 5
 + gp.player.enemiesDefeated * 10
 + gp.player.level * 5
 + gp.quizManager.getScore();
 return Math.min(s, 100);
 }

 public void saveCurrentScore() {
 try {
 GameProgress progress = new GameProgress();
 progress.syncFromPlayer(gp.player);
 progress.setEndingType(gp.currentEnding);
 progress.setFinalScore(calcFinalScore());
 scoreStorage.saveScore(progress);
 addMessage("Score saved!");
 } catch (ScoreFileException e) {
 addMessage("Save failed: " + e.getMessage());
 }
 }

 // Score Screen
 public void drawScoreScreen() {
 drawSubWindow(10,10,gp.screenWidth-20,gp.screenHeight-20);
 g2.setFont(titleFont.deriveFont(Font.BOLD,18f)); g2.setColor(new Color(255,215,0));
 g2.drawString("Score Records", getXforCenteredText("Score Records"), 45);
 g2.setFont(maruMonica.deriveFont(12f)); g2.setColor(Color.WHITE);
 int y = 75;
 for (String line : scoreText.split("\n")) {
 if (y < gp.screenHeight-40) { g2.drawString(line, 25, y); y += 19; }
 }
 g2.setColor(new Color(180,180,180));
 g2.drawString("Press Esc to return", getXforCenteredText("Press Esc to return"), gp.screenHeight-20);
 }

 public void loadScoreData() {
 try { scoreText = scoreStorage.loadScores(); }
 catch (ScoreFileException e) { scoreText = "Error loading scores."; }
 }

 // Sub window - helpers
 public void drawSubWindow(int x, int y, int width, int height) {
 g2.setColor(new Color(0,0,0,210)); g2.fillRoundRect(x,y,width,height,35,35);
 g2.setColor(new Color(255,255,255)); g2.setStroke(new BasicStroke(5));
 g2.drawRoundRect(x+5,y+5,width-10,height-10,25,25);
 g2.setStroke(new BasicStroke(1));
 }

 public int getXforCenteredText(String text) {
 if (g2 == null) return 0;
 return gp.screenWidth/2 - (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth()/2;
 }
}
