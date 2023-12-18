package fp;

/**
 * This is the fusion of "Streams" and "Streams2" exercices from:
 * https://github.com/UCL-INGI/LEPL1402
 **/


import org.javagrader.Grade;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;


// To have nice double numbers like 10.42
import java.math.BigDecimal;
import java.math.RoundingMode;

@Grade
public class StudentStreamTest {
    // For simplicity , use them both for first name / last name
    private String[] studentNames = new String[]{
            "Jacques", "John", "Marie", "Emma", "Olivia", "Alice", "Juliette",
            "Louise", "Jules", "Victor", "Lucas", "Gabriel", "Noah", "Hugo"
    };

    // for the section field
    private Supplier<Integer> section_rng = () -> (int) (Math.random() * 10) + 1; // between 1 and 10

    // for course grade
    private Supplier<Double> grade_rng = () -> BigDecimal
            .valueOf(Math.random() * 20)
            .setScale(2, RoundingMode.HALF_UP)
            .doubleValue();
    private String[] courses = new String[]{"Algorithmn", "Proba & Stat", "ORG"};

    // generate random students
    private Stream<StudentStream.Student> generate_random_students(int number) {
        List<StudentStream.Student> my_list = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            // get random index / values
            int index = new Random().nextInt(studentNames.length);
            int index2 = new Random().nextInt(studentNames.length);
            int section = section_rng.get();
            StudentStream.Student student = new StudentStream.Student(studentNames[index], studentNames[index2], section);
            for (String course : courses) {
                student.addGrade(course, grade_rng.get());
            }
            my_list.add(student);
        }

