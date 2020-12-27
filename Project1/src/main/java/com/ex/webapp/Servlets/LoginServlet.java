package com.ex.webapp.Servlets;

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

@WebServlet(urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    private final Logger log = LogManager.getLogger(LoginServlet.class);
    private final EmployeeDAOPostgres dao = new EmployeeDAOPostgres();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Redirect to login page (index.html)
        resp.sendRedirect("/");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Get email and password
        log.info("Reading in email/password from post request.");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        // Verify login info
        log.info("Verifying login...");
        if (dao.verifyLogin(email, password)) {
            log.info("Login verified.");

            // Get matching employee from the database
            log.info("Collecting employee information...");
            Employee employee = dao.getEmployee(email);
            log.info("Employee object created.");

            // Attach that employee to the session
            log.info("Attaching employee object to the session...");
            HttpSession session = req.getSession();
            session.setAttribute("employee", employee);
            log.info("Employee object attached to the session.");

            // Forward to home page servlet
            log.info("Redirecting to the home page...");
            resp.sendRedirect("/home.html");
        }
        else {
            log.warn("Invalid email/password combination.");
            log.info("Redirecting to the login page...");
            resp.sendRedirect("/");
        }
    }
}
