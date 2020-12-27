package com.ex.webapp.Servlets;

import com.ex.webapp.DAOs.RequestDAO;
import com.ex.webapp.DAOs.RequestDAOPostgres;
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

@WebServlet(urlPatterns = {"/submit-request"})
public class SubmitRequestServlet extends HttpServlet {
    private final Logger log = LogManager.getLogger(SubmitRequestServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Get employee from session object
        log.info("Getting employee information from session object...");
        HttpSession session = req.getSession();
        Employee employee = (Employee)(session.getAttribute("employee"));

        if (employee == null) {
            log.warn("No user signed in.");
            log.info("Redirecting to the login page...");
            resp.sendRedirect("/");
        }
        else if (employee.isManager()) {
            log.warn("Employee is a manager.");
            log.info("Redirecting to the home page...");
            resp.sendRedirect("/home.html");
        }
        else {
            log.info("Redirecting to the submit request form page...");
            resp.sendRedirect("/submit-request-form.html");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Get employee from session object
        log.info("Getting employee information from session object...");
        HttpSession session = req.getSession();
        Employee employee = (Employee)(session.getAttribute("employee"));

        if (employee == null) {
            log.warn("No user logged in.");
            log.info("Redirecting to the login page...");
            resp.sendRedirect("/");
        }
        else if (employee.isManager()) {
            log.warn("Employee is a manager.");
            log.info("Redirecting to the home page...");
            resp.sendRedirect("/home.html");
        }
        else {
            // Get values from form
            log.info("Getting information from request submission form...");
            double amount = Double.parseDouble(req.getParameter("amount"));
            String description = req.getParameter("description");

            // trim amount to 2 decimal places
            log.info("Trimming off trailing decimal places...");
            amount = (double)(((int)(amount * 100)) / 100);

            // Create DAO object to submit request to database
            RequestDAO dao = new RequestDAOPostgres();

            // Submit request to the database
            log.info("Submitting request to the database...");
            dao.submitRequest(employee, amount, description);
            log.info("Request submitted.");

            // Redirect to the view requests page
            log.info("Redirecting to view pending requests page.");
            resp.sendRedirect("/view-pending.html");
        }
    }
}
