package com.ex.webapp.Servlets;

import com.ex.webapp.DAOs.RequestDAO;
import com.ex.webapp.DAOs.RequestDAOPostgres;
import com.ex.webapp.Models.Employee;
import com.ex.webapp.Models.ReimbursementRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;


// www.website.com?

@WebServlet(urlPatterns = {"/get-pending"})
public class GetPendingRequestsServlet extends HttpServlet {
    private final Logger log = LogManager.getLogger(GetPendingRequestsServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Get employee data from session object
        log.info("Getting employee information from session object...");
        HttpSession session = req.getSession();
        Employee employee = (Employee)(session.getAttribute("employee"));

        if (employee == null) {
            log.warn("No user signed in.");
            log.info("Sending client to home page...");
            resp.sendRedirect("/");
        }
        else {
            // Get DAO object to access the database
            RequestDAO dao = new RequestDAOPostgres();
            List<ReimbursementRequest> pendingRequests;

            // Get pending requests from the database
            log.info("Getting list of pending requests...");
            if (employee.isManager()) {
                pendingRequests = dao.viewPendingRequests();
            } else {
                pendingRequests = dao.viewPendingRequests(employee.getId());
            }
            log.info("List received.");

            // Set response type to json
            resp.setContentType("application/json");

            // Write Employee list to response
            log.info("Converting list to JSON...");
            ObjectMapper om = new ObjectMapper();
            String jsonInString = om.writeValueAsString(pendingRequests);

            // Send the employee list back to the client
            log.info("Sending list to client...");
            resp.getWriter().write(jsonInString);
        }
    }
}