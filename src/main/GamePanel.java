package main;

import entity.*;
import quiz.QuizManager;
import learning.LearningManager;
import progress.*;
import tile.TileManager;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * GamePanel - core game class.
 * Uses tempScreen + drawToTempScreen/drawToScreen pattern.
 * Entity arrays are 2D [map][index]. Entity list is Y-sorted for draw order.
 *
 * Created by: Aezekiel
 * Tested by: Habib
 */
public class GamePanel extends JPanel implements Runnable {

 // Screen settings
 final int originalTileSize = 16;
 final int scale = 3;

 public final int tileSize = originalTileSize * scale; // 48
 public final int maxScreenCol = 16;
 public final int maxScreenRow = 12;
 public final int screenWidth = tileSize * maxScreenCol; // 768
 public final int screenHeight = tileSize * maxScreenRow; // 576

 // World settings
 public int maxWorldCol;
 public int maxWorldRow;
 public final int maxMap = 10;
 public int currentMap = 0;

 // Fullscreen
 int screenWidth2 = screenWidth;
 int screenHeight2 = screenHeight;
 BufferedImage tempScreen;
 Graphics2D g2;
 public boolean fullScreenOn = false;

 // FPS
 int FPS = 60;

 // Game states
 public int gameState;
 public final int titleState = 0;
 public final int playState = 1;
 public final int pauseState = 2;
 public final int dialogueState = 3;
 public final int optionsState = 5;
 public final int gameOverState = 6;
 public final int transitionState = 7;
 public final int quizState = 8;
 public final int learningState = 9;
 public final int shopState = 10;
 public final int endingChoiceState = 11;
 public final int endingState = 12;
 public final int scoreState = 13;
 public final int prologueState = 14;

 // Subsystems
 public TileManager tileM;
 public KeyHandler keyH;
 public CollisionChecker cChecker;
 public AssetSetter aSetter;
 public EventHandler eHandler;
 public UI ui;
 public Sound music = new Sound();
 public Sound se = new Sound();
 public QuizManager quizManager;
 public LearningManager learningManager;
 public EndingManager endingManager = new EndingManager();

 // Entities
 public Player player;
 public Entity[][] npc = new Entity[maxMap][10];
 public Entity[][] monster = new Entity[maxMap][10];
 public Entity[][] obj = new Entity[maxMap][20];
 ArrayList<Entity> entityList = new ArrayList<>();

 // Shared context
 public int currentEnemyIndex = -1;
 public int currentScrollIndex = 0;
 public String currentEnding = "NORMAL_ENDING";

 Thread gameThread;

 // Constructor
 public GamePanel() {
 this.setPreferredSize(new Dimension(screenWidth, screenHeight));
 this.setBackground(Color.black);
 this.setDoubleBuffered(true);
 this.setFocusable(true);

 keyH = new KeyHandler(this);
 this.addKeyListener(keyH);

 tileM = new TileManager(this);
 cChecker = new CollisionChecker(this);
 quizManager = new QuizManager();
 learningManager= new LearningManager();
 ui = new UI(this);
 player = new Player(this, keyH);
 }

 // Setup
 public void setupGame() {
 aSetter = new AssetSetter(this);
 eHandler = new EventHandler(this);
 aSetter.setObject();
 aSetter.setNPC();
 aSetter.setMonster();
 quizManager.resetQuiz();
 currentEnding = "NORMAL_ENDING";
 // gameState is set by the caller (titleState on init, playState on New Game)
 tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
 g2 = (Graphics2D) tempScreen.getGraphics();
 g2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
 if (fullScreenOn) setFullScreen();
 }

 public void resetGame(boolean restart) {
 stopMusic();
 eHandler.resetEvents();
 player.setDefaultPositions();
 player.restoreStatus();
 aSetter.setMonster();
 aSetter.setNPC();
 player.resetCounter();
 currentEnemyIndex = -1;
 if (restart) {
 player.setDefaultValues();
 aSetter.setObject();
 quizManager.resetQuiz();
 learningManager = new LearningManager();
 currentEnding = "NORMAL_ENDING";
 // Reload the map to reset any opened gates/doors
 tileM.loadMap("/maps/worldmap.txt", 0);
 }
 }

