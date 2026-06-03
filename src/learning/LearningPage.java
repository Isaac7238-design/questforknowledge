package learning;

/**
 * LearningPage - one SDG 4 learning scroll/page.
 *
 * @author Lee Yun Zhan
 * @see tested by Nathanael
 * Purpose: Represent a single SDG 4 scroll with title, content, and completion state.
 */
public class LearningPage {

 private int pageId;
 private String title;
 private String content;
 private boolean completed;

 public LearningPage(int pageId, String title, String content) {
 this.pageId = pageId;
 this.title = title;
 this.content = content;
 this.completed = false;
 }

 public String displayPage() { return "=== Page " + pageId + ": " + title + " ===\n" + content; }
 public void markCompleted(){ this.completed = true; }
 public String getSummary() { return (completed ? "[READ] " : "[NEW] ") + "Page " + pageId + ": " + title; }

 public int getPageId() { return pageId; }
 public String getTitle() { return title; }
 public String getContent() { return content; }
 public boolean isCompleted() { return completed; }
}
