package progress;

/** Storable - file I/O contract for saving and loading scores. */
public interface Storable {
    void saveScore(GameProgress progress) throws ScoreFileException;
    String loadScores() throws ScoreFileException;
}