 public void setFullScreen() {
 GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
 GraphicsDevice gd = ge.getDefaultScreenDevice();
 gd.setFullScreenWindow(Main.window);
 screenWidth2 = Main.window.getWidth();
 screenHeight2 = Main.window.getHeight();
 }

 // Game thread
 public void startGameThread() {
 gameThread = new Thread(this);
 gameThread.start();
 }

 // @Override
 public void run() {
 double drawInterval = 1_000_000_000.0 / FPS;
 double delta = 0;
 long lastTime = System.nanoTime();
 while (gameThread != null) {
 long currentTime = System.nanoTime();
 delta += (currentTime - lastTime) / drawInterval;
 lastTime = currentTime;
 if (delta >= 1) {
 update();
 drawToTempScreen();
 drawToScreen();
 delta--;
 }
 // Precise sleep to reduce CPU usage without causing lag
 try {
 long sleepTime = (long)((lastTime + (long)drawInterval - System.nanoTime()) / 1_000_000);
 if (sleepTime > 0 && sleepTime < 20) Thread.sleep(sleepTime);
 else Thread.sleep(1);
 } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
 }
 }

 // Update
 public void update() {
 if (gameState == playState) {
 player.update();

 for (int i = 0; i < npc[0].length; i++)
 if (npc[currentMap][i] != null) npc[currentMap][i].update();

 for (int i = 0; i < monster[0].length; i++) {
 if (monster[currentMap][i] != null) {
 if (monster[currentMap][i].alive && !monster[currentMap][i].dying)
 monster[currentMap][i].update();
 if (!monster[currentMap][i].alive) {
 monster[currentMap][i].checkDrop();
 monster[currentMap][i] = null;
 }
 }
 }
 }
 // pauseState: no updates needed
 }

 // Draw to temp screen (pattern)
 public void drawToTempScreen() {
 if (gameState == titleState || gameState == scoreState
 || gameState == endingState || gameState == prologueState) {
 ui.draw(g2);
 return;
 }

 // Screen shake offset
 int shakeX = 0, shakeY = 0;
 if (ui.screenShakeCounter > 0) {
 shakeX = (int)((Math.random() - 0.5) * 8);
 shakeY = (int)((Math.random() - 0.5) * 8);
 g2.translate(shakeX, shakeY);
 ui.screenShakeCounter--;
 }

 // Fill background (prevents white edges beyond map)
 g2.setColor(new Color(34, 139, 34));
 g2.fillRect(-20, -20, screenWidth + 40, screenHeight + 40);

 // Tiles
 tileM.draw(g2);

 // Build Y-sorted entity list (reuse list to avoid GC pressure)
 entityList.clear();
 entityList.add(player);
 for (int i = 0; i < npc[0].length; i++)
 if (npc[currentMap][i] != null) entityList.add(npc[currentMap][i]);
 for (int i = 0; i < obj[0].length; i++)
 if (obj[currentMap][i] != null) entityList.add(obj[currentMap][i]);
 for (int i = 0; i < monster[0].length; i++)
 if (monster[currentMap][i] != null) entityList.add(monster[currentMap][i]);

 // Sort by worldY for depth rendering
 Collections.sort(entityList, new Comparator<Entity>() {
 @Override public int compare(Entity e1, Entity e2) {
 return Integer.compare(e1.worldY, e2.worldY);
 }
 });

 for (int i = 0; i < entityList.size(); i++) entityList.get(i).draw(g2);

 // Reset shake offset before drawing UI
 if (shakeX != 0 || shakeY != 0) {
 g2.translate(-shakeX, -shakeY);
 }

 // UI overlay
 ui.draw(g2);

 // Debug info
 if (keyH.showDebugText) {
 g2.setFont(new Font("Arial", Font.PLAIN, 14));
 g2.setColor(Color.white);
 g2.drawString("Map:" + currentMap + " Col:" + player.getCol()
 + " Row:" + player.getRow(), 10, 380);
 }
 }

