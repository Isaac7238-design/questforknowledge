package learning;

import java.util.ArrayList;

/**
 * LearningManager - manages all 10 SDG 4 learning pages.
 * Implements Learnable.
 *
 * @author Lee Yun Zhan
 * @see tested by Nathanael
 */
public class LearningManager implements Learnable {

 private ArrayList<LearningPage> pages = new ArrayList<>();
 private int currentPageIndex = 0;

 public LearningManager() { createDefaultPages(); }

 private void createDefaultPages() {
 pages.add(new LearningPage(1, "What is SDG 4?",
 "SDG 4 focuses on quality education for everyone.\n"
 + "It aims to make education inclusive, fair, and accessible.\n"
 + "SDG stands for Sustainable Development Goal.\n"
 + "The United Nations created 17 SDGs to improve the world.\n"
 + "SDG 4 ensures all people can access quality learning."));

 pages.add(new LearningPage(2, "Why Education Matters",
 "Education helps people gain knowledge, skills,\n"
 + "confidence, and better opportunities for the future.\n"
 + "Educated communities can solve problems more effectively.\n"
 + "Education reduces poverty and promotes equality.\n"
 + "It empowers individuals to make positive changes."));

 pages.add(new LearningPage(3, "Equal Access to School",
 "Every child should have the chance to attend school\n"
 + "regardless of background, location, gender, or income.\n"
 + "Millions of children worldwide still lack school access.\n"
 + "Barriers include poverty, conflict, and discrimination.\n"
 + "SDG 4 works to remove these barriers for everyone."));

 pages.add(new LearningPage(4, "Literacy and Numeracy",
 "Reading, writing, and counting are basic skills\n"
 + "that help people continue learning throughout life.\n"
 + "Literacy allows people to read and communicate.\n"
 + "Numeracy helps with finances, measurements, planning.\n"
 + "These skills open doors to further education."));

 pages.add(new LearningPage(5, "Inclusive Education",
 "Inclusive education means students with different abilities,\n"
 + "cultures, and backgrounds are welcomed and supported.\n"
 + "No student should be excluded because of a disability.\n"
 + "Diversity in classrooms enriches learning for everyone.\n"
 + "Support tools and adapted lessons help all students."));

 pages.add(new LearningPage(6, "Gender Equality in Education",
 "Boys and girls should receive equal learning opportunities\n"
 + "and be treated fairly in education.\n"
 + "In some regions, girls are still denied school access.\n"
 + "Educated girls build healthier communities.\n"
 + "Gender equality in education benefits entire societies."));

 pages.add(new LearningPage(7, "Digital Learning",
 "Technology can support learning through online materials,\n"
 + "educational apps, and digital communication.\n"
 + "Digital tools must be used responsibly and safely.\n"
 + "The internet provides vast educational resources.\n"
 + "However, the digital divide means not all have access."));

 pages.add(new LearningPage(8, "Safe Learning Environment",
 "Students learn better in places that are safe, respectful,\n"
 + "supportive, and free from bullying.\n"
 + "Fear and insecurity reduce a student's ability to focus.\n"
 + "Schools should have anti-bullying policies.\n"
 + "Mental health support is also part of safety."));

 pages.add(new LearningPage(9, "Lifelong Learning",
 "Learning does not stop after school.\n"
 + "People continue learning new skills throughout their lives.\n"
 + "Adults can attend workshops, courses, or training.\n"
 + "Lifelong learning helps people adapt to changing markets.\n"
 + "It fosters curiosity, creativity, and personal growth."));

 pages.add(new LearningPage(10, "How Students Can Help",
 "Students can support SDG 4 by sharing knowledge,\n"
 + "helping friends, respecting others, and valuing education.\n"
 + "Simple acts like tutoring a classmate make a difference.\n"
 + "Participating in activities promotes a learning culture.\n"
 + "Every student can champion quality education."));
 }

 @Override public LearningPage getPage(int index) { return (index >= 0 && index < pages.size()) ? pages.get(index) : null; }
 @Override public void completePage(int index) { if (index >= 0 && index < pages.size()) pages.get(index).markCompleted(); }
 @Override public int getCompletedCount() { int c = 0; for (LearningPage p : pages) if (p.isCompleted()) c++; return c; }
 @Override public String displayPage(int index) { LearningPage p = getPage(index); return p != null ? p.displayPage() : "Not found."; }
 @Override public String displayPage(String title) { for (LearningPage p : pages) if (p.getTitle().equalsIgnoreCase(title)) return p.displayPage(); return "Not found: " + title; }

 public ArrayList<LearningPage> getPages() { return pages; }
 public int getCurrentPageIndex() { return currentPageIndex; }
 public void setCurrentPageIndex(int i) { if (i >= 0 && i < pages.size()) currentPageIndex = i; }
 public int getTotalPages() { return pages.size(); }
 public LearningPage getNextIncompletePage() { for (LearningPage p : pages) if (!p.isCompleted()) return p; return null; }
}
