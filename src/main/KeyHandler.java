package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * KeyHandler - handles all keyboard input.
 * Routes key events by game state.
 *
 * Created by: Aezekiel
 * Tested by: Habib
 */
public class KeyHandler implements KeyListener {

 GamePanel gp;

 // Movement
 public boolean upPressed, downPressed, leftPressed, rightPressed;

 // Actions
 public boolean enterPressed;
 public boolean spacePressed; // guard / confirm
 public boolean escPressed;
 public boolean pPressed; // pause

 // Quiz text input
 public boolean textInputMode = false;
 public String textInput = "";

 // Debug
 public boolean showDebugText = false;
 public boolean godModeOn = false;

 public KeyHandler(GamePanel gp) {
 this.gp = gp;
 }

 @Override
 public void keyPressed(KeyEvent e) {
 int code = e.getKeyCode();

 // Text input mode (quiz fill-in-blank)
 if (textInputMode) {
 if (code == KeyEvent.VK_BACK_SPACE) {
 if (textInput.length() > 0)
 textInput = textInput.substring(0, textInput.length() - 1);
 } else if (code == KeyEvent.VK_ENTER) {
 enterPressed = true;
 } else {
 char c = e.getKeyChar();
 if (c != KeyEvent.CHAR_UNDEFINED && textInput.length() < 30)
 textInput += c;
 }
 return;
 }

 // Route by game state
 if (gp.gameState == gp.titleState) { titleState(code); return; }
 if (gp.gameState == gp.prologueState) { prologueState(code); return; }
 if (gp.gameState == gp.pauseState) { pauseState(code); return; }
 if (gp.gameState == gp.dialogueState) { dialogueState(code); return; }
 if (gp.gameState == gp.optionsState) { optionsState(code); return; }
 if (gp.gameState == gp.gameOverState) { gameOverState(code); return; }
 if (gp.gameState == gp.quizState) { quizState(code); return; }
 if (gp.gameState == gp.learningState) { learningState(code); return; }
 if (gp.gameState == gp.shopState) { shopState(code); return; }
 if (gp.gameState == gp.endingChoiceState){ endingChoiceState(code); return; }
 if (gp.gameState == gp.endingState) { endingState(code); return; }
 if (gp.gameState == gp.scoreState) { scoreState(code); return; }

 // Play state
 playState(code);
 }

