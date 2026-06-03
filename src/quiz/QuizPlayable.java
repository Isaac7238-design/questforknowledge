package quiz;

/**
 * QuizPlayable interface - implemented by QuizManager.
 *
 * Created by: Nathanael
 * Tested by: Habib
 * Purpose: Define the contract for quiz start, answer checking, and score calculation.
 */
public interface QuizPlayable {
    void startQuiz();
    boolean checkAnswer(String answer) throws InvalidAnswerException;
    int calculateScore();
}
