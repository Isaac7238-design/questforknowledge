package progress;

import java.io.*;

/**
 * ScoreStorage - saves/loads scores from scores.txt.
 * Implements Storable.
 * Format: PlayerName,FinalScore,KnowledgePoints,ScrollsCompleted,Level,EndingType,Badges
 *
 * Created by: Habib
 * Tested by: Aezekiel
 * Desc: Save/load score using text file with ranking and high score display.
 */
public class ScoreStorage implements Storable {

 private String fileName = "scores.txt";

 public ScoreStorage() {}
 public ScoreStorage(String fileName) { this.fileName = fileName; }

 @Override
 public void saveScore(GameProgress progress) throws ScoreFileException {
 try {
 File file = new File(fileName);
 if (!file.exists()) file.createNewFile();

 String line = progress.getPlayerName() + ","
 + progress.getFinalScore() + ","
 + progress.getKnowledgePoints() + ","
 + progress.getScrollsCompleted() + ","
 + progress.getLevel() + ","
 + progress.getEndingType() + ","
 + progress.getBadgesAsText();

 BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
 bw.write(line);
 bw.newLine();
 bw.close();
 } catch (IOException e) {
 throw new ScoreFileException("Failed to save score: " + e.getMessage(), e);
 }
 }

 @Override
 public String loadScores() throws ScoreFileException {
 try {
 File file = new File(fileName);
 if (!file.exists()) return "No scores saved yet.";

 BufferedReader br = new BufferedReader(new FileReader(file));
 StringBuilder sb = new StringBuilder();
 String line;
 int rank = 1;

 while ((line = br.readLine()) != null) {
 if (!line.trim().isEmpty()) {
 String[] p = line.split(",");
 if (p.length >= 7) {
 sb.append("#").append(rank++).append(" ")
 .append(p[0]).append(" Score:").append(p[1])
 .append(" KP:").append(p[2])
 .append(" Scrolls:").append(p[3])
 .append(" Lv:").append(p[4])
 .append(" ").append(p[5])
 .append("\n Badges: ").append(p[6]).append("\n");
 }
 }
 }
 br.close();
 return sb.length() > 0 ? sb.toString() : "No scores saved yet.";
 } catch (IOException e) {
 throw new ScoreFileException("Failed to load scores: " + e.getMessage(), e);
 }
 }

 public String displayHighScore() {
 try {
 File file = new File(fileName);
 if (!file.exists()) return "No high score yet.";
 BufferedReader br = new BufferedReader(new FileReader(file));
 String line, best = null;
 int hi = -1;
 while ((line = br.readLine()) != null) {
 if (!line.trim().isEmpty()) {
 String[] p = line.split(",");
 if (p.length >= 2) {
 try {
 int s = Integer.parseInt(p[1].trim());
 if (s > hi) { hi = s; best = line; }
 } catch (NumberFormatException ignored) {}
 }
 }
 }
 br.close();
 if (best == null) return "No high score yet.";
 String[] p = best.split(",");
 return "Best: " + p[0] + " Score:" + p[1] + " " + (p.length >= 6 ? p[5] : "");
 } catch (IOException e) { return "Could not load high score."; }
 }
}
