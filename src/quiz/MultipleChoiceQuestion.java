package quiz;

/**
 * MultipleChoiceQuestion - player picks A/B/C/D.
 * Demonstrates: inheritance, method overriding
 */
public class MultipleChoiceQuestion extends Question {

    private String[] options;

    public MultipleChoiceQuestion(String questionText, String[] options,
                                  String correctAnswer, int points) {
        super(questionText, correctAnswer, points);
        this.options = options;
    }

    @Override
    public boolean checkAnswer(String answer) throws InvalidAnswerException {
        if (answer == null || answer.trim().isEmpty())
            throw new InvalidAnswerException("Answer cannot be empty.");
        return answer.trim().equalsIgnoreCase(correctAnswer.trim());
    }

    @Override public String   getType()    { return "MC"; }
    public        String[]    getOptions() { return options; }
}
