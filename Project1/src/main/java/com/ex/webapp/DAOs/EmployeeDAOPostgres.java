package com.ex.webapp.DAOs;

import com.ex.webapp.Models.Employee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAOPostgres extends PostgresDAO implements EmployeeDAO {
    private final Logger log = LogManager.getLogger(EmployeeDAOPostgres.class);

    @Override
    public Employee getEmployee(String email) {
        log.info("Attempting to connect to the database...");
        try (Connection connection = this.getConnection()) {
            if (connection != null) {
                log.info("Connected to the database.");

                // Prepare SQL statement
                log.info("Preparing SQL statement...");
                String sql = "SELECT employee_id, first_name, last_name, is_manager FROM employees " +
                        "WHERE email=?";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, email);

                // Execute SQL statement
                log.info("Executing SQL statement...");
                ResultSet rs = ps.executeQuery();
                log.info("Statement executed.");

                // Pull employee data from the result set and return an employee object comprised of that data
                if (rs.next()) {
                    log.info("Collecting employee data...");
                    int id = rs.getInt("employee_id");
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    boolean isManager = rs.getBoolean("is_manager");
                    log.info("Returning employee object...");
                    return new Employee(id, firstName, lastName, email, isManager);
                }

                // Return null if no employee is found
                log.warn("No employee found with matching email address.");
                log.warn("Returning null Employee object...");
                return null;
            }
            else {
                log.warn("Unable to establish a connection.");
            }
        } catch (SQLException e) {
            log.warn(e.getMessage());
        }

        // Return null if all else fails
        log.warn("Returning null employee object...");
        return null;
    }

    @Override
    public Employee getEmployee(int employeeId) {
        try (Connection connection = this.getConnection()) {
            if (connection != null) {
                log.info("Connected to the database.");

                // Prepare SQL statement
                log.info("Preparing SQL statement...");
                String sql = "SELECT first_name, last_name, email, is_manager FROM employees " +
                        "WHERE employee_id=?";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setInt(1, employeeId);

                // Execute SQL statement
                log.info("Executing SQL statement...");
                ResultSet rs = ps.executeQuery();
                log.info("Statement executed.");

                // Pull employee data from the result set and return an employee object comprised of that data
                if (rs.next()) {
                    log.info("Collecting employee data...");
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String email = rs.getString("email");
                    boolean isManager = rs.getBoolean("is_manager");
                    log.info("Returning Employee object...");
                    return new Employee(employeeId, firstName, lastName, email, isManager);
                }

                // Return null if no employee is found
                log.warn("No matching employee found.");
                log.warn("Returning null Employee object...");
                return null;
            }
            else {
                log.warn("Unable to establish a connection.");
            }
        } catch (SQLException e) {
            log.warn(e.getMessage());
        }

        // Return null if all else fails
        log.warn("Returning null Employee object...");
        return null;
    }

    @Override
    public List<Employee> getAllEmployees() {
        try (Connection connection = this.getConnection()) {
            if (connection != null) {
                log.info("Connected to the database.");

                // Prepare SQL statement
                log.info("Preparing SQL statement...");
                Statement statement = connection.createStatement();
                String sql = "SELECT employee_id, first_name, last_name, email, is_manager FROM employees";

                // Execute SQL statement
                log.info("Executing SQL statement...");
                ResultSet rs = statement.executeQuery(sql);
                log.info("Statement executed.");

                // Create empty list to be populated with Employees in the database
                List<Employee> employees = new ArrayList<>();

                // Populate list with all employees in the database
                log.info("Collecting employees...");
                while (rs.next()) {
                    int id = rs.getInt("employee_id");
                    if (id == -1) {
                        continue;
                    }
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String email = rs.getString("email");
                    boolean isManager = rs.getBoolean("is_manager");
                    employees.add(new Employee(id, firstName, lastName, email, isManager));
                }

                // Return list of employees
                log.info("Returning list of Employees...");
                return employees;
            }
            else {
                log.warn("Unable to establish a connection.");
            }
        } catch (SQLException e) {
            log.warn(e.getMessage());
        }

        // Return null if all else fails
        log.warn("Returning null List object...");
        return null;
    }

    @Override
    public boolean verifyLogin(String email, String password) {
        try (Connection connection = this.getConnection()) {
            if (connection != null) {
                log.info("Connected to the database.");

                // Prepare SQL statement
                log.info("Preparing SQL statement...");
                String sql = "SELECT employee_id, first_name, last_name, is_manager FROM employees " +
                        "WHERE email=? AND password_hash=?";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, email);
                ps.setInt(2, password.hashCode());

                // Execute SQL statement
                log.info("Executing SQL statement...");
                ResultSet rs = ps.executeQuery();
                log.info("Statement executed.");

                // Return boolean indicating if an employee was found with the provided credentials or not
                log.info("Returning boolean.");
                return rs.next();
            }
            else {
                log.warn("Unable to establish a connection.");
            }
        } catch (SQLException e) {
            log.warn(e.getMessage());
        }
        // Return false if all else fails
        log.warn("Returning false.");
        return false;
    }

    @Override
    public void updateEmployeeInfo(int employeeId, String firstName, String lastName, String email) {
        try (Connection connection = this.getConnection()) {
            if (connection != null) {
                log.info("Connected to the database.");

                // Prepare SQL statement
                log.info("Preparing SQL statement...");
                String sql = "UPDATE employees " +
                        "SET first_name=?, last_name=?, email=? " +
                        "WHERE employee_id=?";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, firstName);
                ps.setString(2, lastName);
                ps.setString(3, email);
                ps.setInt(4, employeeId);

                // Execute SQL statement
                log.info("Executing SQL statement...");
                ps.executeUpdate();
                log.info("Statement executed.");
            }
            else {
                log.warn("Unable to establish a connection.");
            }
        } catch (SQLException e) {
            log.warn(e.getMessage());
        }
    }

    @Override
    public void registerAnEmployee(String firstName, String lastName, String email, String password) {
        try (Connection connection = this.getConnection()) {
            if (connection != null) {
                log.info("Connected to the database.");

                // Prepare SQL statement
                log.info("Preparing SQL statement...");
                String sql = "INSERT INTO employees (first_name, last_name, email, password_hash) " +
                        "VALUES (?, ?, ?, ?)";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, firstName);
                ps.setString(2, lastName);
                ps.setString(3, email);
                ps.setInt(4, password.hashCode());

                // Execute SQL statement
                log.info("Executing SQL statement...");
                ps.executeUpdate();
                log.info("Statement executed.");
            }
            else {
                log.warn("Unable to establish a connection.");
            }
        } catch (SQLException e) {
            log.warn(e.getMessage());
        }
    }
}
