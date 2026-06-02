package progress;

/** Rewardable - implemented by Badge. */
public interface Rewardable {
    void award();
    String getBadgeName();
    String getDescription();
    boolean isUnlocked();
}
