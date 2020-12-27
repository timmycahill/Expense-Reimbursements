package com.ex.webapp.Servlets;

import com.ex.webapp.DAOs.EmployeeDAO;
import com.ex.webapp.DAOs.EmployeeDAOPostgres;
import com.ex.webapp.Models.Employee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {"/create-employee"})
public class CreateEmployeeServlet extends HttpServlet {
    private final Logger log = LogManager.getLogger(CreateEmployeeServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Get employee from session object
        log.info("Getting employee information from session object...");
        HttpSession session = req.getSession();
        Employee employee = (Employee)(session.getAttribute("employee"));

        if (employee == null) {
            log.warn("No employee logged in.");
            log.info("Redirecting to login page...");
            resp.sendRedirect("/");
        }
        else if (!employee.isManager()) {
            log.warn("Employee is not a manager.");
            log.info("Redirecting to home page...");
            resp.sendRedirect("/home.html");
        }
        else {
            log.info("Redirecting to employee creation form...");
            resp.sendRedirect("/create-employee-form.html");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Get employee from session object
        log.info("Getting employee information from session object...");
        HttpSession session = req.getSession();
        Employee employee = (Employee)(session.getAttribute("employee"));

        if (employee == null) {
            log.warn("No employee logged in.");
            log.info("Redirecting to login page...");
            resp.sendRedirect("/");
        }
        else if (!employee.isManager()) {
            log.warn("Employee is not a manager.");
            log.info("Redirecting to home page...");
            resp.sendRedirect("/home.html");
        }
        else {
            // Get values from form
            log.info("Getting form data...");
            String email = req.getParameter("email");
            String password = req.getParameter("password");
            String firstName = req.getParameter("first-name");
            String lastName = req.getParameter("last-name");

            // Create DAO object to create employee in DB
            EmployeeDAO dao = new EmployeeDAOPostgres();

            // Create employee in db
            log.info("Registering employee to the database...");
            dao.registerAnEmployee(firstName, lastName, email, password);

            // Redirect to the view employees page
            log.info("Redirecting to the view employees page...");
            resp.sendRedirect("/view-employees.html");
        }
    }
}
