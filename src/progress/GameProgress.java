package progress;

import java.util.ArrayList;

/**
 * GameProgress - tracks all player progress at end of game.
 * Synced from Player before saving.
 * Demonstrates: encapsulation, ArrayList
 *
 * Created by: Habib
 * Tested by: Aezekiel
 * Purpose: Track and store all player progress data for score saving and ending determination.
 */
public class GameProgress {

    private int    knowledgePoints;
    private int    scrollsCompleted;
    private int    enemiesDefeated;
    private boolean defeatedShona;
    private boolean foundSheenaMemory;
    private String  finalChoice;
    private ArrayList<Badge> badges;
    private String  endingType;
    private int     finalScore;
    private int     level;
    private String  playerName;

    public GameProgress() {
        knowledgePoints   = 0;
        scrollsCompleted  = 0;
        enemiesDefeated   = 0;
        defeatedShona     = false;
        foundSheenaMemory = false;
        finalChoice       = "";
        badges            = new ArrayList<>();
        endingType        = "NORMAL_ENDING";
        finalScore        = 0;
        level             = 1;
        playerName        = "Jeff Lionhardt";
    }

    public void syncFromPlayer(entity.Player p) {
        this.knowledgePoints   = p.knowledgePoints;
        this.scrollsCompleted  = p.scrollsCompleted;
        this.enemiesDefeated   = p.enemiesDefeated;
        this.defeatedShona     = p.hasDefeatedShona;
        this.foundSheenaMemory = p.hasFoundSheenaMemory;
        this.finalChoice       = p.finalChoice;
        this.level             = p.level;
        this.playerName        = "Jeff Lionhardt";
        this.badges            = new ArrayList<>(p.badges);
    }

    public void unlockBadge(String name) {
        for (Badge b : badges) {
            if (b.getBadgeName().equals(name) && !b.isUnlocked()) b.award();
        }
    }

    public void unlockLightOfLucienne() { unlockBadge("Light of Lucienne"); }

    public String getBadgesAsText() {
        StringBuilder sb = new StringBuilder();
        for (Badge b : badges) {
            if (b.isUnlocked()) {
                if (sb.length() > 0) sb.append("|");
                sb.append(b.getBadgeName());
            }
        }
        return sb.length() > 0 ? sb.toString() : "None";
    }

    // Setters
    public void setEndingType(String t)  { endingType = t; }
    public void setFinalScore(int s)     { finalScore = s; }
    public void setFinalChoice(String c) { finalChoice = c; }

    // Getters
    public int     getKnowledgePoints()   { return knowledgePoints; }
    public int     getScrollsCompleted()  { return scrollsCompleted; }
    public int     getEnemiesDefeated()   { return enemiesDefeated; }
    public boolean hasDefeatedShona()     { return defeatedShona; }
    public boolean hasFoundSheenaMemory() { return foundSheenaMemory; }
    public String  getFinalChoice()       { return finalChoice; }
    public String  getEndingType()        { return endingType; }
    public int     getFinalScore()        { return finalScore; }
    public int     getLevel()             { return level; }
    public String  getPlayerName()        { return playerName; }
    public ArrayList<Badge> getBadges()   { return badges; }
}
