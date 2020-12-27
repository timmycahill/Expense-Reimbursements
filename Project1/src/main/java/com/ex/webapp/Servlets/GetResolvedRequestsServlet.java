package com.ex.webapp.Servlets;

import com.ex.webapp.DAOs.RequestDAO;
import com.ex.webapp.DAOs.RequestDAOPostgres;
import com.ex.webapp.Models.Employee;
import com.ex.webapp.Models.ReimbursementRequest;
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
import java.io.PrintWriter;
import java.util.List;

@WebServlet(urlPatterns = {"/get-resolved"})
public class GetResolvedRequestsServlet extends HttpServlet {
    private final Logger log = LogManager.getLogger(GetResolvedRequestsServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Get employee data from session object
        log.info("Getting employee information from session object...");
        HttpSession session = req.getSession();
        Employee employee = (Employee)(session.getAttribute("employee"));

        if (employee == null) {
            log.warn("No user signed in.");
            log.info("Redirecting to the home page.");
            resp.sendRedirect("/");
        }
        else {
            // Get DAO object to access the database
            RequestDAO dao = new RequestDAOPostgres();
            List<ReimbursementRequest> resolvedRequests;

            // Get pending requests from the database
            log.info("Getting list of resolved requests...");
            if (employee.isManager()) {
                resolvedRequests = dao.viewResolvedRequests();
            } else {
                resolvedRequests = dao.viewResolvedRequests(employee.getId());
            }
            log.info("List received.");

            // Set response type to json
            resp.setContentType("application/json");

            // Write Employee list to response
            ObjectMapper om = new ObjectMapper();
            log.info("Converting list to JSON...");
            String jsonInString = om.writeValueAsString(resolvedRequests);

            // Send the employee list back to the client
            log.info("Writing list to response object.");
            resp.getWriter().write(jsonInString);
        }
    }
}