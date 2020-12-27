package com.ex.webapp.Models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReimbursementRequest {
    private final int requestId;
    private final Employee submittedBy;
    private final Employee resolvedBy;
    private final double amount;
    private final String description;
    private final boolean isPending;
    private final boolean wasApproved;
    private String dateSubmitted;
    private String dateResolved;

    public ReimbursementRequest(int requestId, Employee submittedBy, Employee resolvedBy, double amount, String description, boolean wasApproved, Date dateSubmitted) {
        this.requestId = requestId;
        this.submittedBy = submittedBy;
        this.resolvedBy = resolvedBy;
        this.amount = amount;
        this.description = description;
        this.isPending = resolvedBy.getId() == -1;
        this.wasApproved = wasApproved;

        DateFormat df = new SimpleDateFormat();
        this.dateSubmitted = df.format(dateSubmitted);
    }

    public ReimbursementRequest(int requestId, Employee submittedBy, Employee resolvedBy, double amount, String description, boolean wasApproved, Date dateSubmitted, Date dateResolved) {
        this.requestId = requestId;
        this.submittedBy = submittedBy;
        this.resolvedBy = resolvedBy;
        this.amount = amount;
        this.description = description;
        this.isPending = resolvedBy.getId() == -1;
        this.wasApproved = wasApproved;

        DateFormat df = new SimpleDateFormat();
        this.dateSubmitted = df.format(dateSubmitted);
        this.dateResolved = df.format(dateResolved);
    }

    public ReimbursementRequest(int requestId, Employee submittedBy, Employee resolvedBy, double amount, String description, boolean wasApproved) {
        this.requestId = requestId;
        this.submittedBy = submittedBy;
        this.resolvedBy = resolvedBy;
        this.amount = amount;
        this.description = description;
        this.wasApproved = wasApproved;
        this.isPending = resolvedBy.getId() == -1;
    }

    public ReimbursementRequest(int requestId, Employee submittedBy, double amount, String description, Date dateSubmitted) {
        this.requestId = requestId;
        this.submittedBy = submittedBy;
        this.resolvedBy = null;
        this.amount = amount;
        this.description = description;
        this.isPending = true;
        this.wasApproved = false;

        DateFormat df = new SimpleDateFormat();
        this.dateSubmitted = df.format(dateSubmitted);
    }

    public int getRequestId() {
        return requestId;
    }

    public Employee getSubmittedBy() {
        return submittedBy;
    }

    public Employee getResolvedBy() {
        return resolvedBy;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPending() {
        return isPending;
    }

    public String getDateSubmitted() {
        return dateSubmitted;
    }

    public String getDateResolved() {
        return dateResolved;
    }

    public boolean isWasApproved() {
        return wasApproved;
    }
}
