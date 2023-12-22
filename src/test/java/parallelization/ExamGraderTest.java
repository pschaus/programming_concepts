package parallelization;

import org.javagrader.Allow;
import org.javagrader.Grade;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

@Grade
@Allow("java.lang.Thread")
public class ExamGraderTest {
    ExamGrader.ExamQuestion q1 = new ExamGrader.ExamQuestion(5.5, null);
    ExamGrader.ExamQuestion q2 = new ExamGrader.ExamQuestion(4.7, q1);
    ExamGrader.ExamQuestion q3 = new ExamGrader.ExamQuestion(4.0, q2);

    @Test
    @Grade(value=1, cpuTimeout=1000)
    public void testWithOneQuestion() {
        int examGradeRoundedDown = ExamGrader.calculateExamGrade(q1, new ExamGrader.RoundingFunction() {
            @Override
            public int roundGrade(double sum) {
                return (int) Math.floor(sum);
            }
        });
        assertEquals(5, examGradeRoundedDown);
        int examGradeRoundedUp = ExamGrader.calculateExamGrade(q1, new ExamGrader.RoundingFunction() {
            @Override
            public int roundGrade(double sum) {
                return (int) Math.ceil(sum);
            }
        });
        assertEquals(6, examGradeRoundedUp);
    }

    @Test
    //BEGIN STRIP
    @Grade(value=1, cpuTimeout=1000)
    // END STRIP
    public void testWithTwoQuestions() {
        // calculate the exam grade by rounding down
        int examGradeRoundedDown = ExamGrader.calculateExamGrade(q2, new ExamGrader.RoundingFunction() {
            @Override
            public int roundGrade(double sum) {
                return (int) Math.floor(sum);
            }
        });
        assertTrue(examGradeRoundedDown == 10 || examGradeRoundedDown == 9);

        // calculate the exam grade by rounding up
        int examGradeRoundedUp = ExamGrader.calculateExamGrade(q2, new ExamGrader.RoundingFunction() {
            @Override
            public int roundGrade(double sum) {
                return (int) Math.ceil(sum);
            }
        });
        assertEquals(11, examGradeRoundedUp);
    }

    @Test
    @Grade(value=1, cpuTimeout=1000)
    public void testWithRandomQuestions() {
        // create eleven exam questions, each with 1.5 points
        ExamGrader.ExamQuestion questions=null;
        for(int i=0;i<11;i++) {
            questions = new ExamGrader.ExamQuestion(1.5, questions);
        }

        int examGradeRoundedDown = ExamGrader.calculateExamGrade(questions, new ExamGrader.RoundingFunction() {
            @Override
            public int roundGrade(double sum) {
                return (int) Math.floor(sum);
            }
        });

        assertTrue(examGradeRoundedDown == 16 || examGradeRoundedDown == 11);

        int examGradeRoundedUp = ExamGrader.calculateExamGrade(questions, new ExamGrader.RoundingFunction() {
            @Override
            public int roundGrade(double sum) {
                return (int) Math.ceil(sum);
            }
        });
        assertTrue(examGradeRoundedUp == 17 || examGradeRoundedUp == 22);
    }

    @Test
    @Grade(value=1, cpuTimeout=1000)
    public void testTwoShortExams() {
        // The first exam has questions q1 and q2.
        // The second exam has questions q1, q2, and q3.

        int[] resultsRoundedDown = ExamGrader.gradeExams(q2, q3, new ExamGrader.RoundingFunction() {
            @Override
            public int roundGrade(double sum) {
                return (int) Math.floor(sum);
            }
        });
        assertEquals(2, resultsRoundedDown.length);

        assertTrue(resultsRoundedDown[0] == 10 || resultsRoundedDown[0] == 9);
        assertTrue(resultsRoundedDown[1] == 14 || resultsRoundedDown[1] == 13);


        int[] resultsRoundedUp = ExamGrader.gradeExams(q2, q3, new ExamGrader.RoundingFunction() {
            @Override
            public int roundGrade(double sum) {
                return (int) Math.ceil(sum);
            }
        });
        assertEquals(2, resultsRoundedUp.length);
        assertEquals( resultsRoundedUp[0], 11);
        assertEquals( resultsRoundedUp[1], 15);

        assertTrue(resultsRoundedUp[0] == 11 || resultsRoundedUp[0] == 11);
        assertTrue(resultsRoundedUp[1] == 15 || resultsRoundedUp[1] == 15);
    }

    @Test
    @Grade(value=1, cpuTimeout=1000)
    @Allow("all")
    public void testDelayedExams() {
        // The first exam has questions q1 and q2.
        // The second exam has questions q1, q2, and q3.
        Random rng = new Random();
        int[] resultsRoundedUp = ExamGrader.gradeExams(q2, q3, new ExamGrader.RoundingFunction() {
            @Override
            public int roundGrade(double sum) {
                try {
                    // this exams need some time to grade them...
                    Thread.sleep(300 + rng.nextInt(500));
                } catch (InterruptedException e) {
                }
                return (int) Math.ceil(sum);
            }
        });
        assertEquals(2, resultsRoundedUp.length);
        assertEquals(11, resultsRoundedUp[0]);
        assertEquals(15, resultsRoundedUp[1]);
    }

    @Test
    @Grade(value=1, cpuTimeout=1000)
    public void testTwoRandomExams() {
        // For the first exam, create twenty exam questions, each with 0.71 points
        ExamGrader.ExamQuestion exam1=null;
        for(int i=0;i<20;i++) {
            exam1 = new ExamGrader.ExamQuestion(0.71, exam1);
        }

        // For the second exam, create twenty exam questions, each with 0.67 points
        ExamGrader.ExamQuestion exam2=null;
        for(int i=0;i<20;i++) {
            exam2 = new ExamGrader.ExamQuestion(1.67, exam2);
        }

        int[] resultsRoundedUp = ExamGrader.gradeExams(exam1, exam2, new ExamGrader.RoundingFunction() {
            @Override
            public int roundGrade(double sum) {
                return (int) Math.ceil(sum);
            }
        });
        assertEquals(2, resultsRoundedUp.length);

        assertTrue(resultsRoundedUp[0] == 15 || resultsRoundedUp[0] == 20);
        assertTrue(resultsRoundedUp[1] == 34 || resultsRoundedUp[1] == 40);
    }
}

