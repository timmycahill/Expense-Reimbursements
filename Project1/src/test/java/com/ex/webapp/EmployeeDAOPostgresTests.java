package com.ex.webapp;

import com.ex.webapp.DAOs.EmployeeDAO;
import com.ex.webapp.DAOs.EmployeeDAOPostgres;
import com.ex.webapp.Models.Employee;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Random;

public class EmployeeDAOPostgresTests {
    private final EmployeeDAO dao = new EmployeeDAOPostgres();

    @Test
    public void shouldGetEmployeeByEmail() {
        String testEmail = "tcahill17@hotmail.com";
        Employee employee = dao.getEmployee(testEmail);
        Assert.assertNotNull(employee);
    }

    @Test
    public void shouldNotGetEmployeeByEmail() {
        String badEmail = "notAnEmail";
        Employee employee = dao.getEmployee(badEmail);
        Assert.assertNull(employee);
    }

    @Test
    public void shouldGetEmployeeById() {
        int testId = 1;
        Employee employee = dao.getEmployee(testId);
        Assert.assertNotNull(employee);
    }

    @Test
    public void shouldNotGetEmployeeById() {
        int badId = -2;
        Employee employee = dao.getEmployee(badId);
        Assert.assertNull(employee);
    }

    @Test
    public void shouldGetAllEmployees() {
        List<Employee> employees = null;
        employees = dao.getAllEmployees();
        Assert.assertNotNull(employees);
    }

    @Test
    public void shouldVerifyLogin() {
        String validEmail = "tcahill17@hotmail.com";
        String validPassword = "password";
        Assert.assertTrue(dao.verifyLogin(validEmail, validPassword));
    }

    @Test
    public void shouldNotVerifyLoginBadEmail() {
        String invalidEmail = "bad email";
        String invalidPassword = "bad password";
        Assert.assertFalse(dao.verifyLogin(invalidEmail, invalidPassword));
    }

    @Test
    public void shouldNotVerifyLoginBadPassword() {
        String validEmail = "tcahill17@hotmail.com";
        String invalidPassword = "bad password";
        Assert.assertFalse(dao.verifyLogin(validEmail, invalidPassword));
    }

    @Test
    public void shouldUpdateEmployeeInfo() {
        // List of random employee data values
        String[] randomFirstNames = {"Tim", "Bob", "Billy", "Hayden"};
        String[] randomLastNames = {"Cahill", "Smith", "Bob", "Fields"};

        // Generate random selection
        Random random = new Random();
        int randomNumber = random.nextInt(4);

        // Assign random values to variables
        String newFirstName = randomFirstNames[randomNumber];
        String newLastName = randomLastNames[randomNumber];
        String testEmail = "test@email.com";

        // Set test employee id
        int testEmployeeId = 3;

        // Update employee
        dao.updateEmployeeInfo(testEmployeeId, newFirstName, newLastName, testEmail);

        // Get employee from the database
        Employee employee = dao.getEmployee(testEmployeeId);

        // Run assertions
        Assert.assertEquals(newFirstName, employee.getFirstName());
        Assert.assertEquals(newLastName, employee.getLastName());
    }

    @Test
    public void shouldRegisterAnEmployee() {
        String testFirstName = "John";
        String testLastName = "Travolta";
        String testEmail = "jt@awesome.net";
        String testPassword = "RoyaleWithCheese";

        dao.registerAnEmployee(testFirstName, testLastName, testEmail, testPassword);

        Employee storedEmployee = dao.getEmployee(testEmail);

        Assert.assertNotNull(storedEmployee);
    }
}
