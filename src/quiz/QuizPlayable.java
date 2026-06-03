package quiz;

// Author: Nathanael | Tester: Habib
/**
 * QuizPlayable interface - implemented by QuizManager.
 *
 */
public interface QuizPlayable {
 void startQuiz();
 boolean checkAnswer(String answer) throws InvalidAnswerException;
 int calculateScore();
}
