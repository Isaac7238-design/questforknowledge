package learning;

/**
 * Learnable - implemented by LearningManager.
 *
 * @author Lee Yun Zhan
 * @see tested by Nathanael
 * Purpose: Define the contract for managing and displaying SDG 4 learning pages.
 */
public interface Learnable {
 LearningPage getPage(int index);
 void completePage(int index);
 int getCompletedCount();
 String displayPage(int index);
 String displayPage(String title); // overloaded
}
