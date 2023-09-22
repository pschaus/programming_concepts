package basics;


import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Grade
public class KNNTest {


    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void test1() {

        // small example based on two inginious exercises
        KNN.Student[] students = new KNN.Student[] {
                new KNN.Student(new double[]{90,91}, true),
                new KNN.Student(new double[]{80,75}, true),
                new KNN.Student(new double[]{70,65}, false), // surprisingly, this student has failed despite a good preparation
                new KNN.Student(new double[]{30,40}, true),  // surprisingly, this student has succeeded despite a poor preparation
                new KNN.Student(new double[]{20,30}, false),
                new KNN.Student(new double[]{45,33}, false),
        };


        // let's take a student with good score at the exercises for our prediction with k=3
        double [] goodResults = new double[]{88,95};
        assertTrue(KNN.predictSuccess(students,goodResults,3));

        // let's take a student with poor score at the exercises for our prediction with k=3
        double [] badResults = new double[]{35,41};
        assertFalse(KNN.predictSuccess(students,badResults,3));

        // k = 6 = the number of students in the data (3 true, 3 false)
        // so not strict majority of true, the prediction should be false
        assertFalse(KNN.predictSuccess(students,goodResults,6));

    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void test2() {

        // small example based on one a single inginious exercises
        KNN.Student[] students = new KNN.Student[] {
                new KNN.Student(new double[]{90}, true),
                new KNN.Student(new double[]{81}, true),
                new KNN.Student(new double[]{50}, false),
                new KNN.Student(new double[]{50}, true),
                new KNN.Student(new double[]{20}, false),
                new KNN.Student(new double[]{15}, false),
        };

        double [] goodResults = new double[]{92};
        assertTrue(KNN.predictSuccess(students,goodResults,3));
        double [] badResults = new double[]{21};
        assertFalse(KNN.predictSuccess(students,badResults,3));

    }


    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void test3() {

        KNN.Student[] students = new KNN.Student[] {
                new KNN.Student(new double[]{100,100}, true),
                new KNN.Student(new double[]{90,91}, true),
                new KNN.Student(new double[]{80,75}, true),
                new KNN.Student(new double[]{70,65}, false),
                new KNN.Student(new double[]{70,33}, false),
                new KNN.Student(new double[]{70,50}, true),
                new KNN.Student(new double[]{70,54}, true),
                new KNN.Student(new double[]{30,48}, false),
                new KNN.Student(new double[]{90,10}, false),
                new KNN.Student(new double[]{0,100}, true),
                new KNN.Student(new double[]{30,40}, true),
                new KNN.Student(new double[]{20,30}, false),
                new KNN.Student(new double[]{45,33}, false),
        };




        assertFalse(KNN.predictSuccess(students, new double[]{66,20},6));
        assertFalse(KNN.predictSuccess(students, new double[]{66,20},4));
        assertTrue(KNN.predictSuccess(students, new double[]{0,100},1));
        assertFalse(KNN.predictSuccess(students, new double[]{70,33},1));
        assertTrue(KNN.predictSuccess(students, new double[]{50,50},5));
        assertTrue(KNN.predictSuccess(students, new double[]{60,80},5));

    }


    // BEGIN STRIP

    static class Student {

        final double [] grades; // grades[i] = inginious grades obtained by this student for exercise i for LEPL1402
        final boolean success; // true if the student succeeded at the exam, false otherwise

        public Student(double [] grades, boolean success) {
            this.grades = grades;
            this.success = success;
        }
    }

    public boolean noTies(Student[] students, double[] grades, int k) {
        Student[] s = students.clone();
        Arrays.sort(s, new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                return (int) Math.signum(euclideanDistance(s1.grades, grades) - euclideanDistance(s2.grades, grades));
            }
        });
        return euclideanDistance(grades,s[k-1].grades) != euclideanDistance(grades,s[k].grades);
    }

    public boolean minDistOne(Student[] students, double[] grades, int k) {
        Student[] s = students.clone();
        Arrays.sort(s, new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                return (int) Math.signum(euclideanDistance(s1.grades, grades) - euclideanDistance(s2.grades, grades));
            }
        });
        double dist = Math.abs(euclideanDistance(s[k-1].grades, grades) - euclideanDistance(s[k].grades, grades));
        if (dist < 1) {
            return false;
        }

        return true;
    }

    public static boolean predictSuccessExpected(Student[] students, double[] grades, int k) {
        Arrays.sort(students, new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                return (int) Math.signum(euclideanDistance(s1.grades, grades) - euclideanDistance(s2.grades, grades));
            }
        });
        int numberOfSuccess = 0;
        for (int i = 0; i < k; i++) {
            if (students[i].success) {
                numberOfSuccess++;
            }
        }
        if (numberOfSuccess > k / 2) {
            return true;
        } else {
            return false;
        }
    }

    private static double euclideanDistance(double[] a, double[] b) {
        double dist = 0;
        for (int i = 0; i < a.length; i++) {
            dist += Math.pow(a[i] - b[i], 2);
        }
        return Math.sqrt(dist);
    }



    @Test
    @Grade(value = 2, cpuTimeout = 1000)
    public void testRandomCastOKEqualityNoAmbiguitySmall() {
        int n = 10;
        Student [] studentsExpected = new Student[n];
        KNN.Student [] students = new KNN.Student[n];
        Random rand = new Random(0);

        for (int i = 0; i < n; i+=1) {
            studentsExpected[i] = new Student(new double[] {rand.nextInt(100)},rand.nextBoolean());
            students[i] = new KNN.Student(studentsExpected[i].grades.clone(), studentsExpected[i].success);
        }


        for (int i = 0; i < 1000; i++) {
            double [] student = new double[] {rand.nextInt(100)};
            // possible bug in case of equal distance to the students between the k and k+1
            if (minDistOne(studentsExpected,student,4) && noTies(studentsExpected,student,4)) {
                assertEquals(predictSuccessExpected(studentsExpected,student,4), KNN.predictSuccess(students,student,4));
            }
            if (minDistOne(studentsExpected,student,7) && noTies(studentsExpected,student,7)) {
                assertEquals(predictSuccessExpected(studentsExpected,student,7), KNN.predictSuccess(students,student,7));
            }
        }
    }



    @Test
    @Grade(value = 7, cpuTimeout = 1000)
    public void testRandomCastOKSmall() {
        int n = 10;
        Student [] studentsExpected = new Student[n];
        KNN.Student [] students = new KNN.Student[n];
        Random rand = new Random(0);

        for (int i = 0; i < n; i+=1) {
            studentsExpected[i] = new Student(new double[] {rand.nextDouble()*100},rand.nextBoolean());
            students[i] = new KNN.Student(studentsExpected[i].grades.clone(), studentsExpected[i].success);
        }


        for (int i = 0; i < 100; i++) {
            double [] student = new double[] {rand.nextInt(100)};
            // possible bug in case of equal distance to the students between the k and k+1
            if (minDistOne(studentsExpected,student,4) && noTies(studentsExpected,student,4)) {
                assertEquals(predictSuccessExpected(studentsExpected,student,4), KNN.predictSuccess(students,student,4));
            }
            if (minDistOne(studentsExpected,student,7) && noTies(studentsExpected,student,7)) {
                assertEquals(predictSuccessExpected(studentsExpected,student,7), KNN.predictSuccess(students,student,7));
            }
        }
    }

    @Test
    @Grade(value = 9, cpuTimeout = 1000)
    public void testRandomCastOKMedium() {
        int n = 400;
        Student [] studentsExpected = new Student[n];
        KNN.Student [] students = new KNN.Student[n];
        Random rand = new Random(0);
        int d = 10; // number of inginious exercices

        for (int i = 0; i < n; i+=1) {
            double [] grades = new double[d];
            for (int j = 0; j < d; j++) {
                grades[j] = rand.nextInt(100);
            }
            studentsExpected[i] = new Student(grades,rand.nextBoolean());
            students[i] = new KNN.Student(studentsExpected[i].grades.clone(), studentsExpected[i].success);
        }


        for (int i = 0; i < 100; i++) {

            double [] student = new double[d];
            for (int j = 0; j < d; j++) {
                student[j] = rand.nextInt(100);
            }

            // possible bug in case of equal distance to the students between the k and k+1
            if (minDistOne(studentsExpected,student,4) && noTies(studentsExpected,student,4)) {
                assertEquals(predictSuccessExpected(studentsExpected,student,4), KNN.predictSuccess(students,student,4));
            }
            if (minDistOne(studentsExpected,student,10) && noTies(studentsExpected,student,10)) {
                assertEquals(predictSuccessExpected(studentsExpected,student,10), KNN.predictSuccess(students,student,10));
            }
        }
    }

    @Test
    @Grade(value = 9, cpuTimeout = 1000)
    public void testRandomCastSentitiveAndComplexity() {
        int n = 1000;
        Student [] studentsExpected = new Student[n];
        KNN.Student [] students = new KNN.Student[n];
        Random rand = new Random(0);

        for (int i = 0; i < n; i++) {
            double [] res = new double[5];
            for (int j = 0; j < 5; j++) {
                res[j] = rand.nextDouble()*100;

            }
            studentsExpected[i] = new Student(res, rand.nextBoolean());
            students[i] = new KNN.Student(studentsExpected[i].grades.clone(), studentsExpected[i].success);
        }

        for (int i = 0; i < 200; i++) {
            double [] student = new double[] {rand.nextInt(100), rand.nextInt(100), rand.nextInt(100), rand.nextInt(100), rand.nextInt(100)};
            // possible bug in case of equal distance to the students between the k and k+1
            if (noTies(studentsExpected,student,4)) {
                assertEquals(predictSuccessExpected(studentsExpected,student,4), KNN.predictSuccess(students,student,4));
            }
            if (noTies(studentsExpected,student,10)) {
                assertEquals(predictSuccessExpected(studentsExpected,student,10), KNN.predictSuccess(students,student,10));
            }
        }
    }



    // END STRIP

}
