package quiz;

/**
 * FillBlankQuestion - player types the missing word.
 * Demonstrates: inheritance, method overriding
 */
public class FillBlankQuestion extends Question {

    public FillBlankQuestion(String questionText, String correctAnswer, int points) {
        super(questionText, correctAnswer, points);
    }

    @Override
    public boolean checkAnswer(String answer) throws InvalidAnswerException {
        if (answer == null || answer.trim().isEmpty())
            throw new InvalidAnswerException("Answer cannot be empty.");
        return answer.trim().equalsIgnoreCase(correctAnswer.trim());
    }

    @Override public String getType() { return "FILL"; }
}
