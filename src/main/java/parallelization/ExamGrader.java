package parallelization;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExamGrader {

    // You can add new methods, inner classes, etc.

    public interface RoundingFunction {
        int roundGrade(double grade);
    }


    public static class ExamQuestion {
        private double pointsObtained;
        private ExamQuestion nextQuestion;

        public ExamQuestion(double points, ExamQuestion next) {
            this.pointsObtained = points;
            this.nextQuestion = next;
        }

        public double getPointsObtained() {
            return pointsObtained;
        }

        public ExamQuestion getNextQuestion() {
            return nextQuestion;
        }
    }

    /**
     *  Write a method calculateExamGrade that calculates the grade
     *  obtained for an exam. The grade is the sum of the points
     *  obtained in all questions. The questions are provided in a
     *  linked list (class ExamQuestion).
     *
     *  However, the points are real numbers, while the exam grade
     *  is a natural number. To round the grade, the caller of this
     *  method provides a rounding function that you have to use
     *  to obtain the final result.
     *
     *  Look at the test "testWithTwoQuestions" to see how users will
     *  call this method.
     *
     *  You can assume that all points are positive numbers and that
     *  the list of questions is not empty.
     */
    public static int calculateExamGrade(ExamQuestion questions, RoundingFunction roundingFunction) {
        // STUDENT return 0;
        // BEGIN STRIP
        int score = 0;

        while(questions != null){
            int rounded = roundingFunction.roundGrade(questions.getPointsObtained());
            score += rounded;
            questions = questions.getNextQuestion();
        }
        return score;
        // END STRIP
    }


    /**
     * Write a method gradeExams that calculates the grades of two exams
     * using two threads to accelerate the grading (one exam graded per thread).
     * The method must return the two grades in an int array.
     * Like for the method calculateExamGrade, the caller of this method
     * provides a rounding function.
     *
     * Look at the test "testTwoShortExams" to see how users will
     * call this method.
     *
     * You MUST use threads (or a threadpool).
     * Catch (and ignore) all exceptions.
     */
    public static int[] gradeExams(ExamQuestion exam1, ExamQuestion exam2, RoundingFunction roundingFunction) {
        // STUDENT return null;
        // BEGIN STRIP
        int[] ret = new int[2];
        ExamQuestion[] exam = new ExamQuestion[]{exam1, exam2};
        ExecutorService exec = Executors.newFixedThreadPool(2);
        Future<Integer>[] f = new Future[2];
        for (int i = 0; i < 2; i++){
            int finalI = i;
            f[i] = exec.submit(() -> calculateExamGrade(exam[finalI], roundingFunction));
        }

        for (int i = 0; i < 2; i++){
            try{
                ret[i] = f[i].get();
            }
            catch (Exception ignored){}
        }

        exec.shutdown();
        return ret;
        // END STRIP
    }
}

