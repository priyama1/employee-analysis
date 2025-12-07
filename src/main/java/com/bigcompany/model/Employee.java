package com.bigcompany.model;

import java.util.Objects;

public class Employee {

    private int id;
    private String firstName;
    private String lastName;
    private double salary;
    private Integer managerId;

    public Employee(int id, String firstName, String lastName, double salary, Integer managerId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.managerId = managerId;
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
    public double getSalary() {
        return salary;
    }
    public Integer getManagerId() {
        return managerId;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean isCeo() {
        return managerId == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee employee)) return false;
        return id == employee.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Employee{" + id + ", " + getFullName() + ", $" + salary + "}";
    }
}
