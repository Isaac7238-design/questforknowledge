package quiz;

/** QuizPlayable interface - implemented by QuizManager. */
public interface QuizPlayable {
    void startQuiz();
    boolean checkAnswer(String answer) throws InvalidAnswerException;
    int calculateScore();
}