 public void drawToScreen() {
 if (g2 == null || tempScreen == null) return;
 Graphics g = getGraphics();
 if (g != null) {
 Graphics2D g2d = (Graphics2D) g;
 g2d.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION, java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR);
 g2d.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
 g2d.dispose();
 }
 }

 // Sound helpers
 public void playMusic(int i) {
 music.setFile(i);
 music.play();
 music.loop();
 }
 public void stopMusic() { music.stop(); }
 public void playSE(int i){ se.setFile(i); se.play(); }

 // Save/Load progress for Continue feature
 String getSavePath() {
 try {
 String dir = System.getProperty("user.dir");
 return dir + java.io.File.separator + "progress.dat";
 } catch (Exception e) { return "progress.dat"; }
 }

 public void saveProgress() {
 try {
 java.io.PrintWriter pw = new java.io.PrintWriter(new java.io.FileWriter(getSavePath()));
 pw.println(player.knowledgePoints);
 pw.println(player.scrollsCompleted);
 pw.println(player.enemiesDefeated);
 pw.println(player.level);
 pw.println(player.exp);
 pw.println(player.maxLife);
 pw.println(player.life);
 pw.println(player.hasDefeatedShona);
 pw.println(player.hasFoundSheenaMemory);
 pw.println(player.worldX);
 pw.println(player.worldY);
 pw.println(player.strength);
 pw.println(player.dexterity);
 pw.close();
 } catch (Exception e) {
 // could not write save file
 }
 }

 public boolean loadProgress() {
 java.io.File f = new java.io.File(getSavePath());
 if (!f.exists()) return false;
 try {
 java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(f));
 player.knowledgePoints = Integer.parseInt(br.readLine().trim());
 player.scrollsCompleted = Integer.parseInt(br.readLine().trim());
 player.enemiesDefeated = Integer.parseInt(br.readLine().trim());
 player.level = Integer.parseInt(br.readLine().trim());
 player.exp = Integer.parseInt(br.readLine().trim());
 player.maxLife = Integer.parseInt(br.readLine().trim());
 player.life = Integer.parseInt(br.readLine().trim());
 player.hasDefeatedShona = Boolean.parseBoolean(br.readLine().trim());
 player.hasFoundSheenaMemory = Boolean.parseBoolean(br.readLine().trim());
 player.worldX = Integer.parseInt(br.readLine().trim());
 player.worldY = Integer.parseInt(br.readLine().trim());
 player.strength = Integer.parseInt(br.readLine().trim());
 player.dexterity = Integer.parseInt(br.readLine().trim());
 br.close();

 // Make sure movement state is clean so the player isn't stuck
 player.direction = "down";
 player.speed = player.defaultSpeed;
 player.collisionOn = false;
 player.knockBack = false;
 player.invincible = false;
 player.attacking = false;
 player.guarding = false;

 // Spawn at the safe starting area on continue (progress/stats are kept).
 // This avoids the player loading on top of an NPC or wall and getting stuck.
 player.setDefaultPositions();

 // If player already had castle access, reopen gate
 if (player.knowledgePoints >= 70 && player.scrollsCompleted >= 7) {
 for (int r = 10; r <= 12; r++) {
 tileM.mapTileNum[0][24][r] = 4;
 tileM.mapTileNum[0][25][r] = 4;
 tileM.mapTileNum[0][26][r] = 4;
 }
 }
 // Remove monsters the player already defeated
 for (int i = 0; i < player.enemiesDefeated && i < 5; i++) {
 monster[0][i] = null;
 }
 return true;
 } catch (Exception e) {
 return false;
 }
 }
}
