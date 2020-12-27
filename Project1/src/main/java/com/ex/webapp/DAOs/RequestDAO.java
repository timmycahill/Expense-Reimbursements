package com.ex.webapp.DAOs;

import com.ex.webapp.Models.Employee;
import com.ex.webapp.Models.ReimbursementRequest;

import java.util.List;

public interface RequestDAO {
    /**
     * This method adds a new reimbursement request record to the request database.
     *
     * @param employee Employee submitting the request.
     * @param amount Size of the request in the specified currency.
     * @param description Reason for the request.
     */
    void submitRequest(Employee employee, double amount, String description);

    /**
     * This method returns a list of all stored pending reimbursement requests.
     *
     * @return List of ReimbursementRequest Objects mapping to all stored pending reimbursement requests.
     */
    List<ReimbursementRequest> viewPendingRequests();

    /**
     * This method returns a list of all stored pending reimbursement requests that belong to a specific employee.
     *
     * @param employeeId ID number of the specified employee.
     * @return List of ReimbursementRequest Objects mapping to all stored pending reimbursement requests with the
     * matching Employee ID number.
     */
    List<ReimbursementRequest> viewPendingRequests(int employeeId);

    /**
     * This method returns a list of all stored resolved reimbursement requests.
     *
     * @return List of ReimbursementRequest Objects mapping to all stored resolved reimbursement requests.
     */
    List<ReimbursementRequest> viewResolvedRequests();

    /**
     * This method returns a list of all stored resolved reimbursement requests that belong to a specific employee.
     *
     * @param employeeId ID number of the specified employee.
     * @return List of ReimbursementRequest Objects mapping to all stored resolved reimbursement requests with the
     * matching Employee ID number.
     */
    List<ReimbursementRequest> viewResolvedRequests(int employeeId);

    /**
     * This method returns a list of all stored reimbursement requests that belong to a specific employee.
     *
     * @param employeeId ID number of the specified employee.
     * @return List of ReimbursementRequest Objects mapping to all stored reimbursement requests with the
     * matching Employee ID number.
     */
    List<ReimbursementRequest> viewReimbursementRequests(int employeeId);

    /**
     * This method updates the status of a specified stored reimbursement request as approved.
     *
     * @param requestId ID number of the requests to be updated.
     * @param employeeId ID number of the manager approving the request.
     */
    void approveRequest(int requestId, int employeeId);

    /**
     * This method updates the status of a specified stored reimbursement request as denied.
     *
     * @param requestId ID number of the requests to be updated.
     * @param employeeId ID number of the manager denying the request.
     */
    void denyRequest(int requestId, int employeeId);
}
