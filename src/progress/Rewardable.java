package progress;

/**
 * Rewardable - implemented by Badge.
 *
 * Created by: Habib
 * Tested by: Aezekiel
 * Desc: Define the contract for achievement/badge rewards system.
 */
public interface Rewardable {
 void award();
 String getBadgeName();
 String getDescription();
 boolean isUnlocked();
}