 private void titleState(int code) {
 if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) gp.ui.commandNum--;
 if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) gp.ui.commandNum++;

 // clamp based on titleScreenState
 if (gp.ui.titleScreenState == 0) {
 if (gp.ui.commandNum < 0) gp.ui.commandNum = 4;
 if (gp.ui.commandNum > 4) gp.ui.commandNum = 0;
 if (code == KeyEvent.VK_ENTER) {
 switch (gp.ui.commandNum) {
 case 0: // New Game
 gp.resetGame(true);
 gp.setupGame();
 // Delete old save so Continue won't load stale data
 new java.io.File("progress.dat").delete();
 gp.gameState = gp.prologueState;
 gp.ui.prologueLine = 0;
 gp.ui.prologueTimer = 0;
 break;
 case 1: // Continue
 gp.setupGame();
 if (gp.loadProgress()) {
 gp.gameState = gp.playState;
 gp.playMusic(0);
 if (gp.player.hasDefeatedShona && !gp.player.hasFoundSheenaMemory) {
 gp.ui.showToast("A secret ending awaits somewhere in the map...");
 } else if (gp.player.hasFoundSheenaMemory && !gp.player.hasDefeatedShona) {
 gp.ui.showToast("Congratulations! You unlocked the secret ending!");
 }
 } else {
 // No save file, start fresh
 gp.ui.showToast("No saved progress found. Starting new game.");
 gp.gameState = gp.prologueState;
 gp.ui.prologueLine = 0;
 gp.ui.prologueTimer = 0;
 }
 break;
 case 2: // How to Play
 gp.ui.titleScreenState = 1;
 gp.ui.commandNum = 0;
 break;
 case 3: // View Scores
 gp.ui.loadScoreData();
 gp.gameState = gp.scoreState;
 break;
 case 4: // Quit
 System.exit(0);
 break;
 }
 }
 } else if (gp.ui.titleScreenState == 1) {
 // How to Play screen
 if (code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_ENTER) {
 gp.ui.titleScreenState = 0;
 gp.ui.commandNum = 0;
 }
 }
 }


 private void prologueState(int code) {
 if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
 gp.ui.prologueLine++;
 if (gp.ui.prologueLine >= gp.ui.getPrologueLineCount()) {
 // Prologue done - start the game
 gp.gameState = gp.playState;
 gp.playMusic(0);
 // Auto-trigger Piercehardt dialogue
 if (gp.npc[0][0] != null) gp.npc[0][0].speak();
 }
 }
 if (code == KeyEvent.VK_ESCAPE) {
 // Skip prologue
 gp.gameState = gp.playState;
 gp.playMusic(0);
 }
 }
 private void playState(int code) {
 if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) upPressed = true;
 if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) downPressed = true;
 if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) leftPressed = true;
 if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) rightPressed = true;
 if (code == KeyEvent.VK_ENTER) enterPressed = true;
 if (code == KeyEvent.VK_SPACE) spacePressed = true;
 if (code == KeyEvent.VK_P) { gp.gameState = gp.pauseState; gp.ui.commandNum = 0; }
 if (code == KeyEvent.VK_ESCAPE){ gp.gameState = gp.optionsState; gp.ui.commandNum = 0; }
 if (code == KeyEvent.VK_F3) showDebugText = !showDebugText;
 if (code == KeyEvent.VK_G) godModeOn = !godModeOn;
 }

 private void pauseState(int code) {
 if (code == KeyEvent.VK_P || code == KeyEvent.VK_ESCAPE)
 gp.gameState = gp.playState;
 }

 private void dialogueState(int code) {
 if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE || code == KeyEvent.VK_E) {
 if (gp.ui.npc != null) {
 gp.ui.npc.dialogueIndex++;
 // Check if we've run out of dialogue lines
 if (gp.ui.npc.dialogues[gp.ui.npc.dialogueSet] == null ||
 gp.ui.npc.dialogueIndex >= 20 ||
 gp.ui.npc.dialogues[gp.ui.npc.dialogueSet][gp.ui.npc.dialogueIndex] == null) {
 gp.ui.npc.dialogueIndex = 0;
 gp.gameState = gp.playState;
 gp.ui.npc = null;
 }
 } else {
 gp.gameState = gp.playState;
 }
 }
 if (code == KeyEvent.VK_ESCAPE) {
 gp.gameState = gp.playState;
 if (gp.ui.npc != null) { gp.ui.npc.dialogueIndex = 0; gp.ui.npc = null; }
 }
 }

 private void optionsState(int code) {
 if (code == KeyEvent.VK_ESCAPE) { gp.gameState = gp.playState; return; }
 if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) gp.ui.commandNum--;
 if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) gp.ui.commandNum++;
 if (gp.ui.commandNum < 0) gp.ui.commandNum = 4;
 if (gp.ui.commandNum > 4) gp.ui.commandNum = 0;
 if (code == KeyEvent.VK_ENTER) {
 switch (gp.ui.commandNum) {
 case 4: // End Game
 gp.saveProgress();
 gp.gameState = gp.titleState;
 gp.resetGame(true);
 gp.stopMusic();
 break;
 }
 }
 }

 private void gameOverState(int code) {
 if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) gp.ui.commandNum--;
 if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) gp.ui.commandNum++;
 if (gp.ui.commandNum < 0) gp.ui.commandNum = 1;
 if (gp.ui.commandNum > 1) gp.ui.commandNum = 0;
 if (code == KeyEvent.VK_ENTER) {
 if (gp.ui.commandNum == 0) { // Retry
 gp.resetGame(false);
 gp.playMusic(0);
 gp.gameState = gp.playState;
 } else { // Quit
 gp.resetGame(true);
 gp.stopMusic();
 gp.gameState = gp.titleState;
 }
 }
 }

 private void quizState(int code) {
 // A/B/C/D to select answer
 if (code == KeyEvent.VK_A) { gp.quizManager.selectedOption = 0; submitQuizAnswer(0); }
 if (code == KeyEvent.VK_B) { gp.quizManager.selectedOption = 1; submitQuizAnswer(1); }
 if (code == KeyEvent.VK_C) { gp.quizManager.selectedOption = 2; submitQuizAnswer(2); }
 if (code == KeyEvent.VK_D) { gp.quizManager.selectedOption = 3; submitQuizAnswer(3); }

 // Enter to continue after quiz finished
 if (code == KeyEvent.VK_ENTER && gp.quizManager.isQuizFinished()) {
 gp.keyH.textInputMode = false;
 applyQuizResults();
 gp.gameState = gp.playState;
 }

 // Esc to flee
 if (code == KeyEvent.VK_ESCAPE) {
 gp.player.life -= 1;
 gp.ui.addMessage("You fled! -1 HP");
 gp.keyH.textInputMode = false;
 gp.quizManager.resetQuiz();
 gp.gameState = gp.playState;
 }
 }

 private void submitQuizAnswer(int option) {
 if (gp.quizManager.isQuizFinished()) return;
 boolean correct = gp.quizManager.submitAnswer(option);
 if (correct) {
 gp.player.gainXP(15);
 gp.player.knowledgePoints += 10;
 gp.playSE(2);
 // Damage enemy
 if (gp.currentEnemyIndex >= 0 && gp.currentEnemyIndex < gp.monster[0].length
 && gp.monster[gp.currentMap][gp.currentEnemyIndex] != null) {
 gp.monster[gp.currentMap][gp.currentEnemyIndex].life--;
 if (gp.monster[gp.currentMap][gp.currentEnemyIndex].life <= 0) {
 gp.monster[gp.currentMap][gp.currentEnemyIndex].dying = true;
 }
 }
 } else {
 gp.player.life = Math.max(0, gp.player.life - 1);
 gp.playSE(6);
 gp.ui.screenShakeCounter = 8; // Screen shake on wrong answer
 }

 // Auto-advance to results when quiz finishes
 if (gp.quizManager.isQuizFinished()) {
 // Short delay then process results
 }
 }

 private void applyQuizResults() {
 gp.ui.addMessage(gp.quizManager.getMotivationalMessage());
 if (gp.currentEnemyIndex >= 0 && gp.currentEnemyIndex < gp.monster[0].length
 && gp.monster[gp.currentMap][gp.currentEnemyIndex] != null) {
 if (!gp.monster[gp.currentMap][gp.currentEnemyIndex].alive
 || gp.monster[gp.currentMap][gp.currentEnemyIndex].dying) {
 gp.monster[gp.currentMap][gp.currentEnemyIndex].checkDrop();
 gp.player.enemiesDefeated++;
 gp.player.checkBadgeConditions();
 gp.player.checkAllFragmentsDefeated();
 }
 }
 if (gp.player.hasMemoryCharm) {
 gp.player.gainXP(10, "Memory Charm");
 gp.player.hasMemoryCharm = false;
 }
 gp.currentEnemyIndex = -1;
 gp.quizManager.resetQuiz();
 }

 private void learningState(int code) {
 if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
 // Complete scroll - gives XP only, NOT KP (KP comes from battles)
 gp.learningManager.completePage(gp.currentScrollIndex);
 gp.player.scrollsCompleted++;
 gp.player.exp += 15;
 gp.player.checkLevelUp();
 gp.player.checkBadgeConditions();
 gp.playSE(3);
 gp.ui.addMessage("Scroll complete! +15 XP");
 gp.gameState = gp.playState;
 }
 if (code == KeyEvent.VK_ESCAPE) {
 gp.gameState = gp.playState;
 }
 }

 private void shopState(int code) {
 if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) gp.ui.commandNum--;
 if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) gp.ui.commandNum++;
 if (gp.ui.commandNum < 0) gp.ui.commandNum = 2;
 if (gp.ui.commandNum > 2) gp.ui.commandNum = 0;
 if (code == KeyEvent.VK_ENTER) gp.ui.buyShopItem();
 if (code == KeyEvent.VK_ESCAPE) { gp.gameState = gp.playState; gp.ui.commandNum = 0; }
 }

 private void endingChoiceState(int code) {
 if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) gp.ui.commandNum--;
 if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) gp.ui.commandNum++;
 String[] choices = gp.ui.getAvailableEndingChoices();
 int maxCmd = choices.length - 1;
 if (gp.ui.commandNum < 0) gp.ui.commandNum = maxCmd;
 if (gp.ui.commandNum > maxCmd) gp.ui.commandNum = 0;
 if (code == KeyEvent.VK_ENTER) {
 gp.player.finalChoice = choices[gp.ui.commandNum];
 gp.currentEnding = gp.endingManager.determineEnding(gp.player);
 if ("TRUE_ENDING".equals(gp.currentEnding) || "SECRET_ENDING".equals(gp.currentEnding))
 gp.player.unlockBadge("Light of Lucienne");
 gp.gameState = gp.endingState;
 }
 }

 private void endingState(int code) {
 if (code == KeyEvent.VK_ENTER) {
 gp.ui.saveCurrentScore();
 // Show secret ending hint for good/true endings
 String ending = gp.currentEnding;
 if ("GOOD_ENDING".equals(ending) || "TRUE_ENDING".equals(ending)) {
 if (!gp.player.hasFoundSheenaMemory) {
 gp.ui.addMessage("A secret awaits... explore the hidden forest maze.");
 }
 }
 // Save progress and return to title
 gp.saveProgress();
 gp.resetGame(false);
 gp.stopMusic();
 gp.gameState = gp.titleState;
 gp.ui.commandNum = 0;
 }
 if (code == KeyEvent.VK_ESCAPE) {
 gp.saveProgress();
 gp.resetGame(false);
 gp.stopMusic();
 gp.gameState = gp.titleState;
 gp.ui.commandNum = 0;
 }
 }

 private void scoreState(int code) {
 if (code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_ENTER) {
 gp.gameState = gp.titleState;
 gp.ui.commandNum = 0;
 }
 }

 @Override
 public void keyReleased(KeyEvent e) {
 int code = e.getKeyCode();
 if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) upPressed = false;
 if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) downPressed = false;
 if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) leftPressed = false;
 if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) rightPressed = false;
 if (code == KeyEvent.VK_ENTER) enterPressed = false;
 if (code == KeyEvent.VK_SPACE) spacePressed = false;
 if (code == KeyEvent.VK_ESCAPE) escPressed = false;
 }

 @Override
 public void keyTyped(KeyEvent e) {}

 public void clearTextInput() { textInput = ""; }
}
