package com.ex.webapp.DAOs;

import com.ex.webapp.Models.Employee;
import com.ex.webapp.Models.ReimbursementRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RequestDAOPostgres extends PostgresDAO implements RequestDAO {
    private final Logger log = LogManager.getLogger(RequestDAOPostgres.class);

    @Override
    public void submitRequest(Employee employee, double amount, String description) {
        try (Connection connection = this.getConnection()) {
            if (connection != null) {
                log.info("Connected to the database.");

                // Prepare SQL statement
                log.info("Preparing SQL statement...");
                String sql = "INSERT INTO requests (submitted_by, amount_requested, description) " +
                        "VALUES (?, ?, ?)";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setInt(1, employee.getId());
                ps.setDouble(2, amount);
                ps.setString(3, description);

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
    public List<ReimbursementRequest> viewPendingRequests() {
        try (Connection connection = this.getConnection()) {
            if (connection != null) {
                log.info("Connected to the database.");

                // Prepare SQL statement
                log.info("Preparing SQL statement...");
                Statement statement = connection.createStatement();
                String sql = "SELECT r.request_id, r.amount_requested, r.description, r.date_submitted, e.employee_id, e.email, e.first_name, e.last_name, e.is_manager " +
                        "FROM requests r INNER JOIN employees e ON r.submitted_by = e.employee_id " +
                        "WHERE r.resolved_by = -1";

                // Execute SQL statement
                log.info("Executing SQL statement...");
                ResultSet rs = statement.executeQuery(sql);
                log.info("Statement executed.");

                // Create empty list to be populated with pending requests in the database
                List<ReimbursementRequest> requests = new ArrayList<>();

                // Create Employee DAO to access employee information
                EmployeeDAO employeeDAO = new EmployeeDAOPostgres();

                // Populate list with all pending requests in the database
                log.info("Getting list of pending requests...");
                while (rs.next()) {
                    // Gather request information
                    int requestId = rs.getInt("request_id");
                    double amount = rs.getDouble("amount_requested");
                    String description = rs.getString("description");
                    Date dateSubmitted = rs.getDate("date_submitted");

                    // Gather employee information
                    int employeeId = rs.getInt("employee_id");
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String email = rs.getString("email");
                    boolean isManager = rs.getBoolean("is_manager");
                    Employee submittedByEmployee = new Employee(employeeId,firstName,lastName,email,isManager);

                    // Add request to the list
                    requests.add(new ReimbursementRequest(requestId, submittedByEmployee, amount, description, dateSubmitted));
                    log.info("A request has been added to the list.");
                }
                log.info("Completed request collection.");

                // Return list of requests
                log.info("Returning list of requests...");
                return requests;
            }
            else {
                log.warn("Unable to establish a connection.");
            }
        } catch (SQLException e) {
            log.warn(e.getMessage());
        }

        // Return null if all else fails
        log.warn("Returning null list...");
        return null;
    }

    @Override
    public List<ReimbursementRequest> viewPendingRequests(int employeeId) {
        try (Connection connection = this.getConnection()) {
            if (connection != null) {
                log.info("Connected to the database.");

                // Prepare SQL statement
                log.info("Preparing SQL statement...");
                String sql = "SELECT r.request_id, r.amount_requested, r.description, r.date_submitted, e.email, e.first_name, e.last_name, e.is_manager " +
                        "FROM requests r INNER JOIN employees e ON r.submitted_by = e.employee_id " +
                        "WHERE r.resolved_by = -1 AND r.submitted_by = ?";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setInt(1, employeeId);

                // Execute SQL statement
                log.info("Executing SQL statement...");
                ResultSet rs = ps.executeQuery();
                log.info("Statement executed.");

                // Create empty list to be populated with pending requests in the database
                List<ReimbursementRequest> requests = new ArrayList<>();

                // Create Employee DAO to access employee information
                EmployeeDAO employeeDAO = new EmployeeDAOPostgres();

                // Populate list with all pending requests in the database
                log.info("Collecting pending requests...");
                while (rs.next()) {
                    // Gather request information
                    int requestId = rs.getInt("request_id");
                    double amount = rs.getDouble("amount_requested");
                    String description = rs.getString("description");
                    Date dateSubmitted = rs.getDate("date_submitted");

                    // Gather employee information
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String email = rs.getString("email");
                    boolean isManager = rs.getBoolean("is_manager");
                    Employee submittedByEmployee = new Employee(employeeId,firstName,lastName,email,isManager);

                    // Add request to the list
                    requests.add(new ReimbursementRequest(requestId, submittedByEmployee, amount, description, dateSubmitted));
                    log.info("A request has been added to the list.");
                }
                log.info("Completed request collection.");

                // Return list of requests
                log.info("Returning list of requests...");
                return requests;
            }
            else {
                log.warn("Unable to establish a connection.");
            }
        } catch (SQLException e) {
            log.warn(e.getMessage());
        }

        // Return null if all else fails
        log.warn("Returning null object.");
        return null;
    }

    @Override
    public List<ReimbursementRequest> viewResolvedRequests() {
        try (Connection connection = this.getConnection()) {
            if (connection != null) {
                log.info("Connected to the database.");

                // Prepare SQL statement
                log.info("Preparing SQL statement...");
                Statement statement = connection.createStatement();
                String sql = "SELECT r.request_id, r.resolved_by, r.submitted_by, r.amount_requested, r.description, r.was_approved, r.date_submitted, r.date_resolved, " +
                        "e1.employee_id as s_employee_id, e1.email as s_email, e1.first_name as s_first_name, e1.last_name as s_last_name, e1.is_manager as s_is_manager, " +
                        "e2.employee_id as r_employee_id, e2.email as r_email, e2.first_name as r_first_name, e2.last_name as r_last_name, e2.is_manager as r_is_manager " +
                        "FROM requests r INNER JOIN employees e1 ON r.submitted_by = e1.employee_id " +
                        "INNER JOIN employees e2 ON r.resolved_by = e2.employee_id " +
                        "WHERE r.resolved_by != -1";

                // Execute SQL statement
                log.info("Executing SQL statement...");
                ResultSet rs = statement.executeQuery(sql);
                log.info("Statement executed.");

                // Create empty list to be populated with resolved requests in the database
                List<ReimbursementRequest> requests = new ArrayList<>();

                // Create Employee DAO to access employee information
                EmployeeDAO employeeDAO = new EmployeeDAOPostgres();

                // Populate list with all resolved requests in the database
                log.info("Collecting resolved requests...");
                while (rs.next()) {
                    // Gather request information
                    int requestId = rs.getInt("request_id");
                    double amount = rs.getDouble("amount_requested");
                    String description = rs.getString("description");
                    boolean wasApproved = rs.getBoolean("was_approved");
                    int submittedById = rs.getInt("submitted_by");
                    Date dateSubmitted = rs.getDate("date_submitted");
                    int resolvedById = rs.getInt("resolved_by");
                    Date dateResolved = rs.getDate("date_resolved");

                    // Gather submitting employee data
                    String submittedByFirstName = rs.getString("s_first_name");
                    String submittedByLastName = rs.getString("s_last_name");
                    String submittedByEmail = rs.getString("s_email");
                    boolean submittedByIsManager = rs.getBoolean("s_is_manager");
                    Employee submittedByEmployee = new Employee(submittedById, submittedByFirstName, submittedByLastName, submittedByEmail, submittedByIsManager);

                    // Gather resolving employee data
                    String resolvedByFirstName = rs.getString("r_first_name");
                    String resolvedByLastName = rs.getString("r_last_name");
                    String resolvedByEmail = rs.getString("r_email");
                    boolean resolvedByIsManager = rs.getBoolean("r_is_manager");
                    Employee resolvedByEmployee = new Employee(resolvedById, resolvedByFirstName, resolvedByLastName, resolvedByEmail, resolvedByIsManager);


                    // Add request to the list
                    requests.add(new ReimbursementRequest(requestId, submittedByEmployee, resolvedByEmployee, amount,
                                description, wasApproved, dateSubmitted, dateResolved));

                    log.info("A request has been added to the list.");
                }
                log.info("Completed request collection.");

                // Return list of requests
                log.info("Returning list of requests...");
                return requests;
            }
            else {
                log.warn("Unable to establish a connection.");
            }
        } catch (SQLException e) {
            log.warn(e.getMessage());
        }

        // Return null if all else fails
        log.warn("Returning null list...");
        return null;
    }

    @Override
    public List<ReimbursementRequest> viewResolvedRequests(int employeeId) {
        try (Connection connection = this.getConnection()) {
            if (connection != null) {
                log.info("Connected to the database.");

                // Prepare SQL statement
                log.info("Preparing SQL statement...");
                String sql = "SELECT r.request_id, r.resolved_by, r.submitted_by, r.amount_requested, r.description, r.was_approved, r.date_submitted, r.date_resolved, " +
                        "e1.employee_id as s_employee_id, e1.email as s_email, e1.first_name as s_first_name, e1.last_name as s_last_name, e1.is_manager as s_is_manager, " +
                        "e2.employee_id as r_employee_id, e2.email as r_email, e2.first_name as r_first_name, e2.last_name as r_last_name, e2.is_manager as r_is_manager " +
                        "FROM requests r INNER JOIN employees e1 ON r.submitted_by = e1.employee_id " +
                        "INNER JOIN employees e2 ON r.resolved_by = e2.employee_id " +
                        "WHERE r.resolved_by != -1 AND r.submitted_by = ?";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setInt(1, employeeId);

                // Execute SQL statement
                log.info("Executing SQL Statement");
                ResultSet rs = ps.executeQuery();
                log.info("Statement executed.");

                // Create empty list to be populated with resolved requests in the database
                List<ReimbursementRequest> requests = new ArrayList<>();

                // Create Employee DAO to access employee information
                EmployeeDAO employeeDAO = new EmployeeDAOPostgres();

                // Populate list with all resolved requests in the database
                log.info("Collecting resolved requests...");
                while (rs.next()) {
                    // Gather request information
                    int requestId = rs.getInt("request_id");
                    double amount = rs.getDouble("amount_requested");
                    String description = rs.getString("description");
                    boolean wasApproved = rs.getBoolean("was_approved");
                    int submittedById = rs.getInt("submitted_by");
                    Date dateSubmitted = rs.getDate("date_submitted");
                    int resolvedById = rs.getInt("resolved_by");
                    Date dateResolved = rs.getDate("date_resolved");

                    // Gather submitting employee data
                    String submittedByFirstName = rs.getString("s_first_name");
                    String submittedByLastName = rs.getString("s_last_name");
                    String submittedByEmail = rs.getString("s_email");
                    boolean submittedByIsManager = rs.getBoolean("s_is_manager");
                    Employee submittedByEmployee = new Employee(submittedById, submittedByFirstName, submittedByLastName, submittedByEmail, submittedByIsManager);

                    // Gather resolving employee data
                    String resolvedByFirstName = rs.getString("r_first_name");
                    String resolvedByLastName = rs.getString("r_last_name");
                    String resolvedByEmail = rs.getString("r_email");
                    boolean resolvedByIsManager = rs.getBoolean("r_is_manager");
                    Employee resolvedByEmployee = new Employee(resolvedById, resolvedByFirstName, resolvedByLastName, resolvedByEmail, resolvedByIsManager);


                    // Add request to the list
                    requests.add(new ReimbursementRequest(requestId, submittedByEmployee, resolvedByEmployee, amount,
                            description, wasApproved, dateSubmitted, dateResolved));

                    log.info("A request has been added to the list.");
                }
                log.info("Completed request collection.");

                // Return list of requests
                log.info("Returning list of resolved requests...");
                return requests;
            }
            else {
                log.warn("Unable to establish a connection.");
            }
        } catch (SQLException e) {
            log.warn(e.getMessage());
        }

        // Return null if all else fails
        log.warn("Returning null list object...");
        return null;
    }

    @Override
    public List<ReimbursementRequest> viewReimbursementRequests(int employeeId) {
        try (Connection connection = this.getConnection()) {
            if (connection != null) {
                log.info("Connected to the database.");

                // Prepare SQL statement
                log.info("Preparing SQL statement...");
                String sql = "SELECT request_id, resolved_by, amount_requested, description, was_approved, date_submitted, date_resolved " +
                        "FROM requests WHERE submitted_by=?";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setInt(1, employeeId);

                // Execute SQL statement
                log.info("Executing SQL statement...");
                ResultSet rs = ps.executeQuery();
                log.info("Statement executed.");

                // Create empty list to be populated with requests in the database
                List<ReimbursementRequest> requests = new ArrayList<>();

                // Create Employee DAO to access employee information
                EmployeeDAO employeeDAO = new EmployeeDAOPostgres();

                // Populate list with all requests in the database
                log.info("Collecting all reimbursement requests...");
                while (rs.next()) {
                    // Gather request information
                    int requestId = rs.getInt("request_id");
                    Employee submittedByEmployee = employeeDAO.getEmployee(employeeId);
                    int resolvedById = rs.getInt("resolved_by");
                    Employee resolvedByEmployee = (Employee) (employeeDAO.getEmployee(resolvedById));
                    double amount = rs.getDouble("amount_requested");
                    String description = rs.getString("description");
                    boolean wasApproved = rs.getBoolean("was_approved");
                    Date dateSubmitted = rs.getDate("date_submitted");
                    Date dateResolved = rs.getDate("date_resolved");

                    // Add request to the list
                    requests.add(new ReimbursementRequest(requestId, submittedByEmployee, resolvedByEmployee, amount,
                            description, wasApproved, dateSubmitted, dateResolved));
                    log.info("A request has been added to the list.");
                }
                log.info("Completed request collection.");

                // Return list of requests
                log.info("Returning list or requests...");
                return requests;
            }
            else {
                log.warn("Unable to establish a connection.");
            }
        } catch (SQLException e) {
            log.warn(e.getMessage());
        }

        // Return null if all else fails
        log.warn("Returning null list object...");
        return null;
    }

    @Override
    public void approveRequest(int requestId, int employeeId) {
        try (Connection connection = this.getConnection()) {
            if (connection != null) {
                log.info("Connected to the database.");

                // Prepare SQL statement
                log.info("Preparing SQL statement...");
                String sql = "UPDATE requests " +
                        "SET resolved_by=?, was_approved=true, date_resolved=now()::date " +
                        "WHERE request_id=?";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setInt(1, employeeId);
                ps.setInt(2, requestId);

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
    public void denyRequest(int requestId, int employeeId) {
        try (Connection connection = this.getConnection()) {
            if (connection != null) {
                log.info("Connected to the database.");

                // Prepare SQL statement
                log.info("Preparing SQL statement...");
                String sql = "UPDATE requests " +
                        "SET resolved_by=?, was_approved=false, date_resolved=now()::date " +
                        "WHERE request_id=?";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setInt(1, employeeId);
                ps.setInt(2, requestId);

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
