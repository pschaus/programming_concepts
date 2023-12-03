package fp;

import org.javagrader.Grade;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@Grade
public class EmployeeFinderTest {
    EmployeeFinder.Employee e0 = new EmployeeFinder.Employee(0, 5.4f, 1000);
    EmployeeFinder.Employee e1 = new EmployeeFinder.Employee(1, 6.0f, 3000);
    EmployeeFinder.Employee e2 = new EmployeeFinder.Employee(2, 7.2f, 2000);
    EmployeeFinder.Employee e3 = new EmployeeFinder.Employee(3, 5.0f, 1000);

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testFindBestEmployeeOne() {
        // Only one employee:
        final ArrayList<EmployeeFinder.Employee> employees = new ArrayList<>();
        employees.add(e0);

        // Whatever score we calculate, it doesn't matter because there is only one employee:
        assertTrue(EmployeeFinder.findBestEmployee(employees, (e)-> 1f) == e0);
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testFindBestEmployee() {
        // Four employees:
        final ArrayList<EmployeeFinder.Employee> employees = new ArrayList<EmployeeFinder.Employee>(Arrays.asList(e0, e1, e2, e3));

        // find the employee with the highest experience:
        assertTrue(EmployeeFinder.findBestEmployee(employees, (e)-> -e.experience) == e2);

        // find the employee with the smallest salary:
        assertTrue(EmployeeFinder.findBestEmployee(employees, (e)-> (float)e.salary) == e0);
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testFindBestEmployeeInSmallCompany() {
        final EmployeeFinder.Company company = new EmployeeFinder.Company(
                new ArrayList<EmployeeFinder.Employee>(Arrays.asList(e0, e1)),
                new ArrayList<EmployeeFinder.Employee>(Arrays.asList(e2, e3)));

        // find the employee with the highest experience:
        assertTrue(EmployeeFinder.findBestEmployee(company, (e)-> -e.experience) == e2);

        // find the employee with the smallest salary:
        assertTrue(EmployeeFinder.findBestEmployee(company, (e)-> (float)e.salary) == e0);
    }

    @Test
    @Grade(value = 1, cpuTimeout = 1000)
    public void testFindBestEmployeeInLargeCompany() {
        // The company has 500 employees:
        final ArrayList<EmployeeFinder.Employee> employees = new ArrayList<>();
        for(int i=0;i<500;i++) {
            employees.add(new EmployeeFinder.Employee(i, i/10.0f, 1000+i*5));
        }
        // This is the employee with the biggest salary:
        final EmployeeFinder.Employee richestEmployee = employees.get(employees.size()-1);

        // We randomly change the order of the employees and split them into two departments.
        // 200 employees in department A, 300 employees in department B:
        Collections.shuffle(employees);  // <- this method randomly changes the order of the employees
        final ArrayList<EmployeeFinder.Employee> departmentA = new ArrayList<>(employees.subList(0,200));
        final ArrayList<EmployeeFinder.Employee> departmentB = new ArrayList<>(employees.subList(200,500));
        final EmployeeFinder.Company company = new EmployeeFinder.Company(departmentA, departmentB);

        // find the employee with the biggest salary:
        assertTrue(EmployeeFinder.findBestEmployee(company, (e)-> -(float)e.salary) == richestEmployee);
    }
}
