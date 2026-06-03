package progress;

/**
 * Storable - file I/O contract for saving and loading scores.
 *
 * Created by: Habib
 * Tested by: Aezekiel
 * Purpose: Define the contract for save/load score operations using text files.
 */
public interface Storable {
    void saveScore(GameProgress progress) throws ScoreFileException;
    String loadScores() throws ScoreFileException;
}
