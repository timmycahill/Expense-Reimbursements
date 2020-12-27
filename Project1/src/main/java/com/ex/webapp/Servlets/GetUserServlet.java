package com.ex.webapp.Servlets;

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

@WebServlet(urlPatterns = {"/get-user"})
public class GetUserServlet extends HttpServlet {
    private final Logger log = LogManager.getLogger(GetUserServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Get user from session
        log.info("Getting employee information from session object...");
        HttpSession session = req.getSession();
        Employee employee = (Employee)(session.getAttribute("employee"));

        // Set response type to json
        resp.setContentType("application/json");

        // Write Employee list to response
        ObjectMapper om = new ObjectMapper();
        log.info("Converting employee object to JSON...");
        String jsonInString = om.writeValueAsString(employee);

        // Send the employee list back to the client
        log.info("Writing JSON to response object...");
        resp.getWriter().write(jsonInString);
    }
}
