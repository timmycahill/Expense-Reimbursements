package com.ex.webapp.Models;

public class Employee {
    private final int id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final boolean isManager;

    public Employee(int id, String firstName, String lastName, String email, boolean isManager) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.isManager = isManager;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public boolean isManager() {
        return isManager;
    }
}
