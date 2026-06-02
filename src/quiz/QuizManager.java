package quiz;

import java.awt.*;
import java.util.ArrayList;

/**
 * QuizManager - ALL questions are Multiple Choice (A/B/C/D).
 * Player presses A, B, C, or D to answer. No typing needed.
 * Implements QuizPlayable.
 */
public class QuizManager implements QuizPlayable {

    private ArrayList<Question> questions = new ArrayList<>();
    private int     currentIndex    = 0;
    private int     score           = 0;
    private int     totalAnswered   = 0;
    private int     correctAnswered = 0;
    private boolean quizFinished    = false;
    private int     sessionStart    = 0;
    private int     sessionLimit    = 0;

    public boolean lastAnswerCorrect = false;
    public String  feedbackMessage   = "";
    public int     selectedOption    = -1; // 0=A, 1=B, 2=C, 3=D

    public QuizManager() {
        createDefaultQuestions();
        resetQuiz();
    }

    private void createDefaultQuestions() {

        // 20 Multiple Choice Questions about SDG 4
        questions.add(new MultipleChoiceQuestion("What is the main focus of SDG 4?",
            new String[]{"A. Clean energy","B. Quality education","C. Climate action","D. Ocean life"}, "B", 10));

        questions.add(new MultipleChoiceQuestion("Why is education important?",
            new String[]{"A. Helps gain knowledge & skills","B. Only helps rich people","C. It is not useful","D. Stops people from working"}, "A", 10));

        questions.add(new MultipleChoiceQuestion("Which group should have access to education?",
            new String[]{"A. Only boys","B. Only adults","C. Everyone","D. Only city students"}, "C", 10));

        questions.add(new MultipleChoiceQuestion("What does inclusive education mean?",
            new String[]{"A. Only for top students","B. Welcomes different learners","C. Without teachers","D. Only online"}, "B", 10));

        questions.add(new MultipleChoiceQuestion("Which skill is part of basic education?",
            new String[]{"A. Reading","B. Flying","C. Magic spells","D. Teleporting"}, "A", 10));

        questions.add(new MultipleChoiceQuestion("What can technology support in education?",
            new String[]{"A. Digital learning","B. Forgetting knowledge","C. Destroying schools","D. Removing teachers"}, "A", 10));

        questions.add(new MultipleChoiceQuestion("What should a safe school have?",
            new String[]{"A. Bullying","B. Fear","C. Respect and support","D. No rules"}, "C", 10));

        questions.add(new MultipleChoiceQuestion("What is lifelong learning?",
            new String[]{"A. Learning only in primary school","B. Learning throughout life","C. Only for exams","D. Stops after graduation"}, "B", 10));

        questions.add(new MultipleChoiceQuestion("SDG 4 is related to what?",
            new String[]{"A. Clean water","B. Quality education","C. Zero hunger","D. Good health"}, "B", 10));

        questions.add(new MultipleChoiceQuestion("Who deserves access to education?",
            new String[]{"A. Only children in cities","B. Only wealthy families","C. All children everywhere","D. Only boys"}, "C", 10));

        questions.add(new MultipleChoiceQuestion("What does literacy mean?",
            new String[]{"A. Ability to swim","B. Ability to read and write","C. Ability to cook","D. Ability to drive"}, "B", 10));

        questions.add(new MultipleChoiceQuestion("Education can help reduce what?",
            new String[]{"A. Happiness","B. Poverty","C. Knowledge","D. Health"}, "B", 10));

        questions.add(new MultipleChoiceQuestion("Inclusive education means excluding who?",
            new String[]{"A. No one - everyone is welcome","B. Disabled students","C. Girls","D. Poor families"}, "A", 10));

        questions.add(new MultipleChoiceQuestion("Digital tools can support learning if used how?",
            new String[]{"A. Irresponsibly","B. Properly and safely","C. Without supervision","D. Only for games"}, "B", 10));

        questions.add(new MultipleChoiceQuestion("A good learning environment should be?",
            new String[]{"A. Dangerous","B. Boring","C. Safe and respectful","D. Expensive"}, "C", 10));

        questions.add(new MultipleChoiceQuestion("Lifelong learning means people should?",
            new String[]{"A. Stop after school","B. Never study","C. Learn new skills throughout life","D. Only read books"}, "C", 10));

        questions.add(new MultipleChoiceQuestion("SDG 4 focuses on quality what?",
            new String[]{"A. Food","B. Water","C. Education","D. Transportation"}, "C", 10));

        questions.add(new MultipleChoiceQuestion("Students should be treated with fairness and?",
            new String[]{"A. Cruelty","B. Respect","C. Ignorance","D. Punishment"}, "B", 10));

        questions.add(new MultipleChoiceQuestion("Reading and writing are part of what skills?",
            new String[]{"A. Sports skills","B. Cooking skills","C. Literacy skills","D. Gaming skills"}, "C", 10));

        questions.add(new MultipleChoiceQuestion("How can students support SDG 4?",
            new String[]{"A. Skip school","B. Bully others","C. Share knowledge and help friends","D. Ignore teachers"}, "C", 10));
    }

