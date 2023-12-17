package fp;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class StudentStream {
    public static class Grade {
        private String course;
        private double value;

        public Grade(String course,
                     double value) {
            this.course = course;
            this.value = value;
        }

        public String getCourse() {
            return course;
        }

        public double getValue() {
            return value;
        }
    }
    
    public static class Student {
        private String firstName;
        private String lastName;
        private int section;
        private Map<String, Double> grades = new HashMap<>();

        public Map<String, Double> getCourses_results() {  // TODO REMOVE
            return grades;
        }

        public Student(String firstName,
                       String lastName,
                       int section) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.section = section;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public int getSection() {
            return section;
        }

        public void addGrade(String course,
                             double grade) {
            grades.put(course, grade);
        }

        public boolean hasGrade(String course) {
            return grades.containsKey(course);
        }

        public double getGrade(String course) {
            if (hasGrade(course)) {
                return grades.get(course);
            } else {
                throw new IllegalArgumentException();
            }
        }

        public Stream<Grade> getGradesStream() {
            List<Grade> lst = new ArrayList<>();
            for (Map.Entry<String, Double> entry : grades.entrySet()) {
                lst.add(new Grade(entry.getKey(), entry.getValue()));
            }
            return lst.stream();
        }
    }

    /**
     * Find the N°2 and N°3 top students for the course name given as parameter.
     **/
    public static Stream<Student> findSecondAndThirdTopStudentForGivenCourse(Stream<Student> students, String name) {
        // TODO
        // STUDENT return null;
        // BEGIN STRIP
        Stream<Student> s = students.sorted((s1, s2) -> {
                double v1 = s1.getGrade(name);
                double v2 = s2.getGrade(name);
                if (v1 > v2) {
                    return -1;
                } else if (v1 < v2) {
                    return 1;
                } else {
                    return 0;
                }
            });
        return s.skip(1).limit(2);
        // END STRIP
    }


    /**
     * Compute, for each student in the given section, their average grade result,
     * in the form of an array of arrays structured as follows:
     *
     * [ ["Student FirstName1 LastName1", 7.5], ["Student FirstName2 LastName2", 9.5] ]
     **/
    public static Object[] computeAverageForStudentInSection(Stream<Student> students, int section) {
        // TODO
        // STUDENT return null;
        // BEGIN STRIP
        return students.filter((s)->s.getSection() == section).map((s) -> {
                double average = s.getGradesStream().map(x -> x.getValue()).reduce(0.0, (a, b) -> a + b) / (double) s.getGradesStream().count();
                return new Object[] { "Student " + s.getFirstName() + " " + s.getLastName(), new Double(average) };
            }).toArray();
        // END STRIP
    }

    /**
     * Give the number of students that passed all courses (all grades > 10.0).
     **/
    public static long getNumberOfSuccessfulStudents(Stream<Student> students) {
        // TODO
        // STUDENT return 0;
        // BEGIN STRIP
        return students.filter(student -> student.getGradesStream().allMatch(grade -> grade.getValue() > 10.0)).count();
        // END STRIP
    }

    /**
     * Find the last student in lexicographic order (First compare
     * students on their lastNames and then on their firstNames)
     **/
    public static Student findLastInLexicographicOrder(Stream<Student> students) {
        // TODO
        // STUDENT return null;
        // BEGIN STRIP
        return students.sorted((a, b) -> {
                int r = a.getLastName().compareTo(b.getLastName());
                if (r == 0) {
                    return a.getFirstName().compareTo(b.getFirstName());
                } else {
                    return r;
                }
            }).reduce(null, (a, b) -> b);
        // END STRIP
    }

    /**
     * Give the total sum of all grades obtained by all students.
     **/
    public static double getFullSum(Stream<Student> students) {
        // TODO
        // STUDENT return 0;
        // BEGIN STRIP
        return students.map(s -> s.getGradesStream().map(x -> x.getValue()).reduce(0.0, (a, b) -> a + b)).reduce(0.0, (a, b) -> a + b);
        // END STRIP
    }


    // BEGIN STRIP
    private static boolean isMatch(Student s, Map<String, Predicate<?>> conditions) {
        for (Map.Entry<String, Predicate<?>> entry : conditions.entrySet()) {
            String field = entry.getKey();
            if (field.equals("firstName")) {
                Predicate<String> predicate = (Predicate<String>) entry.getValue();
                if (!predicate.test(s.getFirstName())) {
                    return false;
                }
            } else if (field.equals("lastName")) {
                Predicate<String> predicate = (Predicate<String>) entry.getValue();
                if (!predicate.test(s.getLastName())) {
                    return false;
                }
            } else if (field.equals("section")) {
                Predicate<Integer> predicate = (Predicate<Integer>) entry.getValue();
                if (!predicate.test(s.getSection())) {
                    return false;
                }
            } else if (field.equals("courses_results")) {
                Predicate<Map<String, Double>> predicate = (Predicate<Map<String, Double>>) entry.getValue();
                if (!predicate.test(s.getCourses_results())) {
                    return false;
                }
            } else {
                throw new UnsupportedOperationException();
            }
        }
        return true;
    }
    // END STRIP
    
    
    // In order to test your code efficiently, input conditions take the
    // form of a Map<String, Predicate<?>> object structured as follows:
    //    Key   : String corresponding to one of the fields of Student objects
    //            ("firstName", "lastName", "section", "courses_results")
    //    Value : Predicate bound to the type of the field on which the condition applies
    //
    // For example:
    //    Key   : "firstName"
    //    Value : Predicate<String>


    // Returns a student that matches the given conditions
    // If there is none, returns null
    public static Student findFirst(Stream<Student> studentsStream,
                                    Map<String, Predicate<?>> conditions) {
        // TODO
        // STUDENT return null;
        // BEGIN STRIP
        Optional<Student> o = studentsStream.filter((s) -> isMatch(s, conditions)).findFirst();
        if (o.isPresent()) {
            return o.get();
        } else {
            return null;
        }
        // END STRIP
    }

    // Returns an array of student(s) that match the given conditions
    public static Student[] findAll(Stream<Student> studentsStream,
                                    Map<String, Predicate<?>> conditions) {
        // TODO
        // STUDENT return null;
        // BEGIN STRIP
        return studentsStream.filter((s) -> isMatch(s, conditions)).toArray(Student[]::new);
        // END STRIP
    }

    // Returns true if there are at least n student(s) that match the given conditions
    public static boolean exists(Stream<Student> studentsStream,
                                 Map<String, Predicate<?>> conditions,
                                 int n) {
        // TODO
        // STUDENT return false;
        // BEGIN STRIP
        return studentsStream.filter((s)->isMatch(s, conditions)).count() >= n;
        // END STRIP
    }

    // Returns an array of student(s) that match the given conditions,
    // ordered according to the given comparator
    public static Student[] filterThenSort(Stream<Student> studentsStream,
                                           Map<String, Predicate<?>> conditions,
                                           Comparator<Student> comparator) {
        // TODO
        // STUDENT return null;
        // BEGIN STRIP
        return studentsStream.filter((s)->isMatch(s, conditions)).sorted(comparator).toArray(Student[]::new);
        // END STRIP
    }
}
