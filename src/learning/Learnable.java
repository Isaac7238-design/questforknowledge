package learning;

/** Learnable - implemented by LearningManager. */
public interface Learnable {
    LearningPage getPage(int index);
    void completePage(int index);
    int getCompletedCount();
    String displayPage(int index);
    String displayPage(String title); // overloaded
}