    // ── QuizPlayable interface ────────────────────────────────────

    @Override public void startQuiz() { resetQuiz(); sessionLimit = questions.size(); }

    public void startBattleQuiz(int numQ) { resetQuiz(); sessionLimit = numQ; sessionStart = (int)(System.currentTimeMillis() % questions.size()); }

    @Override
    public boolean checkAnswer(String answer) throws InvalidAnswerException {
        if (quizFinished) return false;
        Question q = getCurrentQuestion();
        if (q == null) return false;
        boolean correct = q.checkAnswer(answer);
        totalAnswered++;
        if (correct) { correctAnswered++; score += q.getPoints(); feedbackMessage = "Correct! +" + q.getPoints() + " pts"; }
        else { feedbackMessage = "Wrong! Answer was: " + q.getCorrectAnswer(); }
        lastAnswerCorrect = correct;
        currentIndex++;
        if (currentIndex >= sessionLimit || currentIndex >= questions.size()) quizFinished = true;
        return correct;
    }

    @Override public int calculateScore() { return score; }

    /** Submit answer by pressing A/B/C/D (mapped to 0/1/2/3) */
    public boolean submitAnswer(int optionIndex) {
        String[] letters = {"A","B","C","D"};
        if (optionIndex < 0 || optionIndex > 3) return false;
        try {
            return checkAnswer(letters[optionIndex]);
        } catch (InvalidAnswerException e) { return false; }
    }

    // ── Accessors ─────────────────────────────────────────────────

    public Question getCurrentQuestion() {
        if (questions.isEmpty()) return null;
        return questions.get((sessionStart + currentIndex) % questions.size());
    }
    public String getCurrentQuestionText() { Question q = getCurrentQuestion(); return q != null ? q.getQuestionText() : ""; }
    public String[] getCurrentOptions() {
        Question q = getCurrentQuestion();
        if (q instanceof MultipleChoiceQuestion) return ((MultipleChoiceQuestion)q).getOptions();
        return new String[0];
    }
    public String getCurrentType() { Question q = getCurrentQuestion(); return q != null ? q.getType() : ""; }
    public boolean isQuizFinished() { return quizFinished; }
    public int getScore() { return score; }
    public int getCorrectAnswered() { return correctAnswered; }
    public int getTotalAnswered() { return totalAnswered; }
    public int getTotalQuestions() { return questions.size(); }

    public String getMotivationalMessage() {
        if (totalAnswered == 0) return "Let's test your knowledge!";
        int pct = (correctAnswered * 100) / totalAnswered;
        if (pct >= 80) return "Outstanding!";
        if (pct >= 60) return "That's good!";
        if (pct >= 40) return "Good try!";
        if (pct >= 20) return "You can do better!";
        return "Don't give up!";
    }

    public void resetQuiz() {
        currentIndex=0; score=0; totalAnswered=0; correctAnswered=0;
        quizFinished=false; sessionStart=0; sessionLimit=questions.size();
        feedbackMessage=""; lastAnswerCorrect=false; selectedOption=-1;
    }

