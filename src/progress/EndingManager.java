package progress;

import java.awt.*;

/**
 * EndingManager - determines and draws the game ending.
 * Priority: SECRET > TRUE > GOOD > NORMAL
 */
public class EndingManager {

    public String determineEnding(entity.Player p) {
        boolean sheena   = p.hasFoundSheenaMemory;
        boolean shona    = p.hasDefeatedShona;
        String  choice   = p.finalChoice;
        int     scrolls  = p.scrollsCompleted;
        int     kp       = p.knowledgePoints;

        if (sheena && shona && "FORGIVE".equals(choice)) return "SECRET_ENDING";
        if (scrolls >= 10 && kp >= 80 && shona && "SHARE".equals(choice)) return "TRUE_ENDING";
        if (kp >= 70 && shona) return "GOOD_ENDING";
        return "NORMAL_ENDING";
    }

    public String getEndingTitle(String type) {
        switch (type) {
            case "SECRET_ENDING": return "Secret Ending: Shona's Memory";
            case "TRUE_ENDING":   return "True Ending: The Light of Lucienne";
            case "GOOD_ENDING":   return "Good Ending: The Kingdom Restored";
            default:              return "Normal Ending: A Lesson Remembered";
        }
    }

    public String getEndingStory(String type) {
        switch (type) {
            case "SECRET_ENDING":
                return "Jeff discovers Sheena's hidden memory.\n"
                     + "Shona feared Lucienne would lose knowledge again.\n"
                     + "Jeff chooses empathy. Shona regrets and helps restore\n"
                     + "the final scroll. Lucienne gains a peaceful future.";
            case "TRUE_ENDING":
                return "Jeff completes all scrolls and chooses to share\n"
                     + "knowledge with everyone. Lucienne becomes a kingdom\n"
                     + "where education is open to all.\n"
                     + "Jeff wakes up inspired to share knowledge in real life.";
            case "GOOD_ENDING":
                return "Jeff defeats Shona and breaks the Knowledge Crystal.\n"
                     + "Most of Lucienne's knowledge returns.\n"
                     + "King Luin thanks Jeff for saving the kingdom.";
            default:
                return "Jeff wakes up in his bedroom.\n"
                     + "Lucienne is not fully restored, but Jeff remembers\n"
                     + "the importance of quality education\n"
                     + "and decides to apply the lesson in real life.";
        }
    }

    public void drawEnding(Graphics2D g2, GameProgress progress,
                           int sw, int sh, Font titleFont, Font normalFont) {
        String type  = progress.getEndingType();
        String title = getEndingTitle(type);
        String story = getEndingStory(type);

        g2.setColor(new Color(10, 10, 30));
        g2.fillRect(0, 0, sw, sh);

        // Stars
        g2.setColor(Color.WHITE);
        for (int i = 0; i < 50; i++)
            g2.fillOval((i * 53 + 7) % sw, (i * 37 + 11) % (sh / 2), 2, 2);

        g2.setFont(titleFont);
        g2.setColor(new Color(255, 215, 0));
        drawCentered(g2, title, sw, 70);

        g2.setFont(normalFont);
        g2.setColor(Color.WHITE);
        int y = 115;
        for (String line : story.split("\n")) { drawCentered(g2, line, sw, y); y += 28; }

        y += 15;
        g2.setColor(new Color(100, 220, 255));
        drawCentered(g2, "Final Score: " + progress.getFinalScore(), sw, y);

        y += 28;
        int stars = progress.getFinalScore() >= 80 ? 3
                  : progress.getFinalScore() >= 60 ? 2
                  : progress.getFinalScore() >= 40 ? 1 : 0;
        String starStr = "Stars: " + "★".repeat(stars) + "☆".repeat(3 - stars);
        drawCentered(g2, starStr, sw, y);

        y += 28;
        g2.setColor(new Color(255, 200, 100));
        drawCentered(g2, "Badges: " + progress.getBadgesAsText(), sw, y);

        y += 40;
        g2.setColor(new Color(180, 180, 180));
        g2.setFont(normalFont.deriveFont(13f));
        drawCentered(g2, "Press Enter to save score  |  Esc to return to title", sw, y);
    }

    private void drawCentered(Graphics2D g2, String text, int sw, int y) {
        if (text == null) return;
        int x = (sw - g2.getFontMetrics().stringWidth(text)) / 2;
        g2.drawString(text, x, y);
    }
}
