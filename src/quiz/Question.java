package quiz;

// Author: Nathanael | Tester: Habib
/**
 * Question - abstract base for all quiz questions.
 * Subclasses: MultipleChoiceQuestion, TrueFalseQuestion, FillBlankQuestion.
 *
 */
public abstract class Question {

 protected String questionText;
 protected String correctAnswer;
 protected int points;

 public Question(String questionText, String correctAnswer, int points) {
 this.questionText = questionText;
 this.correctAnswer = correctAnswer;
 this.points = points;
 }

 public abstract boolean checkAnswer(String answer) throws InvalidAnswerException;
 public abstract String getType();

 public String getQuestionText() { return questionText; }
 public String getCorrectAnswer() { return correctAnswer; }
 public int getPoints() { return points; }
}