    // ── Draw quiz UI (A/B/C/D selection) ──────────────────────────

    public void drawQuiz(Graphics2D g2, int sw, int sh,
                         Font titleFont, Font normalFont, String inputBuffer) {

        // Dark overlay
        g2.setColor(new Color(0, 0, 0, 220));
        g2.fillRoundRect(20, 20, sw - 40, sh - 40, 20, 20);
        g2.setColor(new Color(100, 200, 255));
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(20, 20, sw - 40, sh - 40, 20, 20);
        g2.setStroke(new BasicStroke(1));

        if (quizFinished) {
            g2.setFont(titleFont);
            g2.setColor(new Color(255, 215, 0));
            drawC(g2, "Quiz Complete!", sw, 80);
            g2.setFont(normalFont);
            g2.setColor(Color.WHITE);
            drawC(g2, "Score: " + score, sw, 120);
            drawC(g2, correctAnswered + " / " + totalAnswered + " correct", sw, 145);
            g2.setColor(new Color(100,255,100));
            drawC(g2, getMotivationalMessage(), sw, 180);
            g2.setColor(new Color(180,180,180));
            drawC(g2, "Press ENTER to continue", sw, sh - 50);
            return;
        }

        Question q = getCurrentQuestion();
        if (q == null) return;

        // Question number
        g2.setFont(normalFont.deriveFont(Font.BOLD, 14f));
        g2.setColor(new Color(150, 200, 255));
        g2.drawString("Question " + (currentIndex + 1) + " / " + sessionLimit, 40, 50);

        // Question text
        g2.setFont(normalFont.deriveFont(15f));
        g2.setColor(Color.WHITE);
        drawWrapped(g2, q.getQuestionText(), 40, 80, sw - 80, 22);

        // Options A/B/C/D
        String[] opts = getCurrentOptions();
        int optY = 160;
        for (int i = 0; i < opts.length; i++) {
            // Highlight selected option
            if (selectedOption == i) {
                g2.setColor(new Color(255, 215, 0));
                g2.fillRoundRect(35, optY - 15, sw - 70, 24, 8, 8);
                g2.setColor(Color.BLACK);
            } else {
                g2.setColor(new Color(220, 220, 220));
            }
            g2.setFont(normalFont.deriveFont(14f));
            g2.drawString(opts[i], 50, optY);
            optY += 35;
        }

        // Feedback
        if (!feedbackMessage.isEmpty()) {
            g2.setFont(normalFont.deriveFont(Font.BOLD, 14f));
            g2.setColor(lastAnswerCorrect ? new Color(100,255,100) : new Color(255,100,100));
            drawC(g2, feedbackMessage, sw, sh - 80);
        }

        // Controls hint
        g2.setFont(normalFont.deriveFont(12f));
        g2.setColor(new Color(180, 180, 180));
        drawC(g2, "Press A, B, C, or D to answer  |  Esc to flee", sw, sh - 40);
        g2.setColor(new Color(100,200,100));
        drawC(g2, "Score: " + score + "  Correct: " + correctAnswered, sw, sh - 55);
    }

    private void drawC(Graphics2D g2, String t, int sw, int y) {
        int x = (sw - g2.getFontMetrics().stringWidth(t)) / 2;
        g2.drawString(t, x, y);
    }
    private void drawWrapped(Graphics2D g2, String text, int x, int y, int maxW, int lh) {
        FontMetrics fm = g2.getFontMetrics();
        StringBuilder line = new StringBuilder();
        for (String word : text.split(" ")) {
            String test = line.length() == 0 ? word : line + " " + word;
            if (fm.stringWidth(test) > maxW && line.length() > 0) {
                g2.drawString(line.toString(), x, y); y += lh;
                line = new StringBuilder(word);
            } else { line = new StringBuilder(test); }
        }
        if (line.length() > 0) g2.drawString(line.toString(), x, y);
    }
}
