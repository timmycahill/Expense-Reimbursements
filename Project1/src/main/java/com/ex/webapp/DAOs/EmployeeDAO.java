package com.ex.webapp.DAOs;

import com.ex.webapp.Models.Employee;

import java.util.List;

public interface EmployeeDAO {

    /**
     * This method searches the for an employee with the matching email address. If found, an employee object is created
     * and returned.
     *
     * @param email Employee's email address.
     * @return Employee object containing information on the employee with the corresponding email.
     */
    Employee getEmployee(String email);

    /**
     * This method searches the for an employee with the matching Employee ID. If found, an employee object is created
     * and returned.
     *
     * @param employeeId Employee's ID number.
     * @return Employee object containing information on the employee with the corresponding ID number.
     */
    Employee getEmployee(int employeeId);

    /**
     * This method retrieves all stored employee data and returns it to the caller.
     *
     * @return A list of Employee objects, mapping to each employee in the database.
     */
    List<Employee> getAllEmployees();

    /**
     * This method takes in an email/password combination and compares it to the stored employee data. If a matching
     * combination is found in the database, a boolean is returned indicating the results.
     *
     * @param email Submitted email address.
     * @param password Submitted password.
     * @return True, if a matching combination is found. False, if not.
     */
    boolean verifyLogin(String email, String password);

    /**
     * This method searches the database for an employee with the matching Employee ID. If found, the employee data is
     * updated to match the submitted first name, last name, and email address.
     *
     * @param employeeId ID number of employee to be updated.
     * @param firstName Updated first name.
     * @param lastName Updated last name.
     * @param email Updated email.
     */
    void updateEmployeeInfo(int employeeId, String firstName, String lastName, String email);

    /**
     * This method creates a new employee record in the database with the submitted employee data.
     *
     * @param firstName Employee's first name.
     * @param lastName Employee's last name.
     * @param email Employee's email address.
     * @param password Employee's password.
     */
    void registerAnEmployee(String firstName, String lastName, String email, String password);
}