        return my_list.stream();
    }

    private static void compareStudents(List<StudentStream.Student> a,
                                        List<StudentStream.Student> b) {
        assertEquals(a.size(), b.size());

        for (int j = 0; j < a.size(); j++) {
            assertEquals(a.get(j).getFirstName(), b.get(j).getFirstName());
            assertEquals(a.get(j).getLastName(), b.get(j).getLastName());
            assertEquals(a.get(j).getSection(), b.get(j).getSection());

            List<StudentStream.Grade> c = a.get(j).getGradesStream().sorted((x, y) -> x.getCourse().compareTo(y.getCourse())).collect(Collectors.toList());
            List<StudentStream.Grade> d = b.get(j).getGradesStream().sorted((x, y) -> x.getCourse().compareTo(y.getCourse())).collect(Collectors.toList());
            assertEquals(c.size(), d.size());

            for (int k = 0; k < c.size(); k++) {
                assertEquals(c.get(k).getValue(), d.get(k).getValue(), 0.0000001);
            }
        }
    }

    private static void compareAverages(List<StudentStream.StudentAverage> a,
                                        List<StudentStream.StudentAverage> b) {
        assertEquals(a.size(), b.size());

        for (int j = 0; j < a.size(); j++) {
            assertEquals(a.get(j).getFirstName(), b.get(j).getFirstName());
            assertEquals(a.get(j).getLastName(), b.get(j).getLastName());
            assertEquals(a.get(j).getAverage(), b.get(j).getAverage(), 0.000001);
        }
    }

    private static class TestConditions {
        public StudentStream.StudentConditions newImplementation;
        public Map<String, Predicate<?>> oldImplementation;
    }

    private TestConditions generateConditions(int limit) {
        // some predicates inside
        Map<String, Predicate<?>> conditions = new HashMap<>();
        String randomName = studentNames[new Random().nextInt(studentNames.length)];
        Predicate<String> p1 = (s) -> s.equalsIgnoreCase(randomName);

        int randomInt = section_rng.get();
        Predicate<Integer> p2 = (i) -> i == randomInt;
        String courseRandom;
        if (new Random().nextInt(3) == 0) {
            courseRandom = "MISSING_COURSE";
        } else {
            courseRandom = courses[new Random().nextInt(courses.length)];
        }
        Double d = grade_rng.get();

        Predicate<Map<String, Double>> p3 = (m) -> m.get(courseRandom) >= d;
        conditions.put("firstName", p1);
        conditions.put("lastName", p1);
        conditions.put("section", p2);
        conditions.put("courses_results", p3);

        TestConditions result = new TestConditions();
        result.oldImplementation = conditions
                .entrySet()
                .stream()
                .limit(limit)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        result.newImplementation = new StudentStream.StudentConditions();

        for (Map.Entry<String, Predicate<?>> entry : result.oldImplementation.entrySet()) {
            if (entry.getKey().equals("firstName")) {
                result.newImplementation.setFirstNamePredicate((Predicate<String>) entry.getValue());
                assertEquals(entry.getValue(), p1);
            } else if (entry.getKey().equals("lastName")) {
                result.newImplementation.setLastNamePredicate((Predicate<String>) entry.getValue());
                assertEquals(entry.getValue(), p1);
            } else if (entry.getKey().equals("section")) {
                result.newImplementation.setSectionPredicate((Predicate<Integer>) entry.getValue());
                assertEquals(entry.getValue(), p2);
            } else if (entry.getKey().equals("courses_results")) {
                assertEquals(entry.getValue(), p3);
                result.newImplementation.addCoursePredicate(courseRandom, m -> m >= d);
            }
        }

        return result;
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testFindSecondAndThirdTopStudentForGivenCourse() {

        for (int i = 0; i < 10; i++) {
            // random students ( at least 3 )
            StudentStream.Student[] random_students = generate_random_students(3 + section_rng.get()).toArray(StudentStream.Student[]::new);
            String course = courses[new Random().nextInt(courses.length)];

            List<StudentStream.Student> expected = Stream
                    .of(random_students)
                    .sorted(
                            ((Comparator<StudentStream.Student>) (o1, o2) -> {
                                double d1 = o1.getGrade(course);
                                double d2 = o2.getGrade(course);
                                return Double.compare(d1, d2);
                            }).reversed()
                    )
                    .limit(3)
                    .skip(1)
                    .collect(Collectors.toList());

            List<StudentStream.Student> result = StudentStream
                    .findSecondAndThirdTopStudentForGivenCourse(
                            Stream.of(random_students),
                            course
                    ).collect(Collectors.toList());

            compareStudents(expected, result);
        }
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testComputeAverageForStudentInSection() {
        for (int i = 0; i < 10; i++) {

            StudentStream.Student[] random_students = generate_random_students(section_rng.get()).toArray(StudentStream.Student[]::new);
            int section = section_rng.get();

            List<StudentStream.StudentAverage> expected = Stream
                    .of(random_students)
                    .filter(s -> s.getSection() == section)
                    .map(student -> new StudentStream.StudentAverage(
                            student.getFirstName(), student.getLastName(),
                            student.getGradesStream().map(x -> x.getValue()).reduce(0.0, (a, b) -> a + b) / (double) student.getGradesStream().count()))
                    .collect(Collectors.toList());

            List<StudentStream.StudentAverage> result = StudentStream.computeAverageForStudentInSection(Stream.of(random_students), section).collect(Collectors.toList());

            compareAverages(expected, result);
        }
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testGetNumberOfSuccessfulStudents() {
        for (int i = 0; i < 10; i++) {

            StudentStream.Student[] random_students = generate_random_students(section_rng.get()).toArray(StudentStream.Student[]::new);

            long expected = Stream
                    .of(random_students)
                    .filter(student ->
                            student.getGradesStream().allMatch(grade -> grade.getValue() > 10.0)
                    ).count();

            long result = StudentStream.getNumberOfSuccessfulStudents(Stream.of(random_students));
            assertEquals(expected, result);
        }
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testFindLastInLexicographicOrder() {
        for (int i = 0; i < 1000; i++) {

            StudentStream.Student[] random_students = generate_random_students(section_rng.get()).toArray(StudentStream.Student[]::new);

            StudentStream.Student expected = Stream
                    .of(random_students)
                    .sorted(
                            (o1, o2) ->
                                    Comparator
                                            .comparing(StudentStream.Student::getLastName, Comparator.reverseOrder())
                                            .thenComparing(StudentStream.Student::getFirstName, Comparator.reverseOrder())
                                            .compare(o1, o2)
                    )
                    .limit(1)
                    .findFirst()
                    .get();

            StudentStream.Student result = StudentStream.findLastInLexicographicOrder(Stream.of(random_students));
            assertEquals(expected.getLastName(), result.getLastName());
            assertEquals(expected.getFirstName(), result.getFirstName());
        }
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testGetFullSum() {
        for (int i = 0; i < 10; i++) {

            StudentStream.Student[] random_students = generate_random_students(section_rng.get()).toArray(StudentStream.Student[]::new);

            double expected = Stream
                    .of(random_students)
                    .map(
                            student -> student
                                    .getGradesStream()
                                    .map(x -> x.getValue())
                                    .reduce(0.0, Double::sum)
                    )
                    .reduce(0.0, Double::sum);

            double result = StudentStream.getFullSum(Stream.of(random_students));
            assertEquals(expected, result, 0.00000001);
        }
    }


    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void findOne_simple() {
        StudentStream.Student student = new StudentStream.Student("Jacques", "42", 42);
        StudentStream.Student student1 = new StudentStream.Student("Jacques", "42", 12);

        StudentStream.StudentConditions conditions = new StudentStream.StudentConditions();
        conditions.setFirstNamePredicate(s -> s.equalsIgnoreCase("jacques"));
        conditions.setSectionPredicate(i -> i == 42);

        assertEquals(student, StudentStream.findFirst(Stream.of(student), conditions));
        assertNull(StudentStream.findFirst(Stream.of(student1), conditions));
    }


    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void findOne_advanced() {
        // random students
        StudentStream.Student[] random_students = generate_random_students(section_rng.get()).toArray(StudentStream.Student[]::new);

        for (int i = 0; i < 10; i++) {

            int limit = new Random().nextInt(5);
            TestConditions testedConditions = generateConditions(limit);

            StudentStream.Student expected = handleCondition(Arrays.stream(random_students), testedConditions.oldImplementation).findFirst().orElse(null);
            StudentStream.Student result = StudentStream.findFirst(Arrays.stream(random_students), testedConditions.newImplementation);
            assertEquals(expected, result);
        }

    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void findAll_advanced() {
        // random students
        StudentStream.Student[] random_students = generate_random_students(section_rng.get()).toArray(StudentStream.Student[]::new);

        for (int i = 0; i < 10; i++) {

            int limit = new Random().nextInt(5);
            TestConditions testedConditions = generateConditions(limit);

            List<StudentStream.Student> expected = handleCondition(Arrays.stream(random_students), testedConditions.oldImplementation).collect(Collectors.toList());
            List<StudentStream.Student> result = StudentStream.findAll(Arrays.stream(random_students), testedConditions.newImplementation).collect(Collectors.toList());
            compareStudents(expected, result);
        }

    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void exists_advanced() {
        // random students
        StudentStream.Student[] random_students = generate_random_students(section_rng.get()).toArray(StudentStream.Student[]::new);

        for (int i = 0; i < 100; i++) {

            int limit = new Random().nextInt(5);
            TestConditions testedConditions = generateConditions(limit);
            int atleast = new Random().nextInt(random_students.length);

            boolean expected = handleCondition(Arrays.stream(random_students), testedConditions.oldImplementation).count() >= atleast;
            boolean result = StudentStream.exists(Arrays.stream(random_students), testedConditions.newImplementation, atleast);
            assertEquals(expected, result);
        }

    }


    private static Map<String, Double> getCoursesResult(StudentStream.Student s) {
        return s.getGradesStream().collect(Collectors.toMap(x -> x.getCourse(), x -> x.getValue()));
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void filterThenSort_advanced() {
        // random students
        StudentStream.Student[] random_students = generate_random_students(section_rng.get()).toArray(StudentStream.Student[]::new);

        for (int i = 0; i < 10; i++) {

            int limit = new Random().nextInt(5);
            TestConditions testedConditions = generateConditions(limit);

            Comparator<StudentStream.Student> basic = Comparator
                    .comparing(StudentStream.Student::getSection)
                    .thenComparing(StudentStream.Student::getFirstName)
                    .thenComparing(StudentStream.Student::getLastName)
                    .thenComparing(
                            (s) -> getCoursesResult(s).values().stream().reduce(0.0, Double::sum),
                            Comparator.reverseOrder()
                    );

            //
            Comparator<StudentStream.Student> other = new Comparator<StudentStream.Student>() {
                @Override
                public int compare(StudentStream.Student o1, StudentStream.Student o2) {
                    return getCoursesResult(o1).values().stream()
                            .reduce(0.0, (a, b) -> a + b)
                            .compareTo(
                                    getCoursesResult(o2).values().stream().reduce(0.0, (a, b) -> a + b)
                            );
                }
            };

            // with the basic
            List<StudentStream.Student> expected = handleCondition(Arrays.stream(random_students), testedConditions.oldImplementation).sorted(basic).collect(Collectors.toList());
            List<StudentStream.Student> result = StudentStream.filterThenSort(Arrays.stream(random_students), testedConditions.newImplementation, basic).collect(Collectors.toList());
            compareStudents(expected, result);

            // with custom comparator
            expected = handleCondition(Arrays.stream(random_students), testedConditions.oldImplementation).sorted(other.reversed()).collect(Collectors.toList());
            result = StudentStream.filterThenSort(Arrays.stream(random_students), testedConditions.newImplementation, other.reversed()).collect(Collectors.toList());
            compareStudents(expected, result);
        }

    }

    private Stream<StudentStream.Student> handleCondition(Stream<StudentStream.Student> studentsStream, Map<String, Predicate<?>> conditions) {
        for (Map.Entry<String, Predicate<?>> condition : conditions.entrySet()) {
            switch (condition.getKey()) {
                case "firstName":
                    Predicate<String> s1 = (Predicate<String>) condition.getValue();
                    studentsStream = studentsStream.filter(student -> s1.test(student.getFirstName()));
                    break;
                case "lastName":
                    Predicate<String> s2 = (Predicate<String>) condition.getValue();
                    studentsStream = studentsStream.filter(student -> s2.test(student.getLastName()));
                    break;
                case "section":
                    Predicate<Integer> s3 = (Predicate<Integer>) condition.getValue();
                    studentsStream = studentsStream.filter(student -> s3.test(student.getSection()));
                    break;
                case "courses_results":
                    Predicate<Map<String, Double>> s4 = (Predicate<Map<String, Double>>) condition.getValue();
                    studentsStream = studentsStream.filter(student -> s4.test(getCoursesResult(student)));
                    break;
            }
        }
        return studentsStream;
    }

}
