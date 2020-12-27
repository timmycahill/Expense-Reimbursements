package com.ex.webapp.Servlets;

import com.ex.webapp.DAOs.EmployeeDAO;
import com.ex.webapp.DAOs.EmployeeDAOPostgres;
import com.ex.webapp.Models.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/get-employees"})
public class GetEmployeesServlet extends HttpServlet {
    private final Logger log = LogManager.getLogger(GetEmployeesServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Get session
        log.info("Getting employee information from session object...");
        HttpSession session = req.getSession();
        Employee employee = (Employee)(session.getAttribute("employee"));

        if (employee == null) {
            log.warn("No user signed in.");
            log.info("Redirecting to the login page...");
            resp.sendRedirect("/");
        }
        else if (!employee.isManager()) {
            log.warn("User is not a manager.");
            log.info("Redirecting to the home page...");
            resp.sendRedirect("/home");
        }
        else {
            // Get DAO object to access the database
            EmployeeDAO dao = new EmployeeDAOPostgres();
            log.info("Getting list of employees...");
            List<Employee> employees = dao.getAllEmployees();

            // Set response type to json
            resp.setContentType("application/json");

            // Write Employee list to response
            ObjectMapper om = new ObjectMapper();
            log.info("Converting list to JSON...");
            String jsonInString = om.writeValueAsString(employees);

            // Send the employee list back to the client
            log.info("Writing JSON to response object...");
            resp.getWriter().write(jsonInString);
        }
    }
}
