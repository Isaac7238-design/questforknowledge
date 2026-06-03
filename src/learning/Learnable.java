package learning;

/**
 * Learnable - implemented by LearningManager.
 *
 * Created by: Lee Yun Zhan
 * Tested by: Nathanael
 * Purpose: Define the contract for managing and displaying SDG 4 learning pages.
 */
public interface Learnable {
    LearningPage getPage(int index);
    void completePage(int index);
    int getCompletedCount();
    String displayPage(int index);
    String displayPage(String title); // overloaded
}
