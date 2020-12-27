package com.ex.webapp.Servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {
    private final Logger log = LogManager.getLogger(LogoutServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Get session data
        log.info("Getting session from request object...");
        HttpSession session = req.getSession();

        // Remove employee from session
        log.info("Removing employee from the session object...");
        session.removeAttribute("employee");
        log.info("Employee removed.");

        // Redirect to the login page
        log.info("Redirecting to the login page...");
        resp.sendRedirect("/");
    }
}
