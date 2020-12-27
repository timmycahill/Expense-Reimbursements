package com.ex.webapp;

import com.ex.webapp.DAOs.EmployeeDAO;
import com.ex.webapp.DAOs.EmployeeDAOPostgres;
import com.ex.webapp.DAOs.RequestDAO;
import com.ex.webapp.DAOs.RequestDAOPostgres;
import com.ex.webapp.Models.Employee;
import com.ex.webapp.Models.ReimbursementRequest;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class RequestDAOPostgresTests {
    private final EmployeeDAO eDao = new EmployeeDAOPostgres();
    private final RequestDAO rDao = new RequestDAOPostgres();

    @Test
    public void shouldViewPendingRequests() {
        List<ReimbursementRequest> requests = null;
        requests = rDao.viewPendingRequests();
        Assert.assertNotNull(requests);
    }

    @Test
    public void shouldViewPendingRequestsByEmployeeId() {
        int testEmployeeId = 3;

        List<ReimbursementRequest> requests = null;
        requests = rDao.viewPendingRequests(testEmployeeId);
        Assert.assertNotNull(requests);
    }

    @Test
    public void shouldViewResolvedRequests() {
        List<ReimbursementRequest> requests = null;
        requests = rDao.viewResolvedRequests();
        Assert.assertNotNull(requests);
    }

    @Test
    public void shouldViewResolvedRequestsByEmployeeId() {
        int testEmployeeId = 3;

        List<ReimbursementRequest> requests = null;
        requests = rDao.viewResolvedRequests(testEmployeeId);
        Assert.assertNotNull(requests);
    }

    @Test
    public void shouldViewAllRequestsByEmployee() {
        int testEmployeeId = 3;

        List<ReimbursementRequest> requests = null;
        requests = rDao.viewReimbursementRequests(testEmployeeId);
        Assert.assertNotNull(requests);
    }

    @Test
    public void shouldSubmitRequest() {
        // Get test employee
        int testEmployeeId = 3;
        Employee testEmployee = eDao.getEmployee(testEmployeeId);

        // Generate test data
        double testAmount = 3.50;
        String testDescription = "I need about tree fiddy.";

        // Submit requests
        rDao.submitRequest(testEmployee, testAmount, testDescription);

        // Get list of requests
        List<ReimbursementRequest> pendingRequests = rDao.viewPendingRequests(testEmployeeId);
        int lastRequest = pendingRequests.size() - 1;

        // Check test description against last submitted request
        Assert.assertEquals(pendingRequests.get(lastRequest).getDescription(), testDescription);

        // Deny last submitted request
        int managerId = 10;
        int requestId = pendingRequests.get(lastRequest).getRequestId();
        rDao.denyRequest(requestId, managerId);
    }

    @Test
    public void shouldDenyRequest() {
        // Get test employee
        int testEmployeeId = 3;
        Employee testEmployee = eDao.getEmployee(testEmployeeId);

        // Generate test data
        double testAmount = 3.50;
        String testDescription = "I need about tree fiddy.";

        // Submit requests
        rDao.submitRequest(testEmployee, testAmount, testDescription);

        // Get list of pending requests
        List<ReimbursementRequest> pendingRequests = rDao.viewPendingRequests(testEmployeeId);
        int lastRequest = pendingRequests.size() - 1;

        // Deny last submitted request
        int managerId = 10;
        int requestId = pendingRequests.get(lastRequest).getRequestId();
        rDao.denyRequest(requestId, managerId);

        // Get list of resolved requests
        List<ReimbursementRequest> resolvedRequests = rDao.viewResolvedRequests(testEmployeeId);
        lastRequest = resolvedRequests.size() - 1;


        // Check test description against last submitted request
        Assert.assertEquals(resolvedRequests.get(lastRequest).getDescription(), testDescription);
    }

    @Test
    public void shouldApproveRequest() {
        // Get test employee
        int testEmployeeId = 3;
        Employee testEmployee = eDao.getEmployee(testEmployeeId);

        // Generate test data
        double testAmount = 3.50;
        String testDescription = "I need about tree fiddy.";

        // Submit requests
        rDao.submitRequest(testEmployee, testAmount, testDescription);

        // Get list of pending requests
        List<ReimbursementRequest> pendingRequests = rDao.viewPendingRequests(testEmployeeId);
        int lastRequest = pendingRequests.size() - 1;

        // Deny last submitted request
        int managerId = 10;
        int requestId = pendingRequests.get(lastRequest).getRequestId();
        rDao.approveRequest(requestId, managerId);

        // Get list of resolved requests
        List<ReimbursementRequest> resolvedRequests = rDao.viewResolvedRequests(testEmployeeId);
        lastRequest = resolvedRequests.size() - 1;


        // Check test description against last submitted request
        Assert.assertEquals(resolvedRequests.get(lastRequest).getDescription(), testDescription);
    }
}
