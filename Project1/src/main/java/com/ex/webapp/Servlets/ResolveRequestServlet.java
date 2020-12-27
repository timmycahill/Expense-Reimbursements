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

@WebServlet(urlPatterns = {"/resolve-request"})
public class ResolveRequestServlet extends HttpServlet {
    private final Logger log = LogManager.getLogger(ResolveRequestServlet.class);
    private final RequestDAO dao = new RequestDAOPostgres();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Get employee data from session object
        log.info("Getting employee information from session object...");
        HttpSession session = req.getSession();
        Employee employee = (Employee)(session.getAttribute("employee"));

        if (employee == null) {
            log.warn("No user signed in.");
            log.info("Redirecting to the home page.");
            resp.sendRedirect("/");
        }
        else if (!employee.isManager()) {
            log.warn("User is not a manager.");
            log.info("Redirecting to the home page...");
            resp.sendRedirect("/home");
        }
        else {
            // Get form data
            int requestId = Integer.parseInt(req.getParameter("request-id"));
            String status = req.getParameter("approve-deny");

            // Approve request
            if (status.equals("approve")) {
                dao.approveRequest(requestId, employee.getId());
            }
            // Deny request
            else {
                dao.denyRequest(requestId, employee.getId());
            }

            // Refresh page
            resp.sendRedirect("/view-pending.html");
        }
    }
}
