package quiz;

/**
 * TrueFalseQuestion - player types True or False.
 * Demonstrates: inheritance, method overriding
 */
public class TrueFalseQuestion extends Question {

    public TrueFalseQuestion(String questionText, String correctAnswer, int points) {
        super(questionText, correctAnswer, points);
    }

    @Override
    public boolean checkAnswer(String answer) throws InvalidAnswerException {
        if (answer == null || answer.trim().isEmpty())
            throw new InvalidAnswerException("Answer cannot be empty.");
        String a = answer.trim().toLowerCase();
        String c = correctAnswer.trim().toLowerCase();
        if (a.equals("t")) a = "true";
        if (a.equals("f")) a = "false";
        return a.equals(c);
    }

    @Override public String getType() { return "TF"; }
}
