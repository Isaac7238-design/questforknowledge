package progress;

/**
 * Badge - an achievement the player can earn.
 * Implements Rewardable. Demonstrates: encapsulation, interfaces.
 *
 * Created by: Habib
 * Tested by: Aezekiel
 * Purpose: Represent unlockable achievements/badges the player earns for milestones.
 */
public class Badge implements Rewardable {

    private String  badgeName;
    private String  description;
    private boolean unlocked;

    public Badge(String badgeName, String description) {
        this.badgeName   = badgeName;
        this.description = description;
        this.unlocked    = false;
    }

    @Override public void award()             { this.unlocked = true; }
    @Override public String getBadgeName()    { return badgeName; }
    @Override public String getDescription()  { return description; }
    @Override public boolean isUnlocked()     { return unlocked; }

    @Override
    public String toString() {
        return badgeName + (unlocked ? " [EARNED]" : " [locked]");
    }
}
